package com.Gaurav.LMS3.Service.CourseServicePackage;

import com.Gaurav.LMS3.DTO.CourseDTO.InstructorCourseEntityDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.*;
import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.CourseMediaDTOPreview;
import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaProgressDTORequest;
import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaProgressDTOResponse;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.*;
import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentEntity;
import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentStatus;
import com.Gaurav.LMS3.Entity.UserEntityPackage.InstructorProfileEntity;
import com.Gaurav.LMS3.Entity.UserEntityPackage.UserAccountStatus;
import com.Gaurav.LMS3.Entity.UserEntityPackage.UserEntity;
import com.Gaurav.LMS3.Entity.UserEntityPackage.UserRole;
import com.Gaurav.LMS3.Exception.ResourceNotFoundException;
import com.Gaurav.LMS3.Exception.UserNotFoundException;
import com.Gaurav.LMS3.Repository.*;
import com.cloudinary.Cloudinary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service @Transactional @Slf4j
public class CourseServiceImplementation
        implements CourseService, MediaProgressService{

    private final MediaProgressEntityRepository mediaProgressEntityRepository;
    private final InstructorEntityRepository instructorEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final CourseEntityRepository courseEntityRepository;
    private final MediaEntityRepository mediaEntityRepository;
    private final EnrollmentEntityRepository enrollmentEntityRepository;
    private final Cloudinary cloudinary;
    public CourseServiceImplementation(
            MediaProgressEntityRepository mediaProgressEntityRepository,
            EnrollmentEntityRepository enrollmentEntityRepository,
            Cloudinary cloudinary,
            MediaEntityRepository mediaEntityRepository,
            InstructorEntityRepository instructorEntityRepository,
            UserEntityRepository userEntityRepository,
            CourseEntityRepository courseEntityRepository) {
        this.mediaProgressEntityRepository = mediaProgressEntityRepository;
        this.courseEntityRepository = courseEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.instructorEntityRepository = instructorEntityRepository;
        this.mediaEntityRepository = mediaEntityRepository;
        this.cloudinary = cloudinary;
        this.enrollmentEntityRepository = enrollmentEntityRepository;
    }
//    register course
    @Override
    public CourseRegistrationResponse registerCourse(String email, CourseRegistrationRequest courseRegistrationRequest) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Something went wrong."));
        if (isInstructorAccountDeactivated(user)) throw new AccessDeniedException("Account is DEACTIVATED.");
        if (isInstructorAccountSuspended(user)) throw new AccessDeniedException("Account is SUSPENDED.");
        if (!hasInstructorRole(user)) throw new AccessDeniedException("Only instructors can access this resource.");
        InstructorProfileEntity instructorProfileEntity = this.instructorEntityRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("Instructor profile not found."));
        if(instructorProfileEntity.getInstructorExpertise().isEmpty())
            throw new AccessDeniedException("First add your expertise. i.e; Skills.");
        CourseType courseType;
        try {
            courseType = CourseType.valueOf(courseRegistrationRequest.getCourseType().toUpperCase());
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("Invalid course type. Please select a valid course type."
                        + illegalArgumentException.getMessage());
        }
        String courseCode;
        do {
            courseCode = generateCourseCode(
                    courseRegistrationRequest.getCourseTitle(),
                    instructorProfileEntity.getInstructorId()
            );
        } while (courseEntityRepository.existsByCourseCode(courseCode));
        CourseEntity course = CourseEntity.builder()
                .instructor(instructorProfileEntity)
                .courseTitle(courseRegistrationRequest.getCourseTitle())
                .courseDescription(courseRegistrationRequest.getCourseDescription())
                .courseCode(courseCode)
                .courseStatus(CourseStatus.DRAFT)
                .courseType(courseType)
                .coursePrice(courseRegistrationRequest.getCoursePrice())
                .courseDuration(courseRegistrationRequest.getCourseDuration())
                .isPublic(false)
                .averageRatingOfCourse(0.0)
                .totalEnrollmentInCourse(0)
                .totalReviewsInCourse(0)
                .enrollmentsInCourse(new ArrayList<>())
                .courseReviews(new ArrayList<>())
                .courseMediaEntities(new ArrayList<>())
                .courseCreatedAt(LocalDateTime.now())
                .courseUpdatedAt(LocalDateTime.now())
                .build();
        course = this.courseEntityRepository.save(course);
        return mapToCourseRegistrationResponse(course);
    }
//    upload media
    @Override
    public String uploadMedia(String email,Long courseId, MultipartFile multipartFile, String mediaType) throws Exception {
        log.info("Upload media method started.");
        try {
            log.info("try block executed");
            CourseEntity course = this.courseEntityRepository.findByCourseIdAndInstructor_User_Email(courseId,email)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Course not found. Please check the course id."));
            if(!course.getInstructor().getUser().getEmail().equals(email))
                throw new AccessDeniedException("No course owner.");
            CourseMediaType courseMediaType = CourseMediaType.valueOf(mediaType.toUpperCase());
            Map<String, Object> uploadOptions = new HashMap<>();
            uploadOptions.put("resource_type",
                    ((courseMediaType == CourseMediaType.COURSE_VIDEO || courseMediaType == CourseMediaType.INTRO)
                            ? "video"
                            : "image"));
            Map result = cloudinary.uploader().upload(multipartFile.getBytes(), uploadOptions);
            String url = (String) result.getOrDefault("secure_url", null);
            String cloudId = (String) result.getOrDefault("public_id", null);
            Double duration = (Double) result.get("duration");
            if (duration == null || duration <= 0)
                throw new RuntimeException("Invalid media duration from Cloudinary");
            if (url == null || cloudId == null)
                throw new RuntimeException("Media upload failed: Cloudinary response incomplete");
            Integer lastOrder =
                     this.mediaEntityRepository.findLastMediaOrderByCourseId(courseId);
            int nextOrder = lastOrder + 1;
            CourseMediaEntity courseMediaEntity = CourseMediaEntity.builder()
                    .course(course)
                    .courseMediaType(courseMediaType)
                    .cloudinaryId(cloudId)
                    .mediaUrl(url)
                    .mediaDuration(duration)
                    .mediaOrdering(nextOrder)
                    .mediaUploadedTime(LocalDateTime.now())
                    .build();
            this.mediaEntityRepository.save(courseMediaEntity);
            course.getCourseMediaEntities().add(courseMediaEntity);
            this.courseEntityRepository.save(course);
            return url;
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("Invalid media type. Allowed: INTRO, COURSE_VIDEO, THUMBNAIL " +
                    illegalArgumentException.getMessage());
        }
        catch (Exception ex) {
            log.error("Media upload failed for courseId={}, error={}", courseId, ex.getMessage(), ex);
            throw new RuntimeException("Unable to upload media. Please try again later.");
        }
    }

//    preview of the media
    @Override
    public List<CourseMediaDTOPreview> mediaPreview(String email, Long courseId, String mediaType) {
        CourseEntity course = this.courseEntityRepository.findByCourseIdAndInstructor_User_Email(courseId,email)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found. Please check the course id."));
        List<CourseMediaDTOPreview> mediaUrls = new ArrayList<>();
        CourseMediaType courseMediaType;
        try {
            courseMediaType = CourseMediaType.valueOf(mediaType.toUpperCase());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("Invalid media type. Allowed: INTRO, COURSE_VIDEO, THUMBNAIL " +
                    illegalArgumentException.getMessage());
        }
        for (CourseMediaEntity media : course.getCourseMediaEntities()) {
            if (media.getCourseMediaType() == courseMediaType) {
                mediaUrls.add(mapToCourseMediaDTOPreview(media));
                if (courseMediaType == CourseMediaType.INTRO ||
                        courseMediaType == CourseMediaType.THUMBNAIL) break;
            }
        }
        return mediaUrls;
    }
//    update course
    @Override
    public String updateCourse(String email, Long courseId, CourseUpdateRequest courseUpdateRequest) {
        log.info("1st check.");
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if(isInstructorAccountSuspended(user)) throw new AccessDeniedException("Instructor account suspended. Please try again later.");
        if(isInstructorAccountDeactivated(user)) throw new AccessDeniedException("Instructor Account's deactivated.");
        CourseEntity course = this.courseEntityRepository.findByCourseIdAndInstructor_User_Email(courseId,email)
                .orElseThrow(() -> new ResourceNotFoundException("Something went wrong. Course not found."));
        if(!course.getInstructor().getUser().getEmail().equals(email)) throw new AccessDeniedException("You do not have ownership of this course.");
        log.info("2nd check.");
        if(courseUpdateRequest.getCourseTitle() != null && !courseUpdateRequest.getCourseTitle().isEmpty())
            course.setCourseTitle(courseUpdateRequest.getCourseTitle());
        if(courseUpdateRequest.getCourseDescription() != null && !courseUpdateRequest.getCourseDescription().isEmpty())
            course.setCourseDescription(courseUpdateRequest.getCourseDescription());
        if(courseUpdateRequest.getCourseType() != null && !courseUpdateRequest.getCourseType().isEmpty()) {
            CourseType courseType;
            try {
                courseType = CourseType.valueOf(courseUpdateRequest.getCourseType().toUpperCase());
            }
            catch (IllegalArgumentException illegalArgumentException) {
                throw new IllegalArgumentException("Invalid course type please select the valid course type " +
                        illegalArgumentException.getMessage());
            }
            course.setCourseType(courseType);
        }
        if(courseUpdateRequest.getCoursePrice() != null)
            course.setCoursePrice(courseUpdateRequest.getCoursePrice());
        course.setCourseUpdatedAt(LocalDateTime.now());
        this.courseEntityRepository.save(course);
        log.info("3rd check.");
        return "Course updated successfully.";
    }
//    change availability of the course
    @Override
    public String changeAvailabilityOfCourse(String email, Boolean choice, Long courseId) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if(isInstructorAccountSuspended(user)) throw new AccessDeniedException("Instructor account suspended. Please try again later.");
        if(isInstructorAccountDeactivated(user)) throw new AccessDeniedException("Instructor Account's deactivated.");
        CourseEntity course = this.courseEntityRepository.findByCourseIdAndInstructor_User_Email(courseId,email)
                .orElseThrow(() -> new ResourceNotFoundException("Something went wrong. Course not found please check the course id."));
        if(!course.getInstructor().getUser().getEmail().equals(email)) throw new AccessDeniedException("Access Denied. You do not have the ownership of this course.");
        course.setIsPublic(choice);
        if(choice) course.setCourseStatus(CourseStatus.PUBLISHED);
        else course.setCourseStatus(CourseStatus.DRAFT);
        course.setCourseUpdatedAt(LocalDateTime.now());
        this.courseEntityRepository.save(course);
        return "Availability of course changed.";
    }
//    get course
    @Override @Transactional(readOnly = true)
    public InstructorCourseEntityDTO getCourse(String email, Long courseId) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if(isInstructorAccountSuspended(user)) throw new AccessDeniedException("Instructor account suspended. Please try again later.");
        if(isInstructorAccountDeactivated(user)) throw new AccessDeniedException("Instructor Account's deactivated.");
        CourseEntity course = this.courseEntityRepository.findByCourseIdAndInstructor_User_Email(courseId,email)
                .orElseThrow(() -> new ResourceNotFoundException("Something went wrong. Course not found."));
        if(!course.getInstructor().getUser().getEmail().equals(email)) throw new AccessDeniedException("You do not have ownership of this course.");
        return mapToCourseEntityDTO(course);
    }
//    get all the publicly available courses
    @Override
    public List<PublicCourseEntityDTO> getAllPublicCourses(String email) {
        List<PublicCourseEntityDTO> publicCourses = this.courseEntityRepository
                .findAllPublicCourses(
                        CourseMediaType.THUMBNAIL,
                        CourseMediaType.INTRO,
                        CourseStatus.PUBLISHED);
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElse(null);
        if(user == null) {
            publicCourses.forEach(course -> course.setIntroVideoUrl(null));
            return publicCourses;
        }
        return publicCourses;
    }
//    get publicly available course
    @Override
    public PublicCourseEntityDTO getPublicCourseByLearner(String email, Long courseId) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElse(null);
        try {
            log.info("check 1");
            PublicCourseEntityDTO course = this.courseEntityRepository.findPublicCourse(
                    courseId,
                    CourseMediaType.THUMBNAIL,
                    CourseMediaType.INTRO,
                    CourseStatus.PUBLISHED
            );
            log.info("check 2");
            log.info("public course {}", course);
            if (user == null) course.setIntroVideoUrl(null);
            return course;
        } catch (Exception e) {
            throw new RuntimeException("Some exception occurs " + e.getMessage());
        }
    }
//    get the courses by keyword / searching and filtration
    @Override
    public List<PublicCourseEntityDTO> searchPublicCourses(String email, String keyword) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElse(null);
        List<PublicCourseEntityDTO> courses = this.courseEntityRepository.searchPublicCourses(
                keyword,
                CourseMediaType.THUMBNAIL,
                CourseMediaType.INTRO,
                CourseStatus.PUBLISHED);
        if(user == null) courses.forEach(course -> course.setIntroVideoUrl(null));
        return courses;
    }
//    get public courses by course categories
    @Override
    public List<PublicCourseEntityDTO> getCoursesByCategory(String email, String courseType) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElse(null);
        CourseType courseTyp;
        try {
            courseTyp = CourseType.valueOf(courseType.toUpperCase());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("""
                    Invalid input. Available types are \
                        LANGUAGE,
                        WEBDEV,
                        FRONTEND,
                        BACKEND,
                        PROGRAMMING,
                        DATA_SCIENCE,
                        AIML,
                        CLOUDCOMPUTING,
                        CYBERSECURITY,
                        DESIGN,
                        BUSINESS,
                        MARKETING,
                        PERSONALDEVELOPMENT,
                        OTHER""");
        }
        List<PublicCourseEntityDTO> publicCourseEntityDTOS = this.courseEntityRepository.findCourseByCategory(
                courseTyp,
                CourseMediaType.THUMBNAIL,
                CourseMediaType.INTRO,
                CourseStatus.PUBLISHED);
        if(user == null) publicCourseEntityDTOS.forEach(course -> course.setIntroVideoUrl(null));
        return publicCourseEntityDTOS;
    }
//    delete the media from the course playlist
    @Override
    public String deleteCourseMedia(String email, Long courseId, CourseMediaDeleteRequest courseMediaDeleteRequest) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if(isInstructorAccountSuspended(user)) throw new AccessDeniedException("User's account SUSPENDED.");
        if(isInstructorAccountDeactivated(user)) throw new AccessDeniedException("User's account DEACTIVATED.");
        CourseMediaEntity media = this.mediaEntityRepository.findByMediaIdAndCourse_CourseId(
                courseMediaDeleteRequest.getMediaId(),
                courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found."));
        Integer deletedMediaOrder = media.getMediaOrdering();
        this.mediaEntityRepository.delete(media);
        this.mediaEntityRepository.shiftMediaOrderAfterDelete(courseId,deletedMediaOrder);
        return "Course media deleted successfully";
    }
//    get course medias
    @Override
    public List<CourseMediaDTOPreview> getCourseMedias(String email, Long courseId) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if(isInstructorAccountDeactivated(user)) throw new AccessDeniedException("Account is DEACTIVATED.");
        if(isInstructorAccountSuspended(user)) throw new AccessDeniedException("Account is SUSPENDED.");
        CourseEntity course = this.courseEntityRepository.findByCourseIdAndInstructor_User_Email(
                courseId,
                email)
                .orElseThrow(() -> new ResourceNotFoundException("Courses not found."));
        return course.getCourseMediaEntities()
                .stream()
                .map(this::mapToCourseMediaDTOPreview)
                .toList();
    }
//    evaluate the media progress
    @Override
    public MediaProgressDTOResponse evaluateMediaProgress(String email, Long courseId, MediaProgressDTORequest request) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        EnrollmentEntity enrollment = this.enrollmentEntityRepository
                .findByEnrolledStudent_Student_UserIdAndCourse_CourseId(user.getUserId(), courseId)
                .orElseThrow(() -> new ResourceNotFoundException("No enrollment found for this course."));
        CourseMediaEntity courseMedia = this.mediaEntityRepository
                .findByMediaIdAndCourse_CourseId(request.getMediaId(), courseId)
                .orElseThrow(() -> new ResourceNotFoundException("No media found for this course."));
        double mediaDuration = courseMedia.getMediaDuration();
        if(mediaDuration <= 0) throw new IllegalStateException("Invalid media duration");
        double safeTime = request.getCurrentMediaTimeStamp() == null ? 0.0 : request.getCurrentMediaTimeStamp();
        safeTime = Math.max(0.0, Math.min(safeTime, mediaDuration));
        MediaProgressEntity mediaProgressEntity = this.mediaProgressEntityRepository
                .findByCourseMedia_MediaIdAndEnrollment_EnrollmentId(courseMedia.getMediaId(), enrollment.getEnrollmentId())
                .orElseGet(() -> MediaProgressEntity.builder()
                        .courseMedia(courseMedia)
                        .enrollment(enrollment)
                        .lastWatchTime(0.0)
                        .watchTimePercentage(0.0)
                        .isCompleted(false)
                        .lastUpdatedAt(LocalDateTime.now())
                        .build());
        if(safeTime > mediaProgressEntity.getLastWatchTime())
            mediaProgressEntity.setLastWatchTime(safeTime);
        double percentage = (mediaProgressEntity.getLastWatchTime() / mediaDuration) * 100.0;
        percentage = Math.min(100.0, percentage);
        mediaProgressEntity.setWatchTimePercentage(percentage);
        if(mediaProgressEntity.getWatchTimePercentage() >= 90.0)
            mediaProgressEntity.setIsCompleted(true);
        mediaProgressEntity.setLastUpdatedAt(LocalDateTime.now());
        this.mediaProgressEntityRepository.save(mediaProgressEntity);
        this.updateCourseCompletionPercentage(enrollment);
        return MediaProgressDTOResponse.builder()
                .mediaId(courseMedia.getMediaId())
                .lastWatchTime(mediaProgressEntity.getLastWatchTime())
                .watchPercentage(mediaProgressEntity.getWatchTimePercentage())
                .completed(mediaProgressEntity.getIsCompleted())
                .build();
    }
    //    helper methods
//    update complete course completion percentage
    private void updateCourseCompletionPercentage(EnrollmentEntity enrollment) {
        Long enrollmentId = enrollment.getEnrollmentId();
        List<MediaProgressEntity> mediaProgressEntityList = this.mediaProgressEntityRepository
                .findByEnrollment_EnrollmentId(enrollmentId);
        if(mediaProgressEntityList.isEmpty()) return;
        double totalWatchingPercentage = mediaProgressEntityList.stream()
                .mapToDouble(MediaProgressEntity::getWatchTimePercentage)
                .sum();
        double completionPercentage = totalWatchingPercentage / mediaProgressEntityList.size();
        completionPercentage = Math.min(100, completionPercentage);
        enrollment.setCourseCompletionPercentage(completionPercentage);
        if(completionPercentage == 100.0) enrollment.setCourseEnrollmentStatus(EnrollmentStatus.COMPLETED);
        this.enrollmentEntityRepository.save(enrollment);
    }
//    map to course entity dto
    private InstructorCourseEntityDTO mapToCourseEntityDTO(CourseEntity course) {
        Map<CourseMediaType, String> media = getCourseMediaUrls(course);
        return InstructorCourseEntityDTO.builder()
                .courseId(course.getCourseId())
                .courseTitle(course.getCourseTitle())
                .courseDescription(course.getCourseDescription())
                .courseCode(course.getCourseCode())
                .courseStatus(course.getCourseStatus().toString().toUpperCase())
                .courseType(course.getCourseType().toString().toUpperCase())
                .coursePrice(course.getCoursePrice())
                .courseDuration(course.getCourseDuration())
                .isCoursePublic(course.getIsPublic())
                .averageRatingOfCourse(course.getAverageRatingOfCourse())
                .totalEnrollmentInCourse(course.getTotalEnrollmentInCourse())
                .totalReviewsInCourse(course.getTotalReviewsInCourse())
                .courseThumbnailUrl(media.get(CourseMediaType.THUMBNAIL))
                .courseCourseIntroUrl(media.get(CourseMediaType.INTRO))
                .courseVideoUrl(getCourseVideoUrls(course))
                .courseCreatedAt(course.getCourseCreatedAt())
                .build();
    }
//    get specific url's
    private Map<CourseMediaType, String> getCourseMediaUrls(CourseEntity course) {
        return course.getCourseMediaEntities()
                .stream()
                .filter(media ->
                        media.getCourseMediaType() == CourseMediaType.THUMBNAIL ||
                        media.getCourseMediaType() == CourseMediaType.INTRO)
                .collect(Collectors.toMap(
                        CourseMediaEntity::getCourseMediaType,
                        CourseMediaEntity::getMediaUrl,
                        (existing, replacement) -> existing
                ));
    }
//    get a list of urls (for course videos)
    private List<String> getCourseVideoUrls(CourseEntity course) {
        return course.getCourseMediaEntities()
                .stream()
                .filter(media ->
                        media.getCourseMediaType() == CourseMediaType.COURSE_VIDEO)
                .map(CourseMediaEntity::getMediaUrl)
                .collect(Collectors.toList());
    }
//    map to course media dto preview
    private CourseMediaDTOPreview mapToCourseMediaDTOPreview(CourseMediaEntity courseMediaEntity) {
        return CourseMediaDTOPreview.builder()
                .mediaId(courseMediaEntity.getMediaId())
                .mediaOrder(courseMediaEntity.getMediaOrdering())
                .mediaUrl(courseMediaEntity.getMediaUrl())
                .courseMediaType(courseMediaEntity.getCourseMediaType())
                .build();
    }
//    generating course code
    private String generateCourseCode(String courseTitle, Long instructorId) {
        String prefix = courseTitle
                .toUpperCase()
                .replaceAll("[^A-Z ]", "")
                .trim()
                .replaceAll("\\s+", "-");
        if (prefix.length() > 20)
            prefix = prefix.substring(0, 20);
        String randomPart = String.valueOf(1000 + new SecureRandom().nextInt(9000));
        return prefix + "-" + instructorId + "-" + randomPart;
    }

//    check the role of instructor
    private boolean hasInstructorRole(UserEntity user) {
        return user.getUserRoles().contains(UserRole.INSTRUCTOR);
    }
//    check the status of instructor
    private boolean isInstructorAccountDeactivated(UserEntity user) {
        return user.getAccountStatus().equals(UserAccountStatus.DEACTIVATED);
    }
    private boolean isInstructorAccountSuspended(UserEntity user) {
        return user.getAccountStatus().equals(UserAccountStatus.SUSPENDED);
    }
//    map to course registration response
    private CourseRegistrationResponse mapToCourseRegistrationResponse(CourseEntity course) {
        return CourseRegistrationResponse.builder()
                .courseId(course.getCourseId())
                .instructorName(course.getInstructor().getUser().getUserFullName())
                .courseTitle(course.getCourseTitle())
                .courseDescription(course.getCourseDescription())
                .courseCode(course.getCourseCode())
                .courseDuration(course.getCourseDuration())
                .courseStatus(course.getCourseStatus().toString())
                .coursePrice(course.getCoursePrice())
                .isCoursePublic(course.getIsPublic())
                .courseCreatedAt(course.getCourseCreatedAt())
                .courseUpdateAt(course.getCourseUpdatedAt())
                .build();
    }
}
