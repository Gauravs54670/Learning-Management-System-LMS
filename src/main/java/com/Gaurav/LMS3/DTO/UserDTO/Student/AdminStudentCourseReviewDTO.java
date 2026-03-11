package com.Gaurav.LMS3.DTO.UserDTO.Student;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@NoArgsConstructor
@Getter @Setter
public class AdminStudentCourseReviewDTO {
    private Integer rating;
    private String comment;
    private Long courseId;
    private String courseTitle;
    private Long instructorId;
    private String instructorName;
    private LocalDateTime reviewedAt;
    public AdminStudentCourseReviewDTO(
            Integer rating,
            String comment,
            Long courseId,
            String courseTitle,
            Long instructorId,
            String instructorName,
            LocalDateTime reviewedAt) {
        this.rating = rating;
        this.comment = comment;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.reviewedAt = reviewedAt;
    }
}
