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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final BlogPostService blogPostService;
    private final BlogPostLikeService blogPostLikeService;
    private final BlogPostCommentService blogPostCommentService;
    private final TagService tagService;

    public DBDataInitializer(UserService userService, BlogPostService blogPostService,
                             BlogPostLikeService blogPostLikeService,
                             BlogPostCommentService blogPostCommentService,
                             TagService tagService) {
        this.userService = userService;
        this.blogPostService = blogPostService;
        this.blogPostLikeService = blogPostLikeService;
        this.blogPostCommentService = blogPostCommentService;
        this.tagService = tagService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create and save users first
        MyUser u1 = createUser("john@example.com", "john", "123456", true, "admin");
        MyUser u2 = createUser("eric@example.com", "eric", "654321", true, "user");
        MyUser u3 = createUser("tom@example.com", "tom", "qwerty", false, "user");

        userService.save(u1);
        userService.save(u2);
        userService.save(u3);

        // First create and save all tags
        Tag t1 = new Tag();
        t1.setName("Pregnancy");
        t1 = tagService.save(t1);

        Tag t2 = new Tag();
        t2.setName("Nutrition");
        t2 = tagService.save(t2);

        Tag t3 = new Tag();
        t3.setName("Sleep");
        t3 = tagService.save(t3);

        BlogPost p1 = createBlogPost(
                "Top 10 Foods Every Pregnant Mom Should Include in Her Diet",
                "Content 1",
                "https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby",
                "By Texas Health and Human Services: If you experience any of these symptoms during or after",
                new ArrayList<>(Arrays.asList(t1, t2, t3))
        );

        BlogPost p2 = createBlogPost(
                "The Importance of Sleep for Moms and Babies: Tips to Rest Better",
                "Content 2",
                "https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby",
                "By Texas Health and Human Services: If you experience any of these symptoms during or after",
                new ArrayList<>(Arrays.asList(t1, t2))
        );

        BlogPost p3 = createBlogPost(
                "5 Simple Prenatal Yoga Poses to Reduce Stress and Boost Energy",
                "Content 3",
                "https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby",
                "By Texas Health and Human Services: If you experience any of these symptoms during or after",
                new ArrayList<>(Arrays.asList(t1, t2, t3))
        );

        BlogPost p4 = createBlogPost(
                "Pregnancy Warning Signs You Should Never Ignore",
                "Content 4",
                "https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby",
                "By Texas Health and Human Services: If you experience any of these symptoms during or after",
                new ArrayList<>(Arrays.asList(t1, t3))
        );

        BlogPost p5 = createBlogPost(
                "Fun and Educational Games to Boost Your Baby's Brain Development",
                "Content 5",
                "https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby",
                "For children, play is not just about having fun—it's crucial for developing critical",
                new ArrayList<>(Arrays.asList(t1, t2))
        );

        BlogPost p6 = createBlogPost(
                "Common Newborn Health Issues and How to Handle Them Like a Pro",
                "Content 6",
                "https://images.squarespace-cdn.com/content/v1/57c93870b3db2b6e16992e6c/1512434151171-3RNNOUGZXAIRBBRU342M/pregnant-mom-baby",
                "If you're a first-time parent, and even if you've already been through it before",
                new ArrayList<>(Arrays.asList(t1, t2))
        );

        // Save blog posts
        blogPostService.save(p1);
        blogPostService.save(p2);
        blogPostService.save(p3);

        // Create and save comments
        BlogPostComment bpComment1 = new BlogPostComment();
        bpComment1.setContent("comment1");
        BlogPostComment bpComment2 = new BlogPostComment();
        bpComment2.setContent("comment2");
        BlogPostComment bpComment3 = new BlogPostComment();
        bpComment3.setContent("comment3");

        blogPostCommentService.save(bpComment1);
        blogPostCommentService.save(bpComment2);
        blogPostCommentService.save(bpComment3);
    }

    private MyUser createUser(String email, String username, String password, boolean enabled, String role) {
        MyUser user = new MyUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(enabled);
        user.setRole(role);
        return user;
    }

    private BlogPost createBlogPost(String title, String content, String imageUrl, String description, List<Tag> tags) {
        BlogPost post = new BlogPost();
        post.setPageTitle(title);
        post.setContent(content);
        post.setFeaturedImageUrl(imageUrl);
        post.setShortDescription(description);
        tags.forEach(post::addTag);
        return post;
    }
}