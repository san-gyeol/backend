package com.sangyeol.weather.domain;

import java.time.LocalDateTime;

public record WeatherSnapshot(
        double temperature,
        double feelsLike,
        int precipitationProbability,
        Double precipitationAmount,
        int humidity,
        LocalDateTime sunrise,
        LocalDateTime sunset,
        String source,
        LocalDateTime updatedAt
) {
}
