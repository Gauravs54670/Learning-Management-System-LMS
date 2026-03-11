package com.Gaurav.LMS3.DTO.CourseDTO.CourseReview;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class CourseReviewRequest {
    private Integer rating;
    private String comment;
}
