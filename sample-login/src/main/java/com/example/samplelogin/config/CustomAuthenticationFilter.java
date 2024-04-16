package com.example.samplelogin.config;

import com.example.samplelogin.Const;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

@Component
@Slf4j
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String domain = request.getRequestURI();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 첫 번째 단계의 인증 성공 여부 확인
        if (domain.equals("/") && isAuthenticatedHalf(authentication)) {
            StringBuffer redirect = new StringBuffer();
            redirect.append("http://")
                    .append(request.getServerName())
                    .append(':')
                    .append(request.getServerPort());
            redirect.append("/login");

            if (log.isDebugEnabled()) {
                log.debug(">>>second Redirecting to {} : x-requested-with({})", redirect.toString(), request.getHeader("x-requested-with"));
            }
            response.sendRedirect(redirect.toString());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isAuthenticatedHalf(Authentication authentication) {
        if (Objects.isNull(authentication)) {
            return false;
        }

        // 권한 목록을 가져옴
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // 권한 목록을 반복하여 IS_AUTHENTICATED_HALF와 일치하는지 확인
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(Const.IS_AUTHENTICATED_HALF)) {
                return true;
            }
        }
        return false;
    }
}
