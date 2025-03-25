package com.pregnancy.edu.fetusinfo.fetus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pregnancy.edu.fetusinfo.fetus.converter.FetusDtoToFetusConverter;
import com.pregnancy.edu.fetusinfo.fetus.converter.FetusToFetusDtoConverter;
import com.pregnancy.edu.fetusinfo.fetus.dto.FetusDto;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserService;
import com.pregnancy.edu.pregnancy.Pregnancy;
import com.pregnancy.edu.pregnancy.PregnancyService;
import com.pregnancy.edu.system.StatusCode;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
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
class FetusControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FetusService fetusService;

    @MockitoBean
    UserService userService;

    @MockitoBean
    PregnancyService pregnancyService;

    @Autowired
    ObjectMapper objectMapper;

    String baseUrl = "http://localhost:8080/api/v1/fetuses";

    List<Fetus> fetuses;

    @BeforeEach
    void setUp() {
        this.fetuses = new ArrayList<>();

        Fetus f1 = new Fetus();
        f1.setId(1L);
        f1.setNickName("Baby A");
        f1.setGender("MALE");
        f1.setFetusNumber(1);
        MyUser user1 = new MyUser();
        user1.setId(1L);
        f1.setUser(user1);
        Pregnancy p1 = new Pregnancy();
        p1.setId(1L);
        f1.setPregnancy(p1);
        this.fetuses.add(f1);

        Fetus f2 = new Fetus();
        f2.setId(2L);
        f2.setNickName("Baby B");
        f2.setGender("FEMALE");
        f2.setFetusNumber(2);
        MyUser user2 = new MyUser();
        user2.setId(2L);
        f2.setUser(user2);
        Pregnancy p2 = new Pregnancy();
        p2.setId(2L);
        f2.setPregnancy(p2);
        this.fetuses.add(f2);
    }

    @Test
    void testGetAllFetusesOfUserSuccess() throws Exception {
        // Given
        Long userId = 1L;
        given(fetusService.findAllByUserId(userId)).willReturn(fetuses);
        given(pregnancyService.findById(any())).willReturn(fetuses.get(0).getPregnancy());

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/user/" + userId).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All User's Fetuses Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.fetuses.size())))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].nickName").value("Baby A"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].nickName").value("Baby B"));
    }

    @Test
    void testGetFetusByIdSuccess() throws Exception {
        // Given
        Fetus f = new Fetus();
        f.setId(1L);
        f.setNickName("Baby A");
        f.setGender("MALE");
        f.setFetusNumber(1);

        given(fetusService.findById(1L)).willReturn(f);

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nickName").value("Baby A"));
    }

    @Test
    void testGetFetusByIdNotFound() throws Exception {
        // Given
        given(fetusService.findById(10L)).willThrow(new ObjectNotFoundException("fetus", 10L));

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/10").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find fetus with Id 10 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddFetusSuccess() throws Exception {
        // Given
        FetusDto newFetusDto = new FetusDto(
                null, 1L, 1L, "Baby C", "MALE"
        );

        Fetus savedFetus = new Fetus();
        savedFetus.setId(3L);
        savedFetus.setNickName("Baby C");
        savedFetus.setGender("FEMALE");
        savedFetus.setFetusNumber(3);

        MyUser user = new MyUser();
        user.setId(1L);
        savedFetus.setUser(user);

        Pregnancy pregnancy = new Pregnancy();
        pregnancy.setId(1L);
        savedFetus.setPregnancy(pregnancy);

        String json = objectMapper.writeValueAsString(newFetusDto);

        given(userService.findById(1L)).willReturn(user);
        given(pregnancyService.findById(1L)).willReturn(pregnancy);
        given(fetusService.save(any(Fetus.class))).willReturn(savedFetus);

        // When and then
        this.mockMvc.perform(post(this.baseUrl).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.nickName").value("Baby C"))
                .andExpect(jsonPath("$.data.gender").value("FEMALE"));
    }

    @Test
    void testUpdateFetusSuccess() throws Exception {
        // Given
        FetusDto updateDto = new FetusDto(
                null, 1L, 1L, "Updated Baby A", "FEMALE"
        );

        Fetus updatedFetus = new Fetus();
        updatedFetus.setId(1L);
        updatedFetus.setNickName("Updated Baby A");
        updatedFetus.setGender("FEMALE");

        MyUser user = new MyUser();
        user.setId(1L);
        updatedFetus.setUser(user);

        Pregnancy pregnancy = new Pregnancy();
        pregnancy.setId(1L);
        updatedFetus.setPregnancy(pregnancy);

        String json = objectMapper.writeValueAsString(updateDto);

        given(userService.findById(1L)).willReturn(user);
        given(pregnancyService.findById(1L)).willReturn(pregnancy);
        given(fetusService.update(eq(1L), any(Fetus.class))).willReturn(updatedFetus);

        // When and then
        this.mockMvc.perform(put(this.baseUrl + "/1").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.nickName").value("Updated Baby A"))
                .andExpect(jsonPath("$.data.gender").value("FEMALE"));
    }

    @Test
    void testUpdateFetusNotFound() throws Exception {
        // Given
        FetusDto updateDto = new FetusDto(
                null, 1L, 1L, "updated", "MALE"
        );

        String json = objectMapper.writeValueAsString(updateDto);

        given(fetusService.update(eq(10L), any(Fetus.class))).willThrow(new ObjectNotFoundException("fetus", 10L));

        // When and then
        this.mockMvc.perform(put(this.baseUrl + "/10").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find fetus with Id 10 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteFetusSuccess() throws Exception {
        // Given
        doNothing().when(fetusService).delete(1L);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteFetusNotFound() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("fetus", 10L)).when(fetusService).delete(10L);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/10").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find fetus with Id 10 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}