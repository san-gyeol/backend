package com.sangyeol.weather.infrastructure.openweather;

import com.sangyeol.weather.domain.WeatherSnapshot;
import com.sangyeol.weather.infrastructure.openweather.OpenWeatherCurrentResponse.Main;
import com.sangyeol.weather.infrastructure.openweather.OpenWeatherCurrentResponse.Rain;
import com.sangyeol.weather.infrastructure.openweather.OpenWeatherCurrentResponse.Sys;
import com.sangyeol.weather.infrastructure.openweather.OpenWeatherForecastResponse.Entry;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

final class OpenWeatherMapper {

    private static final String SOURCE = "OpenWeather";
    private static final Duration FORECAST_INTERVAL = Duration.ofHours(3);
    private static final int PERCENT = 100;

    private OpenWeatherMapper() {
    }

    static WeatherSnapshot toSnapshot(OpenWeatherCurrentResponse current, OpenWeatherForecastResponse forecast) {
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(require(current.timezone(), "timezone"));
        LocalDateTime updatedAt = toLocalDateTime(require(current.dt(), "dt"), offset);
        Main main = require(current.main(), "main");
        return new WeatherSnapshot(
                require(main.temp(), "main.temp"), require(main.feelsLike(), "main.feels_like"),
                toProbability(forecast, updatedAt, offset), toPrecipitationAmount(current.rain()),
                require(main.humidity(), "main.humidity"),
                toSunrise(current, offset), toSunset(current, offset),
                SOURCE, updatedAt);
    }

    /**
     * 대표 강수확률: 실황 기준 시각과 같은 날짜에 속하면서 아직 끝나지 않은 3시간 구간들의 최댓값.
     * 자정 직전처럼 남은 구간이 없으면 가장 가까운 구간의 값을 쓴다.
     */
    private static int toProbability(OpenWeatherForecastResponse forecast, LocalDateTime updatedAt,
                                     ZoneOffset offset) {
        List<Entry> entries = requireEntries(forecast);
        double probability = entries.stream()
                .filter(entry -> isRemainingToday(entry, updatedAt, offset))
                .mapToDouble(entry -> require(entry.pop(), "list.pop"))
                .max()
                .orElseGet(() -> require(entries.getFirst().pop(), "list.pop"));
        return (int) Math.round(probability * PERCENT);
    }

    private static boolean isRemainingToday(Entry entry, LocalDateTime updatedAt, ZoneOffset offset) {
        LocalDateTime start = toLocalDateTime(require(entry.dt(), "list.dt"), offset);
        return start.toLocalDate().equals(updatedAt.toLocalDate())
                && start.plus(FORECAST_INTERVAL).isAfter(updatedAt);
    }

    private static List<Entry> requireEntries(OpenWeatherForecastResponse forecast) {
        List<Entry> entries = forecast.list();
        if (entries == null || entries.isEmpty()) {
            throw new IllegalArgumentException("OpenWeather 예보 목록이 비어 있습니다");
        }
        return entries;
    }

    private static Double toPrecipitationAmount(Rain rain) {
        if (rain == null) {
            return null;
        }
        return rain.oneHour();
    }

    private static LocalTime toSunrise(OpenWeatherCurrentResponse current, ZoneOffset offset) {
        Sys sys = require(current.sys(), "sys");
        return toLocalDateTime(require(sys.sunrise(), "sys.sunrise"), offset).toLocalTime();
    }

    private static LocalTime toSunset(OpenWeatherCurrentResponse current, ZoneOffset offset) {
        Sys sys = require(current.sys(), "sys");
        return toLocalDateTime(require(sys.sunset(), "sys.sunset"), offset).toLocalTime();
    }

    private static LocalDateTime toLocalDateTime(long epochSecond, ZoneOffset offset) {
        return LocalDateTime.ofEpochSecond(epochSecond, 0, offset);
    }

    private static <T> T require(T value, String field) {
        if (value == null) {
            throw new IllegalArgumentException("OpenWeather 응답에 " + field + " 값이 없습니다");
        }
        return value;
    }
}
