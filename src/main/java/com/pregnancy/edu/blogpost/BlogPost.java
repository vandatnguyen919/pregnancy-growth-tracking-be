package com.pregnancy.edu.blogpost;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authorId;

    private String heading;

    private String pageTitle;

    private String content;

    private String shortDescription;

    private String featuredImageUrl;

    private String urlHandle;

    private boolean isVisible;

    @CreationTimestamp
    private LocalDateTime publishedDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

}
