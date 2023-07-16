package ru.gransoft.kafka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gransoft.kafka.entity.Document;

import java.util.List;
import java.util.Optional;

/**
 * @author Kuznetsovka 14.07.2023
 */

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findAllByParent_IdOrderBySeq(Long parentId);
    @Override
    Optional<Document> findById(Long id);
}
