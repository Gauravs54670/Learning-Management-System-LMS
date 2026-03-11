package com.Gaurav.LMS3.DTO.UserDTO.Instructor;

import lombok.*;

import java.util.Set;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class InstructorProfileUpdateRequest {
    private Set<String> expertise;
    private String userFullName;
    private String bio;
}
