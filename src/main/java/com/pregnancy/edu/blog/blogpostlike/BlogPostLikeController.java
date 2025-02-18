package com.pregnancy.edu.blog.blogpostlike;

import com.pregnancy.edu.blog.blogpostlike.converter.BlogPostLikeDtoToBlogPostLikeConverter;
import com.pregnancy.edu.blog.blogpostlike.converter.BlogPostLikeToBlogPostLikeDtoConverter;
import com.pregnancy.edu.blog.blogpostlike.dto.BlogPostLikeDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blog-likes")
public class BlogPostLikeController {

    private final BlogPostLikeService blogPostLikeService;
    private final BlogPostLikeToBlogPostLikeDtoConverter toBlogPostLikeDtoConverter;
    private final BlogPostLikeDtoToBlogPostLikeConverter toBlogPostLikeConverter;

    public BlogPostLikeController(BlogPostLikeService blogPostLikeService,
                                  BlogPostLikeToBlogPostLikeDtoConverter toBlogPostLikeDtoConverter,
                                  BlogPostLikeDtoToBlogPostLikeConverter toBlogPostLikeConverter) {
        this.blogPostLikeService = blogPostLikeService;
        this.toBlogPostLikeDtoConverter = toBlogPostLikeDtoConverter;
        this.toBlogPostLikeConverter = toBlogPostLikeConverter;
    }

    @GetMapping
    public Result getAllLikes() {
        List<BlogPostLike> likes = blogPostLikeService.findAll();
        List<BlogPostLikeDto> likeDtos = likes.stream()
                .map(toBlogPostLikeDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", likeDtos);
    }

    @GetMapping("/{likeId}")
    public Result getLikeById(@PathVariable Long likeId) {
        BlogPostLike like = blogPostLikeService.findById(likeId);
        BlogPostLikeDto likeDto = toBlogPostLikeDtoConverter.convert(like);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", likeDto);
    }

    @PostMapping
    public Result addLike(@Valid @RequestBody BlogPostLikeDto likeDto) {
        BlogPostLike like = toBlogPostLikeConverter.convert(likeDto);
        BlogPostLike savedLike = blogPostLikeService.save(like);
        BlogPostLikeDto savedLikeDto = toBlogPostLikeDtoConverter.convert(savedLike);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedLikeDto);
    }
}