package com.Gaurav.LMS3.Service.UserServicePackage;

import com.Gaurav.LMS3.DTO.CourseDTO.CourseContentDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaContentDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Student.StudentProfileDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Student.StudentProfileUpdateRequest;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseEntity;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseMediaEntity;
import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentEntity;
import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentStatus;
import com.Gaurav.LMS3.Entity.UserEntityPackage.*;
import com.Gaurav.LMS3.Exception.ResourceNotFoundException;
import com.Gaurav.LMS3.Exception.UserNotFoundException;
import com.Gaurav.LMS3.Repository.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service @Transactional
public class StudentServiceImplementation implements StudentService{

    private final MediaEntityRepository mediaEntityRepository;
    private final EnrollmentEntityRepository enrollmentEntityRepository;
    private final CourseEntityRepository courseEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final InstructorEntityRepository instructorEntityRepository;
    public StudentServiceImplementation(
            MediaEntityRepository mediaEntityRepository,
            EnrollmentEntityRepository enrollmentEntityRepository,
            CourseEntityRepository courseEntityRepository,
            InstructorEntityRepository instructorEntityRepository,
            UserEntityRepository userEntityRepository) {
        this.instructorEntityRepository = instructorEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.courseEntityRepository = courseEntityRepository;
        this.enrollmentEntityRepository = enrollmentEntityRepository;
        this.mediaEntityRepository = mediaEntityRepository;
    }
//    get my profile
    @Override
    public StudentProfileDTO getMyProfile(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Something went wrong. Profile not found."));
        if(user.getAccountStatus().equals(UserAccountStatus.SUSPENDED))
            throw new AccessDeniedException("Your account is temporarily suspended. Please try after some time");
        return mapToStudentProfileDTO(user);
    }
//    update my profile
    @Override
    public String updateMyProfile(String email, StudentProfileUpdateRequest studentProfileUpdateRequest) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Something went wrong. Profile not found."));
        validateAccount(user);
        if(studentProfileUpdateRequest.getUserFullName() != null &&
                !studentProfileUpdateRequest.getUserFullName().isEmpty())
            user.setUserFullName(studentProfileUpdateRequest.getUserFullName());
        if(studentProfileUpdateRequest.getBio() != null && !studentProfileUpdateRequest.getBio().isEmpty())
            user.setBio(studentProfileUpdateRequest.getBio());
        user.setAccountUpdatedAt(LocalDateTime.now());
        this.userEntityRepository.save(user);
        return "Account updated successfully";
    }
//    update from learner role to instructor role
    @Override
    public String updateToInstructor(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAccount(user);
        if(!user.getUserRoles().contains(UserRole.LEARNER)) throw new AccessDeniedException("First register yourself.");
        if (user.getUserRoles().contains(UserRole.INSTRUCTOR))
            throw new AccessDeniedException("User is already an instructor.");
        user.getUserRoles().add(UserRole.INSTRUCTOR);
        InstructorProfileEntity instructorProfileEntity = InstructorProfileEntity.builder()
                        .user(user).build();
        this.instructorEntityRepository.save(instructorProfileEntity);
        user.setAccountUpdatedAt(LocalDateTime.now());
        this.userEntityRepository.save(user);
        if(!user.getUserRoles().contains(UserRole.INSTRUCTOR)) throw new RuntimeException("Something went wrong. Please try again.");
        return "User's role updated successfully";
    }
//    get a course for studying by student
    @Override
    public CourseContentDTO getCourse(String email, Long  courseId) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        CourseEntity course = this.courseEntityRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found."));
        boolean checkEnrollment = this.enrollmentEntityRepository
                .existsByCourse_CourseIdAndEnrolledStudent_Student_Email(courseId, user.getEmail());
        if(!checkEnrollment) throw new AccessDeniedException("You did not enrolled in this course.");
        List<CourseMediaEntity> courseMediaEntities = course.getCourseMediaEntities();
        return CourseContentDTO.builder()
                .courseId(courseId)
                .instructorName(course.getInstructor().getUser().getUserFullName())
                .courseTitle(course.getCourseTitle())
                .courseDescription(course.getCourseDescription())
                .courseType(course.getCourseType())
                .courseDuration(course.getCourseDuration())
                .medias(courseMediaEntities.stream()
                        .map(this::mapToMediaContentDTO)
                        .toList())
                .build();
    }
//    get course media when student start a course
    @Override
    public MediaContentDTO getMedia(String userEmail, Long mediaId, Long courseId) {
        UserEntity user = this.userEntityRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        EnrollmentEntity enrollment = this.enrollmentEntityRepository
                .findByEnrolledStudent_Student_UserIdAndCourse_CourseId(user.getUserId(), courseId)
                .orElseThrow(() -> new ResourceNotFoundException("No enrollment found for this course." +
                        " Please check the course id."));
        boolean mediaAndCourse = this.mediaEntityRepository.existsByMediaIdAndCourse_CourseId(mediaId, courseId);
        if(!mediaAndCourse) throw new AccessDeniedException("This media is not belongs to the course. Please check the credentials.");
        enrollment.setIsCourseExplored(true);
        enrollment.setCourseEnrollmentStatus(EnrollmentStatus.IN_PROGRESS);
       return this.mediaEntityRepository.getMedia(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found."));
    }

    //    helper methods
//    map the course media entity to media content dto
    private MediaContentDTO mapToMediaContentDTO(CourseMediaEntity courseMedia) {
        return MediaContentDTO.builder()
                .mediaId(courseMedia.getMediaId())
                .mediaOrdering(courseMedia.getMediaOrdering())
                .courseMediaType(courseMedia.getCourseMediaType())
                .mediaUrl(courseMedia.getMediaUrl())
                .build();
    }
//    checking the status of user
    private void validateAccount(UserEntity user) {
        if(isAccountSuspended(user)) throw new AccessDeniedException("User's Account is SUSPENDED.");
        if(isAccountDeactivated(user)) throw new AccessDeniedException("User's Account is DEACTIVATED.");
    }
    private boolean isAccountDeactivated(UserEntity user) {
        return user.getAccountStatus().equals(UserAccountStatus.DEACTIVATED);
    }
    private boolean isAccountSuspended(UserEntity user) {
        return user.getAccountStatus().equals(UserAccountStatus.DEACTIVATED);
    }
//    map to student profile response Dto
    private StudentProfileDTO mapToStudentProfileDTO(UserEntity user) {
        return StudentProfileDTO.builder()
                .email(user.getEmail())
                .userFullName(user.getUserFullName())
                .bio(user.getBio())
                .accountCreatedAt(user.getAccountCreatedAt())
                .build();
    }

}
