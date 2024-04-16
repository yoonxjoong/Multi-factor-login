package com.example.samplelogin.dto;

import com.example.samplelogin.Const;
import com.example.samplelogin.config.CustomAuthenticationType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.security.access.vote.AuthenticatedVoter.IS_AUTHENTICATED_FULLY;

public class UserDetails implements Principal, org.springframework.security.core.userdetails.UserDetails {
    private String loginId;

    private String password;


    public UserDetails(Authentication authentication){
        super();
        this.loginId = authentication.getName();
    }

    @Override
    public String getName() {
        return this.loginId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(IS_AUTHENTICATED_FULLY));
        return authorities;
    }

    public Collection<? extends GrantedAuthority> getSecondaryAuthorities() {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(Const.IS_AUTHENTICATED_HALF));
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
