package com.Gaurav.LMS3.DTO.CourseDTO.CourseReview;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class CourseReviewResponse {
    private Integer rating;
    private String comment;
    private LocalDateTime reviewedAt;
}
