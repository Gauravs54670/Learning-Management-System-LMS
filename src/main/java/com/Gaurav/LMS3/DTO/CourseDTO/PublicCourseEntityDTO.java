package com.Gaurav.LMS3.DTO.CourseDTO;

import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseType;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
public class PublicCourseEntityDTO {
    private Long courseId;
    private String courseTitle;
    private String courseDescription;
    private String courseType;
    private Double coursePrice;
    private String courseDuration;
    private String courseInstructorName;
    private Double averageRating;
    private Integer totalEnrollments;
    private String thumbnailUrl;
    private String introVideoUrl;
    public PublicCourseEntityDTO(
            Long courseId,
            String courseTitle,
            String courseDescription,
            CourseType courseType,
            Double coursePrice,
            String courseDuration,
            String courseInstructorName,
            Double averageRating,
            Integer totalEnrollments,
            String thumbnailUrl,
            String introVideoUrl
    ) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseType = courseType.name();
        this.coursePrice = coursePrice;
        this.courseDuration = courseDuration;
        this.courseInstructorName = courseInstructorName;
        this.averageRating = averageRating;
        this.totalEnrollments = totalEnrollments;
        this.thumbnailUrl = thumbnailUrl;
        this.introVideoUrl = introVideoUrl;
    }
}
