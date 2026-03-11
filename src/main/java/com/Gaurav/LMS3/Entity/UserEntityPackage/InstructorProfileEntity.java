package com.Gaurav.LMS3.Entity.UserEntityPackage;

import com.Gaurav.LMS3.Entity.CourseEntityPackage.CourseEntity;
import com.Gaurav.LMS3.Entity.ReviewEntityPackage.InstructorReviewEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
@Entity @Table(name = "instructor_profiles")
public class InstructorProfileEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instructorId;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "instructor_expertise",
            joinColumns = @JoinColumn(name = "instructor_id"))
    @Column(name = "expertise")
    private Set<String> instructorExpertise = new HashSet<>();
    private Double averageRatingOfInstructor;
    private Integer reviewCount;
    @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY)
    private List<CourseEntity> courseEntities = new ArrayList<>();
    @OneToMany(mappedBy = "instructorProfileEntity", fetch = FetchType.LAZY)
    private List<InstructorReviewEntity> receivedReviews = new ArrayList<>();
}
