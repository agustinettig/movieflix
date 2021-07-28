package com.movieflix.service;

import com.movieflix.domain.Category;
import com.movieflix.domain.dto.CategoryRequestDTO;
import com.movieflix.domain.dto.CategoryResponseDTO;
import com.movieflix.exception.DataNotFoundException;
import com.movieflix.repository.CategoryRepository;
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
class CategoryServiceTest {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void init() {
        this.categoryService = new CategoryService(categoryRepository, new ModelMapper());
    }

    @Test
    void findAll_whenCalled_shouldReturnCategoryList() {
        List<Category> categories = List.of(Category.builder().id("123").title("Title").color("#FFF").build());
        List<CategoryResponseDTO> expectedResponse = List.of(CategoryResponseDTO.builder().id("123").title("Title").color("#FFF").build());
        when(categoryRepository.findAll()).thenReturn(categories);

        List<CategoryResponseDTO> response = categoryService.findAll();

        assertEquals(expectedResponse, response);
    }

    @Test
    void create_whenCalled_shouldReturnCreatedCategoryWithId() {
        Category category = Category.builder().id("123").title("Title").color("#FFF").build();
        CategoryRequestDTO request = CategoryRequestDTO.builder().title("Title").color("#FFF").build();
        CategoryResponseDTO expectedResponse = CategoryResponseDTO.builder().id("123").title("Title").color("#FFF").build();

        when(categoryRepository.save(any())).thenReturn(category);

        CategoryResponseDTO response = categoryService.create(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    void findById_whenFound_shouldReturnCategory() {
        Category category = Category.builder().id("123").title("Title").color("#FFF").build();
        String id = "123";
        CategoryResponseDTO expectedResponse = CategoryResponseDTO.builder().id("123").title("Title").color("#FFF").build();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        CategoryResponseDTO response = categoryService.findById(id);

        assertEquals(expectedResponse, response);
    }

    @Test
    void findById_whenNotFound_shouldThrowException() {
        String id = "123";

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> categoryService.findById(id));
    }

    @Test
    void update_whenFound_shouldReturnUpdatedCategory() {
        Category category = Category.builder().id("123").title("Title").color("#FFF").build();
        CategoryRequestDTO request = CategoryRequestDTO.builder().title("Title").color("#FFF").build();
        CategoryResponseDTO expectedResponse = CategoryResponseDTO.builder().id("123").title("Title").color("#FFF").build();

        when(categoryRepository.findById("123")).thenReturn(Optional.of(category));

        CategoryResponseDTO response = categoryService.update("123", request);

        assertEquals(expectedResponse, response);
    }

    @Test
    void update_whenNotFound_shouldThrowException() {
        CategoryRequestDTO request = CategoryRequestDTO.builder().title("Title").color("#FFF").build();

        when(categoryRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> categoryService.update("123", request));
    }

    @Test
    void delete_whenFound_shouldDeleteTheCategory() {
        Category category = Category.builder().id("123").title("Title").color("#FFF").build();
        String id = "123";

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        categoryService.delete(id);

        verify(categoryRepository).deleteById(id);
    }

    @Test
    void delete_whenNotFound_shouldThrowException() {
        String id = "123";

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> categoryService.delete(id));
        verify(categoryRepository, never()).deleteById(any());
    }
}
