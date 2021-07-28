package com.movieflix.service;


import com.movieflix.domain.Video;
import com.movieflix.domain.dto.VideoRequestDTO;
import com.movieflix.domain.dto.VideoResponseDTO;
import com.movieflix.exception.DataNotFoundException;
import com.movieflix.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

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

    @BeforeEach
    void init() {
        this.videoService = new VideoService(videoRepository, new ModelMapper());
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
        Video video = Video.builder().id("123").title("Title").description("Description").url("url").build();
        VideoRequestDTO request = VideoRequestDTO.builder().title("Title").description("Description").url("url").build();
        VideoResponseDTO expectedResponse = VideoResponseDTO.builder().id("123").title("Title").description("Description").url("url").build();

        when(videoRepository.save(any())).thenReturn(video);

        VideoResponseDTO response = videoService.create(request);

        assertEquals(expectedResponse, response);
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
        Video video = Video.builder().id("123").title("Title").description("Description").url("url").build();
        VideoRequestDTO request = VideoRequestDTO.builder().title("Title updated").description("Description updated").url("url updated").build();
        VideoResponseDTO expectedResponse = VideoResponseDTO.builder().id("123").title("Title updated").description("Description updated").url("url updated").build();

        when(videoRepository.findById("123")).thenReturn(Optional.of(video));

        VideoResponseDTO response = videoService.update("123", request);

        assertEquals(expectedResponse, response);
    }

    @Test
    void update_whenNotFound_shouldThrowException() {
        VideoRequestDTO request = VideoRequestDTO.builder().title("Title updated").description("Description updated").url("url updated").build();

        when(videoRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> videoService.update("123", request));
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
}
