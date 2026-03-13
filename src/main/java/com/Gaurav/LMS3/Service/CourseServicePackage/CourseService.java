package com.Gaurav.LMS3.Service.CourseServicePackage;

import com.Gaurav.LMS3.DTO.CourseDTO.Enrollment.EnrolledCourseDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.InstructorCourseEntityDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.*;
import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.CourseMediaDTOPreview;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {
    CourseRegistrationResponse registerCourse(
            String email,
            CourseRegistrationRequest courseRegistrationRequest);
    String uploadMedia(String email, Long courseId, MultipartFile multipartFile, String mediaType) throws Exception;
    List<CourseMediaDTOPreview> mediaPreview(String email, Long courseId, String mediaType);
    String updateCourse(
            String email,
            Long courseId,
            CourseUpdateRequest courseUpdateRequest);
    String changeAvailabilityOfCourse(String email, Boolean choice, Long courseId);
    InstructorCourseEntityDTO getCourse(String email, Long courseId);
    List<PublicCourseEntityDTO> getAllPublicCourses(String email);
    PublicCourseEntityDTO getPublicCourseByLearner(String email, Long courseId);
    List<PublicCourseEntityDTO> searchPublicCourses(String email, String keyword);
    List<PublicCourseEntityDTO> getCoursesByCategory(String email, String courseType);
    String deleteCourseMedia(
            String email,
            Long courseId,
            CourseMediaDeleteRequest courseMediaDeleteRequest);
    List<CourseMediaDTOPreview> getCourseMedias(String email,Long courseId);
}
