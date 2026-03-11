package com.Gaurav.LMS3.Repository;

import com.Gaurav.LMS3.DTO.CourseDTO.CourseDetailsDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.InstructorCoursesOverview;
import com.Gaurav.LMS3.DTO.CourseDTO.PublicCourseEntityDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorCourseStatsDashBoard;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseEntity;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseMediaType;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseEntityRepository extends JpaRepository<CourseEntity, Long> {
    boolean existsByCourseCode(String courseCode);
    Optional<CourseEntity> findByCourseIdAndInstructor_User_Email(Long courseId, String email);
        @Query("""
    SELECT new com.Gaurav.LMS3.DTO.CourseDTO.InstructorCoursesOverview(
        c.courseId,
        c.courseTitle,
        c.courseDescription,
        c.courseType,
        c.courseStatus,
        c.courseDuration,
        m.mediaUrl
    )
    FROM CourseEntity c
    LEFT JOIN c.courseMediaEntities m
        ON m.courseMediaType = :mediaType
    WHERE c.instructor.user.email = :email
    AND c.courseStatus = :courseStatus
    """)
        List<InstructorCoursesOverview> findInstructorCourses(
                @Param("email") String email,
                @Param("courseStatus") CourseStatus courseStatus,
                @Param("mediaType") CourseMediaType mediaType
        );
        @Query("""
    SELECT new com.Gaurav.LMS3.DTO.CourseDTO.PublicCourseEntityDTO(
        c.courseId,
        c.courseTitle,
        c.courseDescription,
        c.courseType,
        c.coursePrice,
        c.instructor.user.userFullName,
        c.courseDuration,
        c.averageRatingOfCourse,
        c.totalEnrollmentInCourse,
        thumb.mediaUrl,
        intro.mediaUrl
    )
    FROM CourseEntity c
    LEFT JOIN c.courseMediaEntities thumb
        ON thumb.courseMediaType = :thumbnailType
    LEFT JOIN c.courseMediaEntities intro
        ON intro.courseMediaType = :introType
    WHERE c.isPublic = true
    AND c.courseStatus = :status
    """)
        List<PublicCourseEntityDTO> findAllPublicCourses(
                @Param("thumbnailType") CourseMediaType thumbnailType,
                @Param("introType") CourseMediaType introType,
                @Param("status") CourseStatus status
        );
    @Query("""
    SELECT new com.Gaurav.LMS3.DTO.CourseDTO.PublicCourseEntityDTO(
        c.courseId,
        c.courseTitle,
        c.courseDescription,
        c.courseType,
        c.coursePrice,
        c.instructor.user.userFullName,
        c.courseDuration,
        c.averageRatingOfCourse,
        c.totalEnrollmentInCourse,
        thumb.mediaUrl,
        intro.mediaUrl
    )
    FROM CourseEntity c
    LEFT JOIN c.courseMediaEntities thumb
        ON thumb.courseMediaType = :thumbnailType
    LEFT JOIN c.courseMediaEntities intro
        ON intro.courseMediaType = :introType
    WHERE c.courseId = :courseId
    AND c.isPublic = true
    AND c.courseStatus = :status
    """)
    PublicCourseEntityDTO findPublicCourse(
            @Param("courseId") Long courseId,
            @Param("thumbnailType") CourseMediaType thumbnailType,
            @Param("introType") CourseMediaType introType,
            @Param("status") CourseStatus status
    );
    @Query("""
    SELECT new com.Gaurav.LMS3.DTO.CourseDTO.PublicCourseEntityDTO(
        c.courseId,
        c.courseTitle,
        c.courseDescription,
        c.courseType,
        c.coursePrice,
        c.instructor.user.userFullName,
        c.courseDuration,
        c.averageRatingOfCourse,
        c.totalEnrollmentInCourse,
        thumb.mediaUrl,
        intro.mediaUrl
    )
    FROM CourseEntity c
    LEFT JOIN c.courseMediaEntities thumb
        ON thumb.courseMediaType = :thumbnailType
    LEFT JOIN c.courseMediaEntities intro
        ON intro.courseMediaType = :introType
    WHERE c.isPublic = true
    AND c.courseStatus = :status
    AND (
            LOWER(c.courseTitle) LIKE LOWER (CONCAT('%', :keyword, '%'))
            OR LOWER(c.courseDescription) LIKE LOWER (CONCAT('%', :keyword, '%'))
            OR LOWER(c.instructor.user.userFullName) LIKE LOWER (CONCAT('%', :keyword, '%'))
        )
    """)
    List<PublicCourseEntityDTO> searchPublicCourses(
            @Param("keyword") String keyword,
            @Param("thumbnailType") CourseMediaType thumbnailType,
            @Param("introType") CourseMediaType introType,
            @Param("status") CourseStatus status
    );
    @Query("""
    SELECT new com.Gaurav.LMS3.DTO.CourseDTO.PublicCourseEntityDTO(
        c.courseId,
        c.courseTitle,
        c.courseDescription,
        c.courseType,
        c.coursePrice,
        c.instructor.user.userFullName,
        c.courseDuration,
        c.averageRatingOfCourse,
        c.totalEnrollmentInCourse,
        thumb.mediaUrl,
        intro.mediaUrl
    )
    FROM CourseEntity c
    LEFT JOIN c.courseMediaEntities thumb
        ON thumb.courseMediaType = :thumbnailType
    LEFT JOIN c.courseMediaEntities intro
        ON intro.courseMediaType = :introType
    WHERE c.isPublic = true
    AND c.courseStatus = :status
    AND c.courseType = :courseType
    """)
    List<PublicCourseEntityDTO> findCourseByCategory(
            @Param("courseType") CourseType courseType,
            @Param("thumbnailType") CourseMediaType thumbnailType,
            @Param("introType") CourseMediaType introType,
            @Param("status") CourseStatus status
    );
    @Query("SELECT c.courseTitle FROM CourseEntity c WHERE c.courseId = :courseId")
    String getCourseTitleByCourseId(@Param("courseId") Long courseId);
    @Query("""
            SELECT new com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorCourseStatsDashBoard(
                COUNT(DISTINCT c.courseId),
                COUNT(CASE WHEN c.courseStatus = com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus.PUBLISHED THEN 1 END),
                COUNT(CASE WHEN c.courseStatus = com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus.DRAFT THEN 1 END),
                COUNT(DISTINCT e.enrollmentId),
                COUNT(DISTINCT cr.courseReviewId),
                COALESCE(AVG(cr.rating), 0)
            )
                FROM CourseEntity c
                LEFT JOIN c.enrollmentsInCourse e
                LEFT JOIN c.courseReviews cr
                WHERE c.instructor.instructorId = :instructorId
            """)
    InstructorCourseStatsDashBoard getInstructorCourseStats(@Param("instructorId") Long instructorId);
    @Query("""
            SELECT new com.Gaurav.LMS3.DTO.CourseDTO.CourseDetailsDTO(
                c.courseId,
                c.instructor.user.userFullName,
                c.courseTitle,
                c.courseDescription,
                c.courseCode,
                c.courseStatus,
                c.courseType,
                c.coursePrice,
                c.courseDuration,
                c.isPublic,
                c.ratingCount,
                c.averageRatingOfCourse,
                c.totalEnrollmentInCourse,
                c.totalReviewsInCourse,
                c.courseCreatedAt
            )
            FROM CourseEntity c
            WHERE c.instructor.user.email = :email
           """)
    List<CourseDetailsDTO> getCourseOfInstructor(@Param("email") String email);
}
