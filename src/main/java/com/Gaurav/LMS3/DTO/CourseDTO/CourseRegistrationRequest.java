package com.Gaurav.LMS3.DTO.CourseDTO;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
public class CourseRegistrationRequest {
    private String courseTitle;
    private String courseDescription;
    private String courseType;
    private Double coursePrice;
    private String courseDuration;
}
