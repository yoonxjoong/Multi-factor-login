package com.example.samplelogin.config;

import com.example.samplelogin.Const;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

public class CustomAccessDecisionVoter implements AccessDecisionVoter<Object> {
    public static final String IS_AUTHENTICATED_FULLY = "IS_AUTHENTICATED_FULLY";
    public static final String IS_AUTHENTICATED_REMEMBERED = "IS_AUTHENTICATED_REMEMBERED";
    public static final String IS_AUTHENTICATED_ANONYMOUSLY = "IS_AUTHENTICATED_ANONYMOUSLY";
    public static final String IS_AUTHENTICATED_HALF = "IS_AUTHENTICATED_HALF"; // 추가된 사용자 정의 권한

    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    public void setAuthenticationTrustResolver(
            AuthenticationTrustResolver authenticationTrustResolver) {
        Assert.notNull(authenticationTrustResolver,
                "AuthenticationTrustResolver cannot be set to null");
        this.authenticationTrustResolver = authenticationTrustResolver;
    }

    public boolean supports(ConfigAttribute attribute) {
        if ((attribute.getAttribute() != null)
                && (IS_AUTHENTICATED_FULLY.equals(attribute.getAttribute())
                || IS_AUTHENTICATED_REMEMBERED.equals(attribute.getAttribute())
                || IS_AUTHENTICATED_ANONYMOUSLY.equals(attribute.getAttribute())
                || IS_AUTHENTICATED_HALF.equals(attribute.getAttribute()))) { // 추가된 사용자 정의 권한 확인
            return true;
        }
        else {
            return false;
        }
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }

    public int vote(Authentication authentication, Object object,
                    Collection<ConfigAttribute> attributes) {
        int result = ACCESS_ABSTAIN;

        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;

                if (IS_AUTHENTICATED_FULLY.equals(attribute.getAttribute())) {
                    if (isFullyAuthenticated(authentication)) {
                        return ACCESS_GRANTED;
                    }
                }

                if (IS_AUTHENTICATED_REMEMBERED.equals(attribute.getAttribute())) {
                    if (authenticationTrustResolver.isRememberMe(authentication)
                            || this.isFullyAuthenticated(authentication)) {
                        return ACCESS_GRANTED;
                    }
                }

                if (IS_AUTHENTICATED_ANONYMOUSLY.equals(attribute.getAttribute())) {
                    if (authenticationTrustResolver.isAnonymous(authentication)
                            || this.isFullyAuthenticated(authentication)
                            || authenticationTrustResolver.isRememberMe(authentication)) {
                        return ACCESS_GRANTED;
                    }
                }

                if (IS_AUTHENTICATED_HALF.equals(attribute.getAttribute())) { // 추가된 사용자 정의 권한 처리
                    if (this.isHalfAuthenticated(authentication)
                            || this.isAuthenticatedHalf(authentication)) {
                        return ACCESS_GRANTED;
                    }
                }
            }
        }

        return result;
    }

    private boolean isAuthenticatedHalf(Authentication authentication) {
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

    private boolean isFullyAuthenticated(Authentication authentication) {
        return (!authenticationTrustResolver.isAnonymous(authentication)
                && !authenticationTrustResolver.isRememberMe(authentication)
                && !isAuthenticatedHalf(authentication));
    }

    private boolean isHalfAuthenticated(Authentication authentication) {
        return (!authenticationTrustResolver.isAnonymous(authentication)
                && !authenticationTrustResolver.isRememberMe(authentication));
    }
}
