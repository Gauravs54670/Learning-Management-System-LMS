package com.Gaurav.LMS3.Repository;

import com.Gaurav.LMS3.DTO.UserDTO.AdminDashboardStats;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDashboardRepository {
    @Query("""
            SELECT new com.Gaurav.LMS3.DTO.UserDTO.AdminDashBoardStats(
                (SELECT COUNT(u) FROM UserEntity u),
                (SELECT COUNT(u) FROM UserEntity u WHERE u.accountStatus = com.Gaurav.LMS3.Entity.UserEntityPackage.UserAccountStatus.ACTIVE),
                (SELECT COUNT(u) FROM UserEntity u WHERE u.accountStatus = com.Gaurav.LMS3.Entity.UserEntityPackage.UserAccountStatus.SUSPENDED),
                (SELECT COUNT(s) FROM StudentProfileEntity s),
                (SELECT COUNT(i) FROM InstructorProfileEntity i),
                (SELECT COUNT(c) FROM CourseEntity c),
                (SELECT COUNT(c) FROM CourseEntity c WHERE c.courseStatus = com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus.PUBLISHED),
                (SELECT COUNT(c) FROM CourseEntity c WHERE c.courseStatus = com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus.DRAFT),
                (SELECT COUNT(c) FROM CourseEntity c
                 WHERE c.courseStatus = com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus.ADMIN_DISCONTINUE
                       OR c.courseStatus = com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus.INSTRUCTOR_DISCONTINUE),
                (SELECT COUNT(e) FROM EnrollmentEntity e),
                (SELECT COUNT(e) FROM EnrollmentEntity e WHERE e.courseEnrollmentStatus = com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentStatus.ENROLLED),
                (SELECT COUNT(e) FROM EnrollmentEntity e WHERE e.courseEnrollmentStatus = com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentStatus.COMPLETED),
                (SELECT COUNT(e) FROM EnrollmentEntity e
                 WHERE e.courseEnrollmentStatus = com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentStatus.CANCELLED),
                (SELECT COUNT(cr) FROM CourseReviewEntity cr),
                (SELECT COUNT(ir) FROM InstructorReviewEntity ir),
                (SELECT AVG(c.averageRatingOfCourse) FROM CourseEntity c),
                (SELECT AVG(i.averageRatingOfInstructor) FROM InstructorProfileEntity i),
                (SELECT COUNT(c) FROM CourseEntity c
                 WHERE FUNCTION('DATE', c.courseCreatedAt) = CURRENT_DATE),
                (SELECT COUNT(u) FROM UserEntity u
                 WHERE FUNCTION('DATE', u.accountCreatedAt) = CURRENT_DATE)
            )
            """)
    AdminDashboardStats getAdminDashBoardStats();
}
