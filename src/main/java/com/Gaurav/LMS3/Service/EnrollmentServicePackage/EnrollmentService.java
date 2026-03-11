package com.Gaurav.LMS3.Service.EnrollmentServicePackage;

import com.Gaurav.LMS3.DTO.CourseDTO.Enrollment.EnrollmentResponse;
import com.Gaurav.LMS3.DTO.CourseDTO.Enrollment.StudentEnrolledCourseDTO;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse enrollInCourse(String email, Long courseId);
    String isEnrolledInCourse(Long courseId, String email);
    List<StudentEnrolledCourseDTO> getMyEnrollments(String email);
    String cancelEnrollment(String email, Long courseId);
}
