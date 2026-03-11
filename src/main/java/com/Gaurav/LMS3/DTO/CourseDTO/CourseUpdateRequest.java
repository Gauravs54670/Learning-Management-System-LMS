package com.Gaurav.LMS3.DTO.CourseDTO;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class CourseUpdateRequest {
    private String courseTitle;
    private String courseDescription;
    private String courseType;
    private Double coursePrice;
}
