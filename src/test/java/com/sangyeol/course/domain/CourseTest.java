package com.sangyeol.course.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sangyeol.common.domain.Coordinate;
import java.util.List;
import org.junit.jupiter.api.Test;

class CourseTest {

    private static final Waypoint PARKING = new Waypoint("증심사주차장", new Coordinate(35.133447, 126.957845), 115);
    private static final Waypoint JUNGMEORIJAE = new Waypoint("중머리재", new Coordinate(35.119077, 126.984481), 590);
    private static final List<Waypoint> WAYPOINTS = List.of(PARKING, JUNGMEORIJAE);

    @Test
    void 모든_필드가_있으면_정상_생성된다() {
        // given
        long id = 1;
        String name = "당산나무 코스";
        double kilometers = 9.4;
        long minutes = 104;
        Difficulty difficulty = Difficulty.EASY;

        // when & then
        assertThatCode(() -> new Course(id, name, kilometers, minutes, difficulty, WAYPOINTS))
                .doesNotThrowAnyException();
    }

    @Test
    void id가_0이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Course(0, "당산나무 코스", 9.4, 104, Difficulty.EASY, WAYPOINTS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("코스 id는 0보다 커야 합니다");
    }

    @Test
    void 이름이_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Course(1, null, 9.4, 104, Difficulty.EASY, WAYPOINTS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("코스 이름은 비어 있을 수 없습니다");
    }

    @Test
    void 거리가_0이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Course(1, "당산나무 코스", 0, 104, Difficulty.EASY, WAYPOINTS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 0보다 커야 합니다");
    }

    @Test
    void 소요시간이_0이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Course(1, "당산나무 코스", 9.4, 0, Difficulty.EASY, WAYPOINTS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("소요시간은 0보다 커야 합니다");
    }

    @Test
    void 소요시간이_음수면_예외가_발생한다() {
        assertThatThrownBy(() -> new Course(1, "당산나무 코스", 9.4, -13, Difficulty.EASY, WAYPOINTS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("소요시간은 0보다 커야 합니다");
    }

    @Test
    void 난이도가_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Course(1, "당산나무 코스", 9.4, 104, null, WAYPOINTS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("난이도는 비어 있을 수 없습니다");
    }

    @Test
    void 거점이_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Course(1, "당산나무 코스", 9.4, 104, Difficulty.EASY, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("코스에는 거점이 하나 이상 있어야 합니다");
    }

    @Test
    void 자기_id인지_알려준다() {
        Course course = new Course(1, "당산나무 코스", 9.4, 104, Difficulty.EASY, WAYPOINTS);

        assertThat(course.hasId(1)).isTrue();
        assertThat(course.hasId(2)).isFalse();
    }

    @Test
    void 가장_높은_고도의_거점을_알려준다() {
        Course course = new Course(1, "당산나무 코스", 9.4, 104, Difficulty.EASY, WAYPOINTS);

        assertThat(course.highestWaypoint()).isEqualTo(JUNGMEORIJAE);
    }
}
