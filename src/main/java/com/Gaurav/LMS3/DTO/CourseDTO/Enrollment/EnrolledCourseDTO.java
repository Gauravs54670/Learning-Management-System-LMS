package com.Gaurav.LMS3.DTO.CourseDTO.Enrollment;

import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseType;
import lombok.*;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class EnrolledCourseDTO {
    private String instructorName;
    private String courseTitle;
    private String courseDescription;
    private CourseType courseType;
    private CourseStatus courseStatus;
    private String courseDuration;
    private String courseThumbnail;
    private String courseIntro;
}
