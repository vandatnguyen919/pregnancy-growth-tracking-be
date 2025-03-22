package com.pregnancy.edu.blog.blogpostcomment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.blogpostcomment.dto.CommentDto;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.system.StatusCode;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class BlogPostCommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BlogPostCommentService blogPostCommentService;

    @Autowired
    ObjectMapper objectMapper;

    List<BlogPostComment> comments;
    String baseUrl = "/api/v1/blog-comments";

    @BeforeEach
    void setUp() {
        this.comments = new ArrayList<>();

        BlogPostComment c1 = new BlogPostComment();
        c1.setId(1L);
        c1.setContent("Comment 1");
        c1.setCreatedAt(LocalDateTime.now());
        c1.setUpdatedAt(LocalDateTime.now());
        // Set required relationships
        c1.setBlogPost(new BlogPost());
        c1.getBlogPost().setId(1L);
        c1.setUser(new MyUser());
        c1.getUser().setId(1L);

        BlogPostComment c2 = new BlogPostComment();
        c2.setId(2L);
        c2.setContent("Comment 2");
        c2.setCreatedAt(LocalDateTime.now());
        c2.setUpdatedAt(LocalDateTime.now());
        c2.setBlogPost(new BlogPost());
        c2.getBlogPost().setId(1L);
        c2.setUser(new MyUser());
        c2.getUser().setId(1L);

        this.comments.add(c1);
        this.comments.add(c2);
    }

    @Test
    void testGetAllCommentsSuccess() throws Exception {
        given(blogPostCommentService.findAll()).willReturn(comments);

        this.mockMvc.perform(get(baseUrl).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(comments.size())));
    }

    @Test
    void testGetCommentByIdSuccess() throws Exception {
        BlogPostComment comment = comments.get(0);
        given(blogPostCommentService.findById(1L)).willReturn(comment);

        this.mockMvc.perform(get(baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.content").value("Comment 1"));
    }

    @Test
    void testGetCommentByIdNotFound() throws Exception {
        given(blogPostCommentService.findById(5L)).willThrow(new ObjectNotFoundException("comment", 5L));

        this.mockMvc.perform(get(baseUrl + "/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find comment with Id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddCommentSuccess() throws Exception {
        CommentDto commentDto = new CommentDto(
                null,
                "New Comment",
                null,
                null,
                1L,
                1L
        );

        BlogPostComment savedComment = new BlogPostComment();
        savedComment.setId(3L);
        savedComment.setContent("New Comment");
        savedComment.setCreatedAt(LocalDateTime.now());
        savedComment.setUpdatedAt(LocalDateTime.now());

        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        savedComment.setBlogPost(blogPost);

        MyUser user = new MyUser();
        user.setId(1L);
        savedComment.setUser(user);

        given(blogPostCommentService.save(any(BlogPostComment.class)))
                .willReturn(savedComment);

        this.mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.content").value("New Comment"))
                .andExpect(jsonPath("$.data.blogPostId").value(1))
                .andExpect(jsonPath("$.data.userId").value(1));
    }

    @Test
    void testUpdateCommentSuccess() throws Exception {
        CommentDto updateDto = new CommentDto(
                null,
                "Update Comment",
                null,
                null,
                1L,
                1L
        );

        BlogPostComment updatedComment = new BlogPostComment();
        updatedComment.setId(1L);
        updatedComment.setContent("Updated Comment");

        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        updatedComment.setBlogPost(blogPost);

        MyUser user = new MyUser();
        user.setId(1L);
        updatedComment.setUser(user);

        given(blogPostCommentService.update(eq(1L), any(BlogPostComment.class)))
                .willReturn(updatedComment);

        this.mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.content").value("Updated Comment"))
                .andExpect(jsonPath("$.data.blogPostId").value(1))
                .andExpect(jsonPath("$.data.userId").value(1));
    }

    @Test
    void testDeleteCommentSuccess() throws Exception {
        doNothing().when(blogPostCommentService).delete(1L);

        this.mockMvc.perform(delete(baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteCommentNotFound() throws Exception {
        doThrow(new ObjectNotFoundException("comment", 5L)).when(blogPostCommentService).delete(5L);

        this.mockMvc.perform(delete(baseUrl + "/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find comment with Id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}