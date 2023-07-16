package ru.gransoft;


import com.sun.jersey.api.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.gransoft.dto.DocumentDto;
import ru.gransoft.service.DocumentService;
import ru.gransoft.service.ParseService;

/**
 * @author Kuznetsovka 14.07.2023
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/gran-docs")
public class MainController {

  @Autowired
  private DocumentService docService;

  @Autowired
  private ParseService parseService;

  @PostMapping(value ="/add-doc")
  public String addDocument(@RequestBody String body) {
    DocumentDto doc = parseService.parseFromJson(body);
    DocumentDto savedDoc = docService.addDocument(doc);
    return "Сохранен документ: " + parseService.parseToJson(savedDoc);
  }

  @GetMapping("/get-doc/{id}")
  public String get(@PathVariable String id) {
    String json = "Hierarchy json";
    return json;
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
