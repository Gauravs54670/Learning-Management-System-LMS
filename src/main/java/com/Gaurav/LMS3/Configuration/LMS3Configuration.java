package com.Gaurav.LMS3.Configuration;

import com.Gaurav.LMS3.JWT.JwtAuthenticationFilter;
import com.cloudinary.Cloudinary;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class LMS3Configuration {

    @Autowired
    private RoleBasedHandler roleBasedHandler;
    @Value("${cloudinary.cloud_name}")
    private String cloudName;
    @Value("${cloudinary.api_key}")
    private String apiKey;
    @Value("${cloudinary.api_secret}")
    private String apiSecretKey;
    @Autowired
    private JwtAuthenticationFilter authenticationFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(
                request -> request
                        .requestMatchers("/public/**","/auth/**").permitAll()
                        .requestMatchers("/learner/**","/enrollment/**").hasRole("LEARNER")
                        .requestMatchers("/instructor/**","/course/**").hasRole("INSTRUCTOR")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
        );
        httpSecurity.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
//        httpSecurity.httpBasic(Customizer.withDefaults());

        httpSecurity.exceptionHandling(
                exception -> exception
                        .accessDeniedHandler(roleBasedHandler)
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("""
                                {
                                  "status": 401,
                                  "error": "UNAUTHORIZED",
                                  "message": "Please login first"
                                }
                                """);
                        })
        );
        httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    Cloudinary getCloudinaryObject() {
        return new Cloudinary(
                Map.of(
                        "cloud_name", cloudName,
                        "api_key", apiKey,
                        "api_secret", apiSecretKey,
                        "secure", true
                )
        );
    }
}
