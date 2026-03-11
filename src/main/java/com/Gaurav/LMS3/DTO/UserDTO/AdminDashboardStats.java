package com.Gaurav.LMS3.DTO.UserDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter @Setter
public class AdminDashboardStats {
    private Long totalUsers;
    private Long totalActiveStudents;
    private Long totalSuspendedUsers;

    private Long totalStudents;
    private Long totalInstructors;

    private Long totalCourses;
    private Long totalPublishedCourses;
    private Long totalDraftCourses;
    private Long totalDiscontinuedCourses;

    private Long totalEnrollments;
    private Long totalActiveEnrollments;
    private Long totalCompletedEnrollments;
    private Long totalCancelEnrollments;

    private Long totalCourseReviews;
    private Long totalInstructorReviews;

    private Double averageCourseRatingAcrossPlatform;
    private Double averageInstructorRatingAcrossPlatform;

    private Long coursesCreatedToday;
    private Long totalUsersRegisteredToday;
    public AdminDashboardStats(
            Long totalUsers, Long totalActiveStudent, Long totalSuspendedUsers,
            Long totalStudents, Long totalInstructors,
            Long totalCourses, Long totalPublishedCourse, Long totalDraftCourse, Long totalDiscontinuedCourse,
            Long totalEnrollments, Long totalActiveEnrollments, Long totalCompletedEnrollments, Long totalCancelEnrollments,
            Long totalCourseReviews, Long totalInstructorReviews,
            Double averageCourseRatingAcrossPlatform, Double averageInstructorRatingAcrossPlatform,
            Long courseCreatedToday, Long totalUsersRegisteredToday){
        this.totalUsers = totalUsers;
        this.totalActiveStudents = totalActiveStudent;
        this.totalSuspendedUsers = totalSuspendedUsers;
        this.totalStudents = totalStudents;
        this.totalInstructors = totalInstructors;
        this.totalCourses = totalCourses;
        this.totalPublishedCourses = totalPublishedCourse;
        this.totalDraftCourses = totalDraftCourse;
        this.totalDiscontinuedCourses = totalDiscontinuedCourse;
        this.totalEnrollments = totalEnrollments;
        this.totalActiveEnrollments = totalActiveEnrollments;
        this.totalCompletedEnrollments = totalCompletedEnrollments;
        this.totalCancelEnrollments = totalCancelEnrollments;
        this.totalCourseReviews = totalCourseReviews;
        this.totalInstructorReviews = totalInstructorReviews;
        this.averageCourseRatingAcrossPlatform = averageCourseRatingAcrossPlatform;
        this.averageInstructorRatingAcrossPlatform = averageInstructorRatingAcrossPlatform;
        this.coursesCreatedToday = courseCreatedToday;
        this.totalUsersRegisteredToday = totalUsersRegisteredToday;
    }
}
