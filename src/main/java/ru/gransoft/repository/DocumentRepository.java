package ru.gransoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gransoft.entity.Document;

/**
 * @author Kuznetsovka 14.07.2023
 */

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}
