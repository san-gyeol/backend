package com.sangyeol.course.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sangyeol.common.domain.Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CoursesTest {

    private static final List<Waypoint> WAYPOINTS = List.of(
            new Waypoint("중머리재", new Coordinate(35.119077, 126.984481), 590));
    private static final Course DANGSANNAMU = new Course(1, "당산나무 코스", 3.2, 104, Difficulty.NORMAL, WAYPOINTS);
    private static final Course SAEINBONG = new Course(2, "새인봉-입석대 코스", 6.0, 215, Difficulty.NORMAL, WAYPOINTS);

    @Test
    void id로_코스를_찾는다() {
        Courses courses = new Courses(List.of(DANGSANNAMU, SAEINBONG));

        Optional<Course> found = courses.findById(2);

        assertThat(found).contains(SAEINBONG);
    }

    @Test
    void 없는_id면_빈_결과를_준다() {
        Courses courses = new Courses(List.of(DANGSANNAMU, SAEINBONG));

        assertThat(courses.findById(99)).isEmpty();
    }

    @Test
    void 원본_목록을_바꿔도_영향을_받지_않는다() {
        // given
        List<Course> source = new ArrayList<>(List.of(DANGSANNAMU));
        Courses courses = new Courses(source);

        // when — 생성 후 원본에 코스를 추가
        source.add(SAEINBONG);

        // then — 방어적 복사 덕분에 새 코스는 보이지 않는다
        assertThat(courses.findById(2)).isEmpty();
    }
}
