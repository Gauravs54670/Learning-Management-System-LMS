package com.Gaurav.LMS3.Entity.CourseEntityPackage;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
@Entity @Table(name = "course_medias")
@EntityListeners(AuditingEntityListener.class)
public class CourseMediaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediaId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseMediaType courseMediaType;
    @Enumerated(EnumType.STRING)
    private MediaCompletionCheck mediaCompletionCheck;
    @Column(nullable = false)
    private String cloudinaryId;
    @Column(nullable = false)
    private String mediaUrl;
    private Double mediaDuration;
    @Column(nullable = false)
    private Integer mediaOrdering;
    @CreatedDate
    private LocalDateTime mediaUploadedTime;
}
