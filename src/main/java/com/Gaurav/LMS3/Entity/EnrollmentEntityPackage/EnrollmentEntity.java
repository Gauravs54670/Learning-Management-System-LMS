package com.Gaurav.LMS3.Entity.EnrollmentEntityPackage;

import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseEntity;
import com.Gaurav.LMS3.Entity.UserEntityPackage.StudentProfileEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
@Entity @Table(
        name = "enrollments",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"student_id", "course_id"}
        ),
        indexes = {
                @Index(name = "idx_student_enrollment", columnList = "student_id"),
                @Index(name = "idx_course_enrollment", columnList = "course_id")
        }
)
@EntityListeners(AuditingEntityListener.class)
public class EnrollmentEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfileEntity enrolledStudent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;
    private Boolean isCourseExplored = false;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus courseEnrollmentStatus;
    @Column(nullable = false)
    private Double courseCompletionPercentage = 0.0;
    @CreatedDate
    private LocalDateTime enrolledAt;
    private LocalDateTime enrollmentCanceledAt;
}
