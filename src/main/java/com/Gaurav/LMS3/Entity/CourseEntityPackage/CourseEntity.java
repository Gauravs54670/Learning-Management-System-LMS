package com.Gaurav.LMS3.Entity.CourseEntityPackage;

import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentEntity;
import com.Gaurav.LMS3.Entity.ReviewEntityPackage.CourseReviewEntity;
import com.Gaurav.LMS3.Entity.UserEntityPackage.InstructorProfileEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
@Entity @Table(
        name = "courses",
        indexes = {
                @Index(name = "idx_course_status", columnList = "courseStatus"),
                @Index(name = "idx_course_visibility", columnList = "isPublic")
        }
)
public class CourseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    @ManyToOne(fetch = FetchType.LAZY)

//    Course owner mapping
    @JoinColumn(name = "instructor_id", nullable = false)
    private InstructorProfileEntity instructor;

//    Core course fields
    @Column(nullable = false, length = 200)
    private String courseTitle;
    @Column(nullable = false, length = 2500)
    private String courseDescription;
    @Column(nullable = false, unique = true)
    private String courseCode;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseType courseType;
    @Column(nullable = false)
    private Double coursePrice;
    private String courseDuration;
    @Column(nullable = false)
    private Boolean isPublic = false;
    private Long ratingCount = 0L;
    private Long ratingSum = 0L;
    private Double averageRatingOfCourse;
    private Integer totalEnrollmentInCourse;
    private Integer totalReviewsInCourse;
//    relationship with other related entities
    @OneToMany(
            mappedBy = "course",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<EnrollmentEntity> enrollmentsInCourse = new ArrayList<>();
    @OneToMany(
            mappedBy = "course",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<CourseReviewEntity> courseReviews = new ArrayList<>();
    @OneToMany(
            mappedBy = "course",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<CourseMediaEntity> courseMediaEntities = new ArrayList<>();

//    auditing
    @CreatedDate
    private LocalDateTime courseCreatedAt;
    @LastModifiedDate
    private LocalDateTime courseUpdatedAt;
}
