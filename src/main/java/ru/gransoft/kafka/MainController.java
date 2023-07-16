package ru.gransoft.kafka;


import com.sun.jersey.api.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.gransoft.kafka.dto.DocumentDto;
import ru.gransoft.kafka.service.DocumentService;

import java.util.concurrent.ExecutionException;

/**
 * @author Kuznetsovka 14.07.2023
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/gran-docs")
public class MainController {

  @Autowired
  private DocumentService docService;

  @PostMapping(value = "/add-doc", produces = {MediaType.APPLICATION_JSON_VALUE})
  @KafkaListener(topics = "${kafka.post.topic}", groupId = "${kafka.group.id}", containerFactory = "kafkaListenerContainerFactory")
  @SendTo
  public ResponseEntity<DocumentDto> addDocument(@RequestBody DocumentDto doc) {
    DocumentDto savedDoc = docService.addDocument(doc);
    return new ResponseEntity<>(savedDoc, HttpStatus.OK);
  }

  @GetMapping(value = "/get-doc/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @KafkaListener(topics = "${kafka.get.topic}", groupId = "${kafka.group.id}", containerFactory = "kafkaListenerContainerFactory")
  @SendTo
  public ResponseEntity<DocumentDto> getHierarchy(@PathVariable Long id) {
    DocumentDto dto = docService.getHierarchyDocumentById(id);
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @RestControllerAdvice
  static class GlobalDefaultExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessage> mismatchException(MethodArgumentTypeMismatchException exception) {
      return ResponseEntity
              .status(HttpStatus.NOT_FOUND)
              .body(new ErrorMessage(exception.getMessage()));
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(NotFoundException exception) {
      return ResponseEntity
              .status(HttpStatus.NOT_FOUND)
              .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> illegalArgException(IllegalArgumentException exception) {
      return ResponseEntity
              .status(HttpStatus.BAD_REQUEST)
              .body(new ErrorMessage(exception.getMessage()));
    }
  }

}
