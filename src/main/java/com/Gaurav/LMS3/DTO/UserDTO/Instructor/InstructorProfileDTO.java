package com.Gaurav.LMS3.DTO.UserDTO.Instructor;

import com.Gaurav.LMS3.Entity.UserEntityPackage.UserAccountStatus;
import lombok.*;

import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
public class InstructorProfileDTO {
    private String email;
    private String fullName;
    private Set<String> expertise;
    private UserAccountStatus userAccountStatus;
}
