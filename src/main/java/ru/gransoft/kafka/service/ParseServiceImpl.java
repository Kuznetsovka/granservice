package ru.gransoft.kafka.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import ru.gransoft.kafka.JsonHelper;
import ru.gransoft.kafka.dto.DocumentDto;
import ru.gransoft.kafka.entity.Document;


/**
 * @author Kuznetsovka 14.07.2023
 */
@Service
public class ParseServiceImpl implements ParseService {

    @Override
    public DocumentDto parseFromJson(String json) {
        DocumentDto documentDto = JsonHelper.jsonNodeToObject(JsonHelper.parse(json), DocumentDto.class);
        return documentDto;
    }

    @Override
    public String parseToJson(DocumentDto document) {
        String res = JsonHelper.toJsonPrettyStr(document,true);
        return res;
    }

    @Override
    public JsonNode getJsonNodeFromDocument(Document doc, JsonNode node) {
        DocumentDto dto = mapDtoFromEntity(doc);
        node = JsonHelper.objectToJsonNode(dto);
        return node;
    }

    @Override
    public Document mapEntityFromDto(DocumentDto dto, Document parent, int seq) {
        return Document.builder()
                    .text(dto.getText())
                    .seq(++seq)
                    .parent(parent)
                    .build();
    }

    @Override
    public DocumentDto mapDtoFromEntity(Document entity) {
        return DocumentDto.builder()
                .id(entity.getId())
                .text(entity.getText())
                .seq(entity.getSeq())
                .build();
    }
}
