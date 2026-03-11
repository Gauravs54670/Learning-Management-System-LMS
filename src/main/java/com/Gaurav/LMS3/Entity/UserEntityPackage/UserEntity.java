package com.Gaurav.LMS3.Entity.UserEntityPackage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
@Entity @Table(
        name = "user",
        uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String userFullName;
    @JsonIgnore @Column(nullable = false)
    private String accountPassword;
    private String bio;
    private String otpColumn;
    private LocalDateTime otpExpiry;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private UserAccountStatus accountStatus;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "user_role", length = 20)
    private Set<UserRole> userRoles;
    @CreatedDate
    private LocalDateTime accountCreatedAt;
    @LastModifiedDate
    private LocalDateTime accountUpdatedAt;
}
