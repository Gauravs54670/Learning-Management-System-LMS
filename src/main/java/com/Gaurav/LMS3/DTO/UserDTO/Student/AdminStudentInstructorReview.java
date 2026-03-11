package com.Gaurav.LMS3.DTO.UserDTO.Student;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@NoArgsConstructor
@Getter @Setter
public class AdminStudentInstructorReview {
    private Integer rating;
    private String comment;
    private Long instructorId;
    private String instructorName;
    private LocalDateTime reviewedAt;
    public AdminStudentInstructorReview(
            Integer rating,
            String comment,
            Long instructorId,
            String instructorName,
            LocalDateTime reviewedAt) {
        this.rating = rating;
        this.comment = comment;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.reviewedAt = reviewedAt;
    }
}
