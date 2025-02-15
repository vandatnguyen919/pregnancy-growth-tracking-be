package com.pregnancy.edu.blog.blogpost;

import com.pregnancy.edu.blog.blogpostcomment.BlogPostComment;
import com.pregnancy.edu.blog.blogpostlike.BlogPostLike;
import com.pregnancy.edu.blog.tag.Tag;
import com.pregnancy.edu.myuser.MyUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToOne
    private MyUser users;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "blogPosts")
    private List<Tag> tags;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "blogPost")
    private List<BlogPostComment> comments;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "blogPost")
    private List<BlogPostLike> likes;
}
