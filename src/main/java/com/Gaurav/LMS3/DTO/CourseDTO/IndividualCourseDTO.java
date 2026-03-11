package com.Gaurav.LMS3.DTO.CourseDTO;

import com.Gaurav.LMS3.DTO.CourseDTO.MediaDTO.MediaEntityDTO;
import com.Gaurav.LMS3.DTO.UserDTO.Instructor.InstructorProfileDTO;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseStatus;
import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class IndividualCourseDTO {
    private Long courseId;
    private InstructorProfileDTO instructorProfileDTO;
    private String courseTitle;
    private String courseDescription;
    private String courseCode;
    private CourseStatus courseStatus;
    private CourseType courseType;
    private Double coursePrice;
    private String courseDuration;
    private Boolean isPublic;
    private Long ratingCount;
    private Double averageRatingOfCourse;
    private Integer totalEnrollmentInCourse;
    private Integer totalReviewsInCourse;
    private LocalDateTime courseCreatedAt;
    private List<MediaEntityDTO> mediaEntityDTOList;
}
