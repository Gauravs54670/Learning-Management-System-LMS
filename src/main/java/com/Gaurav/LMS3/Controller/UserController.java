package com.Gaurav.LMS3.Controller;

import com.Gaurav.LMS3.Service.UserServicePackage.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController @RequestMapping("/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
//    verify account before changing password
    @PostMapping("/password-change-request/verify-account")
    public ResponseEntity<?> verifyAccountForPasswordChange(
            @RequestParam(name = "old password") String oldPassword,
            Authentication authentication) {
                String email = authentication.getName();
                String message = this.userService.verifyUserForPasswordChange(email, oldPassword);
                return new ResponseEntity<>(Map.of(
                        "message", message
                ), HttpStatus.OK);
    }
//    change password
    @PostMapping("/password-change")
    public ResponseEntity<?> changePassword(
            Authentication authentication,
            @RequestParam(name = "otp") String otp,
            @RequestParam(name = "new password") String newPassword) {
        String email = authentication.getName();
        String message = this.userService.changePassword(email, otp, newPassword);
        return new ResponseEntity<>(Map.of("message", message),HttpStatus.OK);
    }
//    get account status
    @GetMapping("/account-status")
    public ResponseEntity<?> accountStatus(Authentication authentication) {
        String email = authentication.getName();
        String response = this.userService.getMyAccountStatus(email);
        return new ResponseEntity<>(Map.of("response", response),HttpStatus.OK);
    }
//    get user's roles
    @GetMapping("/get-roles")
    public ResponseEntity<?> getRoles(Authentication authentication) {
        String email = authentication.getName();
        Set<String> roles = this.userService.getMyRoles(email);
        return new ResponseEntity<>(Map.of(
                "message", "User's roles fetched successfully",
                "Roles", roles
        ),HttpStatus.OK);
    }

}
