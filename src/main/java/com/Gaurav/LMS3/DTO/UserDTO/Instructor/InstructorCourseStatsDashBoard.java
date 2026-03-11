package com.Gaurav.LMS3.DTO.UserDTO.Instructor;

import lombok.*;


@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class InstructorCourseStatsDashBoard {
    // course counts
    private Long totalCourses;
    private Long publishedCourses;
    private Long draftCourses;
    // enrollment
    private Long totalEnrollments;
    // rating and reviews
    private Long totalCourseReviews;
    private Double averageCourseRating;
}
