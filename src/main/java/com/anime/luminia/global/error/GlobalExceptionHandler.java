package com.anime.luminia.global.error;

import com.anime.luminia.global.dto.ApiResult;
import com.anime.luminia.global.error.exception.BusinessException;
import com.anime.luminia.global.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생합니다.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResult<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResult.failure(errorCode));
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiResult<?>> handleAccessDeniedException(final AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        final ErrorCode errorCode = ErrorCode.HANDLE_ACCESS_DENIED;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResult.failure(errorCode));
    }

    /**
     * {@link jakarta.validation.Valid} annotaion에 의해 validation이 실패했을경우
     * Controller 단에서 발생하여 Error가 넘어옵니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<?>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorCode errorCode = ErrorCode.INVALID_JWT_TOKEN;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResult.failure(errorCode));
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResult<?>> handleBusinessException(final BusinessException e) {
        log.error("handleBusinessException", e);
        final ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResult.failure(errorCode));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ApiResult<?>> handle404(NoHandlerFoundException e){
        return ResponseEntity
                .status(e.getStatusCode().value())
                .body(ApiResult.builder()
                        .success(false)
                        .message(e.getMessage())
                        .build()
                );
    }

    /**
     * 예상치 못한 오류들은 다 이 곳에서 처리됩니다.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResult<?>> handleUnExpectedException(final Exception e) {
        log.error("handleUnExpectedException", e);
        final ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResult.failure(errorCode));
    }
}
