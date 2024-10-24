package com.anime.luminia.domain.auth.jwt;

import com.anime.luminia.global.error.exception.BusinessException;
import com.anime.luminia.global.error.exception.ErrorCode;

public class JwtTokenInvalidException extends BusinessException {

    public static final JwtTokenInvalidException INSTANCE = new JwtTokenInvalidException();

    private JwtTokenInvalidException() {
        super(ErrorCode.INVALID_JWT_TOKEN);
    }

}

