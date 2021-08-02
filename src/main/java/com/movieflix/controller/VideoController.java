package com.movieflix.controller;

import com.movieflix.domain.dto.VideoRequestDTO;
import com.movieflix.domain.dto.VideoResponseDTO;
import com.movieflix.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping
    public List<VideoResponseDTO> findAll(@RequestParam(required = false) String title) {
        return videoService.findAll(title);
    }

    @PostMapping
    public VideoResponseDTO create(@RequestBody @Valid VideoRequestDTO videoRequest) {
        return videoService.create(videoRequest);
    }

    @GetMapping("/{id}")
    public VideoResponseDTO findById(@PathVariable String id) {
        return videoService.findById(id);
    }

    @PutMapping("/{id}")
    public VideoResponseDTO update(@PathVariable String id, @RequestBody @Valid  VideoRequestDTO videoRequest) {
        return videoService.update(id, videoRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        videoService.delete(id);
    }
}
