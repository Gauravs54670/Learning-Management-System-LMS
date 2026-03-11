package com.Gaurav.LMS3.DTO.UserDTO;

import com.Gaurav.LMS3.Entity.UserEntityPackage.UserAccountStatus;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter @Setter
public class UserSummaryDTO {
    private String email;
    private String userFullName;
    private String bio;
    private String accountStatus;
    private LocalDateTime accountCreatedAt;
    public UserSummaryDTO(
           String email,
           String userFullName,
           String bio,
           UserAccountStatus accountStatus,
           LocalDateTime accountCreatedAt) {
        this.email = email;
        this.bio = bio;
        this.userFullName = userFullName;
        this.accountStatus = accountStatus.name();
        this.accountCreatedAt = accountCreatedAt;
    }
}
