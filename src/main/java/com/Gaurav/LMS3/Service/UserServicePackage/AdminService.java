package com.Gaurav.LMS3.Service.UserServicePackage;

import com.Gaurav.LMS3.DTO.CourseDTO.CourseDetailsDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.AdminCourseReviewDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.IndividualCourseDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.AdminInstructorReviewDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Student.AdminStudentCourseReviewDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Student.AdminStudentInstructorReview;
import com.Gaurav.LMS3.DTO.UserDTO.UserDetailsDTO;
import com.Gaurav.LMS3.DTO.UserDTO.UserSummaryDTO;

import java.util.List;

public interface AdminService {
    List<UserSummaryDTO> getAllLearners(String email);
    List<UserSummaryDTO> getAllInstructors(String email);
    String suspendUser(String userEmail);
    String activateUser(String userEmail);
    String deactivateUser(String userEmail);
    UserDetailsDTO getUserProfile(String userEmail);
    List<CourseDetailsDTO> getCourseOfInstructor(String instructorEmail);
    IndividualCourseDTO getIndividualCourse(Long courseId);
    void discontinueCourse(Long courseId);
    void publishCourse(Long courseId);
    void deleteCourseReview(String userEmail,Long reviewId);
    void deleteInstructorReview(String userEmail, Long reviewId);
    List<AdminCourseReviewDTO> getAllReviewsOfCourses(String email, Long courseId);
    List<AdminInstructorReviewDTO> getInstructorReviews(String email, Long instructorId);
    List<AdminStudentCourseReviewDTO> getAllCourseReviewsOfStudent(String email, String studentMail);
    List<AdminStudentInstructorReview> getAllInstructorReviewsOfStudent(String email, String studentMail);
}
