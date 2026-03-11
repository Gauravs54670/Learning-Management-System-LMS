package com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class MediaProgressDTOResponse {
    private Long mediaId;
    private Double lastWatchTime;
    private Double watchPercentage;
    private Boolean completed;
}
