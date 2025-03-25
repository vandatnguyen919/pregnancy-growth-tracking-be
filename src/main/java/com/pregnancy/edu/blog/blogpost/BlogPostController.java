package com.pregnancy.edu.blog.blogpost;

import com.pregnancy.edu.blog.blogpost.converter.BlogPostDtoToBlogPostConverter;
import com.pregnancy.edu.blog.blogpost.converter.BlogPostToBlogPostDtoConverter;
import com.pregnancy.edu.blog.blogpost.dto.BlogPostDto;
import com.pregnancy.edu.blog.tag.Tag;
import com.pregnancy.edu.blog.tag.TagService;
import com.pregnancy.edu.myuser.UserService;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/blog-posts")
public class BlogPostController {

    private final BlogPostService blogPostService;
    private final TagService tagService;
    private final BlogPostToBlogPostDtoConverter blogPostToBlogPostDtoConverter;
    private final BlogPostDtoToBlogPostConverter blogPostDtoToBlogPostConverter;
    private final UserService userService;

    public BlogPostController(BlogPostService blogPostService, TagService tagService, BlogPostToBlogPostDtoConverter blogPostToBlogPostDtoConverter, BlogPostDtoToBlogPostConverter blogPostDtoToBlogPostConverter, UserService userService) {
        this.blogPostService = blogPostService;
        this.tagService = tagService;
        this.blogPostToBlogPostDtoConverter = blogPostToBlogPostDtoConverter;
        this.blogPostDtoToBlogPostConverter = blogPostDtoToBlogPostConverter;
        this.userService = userService;
    }

    @GetMapping
    public Result getAllBlogPosts(Pageable pageable) {
        Page<BlogPost> blogPostPage = blogPostService.findAll(pageable);
        Page<BlogPostDto> blogPostDtoPage = blogPostPage.map(this.blogPostToBlogPostDtoConverter::convert);
        return new Result(true, StatusCode.SUCCESS, "Find All Success", blogPostDtoPage);
    }

    @GetMapping("/{postId}")
    public Result getBlogPostById(@PathVariable Long postId) {
        BlogPost blogPost = blogPostService.findById(postId);
        BlogPostDto blogPostDto = blogPostToBlogPostDtoConverter.convert(blogPost);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", blogPostDto);
    }

    @PostMapping
    public Result addBlogPost(@Valid @RequestBody BlogPostDto newPostDto, JwtAuthenticationToken jwtAuthenticationToken) {
        Jwt jwt = jwtAuthenticationToken.getToken();
        Long userId = jwt.getClaim("userId");
        BlogPost newBlogPost = blogPostDtoToBlogPostConverter.convert(newPostDto);
        newBlogPost.setUser(userService.findById(userId));
        BlogPost savedBlogPost = blogPostService.save(newBlogPost);
        BlogPostDto savedBlogPostDto = blogPostToBlogPostDtoConverter.convert(savedBlogPost);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedBlogPostDto);
    }

    @PutMapping("/{postId}")
    public Result updateBlogPost(@PathVariable Long postId, @Valid @RequestBody BlogPostDto blogPostDto) {
        BlogPost update = blogPostDtoToBlogPostConverter.convert(blogPostDto);
        BlogPost updatedPost = blogPostService.update(postId, update);

        List<Tag> tagList = new ArrayList<>();
        for(String tagName : blogPostDto.nameTags()) {
            Tag existingTag = tagService.findByNameIgnoreCase(tagName);
            if(existingTag != null) {
                tagList.add(existingTag);
            } else {
                return new Result(false, StatusCode.NOT_FOUND, "No Tag found with name " + tagName, null);
            }
        }
        updatedPost.setTags(tagList);
        updatedPost = blogPostService.save(updatedPost);
        BlogPostDto updatedPostDto = blogPostToBlogPostDtoConverter.convert(updatedPost);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedPostDto);
    }

    @DeleteMapping("/{postId}")
    public Result deleteBlogPost(@PathVariable Long postId) {
        this.blogPostService.delete(postId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
