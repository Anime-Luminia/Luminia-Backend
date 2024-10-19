package com.anime.luminia.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest (@Email @NotNull String email, @NotNull String password, @NotNull String nickName){

}
