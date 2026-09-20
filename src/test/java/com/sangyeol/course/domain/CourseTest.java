package com.sangyeol.course.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CourseTest {

    @Test
    void 모든_필드가_있으면_정상_생성된다() {
        // given
        String name = "당산나무 코스";
        double kilometers = 9.4;
        long minutes = 104;
        Difficulty difficulty = Difficulty.EASY;

        // when & then
        assertThatCode(() -> new Course(name, kilometers, minutes, difficulty))
                .doesNotThrowAnyException();
    }

    @Test
    void 이름이_null이면_예외가_발생한다() {
        // given
        String name = null;
        double kilometers = 9.4;
        long minutes = 104;
        Difficulty difficulty = Difficulty.EASY;

        // when & then
        assertThatThrownBy(() -> new Course(name, kilometers, minutes, difficulty))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("코스 이름은 비어 있을 수 없습니다");
    }

    @Test
    void 거리가_0이면_예외가_발생한다() {
        // given
        String name = "당산나무 코스";
        double kilometers = 0;
        long minutes = 104;
        Difficulty difficulty = Difficulty.EASY;

        // when & then
        assertThatThrownBy(() -> new Course(name, kilometers, minutes, difficulty))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 0보다 커야 합니다");
    }

    @Test
    void 소요시간이_0이면_예외가_발생한다() {
        // given
        String name = "당산나무 코스";
        double kilometers = 9.4;
        long minutes = 0;
        Difficulty difficulty = Difficulty.EASY;

        // when & then
        assertThatThrownBy(() -> new Course(name, kilometers, minutes, difficulty))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("소요시간은 0보다 커야 합니다");
    }

    @Test
    void 소요시간이_음수면_예외가_발생한다() {
        // given
        String name = "당산나무 코스";
        double kilometers = 9.4;
        long minutes = -13;
        Difficulty difficulty = Difficulty.EASY;

        // when & then
        assertThatThrownBy(() -> new Course(name, kilometers, minutes, difficulty))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("소요시간은 0보다 커야 합니다");
    }

    @Test
    void 난이도가_null이면_예외가_발생한다() {
        // given
        String name = "당산나무 코스";
        double kilometers = 9.4;
        long minutes = 104;
        Difficulty difficulty = null;

        // when & then
        assertThatThrownBy(() -> new Course(name, kilometers, minutes, difficulty))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("난이도는 비어 있을 수 없습니다");
    }
}
