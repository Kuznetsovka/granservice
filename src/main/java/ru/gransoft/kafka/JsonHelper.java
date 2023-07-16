package ru.gransoft.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import ru.gransoft.kafka.dto.DocumentDto;

import java.io.IOException;

/**
 * @author Kuznetsovka 14.07.2023
 */
@Slf4j
public class JsonHelper {

  private JsonHelper() {
    throw new IllegalStateException("Utility class");
  }

  private static final ObjectMapper mapper = getObjectMapper();

  public static ObjectMapper getObjectMapper(){
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return mapper;
  }

  public static <T> T jsonNodeToObject(JsonNode node, Class<T> clazz) {
    return jsonNodeToObject(node, clazz, mapper);
  }

  public static <T> T jsonNodeToObject(JsonNode node, Class<T> clazz, ObjectMapper mapper) {
    try {
      return mapper.treeToValue(node, clazz);
    }
    catch(Exception e) {
      throw new RuntimeException(e);
    }
  }

  /** Парсинг строки в JsonNode */
  public static JsonNode parse(final String str) {
    return parse(str, mapper);
  }

  /** Парсинг строки в JsonNode, с указанным маппером */
  public static JsonNode parse(final String str, ObjectMapper mapper) {
    try {
      JsonNode actualObj = mapper.readTree(str);
      return actualObj;
    } catch(Throwable t) {
      throw new RuntimeException(t);
    }
  }

  /**
   * Парсинг объекта в JsonNode
   * @param obj объект
   * @return JSON node.
   */
  public static JsonNode objectToJsonNode(Object obj) {
    return objectToJsonNode(obj, mapper);
  }

  public static JsonNode objectToJsonNode(final Object data, ObjectMapper mapper) {
    try {
      return mapper.valueToTree(data);
    } catch(Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String generateJson(Object o, boolean prettyPrint, boolean escapeNonASCII, ObjectMapper mapper) {
    try {
      ObjectWriter writer = mapper.writer();
      if (prettyPrint) {
        writer = writer.with(SerializationFeature.INDENT_OUTPUT);
      }
      if (escapeNonASCII) {
        writer = writer.with(JsonGenerator.Feature.ESCAPE_NON_ASCII);
      }
      return writer.writeValueAsString(o);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String toJsonStr(Object obj){
    try {
      mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      return mapper.writeValueAsString(obj);
    }
    catch (JsonProcessingException ex) {
      log.error(ex.getLocalizedMessage(), ex);
    }

    return null;
  }

  public static String toJsonPrettyStr(DocumentDto dto, boolean prettyPrint) {
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return generateJson(dto, prettyPrint, false, mapper);
  }

}
