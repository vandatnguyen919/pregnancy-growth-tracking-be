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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    List<MyUser> myUsers;

    @BeforeEach
    void setUp() {
        MyUser u1 = new MyUser();
        u1.setId(1L);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRole("admin");

        MyUser u2 = new MyUser();
        u2.setId(2L);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRole("user");

        MyUser u3 = new MyUser();
        u3.setId(3L);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRole("user");

        this.myUsers = new ArrayList<>();
        this.myUsers.add(u1);
        this.myUsers.add(u2);
        this.myUsers.add(u3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(userRepository.findAll()).willReturn(myUsers);

        // When
        List<MyUser> users = userService.findAll();

        // Then
        assertThat(users.size()).isEqualTo(myUsers.size());

        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        // Given
        MyUser u = new MyUser();
        u.setId(1L);
        u.setUsername("john");
        u.setPassword("123456");
        u.setEnabled(true);
        u.setRole("admin");

        given(userRepository.findById(1L)).willReturn(Optional.of(u));

        // When
        MyUser user = userService.findById(1L);

        // Then
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo("john");
        assertThat(user.getPassword()).isEqualTo("123456");
        assertThat(user.getEnabled()).isEqualTo(true);
        assertThat(user.getRole()).isEqualTo("admin");
        verify(this.userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(userRepository.findById(10L)).willReturn(Optional.empty());

        // When
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> userService.findById(10L));

        // Then
        assertThat(ex.getMessage()).isEqualTo("Could not find user with Id 10 :(");
        verify(this.userRepository, times(1)).findById(10L);
    }

    @Test
    void testSaveSuccess() {
        // Given
        MyUser u = new MyUser();
        u.setUsername("john");
        u.setPassword("123456");
        u.setEnabled(true);
        u.setRole("admin");

        given(this.passwordEncoder.encode(u.getPassword())).willReturn("Encoded Password");
        given(this.userRepository.save(u)).willReturn(u);

        // When
        MyUser user = userService.save(u);

        // Then
        assertThat(user.getUsername()).isEqualTo("john");
        assertThat(user.getPassword()).isEqualTo("Encoded Password");
        assertThat(user.getEnabled()).isEqualTo(true);
        assertThat(user.getRole()).isEqualTo("admin");
        verify(this.userRepository, times(1)).save(u);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        MyUser u = new MyUser();
        u.setId(1L);
        u.setUsername("john");
        u.setPassword("123456");
        u.setEnabled(true);
        u.setRole("admin");

        MyUser update = new MyUser();
        update.setUsername("john-update");
        update.setEnabled(true);
        update.setRole("admin");

        given(userRepository.findById(1L)).willReturn(Optional.of(u));
        given(userRepository.save(u)).willReturn(u);

        // When
        MyUser updatedUser = userService.update(1L, update);

        // Then
        assertThat(updatedUser.getId()).isEqualTo(1);
        assertThat(updatedUser.getUsername()).isEqualTo("john-update");
        assertThat(updatedUser.getEnabled()).isEqualTo(true);
        assertThat(updatedUser.getRole()).isEqualTo("admin");
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(u);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        MyUser update = new MyUser();
        update.setUsername("john-update");
        update.setEnabled(true);
        update.setRole("admin");

        given(userRepository.findById(10L)).willReturn(Optional.empty());

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
