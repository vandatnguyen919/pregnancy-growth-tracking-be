package com.pregnancy.edu.blogpost;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.blogpost.BlogPostService;
import com.pregnancy.edu.blog.blogpost.dto.BlogPostDto;
import com.pregnancy.edu.system.StatusCode;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
class BlogPostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BlogPostService blogPostService;

    @Autowired
    ObjectMapper objectMapper;

    String baseUrl = "http://localhost:8080/api/v1/blog-posts";

    List<BlogPost> blogPosts;

    @BeforeEach
    void setUp() {
        this.blogPosts = new ArrayList<>();

        BlogPost p1 = new BlogPost();
        p1.setId(1L);
        p1.setHeading("Heading 1");
        p1.setContent("Content 1");
        BlogPost p2 = new BlogPost();
        p2.setId(2L);
        p2.setHeading("Heading 2");
        p2.setContent("Content 2");
        BlogPost p3 = new BlogPost();
        p3.setId(3L);
        p3.setHeading("Heading 3");
        p3.setContent("Content 3");

        this.blogPosts.add(p1);
        this.blogPosts.add(p2);
        this.blogPosts.add(p3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetAllBlogPostsSuccess() throws Exception {
        given(blogPostService.findAll()).willReturn(blogPosts);

        this.mockMvc.perform(get(baseUrl).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(blogPosts.size())));
    }

    @Test
    void testGetBlogPostByIdSuccess() throws Exception {
        BlogPost p = new BlogPost();
        p.setId(1L);
        p.setHeading("Heading 1");
        p.setContent("Content 1");
        given(blogPostService.findById(1L)).willReturn(p);

        this.mockMvc.perform(get(baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.heading").value("Heading 1"));
    }

    @Test
    void testGetBlogPostByIdErrorWithNonExistentId() throws Exception {
        given(blogPostService.findById(10L)).willThrow(new ObjectNotFoundException("blog post", 10L));

        this.mockMvc.perform(get(baseUrl + "/10").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find blog post with Id 10 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddBlogPostSuccess() throws Exception {
        BlogPost post = new BlogPost();
        post.setId(4L);
        post.setHeading("Heading 4");
        post.setContent("Content 4");
        String json = objectMapper.writeValueAsString(post);

        given(blogPostService.save(any(BlogPost.class))).willReturn(post);

        this.mockMvc.perform(post(baseUrl).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.heading").value("Heading 4"))
                .andExpect(jsonPath("$.data.content").value("Content 4"));
    }

    @Test
    void testUpdateBlogPostSuccess() throws Exception {
        BlogPostDto update = new BlogPostDto(1L, "Updated Post", "Updated Content", "Updated Title", "Updated Description", "Updated URL", true);
        BlogPost updatedPost = new BlogPost();
        updatedPost.setId(2L);
        updatedPost.setHeading("Heading 2");
        updatedPost.setContent("Content 2");
        String json = objectMapper.writeValueAsString(update);

        given(blogPostService.update(eq(1L), any(BlogPost.class))).willReturn(updatedPost);

        this.mockMvc.perform(put(baseUrl + "/1").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.heading").value("Heading 2"));
    }

    @Test
    void testDeleteBlogPostSuccess() throws Exception {
        doNothing().when(blogPostService).delete(1L);

        this.mockMvc.perform(delete(baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
