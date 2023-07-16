package ru.gransoft.kafka.service;

import com.fasterxml.jackson.databind.JsonNode;
import ru.gransoft.kafka.dto.DocumentDto;
import ru.gransoft.kafka.entity.Document;


/**
 * @author Kuznetsovka 14.07.2023
 */
public interface ParseService {
    DocumentDto parseFromJson(String json);
    String parseToJson(DocumentDto document);

    JsonNode getJsonNodeFromDocument(Document root, JsonNode node);

    Document mapEntityFromDto(DocumentDto dto, Document parent, int seq);

    DocumentDto mapDtoFromEntity(Document entity);
}
