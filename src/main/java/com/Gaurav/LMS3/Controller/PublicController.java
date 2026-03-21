package com.Gaurav.LMS3.Controller;

import com.Gaurav.LMS3.DTO.CourseDTO.PublicCourseEntityDTO;
import com.Gaurav.LMS3.DTO.UserDTO.UserRegistrationRequest;
import com.Gaurav.LMS3.DTO.UserDTO.UserRegistrationResponse;
import com.Gaurav.LMS3.Service.CourseServicePackage.CourseService;
import com.Gaurav.LMS3.Service.UserServicePackage.PublicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/public")
public class PublicController {
    private final CourseService courseService;
    private final PublicService publicService;

    public PublicController(
            CourseService courseService,
            PublicService publicService) {
        this.publicService = publicService;
        this.courseService = courseService;
    }

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(
            @RequestBody UserRegistrationRequest userRegistrationRequest) {
        UserRegistrationResponse
                userRegistrationResponse = this.publicService.registerUser(userRegistrationRequest);
        return new ResponseEntity<>(Map.of(
                "message", "User registered successfully",
                "response", userRegistrationResponse
        ), HttpStatus.OK);
    }

    @PostMapping("/verify-user")
    public ResponseEntity<?> verifyUser(@RequestParam("email") String email) {
        String message = this.publicService.forgotPasswordRequest(email);
        return new ResponseEntity<>(Map.of("message", message), HttpStatus.OK);
    }

    @PostMapping("/forgot-password-change")
    public ResponseEntity<?> changePassword(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "otp") String otp,
            @RequestParam(name = "new password") String newPassword) {
        String message = this.publicService.changeAccountPassword(email, otp, newPassword);
        return new ResponseEntity<>(Map.of("message", message), HttpStatus.OK);
    }

    //    get all the publicly available courses
    @GetMapping("/all-published-courses")
    public ResponseEntity<?> getAllPublicAvailableCourses(Authentication authentication) {
        String email = (authentication != null) ? authentication.getName() : null;
        List<PublicCourseEntityDTO> courses = this.courseService.getAllPublicCourses(email);
        return new ResponseEntity<>(Map.of(
                "message", "Courses fetched successfully",
                "response", courses
        ), HttpStatus.OK);
    }
//    get publicly available course
    @GetMapping("/get-public-course/{courseId}")
    public ResponseEntity<?> getPublicAvailableCourse(
            @PathVariable("courseId") Long courseId,
            Authentication authentication) {
        String email = (authentication != null) ? authentication.getName() : null;
        PublicCourseEntityDTO publicCourse = this.courseService.getPublicCourseByLearner(email, courseId);
        return new ResponseEntity<>(Map.of(
                "message", "Your course is here",
                "response", publicCourse
        ),HttpStatus.OK);
    }
//    get courses by keyword
    @GetMapping("/get-public-courses")
    public ResponseEntity<?> getCoursesByKeyword(
            Authentication authentication,
            @RequestParam("keyword") String keyword) {
        String email = (authentication != null) ? authentication.getName() : null;
        List<PublicCourseEntityDTO> courses = this.courseService.searchPublicCourses(email, keyword);
        return new ResponseEntity<>(Map.of(
                "message", "Course list fetched successfully",
                "response", courses
        ),HttpStatus.OK);
    }
//    get courses by course category
    @GetMapping("/get-courses-category")
    public ResponseEntity<?> getCourseByCategory(
            Authentication authentication,
            @RequestParam("category") String category) {
        String email = (authentication != null) ? authentication.getName() : null;
        List<PublicCourseEntityDTO> courses = this.courseService.getCoursesByCategory(email, category);
        return new ResponseEntity<>(Map.of(
                "message", "Course fetched successfully",
                "response", courses
        ),HttpStatus.OK);
    }
}