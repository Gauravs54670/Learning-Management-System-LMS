package com.Gaurav.LMS3.Service.UserServicePackage;

import com.Gaurav.LMS3.Entity.UserEntityPackage.UserEntity;
import com.Gaurav.LMS3.Repository.UserEntityRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserEntityRepository userEntityRepository;
    public CustomUserDetailsService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = this.userEntityRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not registered. Please register yourself."));
        Set<GrantedAuthority> grantedAuthorities = user.getUserRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
        return User.builder()
                .authorities(grantedAuthorities)
                .username(user.getEmail())
                .password(user.getAccountPassword())
                .build();
    }
}
