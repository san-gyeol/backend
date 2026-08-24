package com.sangyeol.control.application;

import com.sangyeol.common.exception.ErrorType;
import org.springframework.http.HttpStatus;

public enum ControlErrorType implements ErrorType {
    CONTROL_UNAVAILABLE(HttpStatus.BAD_GATEWAY, "CONTROL502_001", "통제정보를 불러오지 못했어요. 잠시 후 다시 시도해 주세요.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    ControlErrorType(HttpStatus httpStatus, String errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
