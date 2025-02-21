package com.pregnancy.edu.blog.tag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pregnancy.edu.blog.tag.dto.TagDto;
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
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TagControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TagService tagService;

    @Autowired
    ObjectMapper objectMapper;

    List<Tag> tags;
    String baseUrl = "/api/v1/tags";

    @BeforeEach
    void setUp() {
        this.tags = new ArrayList<>();

        Tag t1 = new Tag();
        t1.setId(1L);
        t1.setName("Tag 1");

        Tag t2 = new Tag();
        t2.setId(2L);
        t2.setName("Tag 2");

        this.tags.add(t1);
        this.tags.add(t2);
    }

    @Test
    void testGetAllTagsSuccess() throws Exception {
        given(tagService.findAll()).willReturn(tags);

        this.mockMvc.perform(get(baseUrl).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(tags.size())));
    }

    @Test
    void testGetTagByIdSuccess() throws Exception {
        Tag tag = tags.get(0);
        given(tagService.findById(1L)).willReturn(tag);

        this.mockMvc.perform(get(baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Tag 1"));
    }

    @Test
    void testGetTagByIdNotFound() throws Exception {
        given(tagService.findById(5L)).willThrow(new ObjectNotFoundException("tag", 5L));

        this.mockMvc.perform(get(baseUrl + "/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find tag with Id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddTagSuccess() throws Exception {
        TagDto tagDto = new TagDto(null, "New Tag", 0);
        String json = objectMapper.writeValueAsString(tagDto);

        Tag savedTag = new Tag();
        savedTag.setId(3L);
        savedTag.setName("New Tag");

        given(tagService.save(any(Tag.class))).willReturn(savedTag);

        this.mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.name").value("New Tag"));
    }

    @Test
    void testUpdateTagSuccess() throws Exception {
        TagDto updateDto = new TagDto(1L, "Updated Tag", 6);
        String json = objectMapper.writeValueAsString(updateDto);

        Tag updatedTag = new Tag();
        updatedTag.setId(1L);
        updatedTag.setName("Updated Tag");

        given(tagService.update(any(Long.class), any(Tag.class))).willReturn(updatedTag);

        this.mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Updated Tag"));
    }

    @Test
    void testDeleteTagSuccess() throws Exception {
        doNothing().when(tagService).delete(1L);

        this.mockMvc.perform(delete(baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteTagNotFound() throws Exception {
        doThrow(new ObjectNotFoundException("tag", 5L)).when(tagService).delete(5L);

        this.mockMvc.perform(delete(baseUrl + "/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find tag with Id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}