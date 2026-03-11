package com.Gaurav.LMS3.Repository;

import com.Gaurav.LMS3.DTO.UserDTO.UserSummaryDTO;
import com.Gaurav.LMS3.Entity.UserEntityPackage.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    @Query("""
        SELECT new com.Gaurav.LMS3.DTO.UserDTO.UserSummaryDTO(
            u.email,
            u.userFullName,
            u.bio,
            u.accountStatus,
            u.accountCreatedAt
        )
        FROM UserEntity u
        WHERE 'LEARNER' MEMBER OF u.userRoles
    """)
    List<UserSummaryDTO> findAllLearners();

    @Query("""
        SELECT new com.Gaurav.LMS3.DTO.UserDTO.UserSummaryDTO(
            u.email,
            u.userFullName,
            u.bio,
            u.accountStatus,
            u.accountCreatedAt
        )
        FROM UserEntity u
        WHERE 'INSTRUCTOR' MEMBER OF u.userRoles
    """)
    List<UserSummaryDTO> findAllInstructors();

}
