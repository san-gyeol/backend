package com.sangyeol.common.exception;

import com.sangyeol.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.warn("[비즈니스 예외] {}", e.getErrorType().getErrorCode(), e);
        return toResponse(e.getErrorType());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException e) {
        return toResponse(CommonErrorType.RESOURCE_NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e, HttpServletRequest request) {
        log.error("[예기치 못한 오류] {} {}", request.getMethod(), request.getRequestURI(), e);
        return toResponse(CommonErrorType.UNEXPECTED_EXCEPTION);
    }

    private ResponseEntity<ErrorResponse> toResponse(ErrorType errorType) {
        return ResponseEntity.status(errorType.getHttpStatus())
                .body(new ErrorResponse(errorType.getErrorMessage(), errorType.getErrorCode()));
    }
}
