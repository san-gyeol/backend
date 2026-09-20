package com.sangyeol.weather.infrastructure.openweather;

import com.sangyeol.common.domain.Coordinate;
import com.sangyeol.weather.domain.WeatherGateway;
import com.sangyeol.weather.domain.WeatherSnapshot;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class OpenWeatherGateway implements WeatherGateway {

    private static final String CURRENT_PATH = "/data/2.5/weather";
    private static final String FORECAST_PATH = "/data/2.5/forecast";
    private static final String METRIC_UNITS = "metric";

    private final RestClient openWeatherRestClient;
    private final String apiKey;

    public OpenWeatherGateway(@Qualifier("openWeatherRestClient") RestClient openWeatherRestClient,
                              @Value("${openweather.api-key}") String apiKey) {
        this.openWeatherRestClient = openWeatherRestClient;
        this.apiKey = apiKey;
    }

    @Override
    public WeatherSnapshot fetch(Coordinate point) {
        OpenWeatherCurrentResponse current = get(CURRENT_PATH, point, OpenWeatherCurrentResponse.class);
        OpenWeatherForecastResponse forecast = get(FORECAST_PATH, point, OpenWeatherForecastResponse.class);
        return OpenWeatherMapper.toSnapshot(current, forecast);
    }

    private <T> T get(String path, Coordinate point, Class<T> responseType) {
        try {
            return request(path, point)
                    .retrieve()
                    .body(responseType);
        } catch (RestClientException e) {
            // 원문 예외 메시지에 appid가 포함된 요청 URL이 들어 있어, 로그에 키가 남지 않도록 원인 예외를 떼어낸다
            throw new IllegalStateException(describe(e));
        }
    }

    private RestClient.RequestHeadersSpec<?> request(String path, Coordinate point) {
        return openWeatherRestClient.get()
                .uri(builder -> builder.path(path)
                        .queryParam("lat", point.latitude())
                        .queryParam("lon", point.longitude())
                        .queryParam("units", METRIC_UNITS)
                        .queryParam("appid", apiKey)
                        .build());
    }

    private static String describe(RestClientException e) {
        if (e instanceof RestClientResponseException responseException) {
            return "OpenWeather 호출 실패: HTTP " + responseException.getStatusCode().value();
        }
        return "OpenWeather 호출 실패: " + e.getClass().getSimpleName();
    }
}
