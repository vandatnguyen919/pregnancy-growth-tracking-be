package com.pregnancy.edu.blog.blogpostlike;

import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BlogPostLikeService {

    private final BlogPostLikeRepository blogPostLikeRepository;

    public BlogPostLikeService(BlogPostLikeRepository blogPostLikeRepository) {
        this.blogPostLikeRepository = blogPostLikeRepository;
    }

    public List<BlogPostLike> findAll() {
        return blogPostLikeRepository.findAll();
    }

    public BlogPostLike findById(Long id) {
        return blogPostLikeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("like", id));
    }

    public BlogPostLike save(BlogPostLike like) {
        return blogPostLikeRepository.save(like);
    }

    public void delete(Long id) {
        blogPostLikeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("like", id));
        blogPostLikeRepository.deleteById(id);
    }
}