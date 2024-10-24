package com.anime.luminia.global.dto;

import com.anime.luminia.global.error.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"success", "code", "message", "response"})
public class ApiResult<T> {

    private final boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T response;

    public static <T> ApiResult<T> success(String message, T response) {
        return ApiResult.<T>builder()
                .success(true)
                .message(message)
                .response(response)
                .build();
    }

    public static <T> ApiResult<T> success(String message) {
        return success(message, null);
    }

    public static <T> ApiResult<T> failure(ErrorCode error) {
        return ApiResult.<T>builder()
                .success(false)
                .code(error.getCode())
                .build();
    }
}