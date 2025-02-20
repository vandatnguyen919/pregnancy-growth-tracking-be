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
        u1.setVerified(true);
        u1.setRole("admin");

        MyUser u2 = new MyUser();
//        u2.setId(2L);
        u2.setEmail("eric@example.com");
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setVerified(true);
        u2.setRole("user");

        MyUser u3 = new MyUser();
//        u3.setId(3L);
        u3.setEmail("tom@example.com");
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setVerified(true);
        u3.setRole("user");

        BlogPost p1 = new BlogPost();
        p1.setPageTitle("Top 10 Foods Every Pregnant Mom Should Include in Her Diet");
        p1.setContent("Content 1");
        p1.setFeaturedImageUrl("https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby");
        p1.setShortDescription("By Texas Health and Human services: If you experience any of these symptoms during or after");
        BlogPost p2 = new BlogPost();
        p2.setPageTitle("The Importance of Sleep for Moms and Babies: Tips to Rest Better");
        p2.setContent("Content 2");
        p2.setFeaturedImageUrl("https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby");
        p2.setShortDescription("By Texas Health and Human services: If you experience any of these symptoms during or after");
        BlogPost p3 = new BlogPost();
        p3.setPageTitle("5 Simple Prenatal Yoga Poses to Reduce Stress and Boost Energy");
        p3.setContent("Content 3");
        p3.setFeaturedImageUrl("https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby");
        p3.setShortDescription("By Texas Health and Human services: If you experience any of these symptoms during or after");
        BlogPost p4 = new BlogPost();
        p4.setPageTitle("Pregnancy Warning Signs You Should Never Ignore");
        p4.setContent("Content 4");
        p4.setFeaturedImageUrl("https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby");
        p4.setShortDescription("By Texas Health and Human services: If you experience any of these symptoms during or after");
        BlogPost p5 = new BlogPost();
        p5.setPageTitle("Fun and Educational Games to Boost Your Baby\\'s Brain Development");
        p5.setContent("Content 5");
        p5.setFeaturedImageUrl("https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby");
        p5.setShortDescription("For children, play is not just about having fun—it\\'s crucial for developing critical");
        BlogPost p6 = new BlogPost();
        p6.setPageTitle("Common Newborn Health Issues and How to Handle Them Like a Pro");
        p6.setContent("Content 6");
        p6.setFeaturedImageUrl("https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby");
        p6.setShortDescription("If you\\'re a first-time parent, and even if you\\'ve already been through it before");

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
        this.blogPostService.save(p4);
        this.blogPostService.save(p5);
        this.blogPostService.save(p6);


        this.blogPostCommentService.save(bpComment1);
        this.blogPostCommentService.save(bpComment2);
        this.blogPostCommentService.save(bpComment3);


    }
}
