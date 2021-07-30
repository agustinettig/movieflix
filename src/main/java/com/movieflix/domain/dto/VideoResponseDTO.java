package com.movieflix.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoResponseDTO {

    private String id;
    private String title;
    private String description;
    private String url;
    private CategoryResponseDTO category;

}
