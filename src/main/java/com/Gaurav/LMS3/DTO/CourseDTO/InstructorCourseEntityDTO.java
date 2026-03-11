package com.Gaurav.LMS3.DTO.CourseDTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
public class InstructorCourseEntityDTO {
    private Long courseId;
    private String courseTitle;
    private String courseDescription;
    private String courseCode;
    private String courseStatus;
    private String courseType;
    private Double coursePrice;
    private String courseDuration;
    private Boolean isCoursePublic;
    private Double averageRatingOfCourse;
    private Integer totalEnrollmentInCourse;
    private Integer totalReviewsInCourse;
    private String courseThumbnailUrl;
    private String courseCourseIntroUrl;
    private List<String> courseVideoUrl;
    private LocalDateTime courseCreatedAt;
}
