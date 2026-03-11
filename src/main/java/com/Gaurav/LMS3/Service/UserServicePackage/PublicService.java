package com.Gaurav.LMS3.Service.UserServicePackage;

import com.Gaurav.LMS3.DTO.UserDTO.UserRegistrationRequest;
import com.Gaurav.LMS3.DTO.UserDTO.UserRegistrationResponse;

public interface PublicService {
    UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest);
    String forgotPasswordRequest(String email);
    String changeAccountPassword(String email, String otp, String newPassword);
}
