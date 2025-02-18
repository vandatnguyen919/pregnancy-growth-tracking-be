package com.pregnancy.edu.blog.blogpostcomment;

import com.pregnancy.edu.blog.blogpostcomment.converter.CommentDtoToCommentConverter;
import com.pregnancy.edu.blog.blogpostcomment.converter.CommentToCommentDtoConverter;
import com.pregnancy.edu.blog.blogpostcomment.dto.CommentDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blog-comments")
public class BlogPostCommentController {

    private final BlogPostCommentService blogPostCommentService;
    private final CommentToCommentDtoConverter toBlogPostCommentDtoConverter;
    private final CommentDtoToCommentConverter toBlogPostCommentConverter;

    public BlogPostCommentController(BlogPostCommentService blogPostCommentService,
                                     CommentToCommentDtoConverter toBlogPostCommentDtoConverter,
                                     CommentDtoToCommentConverter toBlogPostCommentConverter) {
        this.blogPostCommentService = blogPostCommentService;
        this.toBlogPostCommentDtoConverter = toBlogPostCommentDtoConverter;
        this.toBlogPostCommentConverter = toBlogPostCommentConverter;
    }

    @GetMapping
    public Result getAllComments() {
        List<BlogPostComment> comments = blogPostCommentService.findAll();
        List<CommentDto> commentDtos = comments.stream()
                .map(toBlogPostCommentDtoConverter::convert)
                .toList();
        return new Result(true, StatusCode.SUCCESS, "Find All Success", commentDtos);
    }

    @GetMapping("/{commentId}")
    public Result getCommentById(@PathVariable Long commentId) {
        BlogPostComment comment = blogPostCommentService.findById(commentId);
        CommentDto commentDto = toBlogPostCommentDtoConverter.convert(comment);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", commentDto);
    }

    @PostMapping
    public Result addComment(@Valid @RequestBody CommentDto commentDto) {
        BlogPostComment comment = toBlogPostCommentConverter.convert(commentDto);
        BlogPostComment savedComment = blogPostCommentService.save(comment);
        CommentDto savedCommentDto = toBlogPostCommentDtoConverter.convert(savedComment);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedCommentDto);
    }

    @PutMapping("/{commentId}")
    public Result updateComment(@PathVariable Long commentId, @Valid @RequestBody CommentDto commentDto) {
        BlogPostComment comment = toBlogPostCommentConverter.convert(commentDto);
        BlogPostComment updatedComment = blogPostCommentService.update(commentId, comment);
        CommentDto updatedCommentDto = toBlogPostCommentDtoConverter.convert(updatedComment);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedCommentDto);
    }

    @DeleteMapping("/{commentId}")
    public Result deleteComment(@PathVariable Long commentId) {
        blogPostCommentService.delete(commentId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}