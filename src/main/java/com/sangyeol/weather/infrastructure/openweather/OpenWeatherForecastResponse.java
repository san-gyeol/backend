package com.sangyeol.weather.infrastructure.openweather;

import java.util.List;

/**
 * OpenWeather 5 Day / 3 Hour Forecast API(/data/2.5/forecast) 응답의 외부 DTO.
 * list의 각 항목은 3시간 구간 하나이며 dt는 구간 시작 시각(unix, UTC), pop은 강수확률(0~1)이다.
 */
public record OpenWeatherForecastResponse(
        List<Entry> list
) {
    public record Entry(
            Long dt,
            Double pop
    ) {
    }
}
