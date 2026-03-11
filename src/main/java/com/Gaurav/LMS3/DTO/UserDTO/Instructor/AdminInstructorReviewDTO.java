package com.Gaurav.LMS3.DTO.UserDTO.Instructor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@NoArgsConstructor
@Getter @Setter
public class AdminInstructorReviewDTO {
    private Integer rating;
    private String comment;
    private Long studentId;
    private String studentName;
    private LocalDateTime reviewedAt;
    public AdminInstructorReviewDTO(
           Integer rating,
           String comment,
           Long studentId,
           String studentName,
           LocalDateTime reviewedAt) {
        this.rating = rating;
        this.comment = comment;
        this.studentId = studentId;
        this.studentName = studentName;
        this.reviewedAt = reviewedAt;
    }
}
