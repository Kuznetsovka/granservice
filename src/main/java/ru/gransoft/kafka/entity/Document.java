package ru.gransoft.kafka.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kuznetsovka 14.07.2023
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_document")
@EntityListeners(AuditingEntityListener.class)
public class Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(name = "update_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Lob
    @Type(type = "text")
    private String text;

    @ColumnDefault("0")
    private int seq = 0;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Document root;

    @ManyToOne(fetch = FetchType.LAZY)
    private Document parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    @OrderBy("parent, seq")
    private List<Document> children;

    @Transient
    private Boolean hasChildren = null;

    @Transient
    public boolean isHasChildren() {
        if (hasChildren != null)
            return hasChildren;
        else {
            return getChildren() != null && !getChildren().isEmpty();

        }

    }

    @Transient
    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}
