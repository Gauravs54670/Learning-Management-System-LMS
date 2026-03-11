package com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class MediaProgressDTORequest {
    @NotNull
    private Long mediaId;
    private Double currentMediaTimeStamp;
}
