package com.pregnancy.edu.blog.blogpost;

import com.pregnancy.edu.blog.blogpost.converter.BlogPostDtoToBlogPostConverter;
import com.pregnancy.edu.blog.blogpost.converter.BlogPostToBlogPostDtoConverter;
import com.pregnancy.edu.blog.blogpost.dto.BlogPostDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blog-posts")
public class BlogPostController {

    private final BlogPostService blogPostService;
    private final BlogPostToBlogPostDtoConverter blogPostToBlogPostDtoConverter;
    private final BlogPostDtoToBlogPostConverter blogPostDtoToBlogPostConverter;

    public BlogPostController(BlogPostService blogPostService, BlogPostToBlogPostDtoConverter blogPostToBlogPostDtoConverter, BlogPostDtoToBlogPostConverter blogPostDtoToBlogPostConverter) {
        this.blogPostService = blogPostService;
        this.blogPostToBlogPostDtoConverter = blogPostToBlogPostDtoConverter;
        this.blogPostDtoToBlogPostConverter = blogPostDtoToBlogPostConverter;
    }

    @GetMapping
    public Result getAllBlogPosts() {
        List<BlogPost> blogPosts = blogPostService.findAll();
        List<BlogPostDto> blogPostDtos = blogPosts.stream().map(blogPostToBlogPostDtoConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", blogPostDtos);
    }

    @GetMapping("/{postId}")
    public Result getBlogPostById(@PathVariable Long postId) {
        BlogPost blogPost = blogPostService.findById(postId);
        BlogPostDto blogPostDto = blogPostToBlogPostDtoConverter.convert(blogPost);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", blogPostDto);
    }

    @PostMapping
    public Result addBlogPost(@Valid @RequestBody BlogPostDto newPostDto) {
        BlogPost newBlogPost = blogPostDtoToBlogPostConverter.convert(newPostDto);
        BlogPost savedBlogPost = blogPostService.save(newBlogPost);
        BlogPostDto savedBlogPostDto = blogPostToBlogPostDtoConverter.convert(savedBlogPost);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedBlogPostDto);
    }

    @PutMapping("/{postId}")
        public Result updateBlogPost(@PathVariable Long postId, @Valid @RequestBody BlogPostDto blogPostDto) {
        BlogPost update = blogPostDtoToBlogPostConverter.convert(blogPostDto);
        BlogPost updatedPost = blogPostService.update(postId, update);
        BlogPostDto updatedPostDto = blogPostToBlogPostDtoConverter.convert(updatedPost);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedPostDto);
    }

    @DeleteMapping("/{postId}")
    public Result deleteBlogPost(@PathVariable Long postId) {
        this.blogPostService.delete(postId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
