package com.pregnancy.edu.blog.blogpostlike;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.blogpostlike.converter.BlogPostLikeDtoToBlogPostLikeConverter;
import com.pregnancy.edu.blog.blogpostlike.converter.BlogPostLikeToBlogPostLikeDtoConverter;
import com.pregnancy.edu.blog.blogpostlike.dto.BlogPostLikeDto;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    @MockBean
    BlogPostLikeDtoToBlogPostLikeConverter blogPostLikeDtoToBlogPostLikeConverter;

    @MockBean
    BlogPostLikeToBlogPostLikeDtoConverter blogPostLikeToBlogPostLikeDtoConverter;

    @BeforeEach
    void setUp() {
        this.likes = new ArrayList<>();

        BlogPostLike l1 = new BlogPostLike();
        l1.setId(1L);
        // Set up relationships
        BlogPost blogPost1 = new BlogPost();
        blogPost1.setId(1L);
        l1.setBlogPost(blogPost1);
        MyUser user1 = new MyUser();
        user1.setId(1L);
        user1.setUsername("user1");
        l1.setUser(user1);

        BlogPostLike l2 = new BlogPostLike();
        l2.setId(2L);
        // Set up relationships
        BlogPost blogPost2 = new BlogPost();
        blogPost2.setId(1L);
        l2.setBlogPost(blogPost2);
        MyUser user2 = new MyUser();
        user2.setId(1L);
        user2.setUsername("user1");
        l2.setUser(user2);

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
                .andExpect(jsonPath("$.message").value("Find One Success"));
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
        // Create the DTO being sent in the request
        BlogPostLikeDto likeDto = new BlogPostLikeDto(
                null,  // id will be generated
                1L,    // blogPostId
                1L,    // userId
                "user1" // username
        );

        // Mock only the service call, let the converters work as normal
        given(blogPostLikeService.save(any(BlogPostLike.class))).willAnswer(invocation -> {
            BlogPostLike like = invocation.getArgument(0);
            like.setId(3L); // Set the ID to simulate saving
            return like;
        });

        this.mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeDto)))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"));

    }
}