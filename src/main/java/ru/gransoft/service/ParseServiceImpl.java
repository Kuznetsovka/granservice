package ru.gransoft.service;

import org.springframework.stereotype.Service;
import ru.gransoft.JsonHelper;
import ru.gransoft.dto.DocumentDto;

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
        String res = JsonHelper.toJsonStr(document);
        return res;
    }
}
