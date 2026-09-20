package com.sangyeol.course.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sangyeol.common.domain.Coordinate;
import org.junit.jupiter.api.Test;

class WaypointTest {

    private static final Coordinate JUNGMEORIJAE = new Coordinate(35.119077, 126.984481);

    @Test
    void 이름과_좌표와_고도가_있으면_정상_생성된다() {
        assertThatCode(() -> new Waypoint("중머리재", JUNGMEORIJAE, 590))
                .doesNotThrowAnyException();
    }

    @Test
    void 이름이_비어_있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Waypoint(" ", JUNGMEORIJAE, 590))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거점 이름은 비어 있을 수 없습니다");
    }

    @Test
    void 좌표가_null이면_예외가_발생한다() {
        assertThatThrownBy(() -> new Waypoint("중머리재", null, 590))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거점 좌표는 비어 있을 수 없습니다");
    }
}
