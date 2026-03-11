package com.Gaurav.LMS3.Repository;

import com.Gaurav.LMS3.DTO.CourseDTO.Enrollment.StudentEnrolledCourseDTO;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseMediaType;
import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentEntityRepository extends JpaRepository<EnrollmentEntity, Long> {
    boolean existsByCourse_CourseIdAndEnrolledStudent_Student_Email(Long courseId, String studentEmail);
    Optional<EnrollmentEntity> findByEnrolledStudent_Student_UserIdAndCourse_CourseId(Long studentId, Long courseId);
    @Query("""
            SELECT new com.Gaurav.LMS3.DTO.CourseDTO.Enrollment.StudentEnrolledCourseDTO(
                c.courseTitle,
                c.courseDescription,
                c.courseType,
                e.courseEnrollmentStatus,
                c.instructor.user.userFullName,
                MAX(CASE WHEN m.courseMediaType = :introType THEN m.mediaUrl END),
                MAX(CASE WHEN m.courseMediaType = :thumbnailType THEN m.mediaUrl END)
            )
            FROM EnrollmentEntity e
            JOIN e.course c
            LEFT JOIN c.courseMediaEntities m
            WHERE e.enrolledStudent.student.userId = :studentId
            GROUP BY
                c.courseId,
                c.courseTitle,
                c.courseDescription,
                c.courseType,
                e.courseEnrollmentStatus,
                c.instructor.user.userFullName
           """)
    List<StudentEnrolledCourseDTO> findEnrolledCourseByStudentId(
            @Param("studentId") Long studentId,
            @Param("introType") CourseMediaType introType,
            @Param("thumbnailType") CourseMediaType thumbnailType);
}
