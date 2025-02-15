package com.pregnancy.edu.blog.blogpostlike;

import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.myuser.MyUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class BlogPostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MyUser user;

    @ManyToOne
    private BlogPost blogPost;
}
