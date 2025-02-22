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

import java.util.List;

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
        p1.setPageTitle("Top 10 Foods Every Pregnant Mom Should Include in Her Diet");
        p1.setContent("Content 1");
        p1.setFeaturedImageUrl("https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby");
        p1.setShortDescription("By Texas Health and Human services: If you experience any of these symptoms during or after");
        BlogPost p2 = new BlogPost();
        p2.setPageTitle("The Importance of Sleep for Moms and Babies: Tips to Rest Better");
        p2.setContent("<p>123<p/>");
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
        BlogPost p7 = new BlogPost();
        p7.setPageTitle("How to Prepare for Labor: A Guide for First-Time Moms");
        p7.setContent("Content 7");
        p7.setFeaturedImageUrl("https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby");
        p7.setShortDescription("Understanding contractions, hospital bags, and when to call your doctor");

        BlogPost p8 = new BlogPost();
        p8.setPageTitle("Healthy Eating Tips for Breastfeeding Moms");
        p8.setContent("Content 8");
        p8.setFeaturedImageUrl("https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby");
        p8.setShortDescription("Essential nutrients to boost milk supply and maintain your energy levels");

        BlogPost p9 = new BlogPost();
        p9.setPageTitle("Baby Sleep Training: When and How to Start");
        p9.setContent("Content 9");
        p9.setFeaturedImageUrl("https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby");
        p9.setShortDescription("Helping your newborn establish healthy sleeping habits early on");

        BlogPost p10 = new BlogPost();
        p10.setPageTitle("Postpartum Recovery: What to Expect in the First 6 Weeks");
        p10.setContent("Content 10");
        p10.setFeaturedImageUrl("https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby");
        p10.setShortDescription("Tips to recover physically and emotionally after giving birth");

        BlogPost p11 = new BlogPost();
        p11.setPageTitle("The Best Pregnancy Exercises for a Healthy Delivery");
        p11.setContent("Content 11");
        p11.setFeaturedImageUrl("https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby");
        p11.setShortDescription("Safe and effective exercises to stay active during pregnancy");

        BlogPost p12 = new BlogPost();
        p12.setPageTitle("Choosing the Right Pediatrician for Your Newborn");
        p12.setContent("Content 12");
        p12.setFeaturedImageUrl("https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby");
        p12.setShortDescription("Key factors to consider when selecting the best doctor for your baby");

        Tag t1 = new Tag();
        t1.setName("Tag1");

        Tag t2 = new Tag();
        t2.setName("Tag2");

        Tag t3 = new Tag();
        t3.setName("Tag3");

//        p1.setTags(List.of(t1, t2));
//        p2.setTags(List.of(t1, t3));
//        p3.setTags(List.of(t2, t3));
//
//        t1.setBlogPosts(List.of(p2, p3));
//        t2.setBlogPosts(List.of(p1, p3));
//        t3.setBlogPosts(List.of(p1, p2));
//
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
        this.blogPostService.save(p7);
        this.blogPostService.save(p8);
        this.blogPostService.save(p9);
        this.blogPostService.save(p10);
        this.blogPostService.save(p11);
        this.blogPostService.save(p12);


        this.blogPostCommentService.save(bpComment1);
        this.blogPostCommentService.save(bpComment2);
        this.blogPostCommentService.save(bpComment3);

//        this.tagService.save(t1);
//        this.tagService.save(t2);
//        this.tagService.save(t3);
    }
}
