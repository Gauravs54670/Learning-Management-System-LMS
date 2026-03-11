package com.Gaurav.LMS3.DTO.UserDTO.Instructor;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Builder @Getter @Setter
public class InstructorReviewDTO {
    private Integer rating;
    private String comment;
    private String studentName;
    private LocalDateTime reviewedAt;
    public InstructorReviewDTO(
            Integer rating,
            String comment,
            String studentName,
            LocalDateTime reviewedAt) {
        this.rating = rating;
        this.comment = comment;
        this.studentName = studentName;
        this.reviewedAt = reviewedAt;
    }
}
