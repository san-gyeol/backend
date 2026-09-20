package com.sangyeol.weather.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sangyeol.common.domain.Coordinate;
import com.sangyeol.weather.domain.WeatherSnapshot;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class WeatherServiceTest {

    private static final WeatherSnapshot SNAPSHOT = new WeatherSnapshot(27.78, 28.05, 20, null, 48,
            LocalTime.of(5, 49), LocalTime.of(19, 24), "OpenWeather", LocalDateTime.of(2026, 8, 13, 15, 50));

    @Test
    void 게이트웨이가_준_스냅샷을_그대로_반환한다() {
        // given — 가짜 어댑터: 고정 스냅샷 반환 (HTTP 없음)
        WeatherService service = new WeatherService(point -> SNAPSHOT);

        // when
        WeatherSnapshot result = service.findHomeWeather();

        // then
        assertThat(result).isEqualTo(SNAPSHOT);
    }

    @Test
    void 홈_날씨는_무등산_주소지_좌표로_조회한다() {
        // given — 가짜 어댑터: 요청받은 지점을 기록
        List<Coordinate> requestedPoints = new ArrayList<>();
        WeatherService service = new WeatherService(point -> {
            requestedPoints.add(point);
            return SNAPSHOT;
        });

        // when
        service.findHomeWeather();

        // then
        assertThat(requestedPoints).containsExactly(new Coordinate(35.124386, 127.009131));
    }

    @Test
    void 게이트웨이가_실패하면_WeatherUnavailableException으로_변환한다() {
        // given — 가짜 어댑터: 항상 실패
        WeatherService service = new WeatherService(point -> {
            throw new IllegalStateException("OpenWeather 호출 실패: HTTP 401");
        });

        // when & then
        assertThatThrownBy(service::findHomeWeather)
                .isInstanceOf(WeatherUnavailableException.class);
    }
}
