package com.example.samplelogin.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthentication extends UsernamePasswordAuthenticationToken {
    // otp 사용 가능 여부
    private Boolean enableOtpAuthenticated;

    private CustomAuthenticationType customAuthenticationType;

    public CustomAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.customAuthenticationType = CustomAuthenticationType.NON_AUTHENTICATED;
        this.enableOtpAuthenticated = true;
    }

    public void onOtpAuthenticator() {
        this.enableOtpAuthenticated = true;
    }

    public void offOtpAuthenticator() {
        this.enableOtpAuthenticated = false;
    }

    /**
     * 1차 인증 성공 후 권한
     */
    public void setPrimaryAuthenticator(){
        this.customAuthenticationType = CustomAuthenticationType.AUTHENTICATED_PRIMARY;
    }

    /**
     * 1차 인증 진행중인 권한
     */
    public void setInProgressAuthenticator(){
        this.customAuthenticationType = CustomAuthenticationType.AUTHENTICATION_IN_PROGRESS;
    }


    /**
     * 2차 인증 성공 후 권한
     */
    public void setSecondaryAuthenticator(){
        this.customAuthenticationType = CustomAuthenticationType.NON_AUTHENTICATED;
    }
}
