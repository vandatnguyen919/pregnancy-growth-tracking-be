package com.pregnancy.edu.blog.blogpostcomment;

import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.blogpost.BlogPostService;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserService;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BlogPostCommentService {

    private final BlogPostCommentRepository blogPostCommentRepository;
    private final BlogPostService blogPostService;
    private final UserService userService;

    public BlogPostCommentService(BlogPostCommentRepository commentRepository,
                                  BlogPostService blogPostService,
                                  UserService userService) {
        this.blogPostCommentRepository = commentRepository;
        this.blogPostService = blogPostService;
        this.userService = userService;
    }

    public List<BlogPostComment> findAll() {
        return blogPostCommentRepository.findAll();
    }

    public BlogPostComment findById(Long id) {
        return blogPostCommentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("comment", id));
    }

    public BlogPostComment save(BlogPostComment comment) {
        // Validate relationships
        if (comment.getBlogPost() != null && comment.getBlogPost().getId() != null) {
            BlogPost blogPost = blogPostService.findById(comment.getBlogPost().getId());
            comment.setBlogPost(blogPost);
        }

        if (comment.getUser() != null && comment.getUser().getId() != null) {
            MyUser user = userService.findById(comment.getUser().getId());
            comment.setUser(user);
        }

        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        comment.setCreatedAt(now);
        comment.setUpdatedAt(now);

        return blogPostCommentRepository.save(comment);
    }
    public BlogPostComment update(Long id, BlogPostComment comment) {
        return blogPostCommentRepository.findById(id)
                .map(existingComment -> {
                    existingComment.setContent(comment.getContent());
                    existingComment.setUpdatedAt(LocalDateTime.now());

                    // Update relationships if provided
                    if (comment.getBlogPost() != null && comment.getBlogPost().getId() != null) {
                        BlogPost blogPost = blogPostService.findById(comment.getBlogPost().getId());
                        existingComment.setBlogPost(blogPost);
                    }

                    if (comment.getUser() != null && comment.getUser().getId() != null) {
                        MyUser user = userService.findById(comment.getUser().getId());
                        existingComment.setUser(user);
                    }

                    return blogPostCommentRepository.save(existingComment);
                })
                .orElseThrow(() -> new ObjectNotFoundException("comment", id));
    }

    public void delete(Long id) {
        blogPostCommentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("comment", id));
        blogPostCommentRepository.deleteById(id);
    }
}
