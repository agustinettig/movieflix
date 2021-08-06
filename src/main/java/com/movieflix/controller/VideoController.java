package com.movieflix.controller;

import com.movieflix.domain.dto.VideoRequestDTO;
import com.movieflix.domain.dto.VideoResponseDTO;
import com.movieflix.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<VideoResponseDTO> findAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return videoService.findAll(title, PageRequest.of(page, size));
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
