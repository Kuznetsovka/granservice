package ru.gransoft.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentDto that = (DocumentDto) o;
        return seq == that.seq && Objects.equals(id, that.id) && Objects.equals(text, that.text) && Objects.equals(parentId, that.parentId) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, parentId, seq, children);
    }
}
