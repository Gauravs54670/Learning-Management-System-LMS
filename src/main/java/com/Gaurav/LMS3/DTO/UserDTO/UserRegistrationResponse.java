package com.Gaurav.LMS3.DTO.UserDTO;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class UserRegistrationResponse {
    private Long userId;
    private String email;
    private String message;
}
