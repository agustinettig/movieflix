package com.movieflix.service;

import com.movieflix.domain.Category;
import com.movieflix.domain.dto.CategoryRequestDTO;
import com.movieflix.domain.dto.CategoryResponseDTO;
import com.movieflix.exception.DataNotFoundException;
import com.movieflix.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryResponseDTO.class))
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO create(CategoryRequestDTO categoryRequest) {
        Category category = modelMapper.map(categoryRequest, Category.class);
        category = categoryRepository.save(category);
        return modelMapper.map(category, CategoryResponseDTO.class);
    }

    public CategoryResponseDTO findById(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(DataNotFoundException::new);
        return modelMapper.map(category, CategoryResponseDTO.class);
    }

    public CategoryResponseDTO update(String id, CategoryRequestDTO videoRequest) {
        Category category = categoryRepository.findById(id).orElseThrow(DataNotFoundException::new);
        modelMapper.map(videoRequest, category);
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryResponseDTO.class);
    }

    public void delete(String id) {
        categoryRepository.findById(id).orElseThrow(DataNotFoundException::new);
        categoryRepository.deleteById(id);
    }
}
