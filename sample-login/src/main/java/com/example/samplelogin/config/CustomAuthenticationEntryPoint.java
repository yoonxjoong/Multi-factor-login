package com.example.samplelogin.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        StringBuilder redirect = new StringBuilder();
        redirect.append("http://")
                .append(request.getServerName())
                .append(':')
                .append(request.getServerPort());
        redirect.append("/login");

        if (log.isDebugEnabled()) {
            log.debug(">>>Redirecting to {} : x-requested-with({})", redirect.toString(), request.getHeader("x-requested-with"));
        }

        response.sendRedirect(redirect.toString());
    }
}
