package com.Gaurav.LMS3.DTO.CourseDTO.Enrollment;

import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class EnrollmentResponse {
    private Long courseId;
    private String courseTitle;
    private String status;
    private String message;
    private LocalDateTime enrolledAt;
}
