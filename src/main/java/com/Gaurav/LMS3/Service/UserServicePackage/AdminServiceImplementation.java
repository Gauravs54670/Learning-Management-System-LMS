package com.Gaurav.LMS3.Service.UserServicePackage;

import com.Gaurav.LMS3.DTO.CourseDTO.CourseDetailsDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.AdminCourseReviewDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.IndividualCourseDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaEntityDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.AdminInstructorReviewDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorProfileDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Student.AdminStudentCourseReviewDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Student.AdminStudentInstructorReview;
import com.Gaurav.LMS3.DTO.UserDTO.UserDetailsDTO;
import com.Gaurav.LMS3.DTO.UserDTO.UserSummaryDTO;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseEntity;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseMediaEntity;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus;
import com.Gaurav.LMS3.Entity.ReviewEntityPackage.CourseReviewEntity;
import com.Gaurav.LMS3.Entity.ReviewEntityPackage.InstructorReviewEntity;
import com.Gaurav.LMS3.Entity.UserEntityPackage.*;
import com.Gaurav.LMS3.Exception.ResourceNotFoundException;
import com.Gaurav.LMS3.Exception.UserNotFoundException;
import com.Gaurav.LMS3.Repository.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional
public class AdminServiceImplementation implements AdminService{
    private final InstructorReviewEntityRepository instructorReviewEntityRepository;
    private final CourseReviewEntityRepository courseReviewEntityRepository;
    private final StudentEntityRepository studentEntityRepository;
    private final CourseEntityRepository courseEntityRepository;
    private final UserEntityRepository userEntityRepository;
    public AdminServiceImplementation(
            InstructorReviewEntityRepository instructorReviewEntityRepository,
            CourseReviewEntityRepository courseReviewEntityRepository,
            StudentEntityRepository studentEntityRepository,
            CourseEntityRepository courseEntityRepository,
            UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
        this.courseEntityRepository = courseEntityRepository;
        this.studentEntityRepository = studentEntityRepository;
        this.courseReviewEntityRepository = courseReviewEntityRepository;
        this.instructorReviewEntityRepository = instructorReviewEntityRepository;
    }
//    get all learners
    @Override
    public List<UserSummaryDTO> getAllLearners(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        verifyAdmin(user);
        return this.userEntityRepository.findAllLearners();
    }
//    get all instructors
    @Override
    public List<UserSummaryDTO> getAllInstructors(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        verifyAdmin(user);
        return this.userEntityRepository.findAllInstructors();
    }
//    suspend user
    @Override
    public String suspendUser(String userEmail) {
        UserEntity user = this.userEntityRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User profile not found."));
        user.setAccountStatus(UserAccountStatus.SUSPENDED);
        this.userEntityRepository.save(user);
        return "Your account is suspended.";
    }
//    activate user
    @Override
    public String activateUser(String userEmail) {
        UserEntity user = this.userEntityRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User profile not found."));
        user.setAccountStatus(UserAccountStatus.ACTIVE);
        userEntityRepository.save(user);
        return "Welcome back! Your account has been reactivated. " +
                "Please follow our community guidelines and help keep the learning environment positive for everyone.";    }
//    deactivate user
    @Override
    public String deactivateUser(String userEmail) {
        UserEntity user = this.userEntityRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User profile not found."));
        user.setAccountStatus(UserAccountStatus.ACTIVE);
        this.userEntityRepository.save(user);
        return "Your account is temporarily deactivated by admin.";
    }
//    get the user details
    @Override
    public UserDetailsDTO getUserProfile(String userEmail) {
        UserEntity user = this.userEntityRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User profile not found."));
        return UserDetailsDTO.builder()
                .email(user.getEmail())
                .userFullName(user.getUserFullName())
                .accountStatus(user.getAccountStatus())
                .userRoles(user.getUserRoles())
                .accountCreatedAt(user.getAccountCreatedAt())
                .build();
    }
//    get the courses of the instructor
    @Override
    public List<CourseDetailsDTO> getCourseOfInstructor(String instructorEmail) {
        UserEntity user = this.userEntityRepository.findByEmail(instructorEmail)
                .orElseThrow(() -> new UserNotFoundException("User profile not found."));
        validateAdmin(user);        
        return this.courseEntityRepository.getCourseOfInstructor(instructorEmail);
    }
//    get individual course dto
    @Override
    public IndividualCourseDTO getIndividualCourse(Long courseId) {
        CourseEntity course = this.courseEntityRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found."));
        InstructorProfileEntity instructorProfileEntity = course.getInstructor();
        return IndividualCourseDTO.builder()
                .courseId(course.getCourseId())
                .instructorProfileDTO(mapToInstructorProfileDTO(instructorProfileEntity))
                .courseTitle(course.getCourseTitle())
                .courseDescription(course.getCourseDescription())
                .courseCode(course.getCourseCode())
                .courseStatus(course.getCourseStatus())
                .courseType(course.getCourseType())
                .coursePrice(course.getCoursePrice())
                .courseDuration(course.getCourseDuration())
                .isPublic(course.getIsPublic())
                .ratingCount(course.getRatingCount())
                .averageRatingOfCourse(course.getAverageRatingOfCourse())
                .totalEnrollmentInCourse(course.getTotalEnrollmentInCourse())
                .totalReviewsInCourse(course.getTotalReviewsInCourse())
                .mediaEntityDTOList(course.getCourseMediaEntities()
                        .stream()
                        .map(this::mapToMediaEntityDTO)
                        .toList())
                .courseCreatedAt(course.getCourseCreatedAt())
                .build();
    }
//    discontinue course
    @Override
    public void discontinueCourse(Long courseId) {
        CourseEntity course = this.courseEntityRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found."));
        course.setCourseStatus(CourseStatus.ADMIN_DISCONTINUE);
        this.courseEntityRepository.save(course);
        if(!course.getCourseStatus().equals(CourseStatus.ADMIN_DISCONTINUE))
            throw new RuntimeException("Something went wrong. Status not updated. Please try again.");
    }
//    publish course
    @Override
    public void publishCourse(Long courseId) {
        CourseEntity course = this.courseEntityRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found."));
        course.setCourseStatus(CourseStatus.PUBLISHED);
        this.courseEntityRepository.save(course);
        if(!course.getCourseStatus().equals(CourseStatus.PUBLISHED))
            throw new RuntimeException("Something went wrong. Status not updated. Please try again.");
    }
//    delete user's review
    @Override
    public void deleteCourseReview(String userEmail, Long reviewId) {
        UserEntity user = this.userEntityRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        StudentProfileEntity studentProfileEntity = this.studentEntityRepository.findByStudent_UserId(user.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Student Profile not found."));
        CourseReviewEntity courseReviewEntity = this.courseReviewEntityRepository
                .findById(reviewId).orElseThrow(() -> new ResourceNotFoundException("Review not found."));
        boolean check = courseReviewEntity.getStudentProfileEntity().getStudentId().equals(studentProfileEntity.getStudentId());
        if(!check) throw new RuntimeException("The review not belongs to this student. Please check the review Id and the student details.");
        this.courseReviewEntityRepository.delete(courseReviewEntity);
    }
//    delete instructor review
    @Override
    public void deleteInstructorReview(String userEmail, Long reviewId) {
        UserEntity user = this.userEntityRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        StudentProfileEntity student = this.studentEntityRepository.findByStudent_UserId(user.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Student Profile not found."));
        InstructorReviewEntity instructorReviewEntity = this.instructorReviewEntityRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found."));
        boolean check = instructorReviewEntity.getStudentProfileEntity().getStudentId().equals(student.getStudentId());
        if(!check) throw new RuntimeException("The review not belongs to this student. Please check the review Id and the student details.");
        this.instructorReviewEntityRepository.delete(instructorReviewEntity);
    }
//    get all review of course by student
    @Override
    public List<AdminCourseReviewDTO> getAllReviewsOfCourses(String email, Long courseId) {
        UserEntity admin = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAdmin(admin);
        return this.courseReviewEntityRepository.getAllReviewsOfCourse(courseId);
    }
//    get all reviews of instructor by student
    @Override
    public List<AdminInstructorReviewDTO> getInstructorReviews(String email, Long instructorId) {
        UserEntity admin = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAdmin(admin);
        return this.instructorReviewEntityRepository.getAllReviewOFInstructor(instructorId);
    }

//    get all course reviews of student
    @Override
    public List<AdminStudentCourseReviewDTO> getAllCourseReviewsOfStudent
            (String email, String studentMail) {
        UserEntity admin = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAdmin(admin);
        return this.courseReviewEntityRepository.findAllStudentCourseReviews(studentMail);
    }
//    get all instructor reviews of student
    @Override
    public List<AdminStudentInstructorReview> getAllInstructorReviewsOfStudent(String email, String studentMail) {
        UserEntity admin = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        validateAdmin(admin);
        return this.instructorReviewEntityRepository.getAllStudentInstructorReview(studentMail);
    }

    //    helper method
//    map to media entity dto
    private MediaEntityDTO mapToMediaEntityDTO(CourseMediaEntity mediaEntity) {
        return MediaEntityDTO.builder()
                .courseMediaType(mediaEntity.getCourseMediaType())
                .mediaUrl(mediaEntity.getMediaUrl())
                .mediaUploadedTime(mediaEntity.getMediaUploadedTime())
                .build();
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
//    validate admin
    private boolean validateAdmin(UserEntity user) {
        return user.getUserRoles().contains(UserRole.ADMIN);
    }
    private void verifyAdmin(UserEntity user) {
        boolean isAdmin = validateAdmin(user);
        if(!isAdmin) throw new AccessDeniedException("Access Denied. You are forbidden to access this resource.");
    }
}
