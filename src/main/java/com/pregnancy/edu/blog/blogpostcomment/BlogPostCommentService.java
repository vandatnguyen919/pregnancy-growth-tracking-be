package com.pregnancy.edu.blog.blogpostcomment;

import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BlogPostCommentService {

    private final BlogPostCommentRepository blogPostCommentRepository;

    public BlogPostCommentService(BlogPostCommentRepository blogPostCommentRepository) {
        this.blogPostCommentRepository = blogPostCommentRepository;
    }

    public List<BlogPostComment> findAll() {
        return blogPostCommentRepository.findAll();
    }

    public BlogPostComment findById(Long id) {
        return blogPostCommentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("comment", id));
    }

    public BlogPostComment save(BlogPostComment comment) {
        return blogPostCommentRepository.save(comment);
    }

    public BlogPostComment update(Long id, BlogPostComment comment) {
        return blogPostCommentRepository.findById(id)
                .map(oldComment -> {
                    oldComment.setContent(comment.getContent());
                    return blogPostCommentRepository.save(oldComment);
                })
                .orElseThrow(() -> new ObjectNotFoundException("comment", id));
    }

    public void delete(Long id) {
        blogPostCommentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("comment", id));
        blogPostCommentRepository.deleteById(id);
    }
}
