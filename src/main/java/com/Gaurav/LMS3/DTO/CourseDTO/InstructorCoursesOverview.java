package com.Gaurav.LMS3.DTO.CourseDTO;

import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseType;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
public class InstructorCoursesOverview {
    private Long courseId;
    private String courseTitle;
    private String courseDescription;
    private String courseType;
    private String courseStatus;
    private String courseDuration;
    private String url;
    public InstructorCoursesOverview(
            Long courseId,
            String courseTitle,
            String courseDescription,
            CourseType courseType,
            CourseStatus courseStatus,
            String courseDuration,
            String courseThumbnailUrl) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseType = courseType.name();
        this.courseStatus = courseStatus.name();
        this.courseDuration = courseDuration;
        this.url = courseThumbnailUrl;
    }

}
