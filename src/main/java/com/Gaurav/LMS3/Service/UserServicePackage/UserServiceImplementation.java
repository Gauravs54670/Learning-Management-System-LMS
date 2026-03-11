package com.Gaurav.LMS3.Service.UserServicePackage;

import com.Gaurav.LMS3.DTO.UserDTO.UserRegistrationRequest;
import com.Gaurav.LMS3.DTO.UserDTO.UserRegistrationResponse;
import com.Gaurav.LMS3.Entity.UserEntityPackage.StudentProfileEntity;
import com.Gaurav.LMS3.Entity.UserEntityPackage.UserAccountStatus;
import com.Gaurav.LMS3.Entity.UserEntityPackage.UserEntity;
import com.Gaurav.LMS3.Entity.UserEntityPackage.UserRole;
import com.Gaurav.LMS3.Exception.UserNotFoundException;
import com.Gaurav.LMS3.Repository.StudentEntityRepository;
import com.Gaurav.LMS3.Repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service @Transactional @Slf4j
public class UserServiceImplementation implements
        UserService,
        PublicService{
    private final JavaMailSender javaMailSender;
    private final StudentEntityRepository studentEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImplementation(
            JavaMailSender javaMailSender,
            StudentEntityRepository studentEntityRepository,
            PasswordEncoder passwordEncoder,
            UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentEntityRepository = studentEntityRepository;
        this.javaMailSender = javaMailSender;
    }
//    register user
    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest) {
        boolean exist = this.userEntityRepository.findByEmail(userRegistrationRequest.getEmail()).isPresent();
        if (exist)
            throw new RuntimeException("The provided email is already registered. Please check your email.");
        UserEntity user = UserEntity.builder()
                .email(userRegistrationRequest.getEmail())
                .userFullName(userRegistrationRequest.getFullName())
                .accountPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()))
                .bio(null)
                .accountStatus(UserAccountStatus.ACTIVE)
                .userRoles(Set.of(UserRole.LEARNER))
                .accountCreatedAt(LocalDateTime.now())
                .accountUpdatedAt(LocalDateTime.now())
                .build();
        StudentProfileEntity studentProfileEntity = StudentProfileEntity.builder()
                .student(user)
                .build();
        user = this.userEntityRepository.save(user);
        this.studentEntityRepository.save(studentProfileEntity);
        return mapToUserRegistrationResponse(user);
    }
//    forgot password request
    @Override
    public String forgotPasswordRequest(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        String otp = generateOtp();
        user.setOtpColumn(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userEntityRepository.save(user);
        sendOtpMail(user.getEmail(), otp);
        return "OTP sent to registered email";
    }
//    change account password
    @Override
    public String changeAccountPassword(String email, String otp, String newPassword) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if (user.getOtpColumn() == null || user.getOtpExpiry() == null)
            throw new AccessDeniedException("OTP not requested.");
        if (user.getOtpExpiry().isBefore(LocalDateTime.now()))
            throw new AccessDeniedException("OTP expired.");
        if (!otp.equals(user.getOtpColumn()))
            throw new AccessDeniedException("Invalid OTP.");
        user.setAccountPassword(passwordEncoder.encode(newPassword));
        user.setOtpExpiry(null);
        user.setOtpColumn(null);
        user.setAccountUpdatedAt(LocalDateTime.now());
        this.userEntityRepository.save(user);
        return "Password changed successfully.";
    }

    @Override
    public String changeAccountStatus(String email, String status) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Something went wrong. Profile not found."));
        if(user.getAccountStatus().equals(UserAccountStatus.SUSPENDED))
            throw new AccessDeniedException("Your account is suspended. Please try after sometime.");
        try {
            UserAccountStatus accountStatus = UserAccountStatus.valueOf(status);
            if(accountStatus == UserAccountStatus.ACTIVE || accountStatus == UserAccountStatus.DEACTIVATED)
                user.setAccountStatus(accountStatus);
            else throw new IllegalArgumentException("Invalid value.");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("Invalid input. Allowed values are ACTIVE and DEACTIVATED.");
        }
        user.setAccountUpdatedAt(LocalDateTime.now());
        return status.equalsIgnoreCase("ACTIVE") ?
                "Account activated successfully" : "Account deactivated successfully";
    }
//    verify the user for changing the password
    @Override
    public String verifyUserForPasswordChange(String email, String oldPassword) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Something went wrong."));
        if(isAccountDeactivated(user)) throw new AccessDeniedException("Account is DEACTIVATED.");
        if(isAccountSuspended(user)) throw new AccessDeniedException("Account is SUSPENDED.");
        if (!passwordEncoder.matches(oldPassword, user.getAccountPassword()))
            throw new AccessDeniedException("Invalid old password.");
        String otp = generateOtp();
        user.setOtpColumn(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userEntityRepository.save(user);
        sendOtpMail(user.getEmail(), otp);
        return "OTP sent to registered email";
    }

    //    changing password
    @Override
    public String changePassword(String email, String otp ,String newPassword) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if(isAccountDeactivated(user)) throw new AccessDeniedException("Account is DEACTIVATED.");
        if(isAccountSuspended(user)) throw new AccessDeniedException("Account is SUSPENDED.");
        if (user.getOtpColumn() == null || user.getOtpExpiry() == null)
            throw new AccessDeniedException("OTP not requested.");
        if (user.getOtpExpiry().isBefore(LocalDateTime.now()))
            throw new AccessDeniedException("OTP expired.");
        if (!otp.equals(user.getOtpColumn()))
            throw new AccessDeniedException("Invalid OTP.");
        if(passwordEncoder.matches(newPassword, user.getAccountPassword()))
            throw new RuntimeException("New password and old password must be different." +
                    " Please try another password.");
        user.setAccountPassword(passwordEncoder.encode(newPassword));
        user.setOtpColumn(null);
        user.setOtpExpiry(null);
        user.setAccountUpdatedAt(LocalDateTime.now());
        this.userEntityRepository.save(user);
       return "Password changes successfully.";
    }
//    get my account status
    @Override
    public String getMyAccountStatus(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        if(isAccountDeactivated(user)) throw new AccessDeniedException("Account is DEACTIVATED.");
        if(isAccountSuspended(user)) throw new AccessDeniedException("Account is SUSPENDED.");
        return user.getAccountStatus().toString();
    }
//    get use's roles
    @Override
    public Set<String> getMyRoles(String email) {
        UserEntity user = this.userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Something went wrong. User not found."));
        if(isAccountDeactivated(user)) throw new AccessDeniedException("Account is DEACTIVATED.");
        if(isAccountSuspended(user)) throw new AccessDeniedException("Account is SUSPENDED.");
        return user.getUserRoles()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
    //    helper methods
//    verify status of user
    private boolean isAccountDeactivated(UserEntity user) {
        return user.getAccountStatus().equals(UserAccountStatus.DEACTIVATED);
    }
    private boolean isAccountSuspended(UserEntity user) {
        return user.getAccountStatus().equals(UserAccountStatus.SUSPENDED);
    }
//    map to user registration response
    private UserRegistrationResponse mapToUserRegistrationResponse(UserEntity userEntity) {
        return UserRegistrationResponse.builder()
                .userId(userEntity.getUserId())
                .email(userEntity.getEmail())
                .message("Account registered successfully.")
                .build();
    }
//    sent mail helper method
    private void sendOtpMail(String email, String otp) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("Password Change Verification");
        mail.setText(
                "Your 6-digit verification code is: " + otp +
                        "\nThis code is valid for 5 minutes."
        );
        javaMailSender.send(mail);
    }
//    generating otp
    private String generateOtp() {
        return String.valueOf(100000 + new SecureRandom().nextInt(900000));
    }
}
