package com.Gaurav.LMS3.Service.EnrollmentServicePackage;

import com.Gaurav.LMS3.DTO.CourseDTO.Enrollment.EnrollmentResponse;
import com.Gaurav.LMS3.DTO.CourseDTO.Enrollment.StudentEnrolledCourseDTO;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseEntity;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseMediaType;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus;
import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentEntity;
import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentStatus;
import com.Gaurav.LMS3.Entity.UserEntityPackage.StudentProfileEntity;
import com.Gaurav.LMS3.Entity.UserEntityPackage.UserAccountStatus;
import com.Gaurav.LMS3.Entity.UserEntityPackage.UserEntity;
import com.Gaurav.LMS3.Exception.ResourceNotFoundException;
import com.Gaurav.LMS3.Exception.UserNotFoundException;
import com.Gaurav.LMS3.Repository.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalTime.now;

@Service @Transactional
public class EnrollmentServiceImplementation implements EnrollmentService{

    private final StudentEntityRepository studentEntityRepository;
    private final CourseEntityRepository courseEntityRepository;
    private final EnrollmentEntityRepository enrollmentEntityRepository;
    private final UserEntityRepository userEntityRepository;
    public EnrollmentServiceImplementation(
            StudentEntityRepository studentEntityRepository,
            CourseEntityRepository courseEntityRepository,
            EnrollmentEntityRepository enrollmentEntityRepository,
            UserEntityRepository userEntityRepository) {
        this.enrollmentEntityRepository = enrollmentEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.courseEntityRepository = courseEntityRepository;
        this.studentEntityRepository = studentEntityRepository;
    }
//    enroll in a course
    @Override
    public EnrollmentResponse enrollInCourse(String email, Long courseId) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        CourseEntity course = this.courseEntityRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found."));
        if (Boolean.FALSE.equals(course.getIsPublic())
                || course.getCourseStatus() != CourseStatus.PUBLISHED) {
            throw new AccessDeniedException("Failed to enroll in course. Course is discontinued.");
        }
        StudentProfileEntity studentProfileEntity = this.studentEntityRepository.findByStudent_UserId(user.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Student not found."));
        boolean isAlreadyEnrolled = this.enrollmentEntityRepository.existsByCourse_CourseIdAndEnrolledStudent_Student_Email(courseId, email);
        if(isAlreadyEnrolled) throw new AccessDeniedException("You have already done enrollment for this course.");
        EnrollmentEntity enrollmentEntity = EnrollmentEntity.builder()
                .enrolledStudent(studentProfileEntity)
                .course(course)
                .courseEnrollmentStatus(EnrollmentStatus.ENROLLED)
                .courseCompletionPercentage(0.0)
                .enrolledAt(LocalDateTime.now())
                .build();
        enrollmentEntity = this.enrollmentEntityRepository.save(enrollmentEntity);
        return mapToEnrollmentResponse(enrollmentEntity);
    }
//    check is student enrolled in course or not
    @Override
    public String isEnrolledInCourse(Long courseId, String email) {
        UserEntity student = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(student);
        boolean isEnrolled = this.enrollmentEntityRepository
                .existsByCourse_CourseIdAndEnrolledStudent_Student_Email(
                        courseId,
                        email);
        if (!isEnrolled) throw new ResourceNotFoundException("No enrollment found for this course.");
        String courseTitle = this.courseEntityRepository
                .getCourseTitleByCourseId(courseId);
        if(courseTitle == null || courseTitle.isEmpty())
            throw new ResourceNotFoundException("Course not found.");
        return courseTitle;
    }
//    get my enrollments
    @Override
    public List<StudentEnrolledCourseDTO> getMyEnrollments(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        Long studentId = this.studentEntityRepository.findStudentIdByUserId(user.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Student Profile not found."));
        return this.enrollmentEntityRepository
                .findEnrolledCourseByStudentId(
                        studentId,
                        CourseMediaType.INTRO,
                        CourseMediaType.THUMBNAIL);
    }
//    student cancel any enrollment
    @Override
    public String cancelEnrollment(String email, Long courseId) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateUserAccount(user);
        StudentProfileEntity studentProfile = this.studentEntityRepository.findByStudent_UserId(user.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Student profile not found."));
        String courseTitle = this.courseEntityRepository.getCourseTitleByCourseId(courseId);
        if(courseTitle.isEmpty()) throw new ResourceNotFoundException("Something went wrong. Course not found.");
        EnrollmentEntity enrollment = this.enrollmentEntityRepository.
                findByEnrolledStudent_Student_UserIdAndCourse_CourseId(studentProfile.getStudentId(),courseId)
                .orElseThrow(() -> new ResourceNotFoundException("No enrollment found for the course " + courseTitle));
        if (enrollment.getCourseEnrollmentStatus() == EnrollmentStatus.CANCELLED)
            throw new AccessDeniedException("Enrollment already cancelled.");
        if(enrollment.getCourseEnrollmentStatus().equals(EnrollmentStatus.COMPLETED)
            ||
                enrollment.getCourseEnrollmentStatus().equals(EnrollmentStatus.REFUNDED))
            throw new AccessDeniedException("Completed course can't be canceled and refunded.");
        if (enrollment.getCourseEnrollmentStatus() == EnrollmentStatus.IN_PROGRESS
                && enrollment.getCourseCompletionPercentage() >= 50)
            throw new AccessDeniedException("You have completed more than 50% of the course.");
        LocalDateTime enrollmentLog = enrollment.getEnrolledAt();
        if(Duration.between(enrollmentLog, now()).toHours() >= 24)
            throw new AccessDeniedException("Enrollment cancellation failed. Cancellation should be done within 24 hrs");
        if (enrollment.getCourseCompletionPercentage() <= 10
                && enrollment.getCourseEnrollmentStatus() != EnrollmentStatus.IN_PROGRESS) {
            enrollment.setCourseEnrollmentStatus(EnrollmentStatus.REFUND_INITIATED);
        }
        enrollment.setEnrollmentCanceledAt(LocalDateTime.now());
        this.enrollmentEntityRepository.save(enrollment);
        return courseTitle + " enrollment cancellation successfully " +
                "Refunds will be done in next 24 hr.";
    }

    //    helper methods
//    check if the user is deactivated or suspended
    private void validateUserAccount(UserEntity user) {
        if (isUserAccountDeactivated(user))
            throw new AccessDeniedException("User's account is DEACTIVATED.");
        if (isUserAccountSuspended(user))
            throw new AccessDeniedException("User's account is SUSPENDED.");
    }
    private boolean isUserAccountSuspended(UserEntity user) {
        return user.getAccountStatus().equals(UserAccountStatus.SUSPENDED);
    }
    private boolean isUserAccountDeactivated(UserEntity user) {
        return user.getAccountStatus().equals(UserAccountStatus.DEACTIVATED);
    }
//    map to enrolled course response
    private EnrollmentResponse mapToEnrollmentResponse(EnrollmentEntity enrollmentEntity) {
        return EnrollmentResponse.builder()
                .courseTitle(enrollmentEntity.getCourse().getCourseTitle())
                .courseId(enrollmentEntity.getCourse().getCourseId())
                .status(enrollmentEntity.getCourseEnrollmentStatus().toString())
                .message("Enrollment Successful")
                .enrolledAt(enrollmentEntity.getEnrolledAt())
                .build();
    }
}

