package com.Gaurav.LMS3.Entity.CourseEntityPackage;

import com.Gaurav.LMS3.Entity.EnrollmentEntityPackage.EnrollmentEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "media_progress",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"enrollment_id", "media_id"})
        }
)
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class MediaProgressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private EnrollmentEntity enrollment;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "media_id", nullable = false)
    private CourseMediaEntity courseMedia;
    @Column(nullable = false)
    private Double lastWatchTime = 0.0;
    @Column(nullable = false)
    private Double watchTimePercentage = 0.0;
    @Column(nullable = false)
    private Boolean isCompleted = false;
    @LastModifiedDate
    private LocalDateTime lastUpdatedAt;
}
