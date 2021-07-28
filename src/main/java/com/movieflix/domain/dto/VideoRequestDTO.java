package com.movieflix.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
public class VideoRequestDTO {

    @NotBlank
    @Size(min = 1, max = 50)
    public String title;

    @NotBlank
    @Size(min = 1, max = 50)
    public String description;

    @NotBlank
    @Size(min = 1, max = 50)
    public String url;

}
