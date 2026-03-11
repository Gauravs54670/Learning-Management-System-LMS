package com.Gaurav.LMS3.DTO.UserDTO;

import com.Gaurav.LMS3.Entity.UserEntityPackage.UserAccountStatus;
import com.Gaurav.LMS3.Entity.UserEntityPackage.UserRole;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class UserDetailsDTO {
    private String email;
    private String userFullName;
    private String bio;
    private UserAccountStatus accountStatus;
    private Set<UserRole> userRoles;
    private LocalDateTime accountCreatedAt;
}
