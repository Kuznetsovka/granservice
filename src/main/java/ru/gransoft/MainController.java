package ru.gransoft;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/gran-docs")
public class MainController {

  @PostMapping(value ="/add-doc")
  public String addDocument(@RequestBody String body) {
    return "Document add";
  }

  @GetMapping("/get-doc/{id}")
  public String get(@PathVariable String id) {
    String json = "Hierarchy json";
    return json;
  }

}
