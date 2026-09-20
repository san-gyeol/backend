package com.sangyeol.weather.application;

import com.sangyeol.common.exception.BusinessException;

public class WeatherUnavailableException extends BusinessException {

    public WeatherUnavailableException(Throwable cause) {
        super(WeatherErrorType.WEATHER_UNAVAILABLE, cause);
    }
}
