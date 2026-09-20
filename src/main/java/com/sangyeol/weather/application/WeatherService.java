package com.sangyeol.weather.application;

import com.sangyeol.common.domain.Coordinate;
import com.sangyeol.weather.domain.WeatherGateway;
import com.sangyeol.weather.domain.WeatherSnapshot;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    // 홈 날씨 기준 지점: 무등산 주소지(북구 금곡동 산 1-1), 기획 확정 2026-08-09
    private static final Coordinate MUDEUNGSAN_HOME = new Coordinate(35.124386, 127.009131);

    private final WeatherGateway weatherGateway;

    public WeatherService(WeatherGateway weatherGateway) {
        this.weatherGateway = weatherGateway;
    }

    public WeatherSnapshot findHomeWeather() {
        return findWeather(MUDEUNGSAN_HOME);
    }

    public WeatherSnapshot findWeather(Coordinate point) {
        try {
            return weatherGateway.fetch(point);
        } catch (RuntimeException e) {
            throw new WeatherUnavailableException(e);
        }
    }
}
