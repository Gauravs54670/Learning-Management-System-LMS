package com.Gaurav.LMS3.DTO.CourseDTO.Enrollment;

import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class EnrollmentDTO {
    private EnrolledCourseDTO courseDTO;
    private EnrollmentStatus enrollmentStatus;
    private Double courseCompletionPercentage;
    private LocalDateTime enrolledAt;
}
