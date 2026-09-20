package com.sangyeol.weather.ui.dto;

import com.sangyeol.weather.domain.WeatherSnapshot;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record WeatherResponse(
        double temperature,
        double feelsLike,
        int precipitationProbability,
        Double precipitationAmount,
        int humidity,
        String sunrise,
        String sunset,
        String source,
        LocalDateTime updatedAt
) {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public static WeatherResponse from(WeatherSnapshot snapshot) {
        return new WeatherResponse(
                snapshot.temperature(),
                snapshot.feelsLike(),
                snapshot.precipitationProbability(),
                snapshot.precipitationAmount(),
                snapshot.humidity(),
                TIME_FORMAT.format(snapshot.sunrise()),
                TIME_FORMAT.format(snapshot.sunset()),
                snapshot.source(),
                snapshot.updatedAt()
        );
    }
}
