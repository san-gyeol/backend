package com.sangyeol.course.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sangyeol.common.domain.Coordinate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class WaypointsTest {

    private static final Waypoint PARKING = new Waypoint("증심사주차장", new Coordinate(35.133447, 126.957845), 115);
    private static final Waypoint DANGSANNAMU = new Waypoint("당산나무", new Coordinate(35.125694, 126.972125), 294);
    private static final Waypoint JUNGMEORIJAE = new Waypoint("중머리재", new Coordinate(35.119077, 126.984481), 590);

    @Test
    void 가장_높은_고도의_거점을_찾는다() {
        // given — 당산나무 코스의 거점 순서 그대로
        Waypoints waypoints = new Waypoints(List.of(PARKING, DANGSANNAMU, JUNGMEORIJAE));

        // when
        Waypoint highest = waypoints.highest();

        // then
        assertThat(highest).isEqualTo(JUNGMEORIJAE);
    }

    @Test
    void 거점이_하나면_그_거점이_최고_고도_거점이다() {
        Waypoints waypoints = new Waypoints(List.of(PARKING));

        assertThat(waypoints.highest()).isEqualTo(PARKING);
    }

    @Test
    void 거점이_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Waypoints(List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("코스에는 거점이 하나 이상 있어야 합니다");
    }

    @Test
    void 거점_목록이_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Waypoints(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("코스에는 거점이 하나 이상 있어야 합니다");
    }

    @Test
    void 원본_목록을_바꿔도_영향을_받지_않는다() {
        // given
        List<Waypoint> source = new ArrayList<>(List.of(PARKING, DANGSANNAMU));
        Waypoints waypoints = new Waypoints(source);

        // when — 생성 후 원본에 더 높은 거점을 추가
        source.add(JUNGMEORIJAE);

        // then — 방어적 복사 덕분에 최고 거점은 그대로
        assertThat(waypoints.highest()).isEqualTo(DANGSANNAMU);
    }
}
