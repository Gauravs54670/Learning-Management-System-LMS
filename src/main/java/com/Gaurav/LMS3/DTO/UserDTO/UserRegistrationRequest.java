package com.Gaurav.LMS3.DTO.UserDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter @Builder
public class UserRegistrationRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String fullName;
    @NotBlank @Size(min = 8)
    private String password;
}
