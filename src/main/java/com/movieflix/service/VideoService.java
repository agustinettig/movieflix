package com.movieflix.service;

import com.movieflix.domain.Video;
import com.movieflix.domain.dto.VideoRequestDTO;
import com.movieflix.domain.dto.VideoResponseDTO;
import com.movieflix.exception.DataNotFoundException;
import com.movieflix.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

    private final ModelMapper modelMapper;

    public List<VideoResponseDTO> findAll() {
        return videoRepository.findAll().stream()
                .map(video -> modelMapper.map(video, VideoResponseDTO.class))
                .collect(Collectors.toList());
    }

    public VideoResponseDTO create(VideoRequestDTO videoRequest) {
        Video video = modelMapper.map(videoRequest, Video.class);
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
        videoRepository.save(video);
        return modelMapper.map(video, VideoResponseDTO.class);
    }

    public void delete(String id) {
        videoRepository.findById(id).orElseThrow(DataNotFoundException::new);
        videoRepository.deleteById(id);
    }
}

