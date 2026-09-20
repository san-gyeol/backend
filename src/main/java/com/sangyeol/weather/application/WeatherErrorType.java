package com.sangyeol.weather.application;

import com.sangyeol.common.exception.ErrorType;
import org.springframework.http.HttpStatus;

public enum WeatherErrorType implements ErrorType {
    WEATHER_UNAVAILABLE(HttpStatus.BAD_GATEWAY, "WEATHER502_001", "날씨 정보를 불러오지 못했어요. 잠시 후 다시 시도해 주세요.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    WeatherErrorType(HttpStatus httpStatus, String errorCode, String errorMessage) {
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
