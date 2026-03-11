package com.Gaurav.LMS3.Service.UserServicePackage;

import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.CourseReviewDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.InstructorCoursesOverview;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorCourseStatsDashBoard;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorProfileDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorProfileUpdateRequest;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorReviewDTO;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseEntity;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseMediaType;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus;
import com.Gaurav.LMS3.Entity.UserEntityPackage.*;
import com.Gaurav.LMS3.Exception.ResourceNotFoundException;
import com.Gaurav.LMS3.Exception.UserNotFoundException;
import com.Gaurav.LMS3.Repository.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service @Transactional
public class InstructorServiceImplementation implements InstructorService{
    private final InstructorReviewEntityRepository instructorReviewEntityRepository;
    private final InstructorEntityRepository instructorEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final CourseEntityRepository courseEntityRepository;
    private final CourseReviewEntityRepository courseReviewEntityRepository;
    public InstructorServiceImplementation(
            InstructorReviewEntityRepository instructorReviewEntityRepository,
            CourseReviewEntityRepository courseReviewEntityRepository,
            CourseEntityRepository courseEntityRepository,
            InstructorEntityRepository instructorEntityRepository,
            UserEntityRepository userEntityRepository) {
        this.instructorReviewEntityRepository = instructorReviewEntityRepository;
        this.courseEntityRepository = courseEntityRepository;
        this.instructorEntityRepository = instructorEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.courseReviewEntityRepository = courseReviewEntityRepository;
    }
//    update instructor profile
    @Override
    public String updateProfile(String email,InstructorProfileUpdateRequest instructorProfileUpdateRequest) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        InstructorProfileEntity instructorProfileEntity = this.instructorEntityRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Instructor profile not found. First register as instructor."));
        if(instructorProfileUpdateRequest.getUserFullName() != null && !instructorProfileUpdateRequest.getUserFullName().isEmpty())
            user.setUserFullName(instructorProfileUpdateRequest.getUserFullName());
        if(instructorProfileUpdateRequest.getBio() != null && !instructorProfileUpdateRequest.getBio().isEmpty())
            user.setBio(instructorProfileUpdateRequest.getBio());
        if(
                user.getUserRoles().contains(UserRole.INSTRUCTOR) &&
                instructorProfileUpdateRequest.getExpertise() != null) {
            Set<String> newExpertise = instructorProfileUpdateRequest.getExpertise()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(this::normalize)
                    .collect(Collectors.toSet());
            Set<String> expertise = instructorProfileEntity.getInstructorExpertise();
            if(expertise == null || expertise.isEmpty()) {
                expertise = new HashSet<>(newExpertise);
            } else expertise.addAll(newExpertise);
            instructorProfileEntity.setInstructorExpertise(expertise);
        }
        user.setAccountUpdatedAt(LocalDateTime.now());
        this.userEntityRepository.save(user);
        this.instructorEntityRepository.save(instructorProfileEntity);
        return "Profile updated successfully.";
    }
//    get my profile
    @Override
    public InstructorProfileDTO getMyProfile(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if(isAccountSuspended(user))
            throw new AccessDeniedException("Account is SUSPENDED.");
        InstructorProfileEntity instructorProfileEntity = this.instructorEntityRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Instructor Profile not found."));
        return mapToInstructorProfileDTO(instructorProfileEntity);
    }
//    get the instructor stats
    @Override
    public InstructorCourseStatsDashBoard getInstructorStats(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAccount(user);
        InstructorProfileEntity instructorProfile = this.instructorEntityRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Instructor Profile not found."));
        return this.courseEntityRepository.getInstructorCourseStats(instructorProfile.getInstructorId());
    }
//    make course entity discontinue
    @Override
    public void discontinueCourse(String emailId, Long courseId) {
        UserEntity user = this.userEntityRepository.findByEmail(emailId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAccount(user);
        InstructorProfileEntity instructorProfile = this.instructorEntityRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Instructor Profile not found."));
        CourseEntity course = this.courseEntityRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found."));
        boolean checkOwnership = course.getInstructor().getInstructorId().equals(instructorProfile.getInstructorId());
        if(!checkOwnership) throw new AccessDeniedException("Access Denied. This course is not belongs to you.");
        course.setCourseStatus(CourseStatus.INSTRUCTOR_DISCONTINUE);
        course.setCourseUpdatedAt(LocalDateTime.now());
        this.courseEntityRepository.save(course);
        if(!course.getCourseStatus().equals(CourseStatus.INSTRUCTOR_DISCONTINUE)) throw new RuntimeException("Something went wrong." +
                " Status not updated. Please try again.");
    }
//    make course entity publish
    @Override
    public void publishCourse(String emailId, Long courseId) {
        UserEntity user = this.userEntityRepository.findByEmail(emailId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAccount(user);
        InstructorProfileEntity instructorProfile = this.instructorEntityRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Instructor Profile not found."));
        CourseEntity course = this.courseEntityRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found."));
        boolean checkOwnership = course.getInstructor().getInstructorId().equals(instructorProfile.getInstructorId());
        if(!checkOwnership) throw new AccessDeniedException("Access Denied. This course is not belongs to you.");
        if(course.getCourseStatus().equals(CourseStatus.ADMIN_DISCONTINUE)) throw new AccessDeniedException("Access Denied."+
                " Please consent support.");
        course.setCourseStatus(CourseStatus.PUBLISHED);
        course.setCourseUpdatedAt(LocalDateTime.now());
        this.courseEntityRepository.save(course);
        if(!course.getCourseStatus().equals(CourseStatus.PUBLISHED)) throw new RuntimeException("Something went wrong." +
                " Status not updated. Please try again.");
    }
//    make course entity draft
    @Override
    public void draftCourse(String emailId, Long courseId) {
        UserEntity user = this.userEntityRepository.findByEmail(emailId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAccount(user);
        InstructorProfileEntity instructorProfile = this.instructorEntityRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Instructor Profile not found."));
        CourseEntity course = this.courseEntityRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found."));
        boolean checkOwnership = course.getInstructor().getInstructorId().equals(instructorProfile.getInstructorId());
        if(!checkOwnership) throw new AccessDeniedException("Access Denied. This course is not belongs to you.");
        course.setCourseStatus(CourseStatus.DRAFT);
        course.setCourseUpdatedAt(LocalDateTime.now());
        this.courseEntityRepository.save(course);
        if(!course.getCourseStatus().equals(CourseStatus.DRAFT)) throw new RuntimeException("Something went wrong." +
                " Status not updated. Please try again.");
    }

    //    get instructor published course overview
    @Override
    public List<InstructorCoursesOverview> getPublicCourseOverviews(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAccount(user);
        return this.courseEntityRepository
                .findInstructorCourses(
                        email,
                        CourseStatus.PUBLISHED,
                        CourseMediaType.THUMBNAIL);
    }
//    get instructor draft course overview
    @Override
    public List<InstructorCoursesOverview> getDraftCourseOverview(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAccount(user);
        return this.courseEntityRepository.findInstructorCourses(
                email,
                CourseStatus.DRAFT,
                CourseMediaType.THUMBNAIL
        );
    }
//    get instructor discontinued course overview
    @Override
    public List<InstructorCoursesOverview> getDiscontinuedCourseOverview(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAccount(user);
        return this.courseEntityRepository.findInstructorCourses(
                email,
                CourseStatus.INSTRUCTOR_DISCONTINUE,
                CourseMediaType.THUMBNAIL
        );
    }
//    get all my course reviews by instructor
    @Override
    public List<CourseReviewDTO> getAllMyCourseReviews(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAccount(user);
        InstructorProfileEntity instructorProfile = this.instructorEntityRepository
                .findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Instructor profile not found."));
        return this.courseReviewEntityRepository.findAllMyCourseReviews(instructorProfile.getInstructorId());
    }
//    get all reviews of a course
    @Override
    public List<CourseReviewDTO> getAllReviewsOfCourse(String email, Long courseId) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAccount(user);
        InstructorProfileEntity instructorProfile = this.instructorEntityRepository
                .findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Instructor profile not found."));
        return this.courseReviewEntityRepository.findAllReviewOfCourse(instructorProfile.getInstructorId(),courseId);
    }
//    get all my reviews
    @Override
    public List<InstructorReviewDTO> getAllMyReviews(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAccount(user);
        InstructorProfileEntity instructorProfileEntity = this.instructorEntityRepository
                .findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Instructor Profile not found."));
        return this.instructorReviewEntityRepository.findAllMyReviews(instructorProfileEntity.getInstructorId());
    }

    //      helper methods
//    validate user's account
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

//    normalize string
    private String normalize(String str) {
        if(str == null || str.isBlank()) return null;
        return Arrays.stream(str.trim().split("\\s+"))
                .map(string -> Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
//    map to instructor profile dto
    private InstructorProfileDTO mapToInstructorProfileDTO(InstructorProfileEntity instructorProfileEntity) {
        return InstructorProfileDTO.builder()
                .email(instructorProfileEntity.getUser().getEmail())
                .fullName(instructorProfileEntity.getUser().getUserFullName())
                .expertise(instructorProfileEntity.getInstructorExpertise())
                .userAccountStatus(instructorProfileEntity.getUser().getAccountStatus())
                .build();
    }
}
