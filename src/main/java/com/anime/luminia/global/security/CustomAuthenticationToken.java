package com.anime.luminia.global.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final Object credentials;

    public CustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;

        if (!authorities.isEmpty()) {
            super.setAuthenticated(true);
        }
    }

    @Override
    public Object getCredentials() {
        return this.credentials != null ? this.credentials : "";
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
