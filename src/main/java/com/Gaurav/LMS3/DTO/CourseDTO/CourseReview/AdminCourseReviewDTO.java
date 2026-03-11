package com.Gaurav.LMS3.DTO.CourseDTO.CourseReview;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter @Setter
@NoArgsConstructor
public class AdminCourseReviewDTO {
    private Integer rating;
    private String comment;
    private Long instructorId;
    private String instructorName;
    private Long studentId;
    private String studentName;
    private LocalDateTime reviewedAd;
    public AdminCourseReviewDTO(
            Integer rating,
            String comment,
            Long instructorId,
            String instructorName,
            Long studentId,
            String studentName,
            LocalDateTime reviewedAd) {
        this.rating = rating;
        this.comment = comment;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.reviewedAd = reviewedAd;
    }
}
