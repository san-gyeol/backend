package com.sangyeol.weather.infrastructure.openweather;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sangyeol.weather.domain.WeatherSnapshot;
import com.sangyeol.weather.infrastructure.openweather.OpenWeatherCurrentResponse.Main;
import com.sangyeol.weather.infrastructure.openweather.OpenWeatherCurrentResponse.Sys;
import com.sangyeol.weather.infrastructure.openweather.OpenWeatherForecastResponse.Entry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

class OpenWeatherMapperTest {

    private static final JsonMapper JSON = JsonMapper.builder().build();
    private static final ZoneOffset KST = ZoneOffset.ofHours(9);
    private static final int KST_SECONDS = 32400;

    private String loadFixture(String fileName) throws IOException {
        return Files.readString(Path.of("src/test/resources/" + fileName));
    }

    private OpenWeatherCurrentResponse toCurrent(String json) {
        return JSON.readValue(json, OpenWeatherCurrentResponse.class);
    }

    private OpenWeatherForecastResponse toForecast(String json) {
        return JSON.readValue(json, OpenWeatherForecastResponse.class);
    }

    private OpenWeatherCurrentResponse currentAt(LocalDateTime localTime) {
        long epochSecond = localTime.toEpochSecond(KST);
        return new OpenWeatherCurrentResponse(new Main(20.0, 20.0, 50), null, new Sys(epochSecond, epochSecond),
                epochSecond, KST_SECONDS);
    }

    private Entry entryAt(LocalDateTime localTime, double pop) {
        return new Entry(localTime.toEpochSecond(KST), pop);
    }

    @Test
    void 실제_응답을_날씨_스냅샷으로_변환한다() throws IOException {
        // given — 2026-08-13 15:50 KST에 무등산 좌표로 저장한 실제 응답 (비 없음)
        OpenWeatherCurrentResponse current = toCurrent(loadFixture("owm-current.json"));
        OpenWeatherForecastResponse forecast = toForecast(loadFixture("owm-forecast.json"));

        // when
        WeatherSnapshot snapshot = OpenWeatherMapper.toSnapshot(current, forecast);

        // then — 저장 시점의 실측값과 대조 (시각은 timezone 오프셋을 적용한 KST)
        assertThat(snapshot.temperature()).isEqualTo(27.78);
        assertThat(snapshot.feelsLike()).isEqualTo(28.05);
        assertThat(snapshot.humidity()).isEqualTo(48);
        assertThat(snapshot.precipitationAmount()).isNull();
        assertThat(snapshot.precipitationProbability()).isEqualTo(20);
        assertThat(snapshot.sunrise()).isEqualTo(LocalTime.of(5, 49, 54));
        assertThat(snapshot.sunset()).isEqualTo(LocalTime.of(19, 24, 8));
        assertThat(snapshot.updatedAt()).isEqualTo(LocalDateTime.of(2026, 8, 13, 15, 50, 18));
        assertThat(snapshot.source()).isEqualTo("OpenWeather");
    }

    @Test
    void 비가_오면_최근_1시간_강수량을_담는다() throws IOException {
        // given — 실제 응답에 rain 객체가 있는 상황을 재현
        String json = loadFixture("owm-current.json")
                .replace("\"visibility\":10000", "\"visibility\":10000,\"rain\":{\"1h\":0.5}");
        OpenWeatherForecastResponse forecast = toForecast(loadFixture("owm-forecast.json"));

        // when
        WeatherSnapshot snapshot = OpenWeatherMapper.toSnapshot(toCurrent(json), forecast);

        // then
        assertThat(snapshot.precipitationAmount()).isEqualTo(0.5);
    }

    @Test
    void 대표_강수확률은_같은_날_남은_구간의_최댓값이다() {
        // given — 15:50 기준. 12:00 구간은 이미 끝났고, 15:00 구간은 진행 중, 다음 날 구간은 제외
        OpenWeatherCurrentResponse current = currentAt(LocalDateTime.of(2026, 8, 13, 15, 50));
        OpenWeatherForecastResponse forecast = new OpenWeatherForecastResponse(List.of(
                entryAt(LocalDateTime.of(2026, 8, 13, 12, 0), 1.0),
                entryAt(LocalDateTime.of(2026, 8, 13, 15, 0), 0.4),
                entryAt(LocalDateTime.of(2026, 8, 13, 18, 0), 0.88),
                entryAt(LocalDateTime.of(2026, 8, 14, 0, 0), 1.0)
        ));

        // when
        WeatherSnapshot snapshot = OpenWeatherMapper.toSnapshot(current, forecast);

        // then — 0.88을 백분율 정수로 반올림
        assertThat(snapshot.precipitationProbability()).isEqualTo(88);
    }

    @Test
    void 같은_날_남은_구간이_없으면_가장_가까운_구간의_값을_쓴다() {
        // given — 23:30 기준. 예보 목록이 다음 날 00:00부터 시작
        OpenWeatherCurrentResponse current = currentAt(LocalDateTime.of(2026, 8, 13, 23, 30));
        OpenWeatherForecastResponse forecast = new OpenWeatherForecastResponse(List.of(
                entryAt(LocalDateTime.of(2026, 8, 14, 0, 0), 0.6),
                entryAt(LocalDateTime.of(2026, 8, 14, 3, 0), 1.0)
        ));

        // when
        WeatherSnapshot snapshot = OpenWeatherMapper.toSnapshot(current, forecast);

        // then
        assertThat(snapshot.precipitationProbability()).isEqualTo(60);
    }

    @Test
    void 필수_값이_빠지면_예외가_발생한다() throws IOException {
        // given — 응답 구조가 바뀌어 기온이 사라진 상황을 재현
        String json = loadFixture("owm-current.json").replace("\"temp\":27.78,", "");
        OpenWeatherForecastResponse forecast = toForecast(loadFixture("owm-forecast.json"));

        // when & then — 조용히 0도를 주지 않고 시끄럽게 죽는다
        assertThatThrownBy(() -> OpenWeatherMapper.toSnapshot(toCurrent(json), forecast))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("OpenWeather 응답에 main.temp 값이 없습니다");
    }

    @Test
    void 예보_목록이_비어_있으면_예외가_발생한다() throws IOException {
        // given
        OpenWeatherCurrentResponse current = toCurrent(loadFixture("owm-current.json"));
        OpenWeatherForecastResponse forecast = new OpenWeatherForecastResponse(List.of());

        // when & then
        assertThatThrownBy(() -> OpenWeatherMapper.toSnapshot(current, forecast))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("OpenWeather 예보 목록이 비어 있습니다");
    }
}
