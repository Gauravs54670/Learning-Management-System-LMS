package com.Gaurav.LMS3.Controller;

import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.CourseReviewDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.InstructorCoursesOverview;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorCourseStatsDashBoard;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorProfileDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorProfileUpdateRequest;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorReviewDTO;
import com.Gaurav.LMS3.Service.UserServicePackage.InstructorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/instructor")
public class InstructorController {

    private final InstructorService instructorService;
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }
//    update instructor profile
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateInstructorProfile(
            Authentication authentication,
            @RequestBody InstructorProfileUpdateRequest instructorProfileUpdateRequest) {
        String email = authentication.getName();
        String message = this.instructorService.updateProfile(email, instructorProfileUpdateRequest);
        return new ResponseEntity<>(Map.of(
                "message", message
        ), HttpStatus.OK);
    }
    //    getting profile
    @GetMapping("/get-myProfile")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        InstructorProfileDTO instructorProfileDTO = this.instructorService.getMyProfile(username);
        return new ResponseEntity<>(Map.of(
                "message", "Profile fetched successfully",
                "response", instructorProfileDTO
        ), HttpStatus.OK);
    }
//    get instructor course stats dashboard
    @GetMapping("/get-stats")
    public ResponseEntity<?> getInstructorCourseStats(Authentication authentication) {
        String email = authentication.getName();
        InstructorCourseStatsDashBoard statsDashBoard = this.instructorService.getInstructorStats(email);
        return new ResponseEntity<>(Map.of(
                "message","Stats fetched successfully",
                "response", statsDashBoard
        ),HttpStatus.OK);
    }
//    get instructor published courses
    @GetMapping("/get-published-courses")
    public ResponseEntity<?> getPublishedCourse(Authentication authentication) {
        String email = authentication.getName();
        List<InstructorCoursesOverview> publishedCourses = this.instructorService.getPublicCourseOverviews(email);
        return new ResponseEntity<>(Map.of(
                "message","List fetched successfully",
                "response", publishedCourses
        ),HttpStatus.OK);
    }
//    get the instructor draft course
    @GetMapping("/get-draft-courses")
    public ResponseEntity<?> getDraftCourse(Authentication authentication) {
        String email = authentication.getName();
        List<InstructorCoursesOverview> draftCourses = this.instructorService.getDraftCourseOverview(email);
        return new ResponseEntity<>(Map.of(
                "message","List fetched successfully",
                "response", draftCourses
        ),HttpStatus.OK);
    }
//    get the instructor discontinued courses
    @GetMapping("/get-discontinued-courses")
    public ResponseEntity<?> getDiscontinuedCourse(Authentication authentication) {
        String email = authentication.getName();
        List<InstructorCoursesOverview> discontinuedCourse = this.instructorService.getDiscontinuedCourseOverview(email);
        return new ResponseEntity<>(Map.of(
                "message", "List fetched successfully",
                "response", discontinuedCourse
        ),HttpStatus.OK);
    }
    @PutMapping("/publish-course/{courseId}")
    public ResponseEntity<?> publishCourse(
            @PathVariable("/courseId") Long courseId,
            Authentication authentication) {
        String email = authentication.getName();
        this.instructorService.publishCourse(email, courseId);
        return new ResponseEntity<>(Map.of(
                "message","Course published successfully"
        ),HttpStatus.OK);
    }
    @PutMapping("/draft-course/{courseId}")
    public ResponseEntity<?> draftCourse(
            @PathVariable("/courseId") Long courseId,
            Authentication authentication) {
        String email = authentication.getName();
        this.instructorService.draftCourse(email, courseId);
        return new ResponseEntity<>(Map.of(
                "message","Course drafted successfully"
        ),HttpStatus.OK);
    }
    @PutMapping("/discontinue-course/{courseId}")
    public ResponseEntity<?> discontinueCourse(
            @PathVariable("courseId") Long courseId,
            Authentication authentication) {
        String email = authentication.getName();
        this.instructorService.discontinueCourse(email, courseId);
        return new ResponseEntity<>(Map.of(
                "message","Course discontinued successfully"
        ),HttpStatus.OK);
    }
    @GetMapping("/get-allCourse-Reviews")
    public ResponseEntity<?> getAllCourseReviews(Authentication authentication) {
        String email = authentication.getName();
        List<CourseReviewDTO> allCourseReviews = this.instructorService.getAllMyCourseReviews(email);
        return new ResponseEntity<>(Map.of(
                "message", "Course reviews fetched successfully",
                "response", allCourseReviews
        ),HttpStatus.OK);
    }
    @GetMapping("/get-course-reviews/{courseId}")
    public ResponseEntity<?> getAllReviewsOfCourse(
            Authentication authentication,
            @PathVariable("courseId") Long courseId) {
        String email = authentication.getName();
        List<CourseReviewDTO> allReviews = this.instructorService.getAllReviewsOfCourse(email,courseId);
        return new ResponseEntity<>(Map.of(
                "message","Course Reviews",
                "response", allReviews
        ),HttpStatus.OK);
    }
    @GetMapping("/get-myReviews")
    public ResponseEntity<?> getMyReviews(Authentication authentication) {
        String email = authentication.getName();
        List<InstructorReviewDTO> myReviews = this.instructorService.getAllMyReviews(email);
        return new ResponseEntity<>(Map.of(
                "message","Instructor Reviews",
                "response",myReviews
        ),HttpStatus.OK);
    }
}
