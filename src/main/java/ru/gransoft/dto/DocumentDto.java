package ru.gransoft.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Kuznetsovka 14.07.2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDto {
    private Long id;
    private String text;
    private Long parentId;
    private int seq;
    private List<DocumentDto> children;
}
