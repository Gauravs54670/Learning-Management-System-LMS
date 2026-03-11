package com.Gaurav.LMS3.Repository;

import com.Gaurav.LMS3.Entity.UserEntityPackage.InstructorProfileEntity;
import com.Gaurav.LMS3.Entity.UserEntityPackage.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorEntityRepository extends JpaRepository<InstructorProfileEntity, Long> {
    Optional<InstructorProfileEntity> findByUser(UserEntity user);
}
