package com.Gaurav.LMS3.Controller;

import com.Gaurav.LMS3.DTO.CourseDTO.Enrollment.EnrollmentResponse;
import com.Gaurav.LMS3.DTO.CourseDTO.Enrollment.StudentEnrolledCourseDTO;
import com.Gaurav.LMS3.Service.EnrollmentServicePackage.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/enrollment")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }
    //    enroll in course
    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<?> enrollInCourse(
            Authentication authentication,
            @PathVariable("courseId") Long courseId) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Authentication required"));
        }
        String email = authentication.getName();
        EnrollmentResponse response = this.enrollmentService.enrollInCourse(email,courseId);
        return new ResponseEntity<>(Map.of(
                "success", true,
                "data", response,
                "nextStep", "Go to my enrollments to check your purchased course"
        ),HttpStatus.CREATED);
    }
//    check is there any enrollment or not
    @GetMapping("/isEnrolled/{courseId}")
    public ResponseEntity<?> isStudentEnrolledInCourse(
            Authentication authentication,
            @PathVariable("courseId") Long courseId) {
        String email = authentication.getName();
        String response = this.enrollmentService.isEnrolledInCourse(courseId,email);
        return new ResponseEntity<>(Map.of(
                "courseId", courseId,
                "courseTitle", response,
                "enrolled", true,
                "message", "Enrollment is present"
        ), HttpStatus.OK);
    }
//    get my enrollments
    @GetMapping("/myEnrollments")
    public ResponseEntity<?> getMyEnrollments(Authentication authentication) {
        String email = authentication.getName();
        List<StudentEnrolledCourseDTO> myEnrollments = this.enrollmentService.getMyEnrollments(email);
        return new ResponseEntity<>(Map.of(
                "message", "Enrollments fetched successfully",
                "response", myEnrollments
        ),HttpStatus.OK);
    }
//    cancel enrollment
    @PutMapping("/cancel-enrollment/{courseId}")
    public ResponseEntity<?> cancelEnrollment(
            @PathVariable("courseId") Long courseId,
            Authentication authentication) {
        String email = authentication.getName();
        String message = this.enrollmentService.cancelEnrollment(email,courseId);
        return new ResponseEntity<>(Map.of(
                "message",message
        ),HttpStatus.OK);
    }
}
