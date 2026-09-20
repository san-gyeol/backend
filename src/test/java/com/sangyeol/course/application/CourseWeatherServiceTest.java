package com.sangyeol.course.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sangyeol.common.domain.Coordinate;
import com.sangyeol.course.domain.Course;
import com.sangyeol.course.domain.CourseRepository;
import com.sangyeol.course.domain.Difficulty;
import com.sangyeol.course.domain.Waypoint;
import com.sangyeol.weather.application.WeatherService;
import com.sangyeol.weather.application.WeatherUnavailableException;
import com.sangyeol.weather.domain.WeatherSnapshot;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CourseWeatherServiceTest {

    private static final Coordinate JUNGMEORIJAE = new Coordinate(35.119077, 126.984481);
    private static final Course DANGSANNAMU = new Course(1, "당산나무 코스", 3.2, 104, Difficulty.NORMAL, List.of(
            new Waypoint("증심사주차장", new Coordinate(35.133447, 126.957845), 115),
            new Waypoint("중머리재", JUNGMEORIJAE, 590)));
    private static final WeatherSnapshot SNAPSHOT = new WeatherSnapshot(18.99, 18.88, 0, null, 74,
            LocalTime.of(6, 18), LocalTime.of(18, 33), "OpenWeather", LocalDateTime.of(2026, 9, 20, 19, 9));

    // 가짜 저장소: 당산나무 코스 하나만 안다
    private final CourseRepository repository = id -> Optional.of(DANGSANNAMU).filter(course -> course.hasId(id));

    @Test
    void 코스의_최고_고도_거점_좌표로_날씨를_조회한다() {
        // given — 가짜 게이트웨이: 요청받은 좌표를 기록
        List<Coordinate> requestedPoints = new ArrayList<>();
        WeatherService weatherService = new WeatherService(point -> {
            requestedPoints.add(point);
            return SNAPSHOT;
        });
        CourseWeatherService service = new CourseWeatherService(repository, weatherService);

        // when
        WeatherSnapshot result = service.findWeather(1);

        // then
        assertThat(result).isEqualTo(SNAPSHOT);
        assertThat(requestedPoints).containsExactly(JUNGMEORIJAE);
    }

    @Test
    void 없는_코스면_CourseNotFoundException이_발생한다() {
        WeatherService weatherService = new WeatherService(point -> SNAPSHOT);
        CourseWeatherService service = new CourseWeatherService(repository, weatherService);

        assertThatThrownBy(() -> service.findWeather(99))
                .isInstanceOf(CourseNotFoundException.class)
                .hasMessage("코스를 찾을 수 없습니다: id=99");
    }

    @Test
    void 날씨_조회가_실패하면_WeatherUnavailableException이_발생한다() {
        WeatherService weatherService = new WeatherService(point -> {
            throw new IllegalStateException("OpenWeather 호출 실패: HTTP 401");
        });
        CourseWeatherService service = new CourseWeatherService(repository, weatherService);

        assertThatThrownBy(() -> service.findWeather(1))
                .isInstanceOf(WeatherUnavailableException.class);
    }
}
