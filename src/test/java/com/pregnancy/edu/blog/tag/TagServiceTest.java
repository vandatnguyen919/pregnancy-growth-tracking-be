package com.pregnancy.edu.blog.tag;

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
class TagServiceTest {

    @Mock
    TagRepository tagRepository;

    @InjectMocks
    TagService tagService;

    List<Tag> mockTags;

    @BeforeEach
    void setUp() {
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Tag 1");

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("Tag 2");

        this.mockTags = new ArrayList<>();
        this.mockTags.add(tag1);
        this.mockTags.add(tag2);
    }

    @Test
    void testFindAllSuccess() {
        given(tagRepository.findAll()).willReturn(mockTags);

        List<Tag> tags = tagService.findAll();

        assertThat(tags.size()).isEqualTo(mockTags.size());
        verify(tagRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        Tag tag = mockTags.get(0);
        given(tagRepository.findById(1L)).willReturn(Optional.of(tag));

        Tag result = tagService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Tag 1");
        verify(tagRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        given(tagRepository.findById(10L)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> tagService.findById(10L));
        verify(tagRepository, times(1)).findById(10L);
    }

    @Test
    void testSaveSuccess() {
        Tag newTag = new Tag();
        newTag.setName("New Tag");

        given(tagRepository.save(newTag)).willReturn(newTag);

        Tag savedTag = tagService.save(newTag);

        assertThat(savedTag).isNotNull();
        assertThat(savedTag.getName()).isEqualTo("New Tag");
        verify(tagRepository, times(1)).save(newTag);
    }

    @Test
    void testUpdateSuccess() {
        Tag existingTag = mockTags.get(0);
        Tag updateData = new Tag();
        updateData.setName("Updated Tag");

        given(tagRepository.findById(1L)).willReturn(Optional.of(existingTag));
        given(tagRepository.save(existingTag)).willReturn(existingTag);

        Tag updatedTag = tagService.update(1L, updateData);

        assertThat(updatedTag.getName()).isEqualTo("Updated Tag");
        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, times(1)).save(existingTag);
    }

    @Test
    void testDeleteSuccess() {
        given(tagRepository.findById(1L)).willReturn(Optional.of(new Tag()));
        doNothing().when(tagRepository).deleteById(1L);

        tagService.delete(1L);

        verify(tagRepository, times(1)).deleteById(1L);
    }
}