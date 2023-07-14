package ru.gransoft.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kuznetsovka 14.07.2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDto {
    private String text;
    private Long parentId;
    private int seq;
}
