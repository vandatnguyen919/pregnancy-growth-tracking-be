package com.pregnancy.edu.system;

import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.blogpost.BlogPostService;
import com.pregnancy.edu.blog.blogpostcomment.BlogPostComment;
import com.pregnancy.edu.blog.blogpostcomment.BlogPostCommentService;
import com.pregnancy.edu.blog.blogpostlike.BlogPostLike;
import com.pregnancy.edu.blog.blogpostlike.BlogPostLikeService;
import com.pregnancy.edu.blog.tag.Tag;
import com.pregnancy.edu.blog.tag.TagService;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserService;
import com.pregnancy.edu.system.consts.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final BlogPostService blogPostService;
    private final BlogPostLikeService blogPostLikeService;
    private final BlogPostCommentService blogPostCommentService;
    private final TagService tagService;

    public DBDataInitializer(UserService userService, BlogPostService blogPostService, BlogPostLikeService blogPostLikeService, BlogPostCommentService blogPostCommentService, TagService tagService) {
        this.userService = userService;
        this.blogPostService = blogPostService;
        this.blogPostLikeService = blogPostLikeService;
        this.blogPostCommentService = blogPostCommentService;
        this.tagService = tagService;
    }

    @Override
    public void run(String... args) throws Exception {

        // Create some users.
        MyUser u1 = new MyUser();
//        u1.setId(1L);
        u1.setEmail("john@example.com");
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRole("admin");

        MyUser u2 = new MyUser();
//        u2.setId(2L);
        u2.setEmail("eric@example.com");
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRole("user");

        MyUser u3 = new MyUser();
//        u3.setId(3L);
        u3.setEmail("tom@example.com");
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRole("user");

        BlogPost p1 = new BlogPost();
        p1.setHeading("Heading 1");
        p1.setContent("Content 1");

        BlogPost p2 = new BlogPost();
        p2.setHeading("Heading 2");
        p2.setContent("Content 2");

        BlogPost p3 = new BlogPost();
        p3.setHeading("Heading 3");
        p3.setContent("Content 3");

        Tag t1 = new Tag();
        t1.setName("Tag1");

        Tag t2 = new Tag();
        t2.setName("Tag2");

        Tag t3 = new Tag();
        t3.setName("Tag3");

        BlogPostComment bpComment1 = new BlogPostComment();
        bpComment1.setContent("comment1");
        BlogPostComment bpComment2 = new BlogPostComment();
        bpComment2.setContent("comment2");
        BlogPostComment bpComment3 = new BlogPostComment();
        bpComment3.setContent("comment3");

        this.userService.save(u1);
        this.userService.save(u2);
        this.userService.save(u3);

        this.blogPostService.save(p1);
        this.blogPostService.save(p2);
        this.blogPostService.save(p3);

        this.blogPostCommentService.save(bpComment1);
        this.blogPostCommentService.save(bpComment2);
        this.blogPostCommentService.save(bpComment3);


    }
}
