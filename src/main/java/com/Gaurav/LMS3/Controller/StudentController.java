package com.Gaurav.LMS3.Controller;

import com.Gaurav.LMS3.DTO.CourseDTO.CourseContentDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.CourseReviewRequest;
import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.CourseReviewResponse;
import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaContentDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaProgressDTORequest;
import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaProgressDTOResponse;
import com.Gaurav.LMS3.DTO.UserDTO.Student.StudentProfileDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Student.StudentProfileUpdateRequest;
import com.Gaurav.LMS3.Service.CourseServicePackage.MediaService;
import com.Gaurav.LMS3.Service.ReviewServicePackage.CourseReviewService;
import com.Gaurav.LMS3.Service.UserServicePackage.StudentService;
import com.Gaurav.LMS3.Service.UserServicePackage.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController @RequestMapping("/learner")
public class StudentController {
    private final MediaService mediaProgressService;
    private final CourseReviewService courseReviewService;
    private final UserService userService;
    private final StudentService studentService;
    public StudentController(
            MediaService mediaProgressService,
            CourseReviewService courseReviewService,
            UserService userService,
            StudentService studentService) {
        this.studentService = studentService;
        this.userService = userService;
        this.courseReviewService = courseReviewService;
        this.mediaProgressService = mediaProgressService;
    }
//    getting profile
    @GetMapping("/get-myProfile")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        StudentProfileDTO studentProfileDTO = this.studentService.getMyProfile(username);
        return new ResponseEntity<>(Map.of(
                "message", "Profile fetched successfully",
                "response", studentProfileDTO
        ), HttpStatus.OK);
    }
//    updating profile
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(
            @RequestBody StudentProfileUpdateRequest studentProfileUpdateRequest,
            Authentication authentication) {
        String username = authentication.getName();
        String message = this.studentService.updateMyProfile(username, studentProfileUpdateRequest);
        return new ResponseEntity<>(Map.of(
                "message", message
        ),HttpStatus.OK);
    }
//    updating the status of user's account
    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatus(
            Authentication authentication,
            @RequestParam(name = "status") String accountStatus) {
        String username = authentication.getName();
        String message = this.userService.changeAccountStatus(username, accountStatus);
        return new ResponseEntity<>(Map.of(
                "message", message
        ),HttpStatus.OK);
    }
//    update the role of a student to instructor
    @PutMapping("/switch-profile")
    public ResponseEntity<?> updateRole(Authentication authentication) {
        String email = authentication.getName();
        String message = this.studentService.updateToInstructor(email);
        return new ResponseEntity<>(Map.of(
                "message", message
        ),HttpStatus.OK);
    }
//    review to the course by learner
    @PostMapping("/addReview-Course/{courseId}")
    public ResponseEntity<?> reviewToCourse(
            @PathVariable("courseId") Long courseId,
            @RequestBody CourseReviewRequest courseReviewRequest,
            Authentication authentication) {
        String email = authentication.getName();
        CourseReviewResponse response = this.courseReviewService.addReview(
                courseId,
                email,
                courseReviewRequest);
        return new ResponseEntity<>(Map.of(
                "message", "Review posted.",
                "response", response
        ),HttpStatus.OK);
    }
//    delete course review by student
    @DeleteMapping("deleteReview-Course/{courseId}")
    public ResponseEntity<?> deleteCourseReview(
            @PathVariable("courseId") Long courseId,
            Authentication authentication) {
        String email = authentication.getName();
        String message = this.courseReviewService.deleteReview(courseId,email);
        return new ResponseEntity<>(Map.of(
                "response", message
        ),HttpStatus.OK);
    }
    @GetMapping("get-course/{courseId}")
    public ResponseEntity<?> getCourseContent(
            Authentication authentication,
            @PathVariable("courseId") Long courseId) {
        String email = authentication.getName();
        CourseContentDTO courseContent = this.studentService.getCourse(email, courseId);
        return new ResponseEntity<>(Map.of(
                "message", "Course content fetched successfully",
                "response", courseContent
        ),HttpStatus.OK);
    }
    @GetMapping("get-courseMedia/{courseId}/{mediaId}")
    public ResponseEntity<?> getCourseMedia(
            Authentication authentication,
            @PathVariable("mediaId") Long mediaId,
            @PathVariable("courseId") Long courseId) {
        String email = authentication.getName();
        MediaContentDTO mediaContent = this.studentService.getMedia(email, mediaId, courseId);
        return new ResponseEntity<>(Map.of(
                "message", "Media fetched successfully",
                "response", mediaContent
        ),HttpStatus.OK);
    }
    @PutMapping("/update-media-progress/{courseId}")
    public ResponseEntity<?> updateMediaProgress(
            @RequestBody MediaProgressDTORequest mediaProgressDTORequest,
            @PathVariable("courseId") Long courseId,
            Authentication authentication) {
        String email = authentication.getName();
        MediaProgressDTOResponse mediaProgressDTOResponse = this.mediaProgressService
                .evaluateMediaProgress(email, courseId, mediaProgressDTORequest);
        return new ResponseEntity<>(Map.of(
                "message", "Media Progress Evaluated successfully",
                "response", mediaProgressDTOResponse
        ),HttpStatus.OK);
    }
}
