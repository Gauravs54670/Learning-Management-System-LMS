package com.Gaurav.LMS3.Repository;

import com.Gaurav.LMS3.Entity.UserEntityPackage.StudentProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentEntityRepository extends JpaRepository<StudentProfileEntity, Long> {
    Optional<StudentProfileEntity> findByStudent_UserId(Long userId);
    @Query("""
            SELECT s.studentId
            FROM StudentProfileEntity s
            WHERE s.student.userId = :userId
            """)
    Optional<Long> findStudentIdByUserId(@Param("userId") Long userId);

}
