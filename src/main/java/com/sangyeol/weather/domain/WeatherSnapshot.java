package com.sangyeol.weather.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record WeatherSnapshot(
        double temperature,
        double feelsLike,
        int precipitationProbability,
        Double precipitationAmount,
        int humidity,
        LocalTime sunrise,
        LocalTime sunset,
        String source,
        LocalDateTime updatedAt
) {
}
