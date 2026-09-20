package com.sangyeol.weather.infrastructure.openweather;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OpenWeather Current Weather API(/data/2.5/weather) 응답의 외부 DTO.
 * 필드명은 외부 계약을 그대로 따르고, 모든 값은 래퍼 타입으로 부재를 허용한다(검증은 OpenWeatherMapper 책임).
 */
public record OpenWeatherCurrentResponse(
        Main main,
        Rain rain,
        Sys sys,
        Long dt,
        Integer timezone
) {
    public record Main(
            Double temp,
            @JsonProperty("feels_like") Double feelsLike,
            Integer humidity
    ) {
    }

    public record Rain(
            @JsonProperty("1h") Double oneHour
    ) {
    }

    public record Sys(
            Long sunrise,
            Long sunset
    ) {
    }
}
