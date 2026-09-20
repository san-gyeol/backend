package com.sangyeol.course.application;

import com.sangyeol.common.exception.ErrorType;
import org.springframework.http.HttpStatus;

public enum CourseErrorType implements ErrorType {
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "COURSE404_001", "해당 코스를 찾을 수 없어요.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    CourseErrorType(HttpStatus httpStatus, String errorCode, String errorMessage) {
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
