package com.Gaurav.LMS3.Controller;

import com.Gaurav.LMS3.DTO.CourseDTO.*;
import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.CourseMediaDTOPreview;
import com.Gaurav.LMS3.Service.CourseServicePackage.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }
//    registering course
    @PostMapping("/register-course")
    public ResponseEntity<?> registerCourse(
            Authentication authentication,
            @RequestBody CourseRegistrationRequest courseRegistrationRequest) {
        String email = authentication.getName();
        CourseRegistrationResponse courseRegistrationResponse = this.courseService.registerCourse(
                email,
                courseRegistrationRequest);
        return new ResponseEntity<>(Map.of(
                "message", "Course registered successfully",
                "response", courseRegistrationResponse
        ), HttpStatus.OK);
    }
//    upload media
    @PostMapping("/upload-media/{courseId}")
    public ResponseEntity<?> uploadMedia(
            Authentication authentication,
            @RequestParam("media") MultipartFile file,
            @RequestParam("mediaType") String courseMediaType,
            @PathVariable("courseId") Long courseId) throws Exception {
        String url = this.courseService.uploadMedia(
                authentication.getName(),
                courseId,
                file,
                courseMediaType);
        return new ResponseEntity<>(Map.of(
                "message", "Media uploaded successfully",
                "url", url
        ),HttpStatus.OK);
    }
//    get media preview
    @GetMapping("/media-preview/{courseId}")
    public ResponseEntity<?> getMediaPreview(
            Authentication authentication,
            @PathVariable("courseId") Long courseId,
            @RequestParam("mediaType") String mediaType) {
        String email = authentication.getName();
        List<CourseMediaDTOPreview> mediaUrls = this.courseService.mediaPreview(email, courseId, mediaType);
        return new ResponseEntity<>(Map.of(
                "message", "preview fetched",
                "response", mediaUrls
        ),HttpStatus.OK);
    }
//    update course
    @PutMapping("update-course/{courseId}")
    public ResponseEntity<?> updateCourse(
            @PathVariable("courseId") Long courseId,
            @RequestBody CourseUpdateRequest courseUpdateRequest,
            Authentication authentication) {
        String email = authentication.getName();
        String response = this.courseService.updateCourse(email, courseId, courseUpdateRequest);
        return new ResponseEntity<>(Map.of(
                "message", response
        ),HttpStatus.OK);
    }
//    change availability, of course
    @PutMapping("/change-availability/{courseId}")
    public ResponseEntity<?> changeAvailability(
            Authentication authentication,
            @RequestParam("choice") Boolean choice,
            @PathVariable("courseId") Long courseId) {
        String email = authentication.getName();
        String response = this.courseService.changeAvailabilityOfCourse(email,choice,courseId);
        return new ResponseEntity<>(Map.of(
                "response", response
        ),HttpStatus.OK);
    }
//    get the course by instructor
    @GetMapping("/get-course/{courseId}")
    public ResponseEntity<?> getCourse(
            Authentication authentication,
            @PathVariable("courseId") Long courseId) {
        String email = authentication.getName();
        InstructorCourseEntityDTO courseEntityDTO = this.courseService.getCourse(email, courseId);
        return new ResponseEntity<>(Map.of(
                "message", "Course fetched successfully",
                "response", courseEntityDTO
        ),HttpStatus.OK);
    }
//    delete the course media
    @DeleteMapping("/delete-media/{courseId}")
    public ResponseEntity<?> deleteMedia(
            Authentication authentication,
            @PathVariable("courseId") Long courseId,
            @RequestBody CourseMediaDeleteRequest courseMediaDeleteRequest) {
        String email = authentication.getName();
        String message = this.courseService.deleteCourseMedia(email, courseId, courseMediaDeleteRequest);
        return new ResponseEntity<>(Map.of(
                "message", message
        ),HttpStatus.OK);
    }
//    get course medias
    @GetMapping("/get-course-medias/{courseId}")
    public ResponseEntity<?> getCourseMedias(
            Authentication authentication,
            @PathVariable("courseId") Long courseId) {
        String email = authentication.getName();
        List<CourseMediaDTOPreview> courseMedias = this.courseService.getCourseMedias(email, courseId);
        return new ResponseEntity<>(Map.of(
                "message", "Course medias fetched successfully",
                "response", courseMedias
        ),HttpStatus.OK);
    }
}
