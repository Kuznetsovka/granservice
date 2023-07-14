package ru.gransoft.service;

import ru.gransoft.dto.DocumentDto;

/**
 * @author Kuznetsovka 14.07.2023
 */
public interface ParseService {
    DocumentDto parseFromJson(String json);
    String parseToJson(DocumentDto document);
}
