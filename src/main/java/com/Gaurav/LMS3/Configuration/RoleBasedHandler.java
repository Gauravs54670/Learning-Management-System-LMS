package com.Gaurav.LMS3.Configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleBasedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        String requestPath = request.getRequestURI();
        String message = "Access Denied.";
        if (requestPath.startsWith("/instructor") || requestPath.startsWith("/course")) {
            message = "Only INSTRUCTOR can access this resource";
        } else if (requestPath.startsWith("/admin")) {
            message = "Only ADMIN can access this resource";
        } else if (requestPath.startsWith("/learner")) {
            message = "Only LEARNER can access this resource";
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(
                """
                {
                  "status": 403,
                  "error": "FORBIDDEN",
                  "message": "%s"
                }
                """.formatted(message)
        );
    }
}
