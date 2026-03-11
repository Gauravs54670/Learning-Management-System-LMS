package com.Gaurav.LMS3.DTO.CourseDTO.CourseReview;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter @Builder
public class CourseReviewDTO {
    private Integer rating;
    private String comment;
    private LocalDateTime reviewedAt;
    private String studentName;
    public CourseReviewDTO(
            Integer rating,
            String comment,
            LocalDateTime reviewedAt,
            String studentName) {
        this.rating = rating;
        this.comment = comment;
        this.reviewedAt = reviewedAt;
        this.studentName = studentName;
    }
}
