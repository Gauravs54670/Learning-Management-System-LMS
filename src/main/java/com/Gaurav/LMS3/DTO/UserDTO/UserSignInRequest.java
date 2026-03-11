package com.Gaurav.LMS3.DTO.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSignInRequest {
    private String email;
    private String password;
}
