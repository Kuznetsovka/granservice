package ru.gransoft.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gransoft.kafka.dto.DocumentDto;
import ru.gransoft.kafka.entity.Document;
import ru.gransoft.kafka.dto.DocumentDto;
import ru.gransoft.kafka.entity.Document;
import ru.gransoft.kafka.repository.DocumentRepository;
import ru.gransoft.kafka.repository.DocumentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Kuznetsovka 14.07.2023
 */
@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository repository;

    @Autowired
    private ParseService parseService;

    //Если бы запросы были бы сложнее выбрал бы способ написания через entityManager.
//    @PersistenceContext
//    protected EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    @Override
    public DocumentDto addDocument(DocumentDto dto) {
        Document parent = getParent(dto.getParentId());
        int seq = parent != null ? parent.getChildren().stream()
                .max(Comparator.comparing(Document::getSeq))
                .map(Document::getSeq)
                .orElse(0) : 0;
        Document document = parseService.mapEntityFromDto(dto, parent, seq);
        Document savedDocument = repository.save(document);
        Long parentId = savedDocument.getParent() != null ? savedDocument.getParent().getId() : null;
        DocumentDto savedDto = DocumentDto.builder()
                .seq(savedDocument.getSeq())
                .parentId(parentId)
                .text(savedDocument.getText())
                .build();
        return savedDto;
    }

    /** {@inheritDoc} */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRED)
    @Override
    public DocumentDto getHierarchyDocumentById(Long id) {
        DocumentDto dto = getRootDocument(id);
        return dto;
    }

    private Document getParent(Long parentId) {
        Document parent = null;
        if (parentId != null) {
            parent = checkExistParent(parentId).orElse(null);
            if (parent == null) {
                String msg = String.format("Документ с id = %d не существует!", parentId);
                log.error(msg);
                throw new IllegalArgumentException(msg);
            }
        }
        return parent;
    }


    private Optional<Document> checkExistParent(Long parentId) {
        return repository.findById(parentId);

    }

    private DocumentDto getRootDocument(Long id) {
//        Document root = em.createQuery("select d from Document d"
//                        + " where d.id = :id", Document.class)
//                .setParameter("id", id)
//                .getResultStream()
//                .findAny()
//                .orElse(null);
        Optional<Document> root = repository.findById(id);
        if (root.isEmpty()) {
            String msg = String.format("Документа id=%d не существует!", id);
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        DocumentDto dto = parseService.mapDtoFromEntity(root.get());
        getDocumentRecursive(id, dto);
        return dto;
    }


    /**
     * Выбрал способ запросов детей в новой транзакции.
     */
    @Transactional(transactionManager = "transactionManager", propagation = Propagation.REQUIRES_NEW)
    void getDocumentRecursive(Long parentId, DocumentDto dto) {
//        List<Document> docs = em.createQuery("select d from Document d"
//                        + " where d.parent.id = :parentId"
//                        + " order by d.seq", Document.class)
//                .setParameter("parentId", parentId)
//                .getResultList();
        List<Document> docs = repository.findAllByParent_IdOrderBySeq(parentId);
        List<DocumentDto> children = docs.stream()
                .map(e -> parseService.mapDtoFromEntity(e))
                .collect(Collectors.toList());
        children = children.isEmpty() ? null : children;
        dto.setChildren(children);
        if (children == null) return;
        for (DocumentDto doc : dto.getChildren()) {
            getDocumentRecursive(doc.getId(), doc);
        }
    }
}
