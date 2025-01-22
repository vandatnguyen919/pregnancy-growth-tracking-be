package com.pregnancy.edu.myuser;

import com.fasterxml.jackson.databind.ObjectMapper;
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
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    String baseUrl = "http://localhost:8080/api/v1";

    List<MyUser> users;

    @BeforeEach
    void setUp() {
        this.users = new ArrayList<>();

        MyUser u1 = new MyUser();
        u1.setId(1L);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRole("admin");
        this.users.add(u1);

        MyUser u2 = new MyUser();
        u2.setId(2L);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRole("user");
        this.users.add(u2);

        MyUser u3 = new MyUser();
        u3.setId(3L);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRole("user");
        this.users.add(u3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
        // Given
        given(userService.findAll()).willReturn(users);

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.users.size())))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("john"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].username").value("eric"));
    }

    @Test
    void testFindUserByIdSuccess() throws Exception {
        // Given
        MyUser u = new MyUser();
        u.setId(1L);
        u.setUsername("john");
        u.setPassword("123456");
        u.setEnabled(true);
        u.setRole("admin");

        given(userService.findById(1L)).willReturn(u);

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("john"));
    }

    @Test
    void testFindUserByIdErrorWithNonExistentId() throws Exception {
        // Given
        given(userService.findById(10L)).willThrow(new ObjectNotFoundException("user", 10L));

        // When and then
        this.mockMvc.perform(get(this.baseUrl + "/users/10").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id 10 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddUserSuccess() throws Exception {
        // Given
        MyUser u = new MyUser();
        u.setId(10L);
        u.setUsername("john");
        u.setPassword("123456");
        u.setEnabled(true);
        u.setRole("admin");

        String json = this.objectMapper.writeValueAsString(u);

        given(userService.save(any(MyUser.class))).willReturn(u);

        // When and then
        this.mockMvc.perform(post(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("john"))
                .andExpect(jsonPath("$.data.enabled").value(true))
                .andExpect(jsonPath("$.data.role").value("admin"));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        // Given
        UserDto update = new UserDto(null, "john-update", "John", true, "admin");

        MyUser updatedUser = new MyUser();
        updatedUser.setId(1L);
        updatedUser.setUsername("john-update");
        updatedUser.setEnabled(false);
        updatedUser.setRole("admin");

        String json = objectMapper.writeValueAsString(update);

        given(userService.update(eq(1L), any(MyUser.class))).willReturn(updatedUser);

        // When and then
        this.mockMvc.perform(put(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("john-update"))
                .andExpect(jsonPath("$.data.enabled").value(false))
                .andExpect(jsonPath("$.data.role").value("admin"));
    }

    @Test
    void testUserUpdateUserErrorWithNonExistentId() throws Exception {
        // Given
        UserDto update = new UserDto(null, "john-update", "John", true, "admin");

        String json = objectMapper.writeValueAsString(update);

        given(userService.update(eq(10L), any(MyUser.class))).willThrow(new ObjectNotFoundException("user", 10L));

        // When and then
        this.mockMvc.perform(put(this.baseUrl + "/users/10").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id 10 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testUserDeleteSuccess() throws Exception {
        // Given
        doNothing().when(userService).delete(10L);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/users/10").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testUserDeleteErrorWithNonExistentId() throws Exception {
        // Given
        doThrow(new ObjectNotFoundException("user", 10L)).when(userService).delete(10L);

        // When and then
        this.mockMvc.perform(delete(this.baseUrl + "/users/10").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id 10 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
