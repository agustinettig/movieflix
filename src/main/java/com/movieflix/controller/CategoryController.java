package com.movieflix.controller;

import com.movieflix.domain.dto.CategoryRequestDTO;
import com.movieflix.domain.dto.CategoryResponseDTO;
import com.movieflix.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDTO> findAll() {
        return categoryService.findAll();
    }

    @PostMapping
    public CategoryResponseDTO create(@RequestBody @Valid CategoryRequestDTO categoryRequest) {
        return categoryService.create(categoryRequest);
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO findById(@PathVariable String id) {
        return categoryService.findById(id);
    }

    @PutMapping("/{id}")
    public CategoryResponseDTO update(@PathVariable String id, @RequestBody @Valid  CategoryRequestDTO categoryRequest) {
        return categoryService.update(id, categoryRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        categoryService.delete(id);
    }
}
