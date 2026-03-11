package com.Gaurav.LMS3.Repository;

import com.Gaurav.LMS3.DTO.UserDTO.Instructor.AdminInstructorReviewDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorReviewDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Student.AdminStudentInstructorReview;
import com.Gaurav.LMS3.Entity.ReviewEntityPackage.InstructorReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorReviewEntityRepository extends JpaRepository<InstructorReviewEntity, Long> {
    @Query("""
            SELECT new com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorReviewDTO(
                ir.rating,
                ir.comment,
                ir.studentProfileEntity.student.userFullName,
                ir.reviewedAt
            )
            FROM InstructorReviewEntity ir
            WHERE ir.instructorProfileEntity.instructorId = :instructorId
            """)
    List<InstructorReviewDTO> findAllMyReviews(@Param("instructorId") Long instructorId);
    @Query("""
            SELECT new com.Gaurav.LMS3.DTO.UserDTO.Instructor.AdminInstructorReviewDTO(
                ir.rating,
                ir.comment,
                ir.studentProfileEntity.studentId,
                ir.studentProfileEntity.student.userFullName,
                ir.reviewedAt
            )
            FROM InstructorReviewEntity ir
            WHERE ir.instructorProfileEntity.instructorId = :instructorId
            """)
    List<AdminInstructorReviewDTO> getAllReviewOFInstructor(@Param("instructorId") Long instructorId);
    @Query("""
            SELECT new com.Gaurav.LMS3.DTO.UserDTO.Student.AdminStudentInstructorReview(
                ir.rating,
                ir.comment,
                ir.instructorProfileEntity.instructorId,
                ir.instructorProfileEntity.user.userFullName,
                ir.reviewedAt
            )
            FROM InstructorReviewEntity ir
            WHERE ir.studentProfileEntity.student.email = :email
            """)
    List<AdminStudentInstructorReview> getAllStudentInstructorReview(@Param("email") String studentMail);
}
