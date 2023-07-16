package ru.gransoft.kafka.service;

import ru.gransoft.kafka.dto.DocumentDto;

/**
 * @author Kuznetsovka 14.07.2023
 */
public interface DocumentService {

    /**
     * Добавление нового документа
     * @param dto - модель сохраняемого документа
     * @return модель сохраненного документа
     */
    DocumentDto addDocument(DocumentDto dto);

    /**
     * Получение дерева документов
     * @param parentId - ID родителя документа от которого запрашивается дерево документов
     * @return json дерева документов
     */
    DocumentDto getHierarchyDocumentById(Long parentId);
}
