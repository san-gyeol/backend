package com.sangyeol.course.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class CourseTest {

    @Test
    void 모든_필드가_있으면_정상_생성된다() {
        // given
        CourseName courseName = new CourseName("당산나무 코스");
        Distance distance = new Distance(9.4);
        Duration duration = Duration.ofMinutes(104);
        Difficulty difficulty = Difficulty.EASY;

        // when & then
        assertThatCode(() -> new Course(courseName, distance, duration, difficulty))
                .doesNotThrowAnyException();
    }

    @Test
    void name필드가_null이면_예외가_발생한다() {
        // given
        CourseName courseName = null;
        Distance distance = new Distance(9.4);
        Duration duration = Duration.ofMinutes(104);
        Difficulty difficulty = Difficulty.EASY;

        // when & then
        assertThatThrownBy(() -> new Course(courseName, distance, duration, difficulty))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Course 생성에 CourseName이 필요합니다");
    }

    @Test
    void distance필드가_null이면_예외가_발생한다() {
        // given
        CourseName courseName = new CourseName("당산나무 코스");
        Distance distance = null;
        Duration duration = Duration.ofMinutes(104);
        Difficulty difficulty = Difficulty.EASY;

        // when & then
        assertThatThrownBy(() -> new Course(courseName, distance, duration, difficulty))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Course 생성에 Distance가 필요합니다");
    }

    @Test
    void duration필드가_null이면_예외가_발생한다() {
        // given
        CourseName courseName = new CourseName("당산나무 코스");
        Distance distance = new Distance(9.4);
        Duration duration = null;
        Difficulty difficulty = Difficulty.EASY;

        // when & then
        assertThatThrownBy(() -> new Course(courseName, distance, duration, difficulty))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Course 생성에 Duration이 필요합니다");
    }

    @Test
    void difficulty필드가_null이면_예외가_발생한다() {
        // given
        CourseName courseName = new CourseName("당산나무 코스");
        Distance distance = new Distance(9.4);
        Duration duration = Duration.ofMinutes(104);
        Difficulty difficulty = null;

        // when & then
        assertThatThrownBy(() -> new Course(courseName, distance, duration, difficulty))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Course 생성에 Difficulty가 필요합니다");
    }

    @Test
    void duration이_0이면_예외가_발생한다() {
        // given
        CourseName courseName = new CourseName("당산나무 코스");
        Distance distance = new Distance(9.4);
        Duration duration = Duration.ofMinutes(0);
        Difficulty difficulty = Difficulty.EASY;

        // when & then
        assertThatThrownBy(() -> new Course(courseName, distance, duration, difficulty))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void duration이_음수면_예외가_발생한다() {
        // given
        CourseName courseName = new CourseName("당산나무 코스");
        Distance distance = new Distance(9.4);
        Duration duration = Duration.ofMinutes(-13);
        Difficulty difficulty = Difficulty.EASY;

        // when & then
        assertThatThrownBy(() -> new Course(courseName, distance, duration, difficulty))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
