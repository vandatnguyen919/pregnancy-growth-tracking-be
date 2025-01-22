package com.pregnancy.edu.myuser;

import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    List<MyUser> users;

    @BeforeEach
    void setUp() {
        MyUser user1 = new MyUser();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setPhoneNumber("1234567890");
        user1.setFullName("User One");

        MyUser user2 = new MyUser();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setPhoneNumber("0987654321");
        user2.setFullName("User Two");

        MyUser user3 = new MyUser();
        user3.setId(3L);
        user3.setEmail("user3@example.com");
        user3.setUsername("user3");
        user3.setPassword("password3");
        user3.setPhoneNumber("1122334455");
        user3.setFullName("User Three");

        users = Arrays.asList(user1, user2, user3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(userRepository.findAll()).willReturn(users);

        // When
        List<MyUser> myUsers = userService.findAll();

        // Then
        assertThat(myUsers.size()).isEqualTo(this.users.size());

        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        // Given
        MyUser u = new MyUser();
        u.setId(1L);
        u.setEmail("user@example.com");
        u.setUsername("user");
        u.setPassword("password");
        u.setPhoneNumber("1234567890");
        u.setFullName("My User");

        given(this.userRepository.findById(1L)).willReturn(Optional.of(u));

        // When
        MyUser user = this.userService.findById(1L);

        // Then
        assertThat(user.getId()).isEqualTo(u.getId());
        verify(this.userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(this.userRepository.findById(10L)).willReturn(Optional.empty());

        // When
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> this.userService.findById(10L));

        // Then
        assertThat(ex.getMessage()).isEqualTo("Could not find user with Id 10 :(");
        verify(this.userRepository, times(1)).findById(10L);
    }

    @Test
    void testSaveSuccess() {
        // Given
        MyUser u = new MyUser();
        u.setEmail("user@example.com");
        u.setUsername("user");
        u.setPassword("password");
        u.setPhoneNumber("1234567890");
        u.setFullName("My User");

        given(this.passwordEncoder.encode(u.getPassword())).willReturn("encodedPassword");
        given(this.userRepository.save(u)).willReturn(u);

        // When
        MyUser newUser = userService.save(u);

        // Then
        assertThat(newUser.getEmail()).isEqualTo("user@example.com");
        assertThat(newUser.getUsername()).isEqualTo("user");
        assertThat(newUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(newUser.getPhoneNumber()).isEqualTo("1234567890");
        assertThat(newUser.getFullName()).isEqualTo("My User");
    }

    @Test
    void testUpdateSuccess() {
        // Given
        MyUser u = new MyUser();
        u.setId(1L);
        u.setEmail("user@example.com");
        u.setUsername("user");
        u.setPassword("password");
        u.setPhoneNumber("1234567890");
        u.setFullName("My User");

        MyUser update = new MyUser();
        update.setFullName("My User-update");

        given(this.userRepository.findById(1L)).willReturn(Optional.of(u));
        given(this.userRepository.save(u)).willReturn(u);

        // When
        MyUser updatedUser = this.userService.update(1L, update);

        // Then
        assertThat(updatedUser.getFullName()).isEqualTo("My User-update");
        verify(this.userRepository, times(1)).findById(1L);
        verify(this.userRepository, times(1)).save(u);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        MyUser update = new MyUser();
        update.setFullName("My User-update");

        given(this.userRepository.findById(10L)).willReturn(Optional.empty());

        // When
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> userService.update(10L, update));

        // Then
        assertThat(ex.getMessage()).isEqualTo("Could not find user with Id 10 :(");
        verify(this.userRepository, times(1)).findById(10L);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        given(this.userRepository.findById(1L)).willReturn(Optional.of(new MyUser()));
        doNothing().when(this.userRepository).deleteById(1L);

        // When
        this.userService.delete(1L);

        // Then
        verify(this.userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(this.userRepository.findById(1L)).willReturn(Optional.empty());

        // When
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> this.userService.delete(1L));

        // Then
        assertThat(ex.getMessage()).isEqualTo("Could not find user with Id 1 :(");
        verify(userRepository, times(1)).findById(1L);
    }
}
