package com.example.samplelogin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import com.example.samplelogin.dto.UserDetails;
import org.springframework.util.ObjectUtils;


@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetail = new UserDetails(authentication);
        String principalKey = ObjectUtils.nullSafeToString(userDetail.getName()).concat("-");

        CustomAuthentication token = new CustomAuthentication(principalKey,
                ObjectUtils.nullSafeToString(authentication.getCredentials()).replaceAll("&amp;", "&"),
                userDetail.getSecondaryAuthorities()
        );

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
