package com.movieflix.service;

import com.movieflix.domain.Category;
import com.movieflix.domain.Video;
import com.movieflix.domain.dto.VideoRequestDTO;
import com.movieflix.domain.dto.VideoResponseDTO;
import com.movieflix.exception.CategoryNotFoundException;
import com.movieflix.exception.DataNotFoundException;
import com.movieflix.repository.CategoryRepository;
import com.movieflix.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    @Value("${default-category.id}")
    private String defaultCategoryId;

    public List<VideoResponseDTO> findAll() {
        return videoRepository.findAll().stream()
                .map(video -> modelMapper.map(video, VideoResponseDTO.class))
                .collect(Collectors.toList());
    }

    public VideoResponseDTO create(VideoRequestDTO videoRequest) {
        Video video = modelMapper.map(videoRequest, Video.class);
        fillCategory(video, videoRequest.getCategoryId());
        video = videoRepository.save(video);
        return modelMapper.map(video, VideoResponseDTO.class);
    }

    public VideoResponseDTO findById(String id) {
        Video video = videoRepository.findById(id).orElseThrow(DataNotFoundException::new);
        return modelMapper.map(video, VideoResponseDTO.class);
    }

    public VideoResponseDTO update(String id, VideoRequestDTO videoRequest) {
        Video video = videoRepository.findById(id).orElseThrow(DataNotFoundException::new);
        modelMapper.map(videoRequest, video);

        fillCategory(video, videoRequest.getCategoryId());
        videoRepository.save(video);
        return modelMapper.map(video, VideoResponseDTO.class);
    }

    public void delete(String id) {
        videoRepository.findById(id).orElseThrow(DataNotFoundException::new);
        videoRepository.deleteById(id);
    }

    public List<VideoResponseDTO> findAllByCategory(String categoryId) {
        return videoRepository.findByCategoryId(categoryId).stream()
                .map(video -> modelMapper.map(video, VideoResponseDTO.class))
                .collect(Collectors.toList());
    }

    private void fillCategory(Video video, String requestCategoryId) {
        String categoryId = requestCategoryId != null ? requestCategoryId : defaultCategoryId;
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        video.setCategory(category);
    }
}

