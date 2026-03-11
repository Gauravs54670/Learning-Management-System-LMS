package com.Gaurav.LMS3.Entity.ReviewEntityPackage;

import com.Gaurav.LMS3.Entity.UserEntityPackage.InstructorProfileEntity;
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
        name = "instructor_reviews",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"student_id", "instructor_id"}
        ),
        indexes = {
                @Index(
                        name = "idx_instructor_reviews_instructor",
                        columnList = "instructor_id"
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
public class InstructorReviewEntity {

//    core review fields
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instructorReviewId;
    @Column(nullable = false)
    @Max(5) @Min(1)
    private Integer rating;
    @Column(nullable = false, length = 2000)
    private String comment;

//    student who gave the review
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfileEntity studentProfileEntity;

//    instructor who is reviewed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private InstructorProfileEntity instructorProfileEntity;

//    auditing
    @CreatedDate
    private LocalDateTime reviewedAt;
}
