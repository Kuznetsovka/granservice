package ru.gransoft;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

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
}
