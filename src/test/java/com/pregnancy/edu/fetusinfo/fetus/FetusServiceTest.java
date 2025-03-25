package com.pregnancy.edu.fetusinfo.fetus;

import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserRepository;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetusServiceTest {

    @Mock
    FetusRepository fetusRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    FetusService fetusService;

    List<Fetus> fetusList;

    @BeforeEach
    void setUp() {
        this.fetusList = new ArrayList<>();

        Fetus f1 = new Fetus();
        f1.setId(1L);
        f1.setNickName("Baby A");
        f1.setGender("MALE");
        f1.setFetusNumber(1);

        Fetus f2 = new Fetus();
        f2.setId(2L);
        f2.setNickName("Baby B");
        f2.setGender("FEMALE");
        f2.setFetusNumber(2);

        this.fetusList.add(f1);
        this.fetusList.add(f2);
    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(fetusRepository.findAll()).willReturn(fetusList);

        // When
        List<Fetus> fetuses = fetusService.findAll();

        // Then
        assertThat(fetuses.size()).isEqualTo(fetusList.size());
        verify(fetusRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        // Given
        Fetus f = new Fetus();
        f.setId(1L);
        f.setNickName("Baby A");
        f.setGender("MALE");
        f.setFetusNumber(1);

        given(fetusRepository.findById(1L)).willReturn(Optional.of(f));

        // When
        Fetus fetus = fetusService.findById(1L);

        // Then
        assertThat(fetus.getId()).isEqualTo(1L);
        assertThat(fetus.getNickName()).isEqualTo("Baby A");
        verify(fetusRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(fetusRepository.findById(10L)).willReturn(Optional.empty());

        // When
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> fetusService.findById(10L));

        // Then
        assertThat(ex.getMessage()).contains("Could not find fetus with Id 10");
        verify(fetusRepository, times(1)).findById(10L);
    }

    @Test
    void testSaveSuccess() {
        // Given
        Fetus newFetus = new Fetus();
        newFetus.setNickName("Baby C");
        newFetus.setGender("FEMALE");
        newFetus.setFetusNumber(3);

        given(fetusRepository.save(newFetus)).willReturn(newFetus);

        // When
        Fetus savedFetus = fetusService.save(newFetus);

        // Then
        assertThat(savedFetus.getNickName()).isEqualTo("Baby C");
        verify(fetusRepository, times(1)).save(newFetus);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        Fetus existingFetus = new Fetus();
        existingFetus.setId(1L);
        existingFetus.setNickName("Baby A");

        Fetus updateFetus = new Fetus();
        updateFetus.setNickName("Updated Baby A");
        updateFetus.setGender("FEMALE");

        given(fetusRepository.findById(1L)).willReturn(Optional.of(existingFetus));
        given(fetusRepository.save(existingFetus)).willReturn(existingFetus);

        // When
        Fetus updatedFetus = fetusService.update(1L, updateFetus);

        // Then
        assertThat(updatedFetus.getNickName()).isEqualTo("Updated Baby A");
        assertThat(updatedFetus.getGender()).isEqualTo("FEMALE");
        verify(fetusRepository, times(1)).findById(1L);
        verify(fetusRepository, times(1)).save(existingFetus);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        Fetus updateFetus = new Fetus();
        updateFetus.setNickName("Updated Baby");

        given(fetusRepository.findById(10L)).willReturn(Optional.empty());

        // When
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class,
                () -> fetusService.update(10L, updateFetus));

        // Then
        assertThat(ex.getMessage()).contains("Could not find fetus with Id 10");
        verify(fetusRepository, times(1)).findById(10L);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        given(fetusRepository.findById(1L)).willReturn(Optional.of(new Fetus()));
        doNothing().when(fetusRepository).deleteById(1L);

        // When
        fetusService.delete(1L);

        // Then
        verify(fetusRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(fetusRepository.findById(10L)).willReturn(Optional.empty());

        // When
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class,
                () -> fetusService.delete(10L));

        // Then
        assertThat(ex.getMessage()).contains("Could not find fetus with Id 10");
        verify(fetusRepository, times(1)).findById(10L);
    }

    @Test
    void testFindAllByUserIdSuccess() {
        // Given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(new MyUser()));
        given(fetusRepository.findAllByUserId(userId)).willReturn(fetusList);

        // When
        List<Fetus> userFetuses = fetusService.findAllByUserId(userId);

        // Then
        assertThat(userFetuses.size()).isEqualTo(fetusList.size());
        verify(userRepository, times(1)).findById(userId);
        verify(fetusRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void testFindAllByUserIdNotFound() {
        // Given
        Long userId = 10L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // When
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class,
                () -> fetusService.findAllByUserId(userId));

        // Then
        assertThat(ex.getMessage()).contains("Could not find user with Id 10");
        verify(userRepository, times(1)).findById(userId);
    }
}