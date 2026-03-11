package com.Gaurav.LMS3.Entity.ReviewEntityPackage;

import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseEntity;
import com.Gaurav.LMS3.Entity.UserEntityPackage.StudentProfileEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
@Entity @Table(
        name = "course_reviews",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"student_id", "course_id"}
        ),
        indexes = {
                @Index(
                        name = "idx_course_reviews_course",
                        columnList = "course_id")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class CourseReviewEntity {
//    core reviews fields
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseReviewId;
    @Column(nullable = false)
    @Min(1) @Max(5)
    private Integer rating;
    @Column(nullable = false, length = 2000)
    private String comment;
//    student who reviewed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfileEntity studentProfileEntity;
//    course which reviewed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;
//    auditing
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime reviewedAt;
}
