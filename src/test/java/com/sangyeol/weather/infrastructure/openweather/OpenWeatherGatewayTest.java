package com.sangyeol.weather.infrastructure.openweather;

import static org.assertj.core.api.Assertions.assertThat;

import com.sangyeol.common.domain.Coordinate;
import com.sangyeol.weather.domain.WeatherSnapshot;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

@Disabled("실제 OpenWeather 호출 — 수동 검증용. 환경변수 OPENWEATHER_API_KEY 필요")
class OpenWeatherGatewayTest {

    @Test
    void 실제_OpenWeather에서_무등산_날씨를_가져온다() {
        // given — Spring 컨텍스트 없이 직접 조립 (실제 네트워크 사용)
        RestClient client = RestClient.builder()
                .baseUrl("https://api.openweathermap.org")
                .build();
        OpenWeatherGateway gateway = new OpenWeatherGateway(client, System.getenv("OPENWEATHER_API_KEY"));

        // when
        WeatherSnapshot snapshot = gateway.fetch(new Coordinate(35.124386, 127.009131));

        // then — 값은 시점마다 다르므로 범위와 출처만 검증
        assertThat(snapshot.source()).isEqualTo("OpenWeather");
        assertThat(snapshot.precipitationProbability()).isBetween(0, 100);
        assertThat(snapshot.humidity()).isBetween(0, 100);
        assertThat(snapshot.sunrise()).isBefore(snapshot.sunset());
        assertThat(snapshot.updatedAt()).isNotNull();
    }
}
