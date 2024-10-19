package com.anime.luminia.global.security;

import com.anime.luminia.domain.user.LuminiaUser;
import com.anime.luminia.domain.user.UserService;
import com.anime.luminia.global.error.exception.BusinessException;
import com.anime.luminia.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("HI3");
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        log.info(email);

        try{
            LuminiaUser user = userService.fetchUserByEmail(email);

            if(!passwordEncoder.matches(password, user.getPassword())){
                throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
            }

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));

            CustomAuthenticationToken token = new CustomAuthenticationToken(
                    user,
                    null,
                    authorities
            );

            log.info(token.toString());

            return token;
        }
        catch (BusinessException ex){
            throw new BadCredentialsException("User not found or invalid crentials", ex);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        log.info("H2");
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
