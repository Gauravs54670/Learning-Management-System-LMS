package com.Gaurav.LMS3.Entity.UserEntityPackage;

import jakarta.persistence.*;
@Entity
@Table(name = "user_role")
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;
}
