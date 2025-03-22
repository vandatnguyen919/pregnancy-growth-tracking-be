package com.pregnancy.edu.blog.blogpost;

import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
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
class BlogPostServiceTest {

    @Mock
    BlogPostRepository blogPostRepository;

    @InjectMocks
    BlogPostService blogPostService;

    List<BlogPost> mockPosts;

    @BeforeEach
    void setUp() {
        BlogPost bp1 = new BlogPost();
        bp1.setId(1L);
        bp1.setHeading("5 steps to get pregnant");
        bp1.setPageTitle("How to get pregnant?");
        bp1.setVisible(true);
        bp1.setFeaturedImageUrl("http://www.google.com");
        bp1.setUrlHandle("http://www.google.com");
        bp1.setShortDescription("Getting pregnant is easy");
        bp1.setContent("Pregnancy, Birth and Baby's information and advice...");

        BlogPost bp2 = new BlogPost();
        bp2.setId(2L);
        bp2.setHeading("150 steps to get pregnant");
        bp2.setPageTitle("What should we do after getting pregnant?");
        bp2.setVisible(true);
        bp2.setFeaturedImageUrl("http://www.youtube.com");
        bp2.setUrlHandle("http://www.google.com");
        bp2.setShortDescription("Getting pregnant is easy");
        bp2.setContent("All you need to know about pregnancy, labour and birth...");

        this.mockPosts = new ArrayList<>();
        this.mockPosts.add(bp1);
        this.mockPosts.add(bp2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(blogPostRepository.findAll()).willReturn(mockPosts);

        // When
        List<BlogPost> blogPosts = blogPostService.findAll();

        // Then
        assertThat(blogPosts.size()).isEqualTo(mockPosts.size());
        verify(blogPostRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        // Given
        BlogPost blogPost = mockPosts.get(0);
        given(blogPostRepository.findById(1L)).willReturn(Optional.of(blogPost));

        // When
        BlogPost result = blogPostService.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(blogPostRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        Long nonExistentId = 10L;
        given(blogPostRepository.findById(nonExistentId)).willReturn(Optional.empty());

        // When & Then
        assertThrows(ObjectNotFoundException.class, () -> {
            blogPostService.findById(nonExistentId);
        });
        verify(blogPostRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void testSaveSuccess() {
        // Given
        BlogPost newPost = new BlogPost();
        newPost.setHeading("New Blog Post");
        newPost.setPageTitle("New Blog Title");

        given(blogPostRepository.save(newPost)).willReturn(newPost);

        // When
        BlogPost savedPost = blogPostService.save(newPost);

        // Then
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getHeading()).isEqualTo("New Blog Post");
        verify(blogPostRepository, times(1)).save(newPost);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        BlogPost existingPost = mockPosts.get(0);
        BlogPost updateData = new BlogPost();
        updateData.setHeading("Updated Blog Post");
        updateData.setPageTitle("Updated Blog Title");

        given(blogPostRepository.findById(1L)).willReturn(Optional.of(existingPost));
        given(blogPostRepository.save(existingPost)).willReturn(existingPost);

        // When
        BlogPost updatedPost = blogPostService.update(1L, updateData);

        //Then
        assertThat(updatedPost.getHeading()).isEqualTo("Updated Blog Post");
        verify(blogPostRepository, times(1)).findById(1L);
        verify(blogPostRepository, times(1)).save(existingPost);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        Long nonExistentId = 10L;
        BlogPost updateData = new BlogPost();
        updateData.setHeading("Updated Blog Post");

        given(blogPostRepository.findById(nonExistentId)).willReturn(Optional.empty());

        // When & Then
        assertThrows(ObjectNotFoundException.class, () -> {
            blogPostService.update(nonExistentId, updateData);
        });
        verify(blogPostRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        given(blogPostRepository.findById(1L)).willReturn(Optional.of(new BlogPost()));
        doNothing().when(blogPostRepository).deleteById(1L);

        // When
        blogPostService.delete(1L);

        // Then
        verify(blogPostRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        // Given
        Long nonExistentId = 10L;
        given(blogPostRepository.findById(nonExistentId)).willReturn(Optional.empty());

        // When & Then
        assertThrows(ObjectNotFoundException.class, () -> {
            blogPostService.delete(nonExistentId);
        });
        verify(blogPostRepository, times(1)).findById(nonExistentId);
    }
}
