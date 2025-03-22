package com.pregnancy.edu.blog.blogpost;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pregnancy.edu.blog.blogpost.dto.BlogPostDto;
import com.pregnancy.edu.myuser.dto.UserDto;
import com.pregnancy.edu.system.StatusCode;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
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
    void testGetAllBlogPostsWithPaginationAndSortingSuccess() throws Exception {
        int page = 0;
        int size = 10;
        String sortBy = "heading";
        String sortDir = "asc";

        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<BlogPost> blogPostPage = new PageImpl<>(blogPosts, pageRequest, blogPosts.size());

        given(blogPostService.findAll(any(PageRequest.class))).willReturn(blogPostPage);

        this.mockMvc.perform(get(baseUrl)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("sortDir", sortDir)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data.content", Matchers.hasSize(blogPosts.size())))
                .andExpect(jsonPath("$.data.totalElements").value(blogPosts.size()))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.number").value(0));
    }

    @Test
    void testGetAllBlogPostsWithDefaultPaginationSuccess() throws Exception {
        PageRequest defaultPageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<BlogPost> blogPostPage = new PageImpl<>(blogPosts, defaultPageRequest, blogPosts.size());

        given(blogPostService.findAll(any(PageRequest.class))).willReturn(blogPostPage);

        this.mockMvc.perform(get(baseUrl)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data.content", Matchers.hasSize(blogPosts.size())))
                .andExpect(jsonPath("$.data.totalElements").value(blogPosts.size()))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.number").value(0));
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
        BlogPostDto blogPostDto = new BlogPostDto(
                null,
                "Heading 4",
                "Content 4",
                "Page Title 4",
                "Short Description 4",
                "https://example.com/image4.jpg",
                true,
                0,
                0,
                List.of("health", "pregnancy"),
                new UserDto(1L, "user", "example@gmail.com", "user", true, true, "USER", null, null, null, null, null, null, null, null, null)
        );

        BlogPost savedBlogPost = new BlogPost();
        savedBlogPost.setId(4L);
        savedBlogPost.setHeading("Heading 4");
        savedBlogPost.setContent("Content 4");
        savedBlogPost.setPageTitle("Page Title 4");
        savedBlogPost.setShortDescription("Short Description 4");
        savedBlogPost.setFeaturedImageUrl("https://example.com/image4.jpg");
        savedBlogPost.setVisible(true);
        savedBlogPost.setComments(new ArrayList<>());
        savedBlogPost.setLikes(new ArrayList<>());
        savedBlogPost.setTags(new ArrayList<>());

        String json = objectMapper.writeValueAsString(blogPostDto);

        given(blogPostService.save(any(BlogPost.class))).willReturn(savedBlogPost);

        this.mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(4))
                .andExpect(jsonPath("$.data.heading").value("Heading 4"))
                .andExpect(jsonPath("$.data.content").value("Content 4"))
                .andExpect(jsonPath("$.data.isVisible").value(true));
    }

    @Test
    void testUpdateBlogPostSuccess() throws Exception {
        BlogPostDto update = new BlogPostDto(1L, "Updated Post", "Updated Content", "Updated Title", "Updated Description", "Updated URL", true, 2, 2, new ArrayList<>(3),
                new UserDto(1L, "user", "example@gmail.com", "user", true, true, "USER", null, null, null, null, null, null, null, null, null));
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
