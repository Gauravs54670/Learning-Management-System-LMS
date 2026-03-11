package com.Gaurav.LMS3.DTO.CourseDTO;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
public class CourseRegistrationResponse {
    private Long courseId;
    private String instructorName;
    private String courseTitle;
    private String courseDescription;
    private String courseCode;
    private String courseStatus;
    private Double coursePrice;
    private String courseDuration;
    private Boolean isCoursePublic;
    private LocalDateTime courseCreatedAt;
    private LocalDateTime courseUpdateAt;
}
