package com.sangyeol.course.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.sangyeol.common.domain.Coordinate;
import com.sangyeol.course.domain.Waypoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class InMemoryCourseRepositoryTest {

    private final InMemoryCourseRepository repository = new InMemoryCourseRepository();

    @ParameterizedTest(name = "코스 {0}의 최고 고도 거점은 {1} {2}m")
    @CsvSource({
            "1, 중머리재, 590, 35.119077, 126.984481",
            "2, 입석대, 965, 35.117551, 127.002573",
            "3, 장불재, 894, 35.116271, 126.998612",
            "4, 장불재, 894, 35.116271, 126.998612"
    })
    void 코스별_최고_고도_거점이_정적_데이터와_같다(long id, String name, int altitude, double latitude, double longitude) {
        // when
        Waypoint highest = repository.findById(id).orElseThrow().highestWaypoint();

        // then — 프론트에 전달한 정적 JSON(2026-08-16)에서 고도가 가장 높은 거점과 대조
        assertThat(highest).isEqualTo(new Waypoint(name, new Coordinate(latitude, longitude), altitude));
    }

    @Test
    void 없는_id면_빈_결과를_준다() {
        assertThat(repository.findById(5)).isEmpty();
    }
}
