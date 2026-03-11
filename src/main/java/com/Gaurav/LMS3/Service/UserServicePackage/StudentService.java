package com.Gaurav.LMS3.Service.UserServicePackage;

import com.Gaurav.LMS3.DTO.CourseDTO.CourseContentDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaContentDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Student.StudentProfileDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Student.StudentProfileUpdateRequest;

public interface StudentService {
    StudentProfileDTO getMyProfile(String email);
    String updateMyProfile(
            String email,
            StudentProfileUpdateRequest studentProfileUpdateRequest);
    String updateToInstructor(String email);
    CourseContentDTO getCourse(String email, Long courseId);
    MediaContentDTO getMedia(String userEmail, Long mediaId, Long courseId);
}
