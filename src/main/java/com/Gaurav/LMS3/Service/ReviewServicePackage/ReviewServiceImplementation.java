package com.Gaurav.LMS3.Service.ReviewServicePackage;

import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.CourseReviewRequest;
import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.CourseReviewResponse;
import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentEntity;
import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentStatus;
import com.Gaurav.LMS3.Entity.ReviewEntityPackage.CourseReviewEntity;
import com.Gaurav.LMS3.Entity.UserEntityPackage.StudentProfileEntity;
import com.Gaurav.LMS3.Entity.UserEntityPackage.UserAccountStatus;
import com.Gaurav.LMS3.Entity.UserEntityPackage.UserEntity;
import com.Gaurav.LMS3.Exception.ResourceNotFoundException;
import com.Gaurav.LMS3.Exception.UserNotFoundException;
import com.Gaurav.LMS3.Repository.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service @Transactional
public class ReviewServiceImplementation implements CourseReviewService{
    private final StudentEntityRepository studentEntityRepository;
    private final EnrollmentEntityRepository enrollmentEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final CourseReviewEntityRepository courseReviewEntityRepository;
    public ReviewServiceImplementation(
            EnrollmentEntityRepository enrollmentEntityRepository,
            StudentEntityRepository studentEntityRepository,
            UserEntityRepository userEntityRepository,
            CourseReviewEntityRepository courseReviewEntityRepository) {
        this.courseReviewEntityRepository = courseReviewEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.studentEntityRepository = studentEntityRepository;
        this.enrollmentEntityRepository = enrollmentEntityRepository;
    }
//    add review to the course
    @Override
    public CourseReviewResponse addReview(Long courseId, String email, CourseReviewRequest courseReviewRequest) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        StudentProfileEntity studentProfileEntity = this.studentEntityRepository.findByStudent_UserId(user.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Student Profile not found."));
        EnrollmentEntity enrollment = this.enrollmentEntityRepository
                .findByEnrolledStudent_Student_UserIdAndCourse_CourseId(studentProfileEntity.getStudentId(), courseId)
                .orElseThrow(() -> new ResourceNotFoundException("You have not done enrollment for this course. " +
                        "Only enrolled students can review any course."));
        boolean eligible =
                enrollment.getCourseEnrollmentStatus() == EnrollmentStatus.COMPLETED
                        || (enrollment.getCourseEnrollmentStatus() == EnrollmentStatus.IN_PROGRESS
                        && enrollment.getCourseCompletionPercentage() >= 40.0);
        if (!eligible)
            throw new AccessDeniedException(
                    "Complete at least 40% of the course before reviewing.");
        boolean isReviewExist = this.courseReviewEntityRepository
                .existsByStudentProfileEntity_StudentIdAndCourse_CourseId(studentProfileEntity.getStudentId(), courseId);
        if(isReviewExist) throw new AccessDeniedException("Review already done by you for this course.");
        CourseReviewEntity courseReviewEntity = CourseReviewEntity.builder()
                .rating(courseReviewRequest.getRating())
                .comment(courseReviewRequest.getComment())
                .reviewedAt(LocalDateTime.now())
                .studentProfileEntity(studentProfileEntity)
                .course(enrollment.getCourse())
                .build();
        courseReviewEntity = this.courseReviewEntityRepository.save(courseReviewEntity);
        this.courseReviewEntityRepository.updateCourseAverageRating(courseId,courseReviewRequest.getRating().longValue());
        return mapToCourseReviewResponse(courseReviewEntity);
    }
//    delete review of course by learner
    @Override
    public String deleteReview(Long courseId, String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        StudentProfileEntity studentProfileEntity = this.studentEntityRepository.findByStudent_UserId(user.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Student Profile not found."));
        CourseReviewEntity courseReviewEntity = this.courseReviewEntityRepository
                .findByStudentProfileEntity_StudentIdAndCourse_CourseId(
                        studentProfileEntity.getStudentId(),
                        courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Review already deleted or not found."));
//        updating the rating of the course
        this.courseReviewEntityRepository.decreaseCourseReviewRating(courseId, Long.valueOf(courseReviewEntity.getRating()));
        this.courseReviewEntityRepository.delete(courseReviewEntity);
        return "Review deleted successfully";
    }

    //    helper methods
//    map the course review entity to course review response
    private CourseReviewResponse mapToCourseReviewResponse(
            CourseReviewEntity courseReviewEntity) {
        return CourseReviewResponse.builder()
                .rating(courseReviewEntity.getRating())
                .comment(courseReviewEntity.getComment())
                .reviewedAt(courseReviewEntity.getReviewedAt())
                .build();
    }
//    validate the user's Account
    private void validateUserAccount(UserEntity user) {
        if(isAccountDeactivated(user)) throw new AccessDeniedException("Account is DEACTIVATED.");
        if(isAccountSuspended(user)) throw new AccessDeniedException("Account is SUSPENDED.");
    }
    private boolean isAccountSuspended(UserEntity user) {
        return user.getAccountStatus().equals(UserAccountStatus.SUSPENDED);
    }
    private boolean isAccountDeactivated(UserEntity user) {
        return user.getAccountStatus().equals(UserAccountStatus.DEACTIVATED);
    }
}
