package com.Gaurav.LMS3.Entity.UserEntityPackage;

import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentEntity;
import com.Gaurav.LMS3.Entity.ReviewEntityPackage.CourseReviewEntity;
import com.Gaurav.LMS3.Entity.ReviewEntityPackage.InstructorReviewEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
@Entity @Table(name = "student_profiles")
public class StudentProfileEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity student;
    @OneToMany(mappedBy = "enrolledStudent", fetch = FetchType.LAZY)
    private List<EnrollmentEntity> myEnrollments = new ArrayList<>();
    @OneToMany(mappedBy = "studentProfileEntity", fetch = FetchType.LAZY)
    private List<CourseReviewEntity> courseReviewed = new ArrayList<>();
    @OneToMany(mappedBy = "studentProfileEntity", fetch = FetchType.LAZY)
    private List<InstructorReviewEntity> reviewsGivenToInstructor = new ArrayList<>();
}
