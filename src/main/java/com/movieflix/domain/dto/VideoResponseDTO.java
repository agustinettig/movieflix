package com.movieflix.domain.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoResponseDTO {

    public String id;
    public String title;
    public String description;
    public String url;

}
