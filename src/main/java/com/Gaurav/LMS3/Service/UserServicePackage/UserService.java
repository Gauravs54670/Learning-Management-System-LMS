package com.Gaurav.LMS3.Service.UserServicePackage;


import java.util.Set;

public interface UserService {
    String changeAccountStatus(String email, String status);
    String verifyUserForPasswordChange(String email, String oldPassword);
    String changePassword(String email, String otp ,String newPassword);
    String getMyAccountStatus(String email);
    Set<String> getMyRoles(String email);
}
