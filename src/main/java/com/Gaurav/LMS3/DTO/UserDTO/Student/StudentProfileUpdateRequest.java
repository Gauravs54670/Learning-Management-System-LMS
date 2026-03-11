package com.Gaurav.LMS3.DTO.UserDTO.Student;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter @Setter
public class StudentProfileUpdateRequest {
    private String userFullName;
    private String bio;
}
