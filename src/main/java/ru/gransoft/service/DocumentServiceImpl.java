package ru.gransoft.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gransoft.dto.DocumentDto;
import ru.gransoft.entity.Document;
import ru.gransoft.repository.DocumentRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author Kuznetsovka 14.07.2023
 */
@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository repository;

    @PersistenceContext
    protected EntityManager entityManager;

    /** {@inheritDoc} */
    @Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRED)
    @Override
    public DocumentDto addDocument(DocumentDto dto) {
        Document parent = getParent(dto.getParentId());
        int seq = parent != null ? parent.getChildren().stream()
                .max(Comparator.comparing(Document::getSeq))
                .map(Document::getSeq)
                .orElse(0) : 0;
        Document document = Document.builder()
                .text(dto.getText())
                .seq(++seq)
                .parent(parent)
                .build();
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
    @Transactional(transactionManager="transactionManager", propagation = Propagation.REQUIRED)
    @Override
    public String getDocumentByNaturalOrder(Long parentId) {
        //getDocumentRecursive(parentId, entityManager);
        return null;
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

    private void getDocumentRecursive(Long parent_id, EntityManager em){
        List<Document> nodes = em.createQuery("select d from Document d"
                        + " where d.parent.id = :pId"
                        + " order by d.id", Document.class)
                .setParameter("pId", parent_id)
                .getResultList();
        for (Document node:nodes) {
            getDocumentRecursive(node.getId(), em);
        }
    }
}
