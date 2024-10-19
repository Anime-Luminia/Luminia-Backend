package com.anime.luminia.domain.user;

import com.anime.luminia.domain.user.dto.RegisterRequest;
import com.anime.luminia.global.error.exception.BusinessException;
import com.anime.luminia.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final LuminiaUserRepositroy userRepository;
    private final PasswordEncoder passwordEncoder;

    public LuminiaUser fetchUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public LuminiaUser registerUser(RegisterRequest request){

        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new BusinessException(ErrorCode.ALREADY_EMAIL_EXIST);
        }

        LuminiaUser user = LuminiaUser
                .builder()
                .role(Role.ROLE_USER)
                .username(request.nickName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LuminiaUser user = userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return user;
    }
}
