package com.Gaurav.LMS3.DTO.CourseDTO.Enrollment;

import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseType;
import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentStatus;
import lombok.*;

@NoArgsConstructor
@Getter @Setter @Builder
public class StudentEnrolledCourseDTO {
    private String courseTitle;
    private String courseDescription;
    private CourseType courseType;
    private EnrollmentStatus courseEnrollmentStatus;
    private String courseInstructorName;
    private String introUrl;
    private String thumbnailUrl;
    public StudentEnrolledCourseDTO(
            String courseTitle,
            String courseDescription,
            CourseType courseType,
            EnrollmentStatus enrollmentStatus,
            String courseInstructorName,
            String introUrl,
            String thumbnailUrl) {
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseEnrollmentStatus = enrollmentStatus;
        this.courseType = courseType;
        this.courseInstructorName = courseInstructorName;
        this.introUrl = introUrl;
        this.thumbnailUrl = thumbnailUrl;
    }
}
