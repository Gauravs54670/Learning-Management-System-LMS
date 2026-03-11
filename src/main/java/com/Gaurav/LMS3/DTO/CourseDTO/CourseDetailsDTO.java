package com.Gaurav.LMS3.DTO.CourseDTO;

import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseType;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor
public class CourseDetailsDTO {
    private Long courseId;
    private String instructorName;
    private String courseTitle;
    private String courseDescription;
    private String courseCode;
    private CourseStatus courseStatus;
    private CourseType courseType;
    private Double coursePrice;
    private String courseDuration;
    private Boolean isPublic;
    private Long ratingCount;
    private Double averageRatingOfCourse;
    private Integer totalEnrollmentInCourse;
    private Integer totalReviewsInCourse;
    private LocalDateTime courseCreatedAt;
    public CourseDetailsDTO(
            Long courseId,
            String instructorName,
            String courseTitle,
            String courseDescription,
            String courseCode,
            CourseStatus courseStatus,
            CourseType coursetype,
            Double coursePrice,
            String courseDuration,
            Boolean isPublic,
            Long ratingCount,
            Double averageRatingOfCourse,
            Integer totalEnrollmentInCourse,
            Integer totalReviewsInCourse,
            LocalDateTime courseCreatedAt) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.instructorName = instructorName;
        this.courseDescription = courseDescription;
        this.courseCode = courseCode;
        this.courseStatus = courseStatus;
        this.courseType = coursetype;
        this.coursePrice = coursePrice;
        this.courseDuration = courseDuration;
        this.isPublic = isPublic;
        this.ratingCount = ratingCount;
        this.averageRatingOfCourse = averageRatingOfCourse;
        this.totalEnrollmentInCourse = totalEnrollmentInCourse;
        this.totalReviewsInCourse = totalReviewsInCourse;
        this.courseCreatedAt = courseCreatedAt;
    }
}
