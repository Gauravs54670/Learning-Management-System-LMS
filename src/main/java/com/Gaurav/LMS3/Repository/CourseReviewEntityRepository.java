package com.Gaurav.LMS3.Repository;

import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.AdminCourseReviewDTO;
import com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.CourseReviewDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Student.AdminStudentCourseReviewDTO;
import com.Gaurav.LMS3.Entity.ReviewEntityPackage.CourseReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseReviewEntityRepository extends JpaRepository<CourseReviewEntity, Long> {
    Optional<CourseReviewEntity> findByStudentProfileEntity_StudentIdAndCourse_CourseId(Long studentId, Long courseId);
    boolean existsByStudentProfileEntity_StudentIdAndCourse_CourseId(Long studentId, Long courseId);
    @Modifying
    @Query("""
            UPDATE CourseEntity c
                SET c.ratingSum = c.ratingSum + :rating,
                    c.ratingCount = c.ratingCount + 1,
                    c.totalReviewsInCourse = c.totalReviewsInCourse + 1,
                    c.averageRatingOfCourse = (c.ratingSum + :rating) * 1.0 / (c.ratingCount + 1)
                WHERE c.courseId = :courseId
            """)
    void updateCourseAverageRating(
            @Param("courseId") Long courseId,
            @Param("rating") Long rating);

    @Modifying
    @Query("""
        UPDATE CourseEntity c
           SET c.ratingSum =
                 CASE WHEN c.ratingSum >= :rating
                      THEN c.ratingSum - :rating
                      ELSE 0 END,
               c.ratingCount =
                 CASE WHEN c.ratingCount > 0
                      THEN c.ratingCount - 1
                      ELSE 0 END,
               c.totalReviewsInCourse =
                 CASE WHEN c.totalReviewsInCourse > 0
                      THEN c.totalReviewsInCourse - 1
                      ELSE 0 END,
               c.averageRatingOfCourse =
                 CASE
                   WHEN (c.ratingCount - 1) <= 0 THEN 0
                   ELSE (c.ratingSum - :rating) * 1.0 / (c.ratingCount - 1)
                 END
         WHERE c.courseId = :courseId
    """)
        void decreaseCourseReviewRating(
                @Param("courseId") Long courseId,
                @Param("rating") Long rating
        );
    @Query("""
            SELECT new com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.CourseReviewDTO(
                cr.rating,
                cr.comment,
                cr.reviewedAt,
                cr.studentProfileEntity.student.userFullName
            )
            FROM CourseReviewEntity cr
            WHERE cr.course.instructor.instructorId = :instructorId
            """)
    List<CourseReviewDTO> findAllMyCourseReviews(@Param("instructorId") Long instructorId);
    @Query("""
            SELECT new com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.CourseReviewDTO(
                cr.rating,
                cr.comment,
                cr.reviewedAt,
                cr.studentProfileEntity.student.userFullName
            )
            FROM CourseReviewEntity cr
            WHERE cr.course.instructor.instructorId = :instructorId
            AND cr.course.courseId = :courseId
            """)
    List<CourseReviewDTO> findAllReviewOfCourse(
            @Param("instructorId") Long instructorId,
            @Param("courseId") Long courseId);
    @Query("""
            SELECT new com.Gaurav.LMS3.DTO.CourseDTO.CourseReview.AdminCourseReviewDTO(
                cr.rating,
                cr.comment,
                cr.course.instructor.instructorId,
                cr.course.instructor.user.userFullName,
                cr.studentProfileEntity.studentId,
                cr.studentProfileEntity.student.userFullName,
                cr.reviewedAt
            )
            FROM CourseReviewEntity cr
            WHERE cr.course.courseId = :courseId
            """)
        List<AdminCourseReviewDTO> getAllReviewsOfCourse(@Param("courseId") Long courseId);
    @Query("""
                SELECT new com.Gaurav.LMS3.DTO.UserDTO.Student.AdminStudentCourseReviewDTO(
                    cr.rating,
                    cr.comment,
                    cr.course.courseId,
                    cr.course.courseTitle,
                    cr.course.instructor.instructorId,
                    cr.course.instructor.user.userFullName,
                    cr.reviewedAt
                )
                FROM CourseReviewEntity cr
                WHERE cr.studentProfileEntity.student.email = :email
            """)
    List<AdminStudentCourseReviewDTO> findAllStudentCourseReviews(@Param("email") String studentMail);
}
