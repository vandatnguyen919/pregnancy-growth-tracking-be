package com.pregnancy.edu.blog.blogpostlike;

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
class BlogPostLikeServiceTest {

    @Mock
    BlogPostLikeRepository blogPostLikeRepository;

    @InjectMocks
    BlogPostLikeService blogPostLikeService;

    List<BlogPostLike> mockLikes;

    @BeforeEach
    void setUp() {
        BlogPostLike like1 = new BlogPostLike();
        like1.setId(1L);

        BlogPostLike like2 = new BlogPostLike();
        like2.setId(2L);

        this.mockLikes = new ArrayList<>();
        this.mockLikes.add(like1);
        this.mockLikes.add(like2);
    }

    @Test
    void testFindAllSuccess() {
        given(blogPostLikeRepository.findAll()).willReturn(mockLikes);

        List<BlogPostLike> likes = blogPostLikeService.findAll();

        assertThat(likes.size()).isEqualTo(mockLikes.size());
        verify(blogPostLikeRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        BlogPostLike like = mockLikes.get(0);
        given(blogPostLikeRepository.findById(1L)).willReturn(Optional.of(like));

        BlogPostLike result = blogPostLikeService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(blogPostLikeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        given(blogPostLikeRepository.findById(10L)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> blogPostLikeService.findById(10L));
        verify(blogPostLikeRepository, times(1)).findById(10L);
    }

    @Test
    void testSaveSuccess() {
        BlogPostLike newLike = new BlogPostLike();
        given(blogPostLikeRepository.save(newLike)).willReturn(newLike);

        BlogPostLike savedLike = blogPostLikeService.save(newLike);

        assertThat(savedLike).isNotNull();
        verify(blogPostLikeRepository, times(1)).save(newLike);
    }

    @Test
    void testDeleteSuccess() {
        given(blogPostLikeRepository.findById(1L)).willReturn(Optional.of(new BlogPostLike()));
        doNothing().when(blogPostLikeRepository).deleteById(1L);

        blogPostLikeService.delete(1L);

        verify(blogPostLikeRepository, times(1)).deleteById(1L);
    }
}