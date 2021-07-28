package com.movieflix.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
public class CategoryRequestDTO {

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    @NotBlank
    @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Invalid format. Has to be a HEX color")
    private String color;

}
