package com.movieflix.service;


import com.movieflix.domain.Category;
import com.movieflix.domain.Video;
import com.movieflix.domain.dto.CategoryResponseDTO;
import com.movieflix.domain.dto.VideoRequestDTO;
import com.movieflix.domain.dto.VideoResponseDTO;
import com.movieflix.exception.CategoryNotFoundException;
import com.movieflix.exception.DataNotFoundException;
import com.movieflix.repository.CategoryRepository;
import com.movieflix.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    private VideoService videoService;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void init() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        this.videoService = new VideoService(videoRepository, categoryRepository, modelMapper);
        ReflectionTestUtils.setField(videoService, "defaultCategoryId", "111");
    }

    @Test
    void findAll_whenCalled_shouldReturnVideoList() {
        List<Video> videos = List.of(Video.builder().id("123").title("Title").description("Description").url("url").build());
        List<VideoResponseDTO> expectedResponse = List.of(VideoResponseDTO.builder().id("123").title("Title").description("Description").url("url").build());
        when(videoRepository.findAll()).thenReturn(videos);

        List<VideoResponseDTO> response = videoService.findAll();

        assertEquals(expectedResponse, response);
    }

    @Test
    void create_whenCalled_shouldReturnCreatedVideoWithId() {
        Category category = Category.builder().id("catId").build();
        Video video = Video.builder().id("123").title("Title").description("Description").url("url").category(category).build();
        VideoRequestDTO request = VideoRequestDTO.builder().title("Title").description("Description").url("url").categoryId("catId").build();
        VideoResponseDTO expectedResponse = VideoResponseDTO.builder()
                .id("123")
                .title("Title")
                .description("Description")
                .url("url")
                .category(CategoryResponseDTO.builder().id("catId").build())
                .build();

        when(videoRepository.save(any())).thenReturn(video);
        when(categoryRepository.findById("catId")).thenReturn(Optional.of(Category.builder().id("catId").build()));

        VideoResponseDTO response = videoService.create(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    void create_whenCalledWithoutCategoryId_shouldReturnCreatedVideoWithDefaultCategory() {
        Category category = Category.builder().id("111").build();
        Video video = Video.builder().id("123").title("Title").description("Description").url("url").category(category).build();
        VideoRequestDTO request = VideoRequestDTO.builder().title("Title").description("Description").url("url").build();
        VideoResponseDTO expectedResponse = VideoResponseDTO.builder()
                .id("123")
                .title("Title")
                .description("Description")
                .url("url")
                .category(CategoryResponseDTO.builder().id("111").build())
                .build();

        when(videoRepository.save(any())).thenReturn(video);
        when(categoryRepository.findById("111")).thenReturn(Optional.of(category));

        VideoResponseDTO response = videoService.create(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    void create_whenCalledWithInvalidCategoryId_shouldThrowException() {
        VideoRequestDTO request = VideoRequestDTO.builder().title("Title").description("Description").url("url").categoryId("catId").build();

        when(categoryRepository.findById("catId")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> videoService.create(request));
        verify(videoRepository, never()).save(any());
    }

    @Test
    void findById_whenFound_shouldReturnVideo() {
        Video video = Video.builder().id("123").title("Title").description("Description").url("url").build();
        String id = "123";
        VideoResponseDTO expectedResponse =VideoResponseDTO.builder().id("123").title("Title").description("Description").url("url").build();

        when(videoRepository.findById(id)).thenReturn(Optional.of(video));

        VideoResponseDTO response = videoService.findById(id);

        assertEquals(expectedResponse, response);
    }

    @Test
    void findById_whenNotFound_shouldThrowException() {
        String id = "123";

        when(videoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> videoService.findById(id));
    }

    @Test
    void update_whenFound_shouldReturnUpdatedVideo() {
        Video video = Video.builder().id("123").title("Title").description("Description").url("url").category(Category.builder().id("111").build()).build();
        VideoRequestDTO request = VideoRequestDTO.builder().title("Title updated").description("Description updated").url("url updated").build();
        VideoResponseDTO expectedResponse = VideoResponseDTO.builder()
                .id("123")
                .title("Title updated")
                .description("Description updated")
                .url("url updated")
                .category(CategoryResponseDTO.builder().id("111").build())
                .build();

        when(videoRepository.findById("123")).thenReturn(Optional.of(video));
        when(videoRepository.save(any())).thenReturn(video);
        when(categoryRepository.findById(any())).thenReturn(Optional.of(Category.builder().id("111").build()));

        VideoResponseDTO response = videoService.update("123", request);

        assertEquals(expectedResponse, response);
    }

    @Test
    void update_whenNotFound_shouldThrowException() {
        VideoRequestDTO request = VideoRequestDTO.builder().title("Title updated").description("Description updated").url("url updated").build();

        when(videoRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> videoService.update("123", request));
        verify(videoRepository, never()).save(any());
    }

    @Test
    void delete_whenFound_shouldDeleteTheVideo() {
        Video video = Video.builder().id("123").title("Title").description("Description").url("url").build();
        String id = "123";

        when(videoRepository.findById(id)).thenReturn(Optional.of(video));

        videoService.delete(id);

        verify(videoRepository).deleteById(id);
    }

    @Test
    void delete_whenNotFound_shouldThrowException() {
        String id = "123";

        when(videoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> videoService.delete(id));
        verify(videoRepository, never()).deleteById(any());
    }

    @Test
    void findAllByCategory_whenCalled_shouldReturnVideoList() {
        List<Video> videos = List.of(Video.builder().id("123").title("Title").description("Description").url("url").build());
        List<VideoResponseDTO> expectedResponse = List.of(VideoResponseDTO.builder().id("123").title("Title").description("Description").url("url").build());
        when(videoRepository.findByCategoryId("111")).thenReturn(videos);

        List<VideoResponseDTO> response = videoService.findAllByCategory("111");

        assertEquals(expectedResponse, response);
    }
}
