package com.pregnancy.edu.blog.blogpost;

import com.pregnancy.edu.system.base.BaseCrudService;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional

public class BlogPostService implements BaseCrudService<BlogPost, Long> {

    private final BlogPostRepository blogPostRepository;

    public BlogPostService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public List<BlogPost> findAll() {
        return blogPostRepository.findAll();
    }

    @Override
    public BlogPost findById(Long postId) {
        return this.blogPostRepository.findById(postId).orElseThrow(() -> new ObjectNotFoundException("blogPost", postId));
    }

    @Override
    public BlogPost save(BlogPost newPost) {
        return this.blogPostRepository.save(newPost);
    }

    @Override
    public BlogPost update(Long postId, BlogPost post) {
        return this.blogPostRepository.findById(postId)
                .map(oldPost -> {
                    oldPost.setContent(post.getContent());
                    oldPost.setHeading(post.getHeading());
                    oldPost.setFeaturedImageUrl(post.getFeaturedImageUrl());
                    oldPost.setPageTitle(post.getPageTitle());
                    oldPost.setShortDescription(post.getShortDescription());
                    oldPost.setVisible(post.isVisible());
                    oldPost.setUrlHandle(post.getUrlHandle());
                    return this.blogPostRepository.save(oldPost);
                })
                .orElseThrow(() -> new ObjectNotFoundException("blogPost", postId));
    };

    @Override
    public void delete(Long postId) {
        this.blogPostRepository.findById(postId).orElseThrow(() -> new ObjectNotFoundException("blogPost", postId));
        this.blogPostRepository.deleteById(postId);
    }
}
