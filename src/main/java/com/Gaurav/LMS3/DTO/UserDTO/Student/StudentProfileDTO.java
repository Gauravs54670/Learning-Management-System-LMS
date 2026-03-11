package com.Gaurav.LMS3.DTO.UserDTO.Student;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter @Setter @Builder
public class StudentProfileDTO {
    private String email;
    private String userFullName;
    private String bio;
    private LocalDateTime accountCreatedAt;
    public StudentProfileDTO(String email,String userFullName, String bio, LocalDateTime accountCreatedAt) {
        this.email = email;
        this.userFullName = userFullName;
        this.bio = bio;
        this.accountCreatedAt = accountCreatedAt;
    }
}
