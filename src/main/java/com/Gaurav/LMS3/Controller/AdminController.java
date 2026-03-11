package com.Gaurav.LMS3.Controller;

import com.Gaurav.LMS3.DTO.CourseDTO.CourseDetailsDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.AdminCourseReviewDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.IndividualCourseDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.AdminInstructorReviewDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Student.AdminStudentCourseReviewDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Student.AdminStudentInstructorReview;
import com.Gaurav.LMS3.DTO.UserDTO.UserDetailsDTO;
import com.Gaurav.LMS3.DTO.UserDTO.UserSummaryDTO;
import com.Gaurav.LMS3.Service.UserServicePackage.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @GetMapping("/all-learners")
    public ResponseEntity<?> getAllLearners(Authentication authentication) {
        String email = authentication.getName();
        List<UserSummaryDTO> learners = this.adminService.getAllLearners(email);
        return new ResponseEntity<>(Map.of(
                "message","Learner's list fetched successfully",
                "response", learners
        ), HttpStatus.OK);
    }
    @GetMapping("/all-instructors")
    public ResponseEntity<?> getAllInstructors(Authentication authentication) {
        String email = authentication.getName();
        List<UserSummaryDTO> instructors = this.adminService.getAllInstructors(email);
        return new ResponseEntity<>(Map.of(
                "message", "Instructor's list fetched successfully",
                "response", instructors
        ),HttpStatus.OK);
    }
//    suspend user
    @PutMapping("/suspend-user")
    public ResponseEntity<?> suspendUser(@RequestParam("userEmail") String email) {
        String response = this.adminService.suspendUser(email);
        return new ResponseEntity<>(Map.of(
                "message",response
        ),HttpStatus.OK);
    }
//    deactivate user
    @PutMapping("/deactivate-user")
    public ResponseEntity<?> deactivateUser(@RequestParam("userEmail") String email) {
        String response = this.adminService.deactivateUser(email);
        return new ResponseEntity<>(Map.of(
                "message", response
        ),HttpStatus.OK);
    }
//    activate user
    @PutMapping("/activate-user")
    public ResponseEntity<?> activateUser(@RequestParam("userEmail") String email) {
        String response = this.adminService.activateUser(email);
        return new ResponseEntity<>(Map.of(
                "message", response
        ),HttpStatus.OK);
    }
//    get user profile
    @GetMapping("/get-userDetails")
    public ResponseEntity<?> getUserDetails(@RequestParam("email") String email) {
        UserDetailsDTO profile = this.adminService.getUserProfile(email);
        return new ResponseEntity<>(Map.of(
                "message", "Profile fetched successfully",
                "response", profile
        ),HttpStatus.OK);
    }
//    get instructor courses
    @GetMapping("/get-InstructorCourse")
    public ResponseEntity<?> getInstructorCourses(@RequestParam("userEmail") String userEmail) {
        List<CourseDetailsDTO> courses = this.adminService.getCourseOfInstructor(userEmail);
        return new ResponseEntity<>(Map.of(
                "message", "Course fetched successfully",
                "response", courses
        ),HttpStatus.OK);
    }
//    get individual course
    @GetMapping("/get-course/{courseId}")
    public ResponseEntity<?> getCourse(@PathVariable("courseId") Long courseId) {
        IndividualCourseDTO courseDTO = this.adminService.getIndividualCourse(courseId);
        return new ResponseEntity<>(Map.of(
                "message", "Course fetched successfully",
                "response", courseDTO
        ),HttpStatus.OK);
    }
//    discontinue course
    public ResponseEntity<?> discontinueCourse(@PathVariable("courseId") Long courseId) {
        this.adminService.discontinueCourse(courseId);
        return new ResponseEntity<>(Map.of(
                "message", "Course discontinued successfully"
        ),HttpStatus.OK);
    }
//    publish course
    public ResponseEntity<?> publishCourse(@PathVariable("courseId") Long courseId) {
        this.adminService.publishCourse(courseId);
        return new ResponseEntity<>(Map.of(
                "message", "Course published successfully"
        ),HttpStatus.OK);
    }
//    delete course review of student
    @DeleteMapping("/delete-courseReview")
    public ResponseEntity<?> deleteStudentReview(
            @RequestParam("reviewId") Long reviewId,
            @RequestParam("studentMail") String studentMail) {
        this.adminService.deleteCourseReview(studentMail,reviewId);
        return new ResponseEntity<>(Map.of(
                "message", "Course review deleted successfully"
        ),HttpStatus.OK);
    }
//    delete instructor review by student
    @DeleteMapping("/delete-instructorReview")
    public ResponseEntity<?> deleteInstructorReview(
            @RequestParam("reviewId") Long reviewId,
            @RequestParam("studentMail") String studentMail) {
        this.adminService.deleteInstructorReview(studentMail,reviewId);
        return new ResponseEntity<>(Map.of(
                "message", "Instructor review deleted successfully"
        ),HttpStatus.OK);
    }
//    get all reviews of a course
    @GetMapping("/get-allCourseReview/{courseId}")
    public ResponseEntity<?> getAllCourseReviews(
            @PathVariable("courseId") Long courseId,
            Authentication authentication) {
        String email = authentication.getName();
        List<AdminCourseReviewDTO> courseReviews = this.adminService.getAllReviewsOfCourses(email, courseId);
        return new ResponseEntity<>(Map.of(
                "message", "Course Reviews",
                "response", courseReviews
        ),HttpStatus.OK);
    }
//    get all reviews of instructor
    @GetMapping("/get-allInstructorReviews")
    public ResponseEntity<?> getAllInstructorReviews(
            @RequestParam("instructorId") Long instructorId,
            Authentication authentication) {
        String email = authentication.getName();
        List<AdminInstructorReviewDTO> instructorReviews = this.adminService.getInstructorReviews(email,instructorId);
        return new ResponseEntity<>(Map.of(
                "message","Instructor Reviews",
                "response", instructorReviews
        ),HttpStatus.OK);
    }
//    get all course reviews of student
    @GetMapping("/get-student-courseReviews")
    public ResponseEntity<?> getAllStudentCourseReviews(
            @RequestParam("email") String studentMail,
            Authentication authentication) {
        String mail = authentication.getName();
        List<AdminStudentCourseReviewDTO> courseReviews = this.adminService.getAllCourseReviewsOfStudent(mail,studentMail);
        return new ResponseEntity<>(Map.of(
                "message","Student Course Reviews",
                "response", courseReviews
        ),HttpStatus.OK);
    }
//    get all instructor reviews of student
    @GetMapping("/get-student-instructorReviews")
    public ResponseEntity<?> getAllStudentInstructorReviews(
            @RequestParam("email") String studentMail,
            Authentication authentication) {
        String email = authentication.getName();
        List<AdminStudentInstructorReview> studentInstructorReviews = this.adminService
                .getAllInstructorReviewsOfStudent(email,studentMail);
        return new ResponseEntity<>(Map.of(
                "message", "Student Instructor Reviews",
                "response", studentInstructorReviews
        ),HttpStatus.OK);
    }
}
