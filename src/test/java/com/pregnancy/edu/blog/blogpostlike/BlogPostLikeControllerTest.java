package com.pregnancy.edu.blog.blogpostlike;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pregnancy.edu.blog.blogpostlike.dto.BlogPostLikeDto;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class BlogPostLikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BlogPostLikeService blogPostLikeService;

    @Autowired
    ObjectMapper objectMapper;

    List<BlogPostLike> likes;
    String baseUrl = "/api/v1/blog-likes";

    @BeforeEach
    void setUp() {
        this.likes = new ArrayList<>();

        BlogPostLike l1 = new BlogPostLike();
        l1.setId(1L);

        BlogPostLike l2 = new BlogPostLike();
        l2.setId(2L);

        this.likes.add(l1);
        this.likes.add(l2);
    }

    @Test
    void testGetAllLikesSuccess() throws Exception {
        given(blogPostLikeService.findAll()).willReturn(likes);

        this.mockMvc.perform(get(baseUrl).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(likes.size())));
    }

    @Test
    void testGetLikeByIdSuccess() throws Exception {
        BlogPostLike like = likes.get(0);
        given(blogPostLikeService.findById(1L)).willReturn(like);

        this.mockMvc.perform(get(baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void testGetLikeByIdNotFound() throws Exception {
        given(blogPostLikeService.findById(5L)).willThrow(new ObjectNotFoundException("like", 5L));

        this.mockMvc.perform(get(baseUrl + "/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find like with Id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddLikeSuccess() throws Exception {
        BlogPostLikeDto likeDto = new BlogPostLikeDto(null, 1L, 1L, "user1");
        String json = objectMapper.writeValueAsString(likeDto);

        BlogPostLike savedLike = new BlogPostLike();
        savedLike.setId(3L);

        given(blogPostLikeService.save(any(BlogPostLike.class))).willReturn(savedLike);

        this.mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(3));
    }

    @Test
    void testDeleteLikeSuccess() throws Exception {
        doNothing().when(blogPostLikeService).delete(1L);

        this.mockMvc.perform(delete(baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}