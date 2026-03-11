package com.Gaurav.LMS3.Service.UserServicePackage;


import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.CourseReviewDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.InstructorCoursesOverview;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorCourseStatsDashBoard;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorProfileDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorProfileUpdateRequest;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorReviewDTO;

import java.util.List;

public interface InstructorService {
    String updateProfile(String email,InstructorProfileUpdateRequest instructorProfileUpdateRequest);
    InstructorProfileDTO getMyProfile(String email);
    InstructorCourseStatsDashBoard getInstructorStats(String email);
    void discontinueCourse(String emailId, Long courseId);
    void publishCourse(String emailId, Long courseId);
    void draftCourse(String emailId, Long courseId);
    List<InstructorCoursesOverview> getPublicCourseOverviews(String email);
    List<InstructorCoursesOverview> getDraftCourseOverview(String email);
    List<InstructorCoursesOverview> getDiscontinuedCourseOverview(String email);
    List<CourseReviewDTO> getAllMyCourseReviews(String email);
    List<CourseReviewDTO> getAllReviewsOfCourse(String email, Long courseId);
    List<InstructorReviewDTO> getAllMyReviews(String email);
}
