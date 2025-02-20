package com.pregnancy.edu.blog.blogpostcomment;

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
class BlogPostCommentServiceTest {

    @Mock
    BlogPostCommentRepository blogPostCommentRepository;

    @InjectMocks
    BlogPostCommentService blogPostCommentService;

    List<BlogPostComment> mockComments;

    @BeforeEach
    void setUp() {
        BlogPostComment comment1 = new BlogPostComment();
        comment1.setId(1L);
        comment1.setContent("Test Comment 1");

        BlogPostComment comment2 = new BlogPostComment();
        comment2.setId(2L);
        comment2.setContent("Test Comment 2");

        this.mockComments = new ArrayList<>();
        this.mockComments.add(comment1);
        this.mockComments.add(comment2);
    }

    @Test
    void testFindAllSuccess() {
        given(blogPostCommentRepository.findAll()).willReturn(mockComments);

        List<BlogPostComment> comments = blogPostCommentService.findAll();

        assertThat(comments.size()).isEqualTo(mockComments.size());
        verify(blogPostCommentRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        BlogPostComment comment = mockComments.get(0);
        given(blogPostCommentRepository.findById(1L)).willReturn(Optional.of(comment));

        BlogPostComment result = blogPostCommentService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(blogPostCommentRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        given(blogPostCommentRepository.findById(10L)).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> blogPostCommentService.findById(10L));
        verify(blogPostCommentRepository, times(1)).findById(10L);
    }

    @Test
    void testSaveSuccess() {
        BlogPostComment newComment = new BlogPostComment();
        newComment.setContent("New Comment");

        given(blogPostCommentRepository.save(newComment)).willReturn(newComment);

        BlogPostComment savedComment = blogPostCommentService.save(newComment);

        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("New Comment");
        verify(blogPostCommentRepository, times(1)).save(newComment);
    }

    @Test
    void testUpdateSuccess() {
        BlogPostComment existingComment = mockComments.get(0);
        BlogPostComment updateData = new BlogPostComment();
        updateData.setContent("Updated Comment");

        given(blogPostCommentRepository.findById(1L)).willReturn(Optional.of(existingComment));
        given(blogPostCommentRepository.save(existingComment)).willReturn(existingComment);

        BlogPostComment updatedComment = blogPostCommentService.update(1L, updateData);

        assertThat(updatedComment.getContent()).isEqualTo("Updated Comment");
        verify(blogPostCommentRepository, times(1)).findById(1L);
        verify(blogPostCommentRepository, times(1)).save(existingComment);
    }

    @Test
    void testDeleteSuccess() {
        given(blogPostCommentRepository.findById(1L)).willReturn(Optional.of(new BlogPostComment()));
        doNothing().when(blogPostCommentRepository).deleteById(1L);

        blogPostCommentService.delete(1L);

        verify(blogPostCommentRepository, times(1)).deleteById(1L);
    }
}