package com.sangyeol.course.infrastructure;

import com.sangyeol.common.domain.Coordinate;
import com.sangyeol.course.domain.Course;
import com.sangyeol.course.domain.CourseRepository;
import com.sangyeol.course.domain.Courses;
import com.sangyeol.course.domain.Difficulty;
import com.sangyeol.course.domain.Waypoint;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * 무등산 4개 코스 마스터 데이터. 프론트에 전달한 정적 JSON(2026-08-16)과 같은 원본이며,
 * 거리와 소요시간은 보유 실측 세트, 난이도는 공단 기준으로 모두 NORMAL (팀 결정 2026-08-16).
 * 거점 좌표는 소수 여섯째 자리까지, 고도는 m 단위.
 */
@Repository
public class InMemoryCourseRepository implements CourseRepository {

    private static final Waypoint JUNGSIMSA_PARKING = new Waypoint("증심사주차장", new Coordinate(35.133447, 126.957845), 115);
    private static final Waypoint JUNGMEORIJAE = new Waypoint("중머리재", new Coordinate(35.119077, 126.984481), 590);
    private static final Waypoint JANGBULJAE = new Waypoint("장불재", new Coordinate(35.116271, 126.998612), 894);
    private static final Waypoint WONHYOSA_ENTRANCE = new Waypoint("원효사입구", new Coordinate(35.147731, 126.985122), 404);

    private static final Courses COURSES = new Courses(List.of(
            new Course(1, "당산나무 코스", 3.2, 104, Difficulty.NORMAL, List.of(
                    JUNGSIMSA_PARKING,
                    new Waypoint("당산나무", new Coordinate(35.125694, 126.972125), 294),
                    JUNGMEORIJAE)),
            new Course(2, "새인봉-입석대 코스", 6.0, 215, Difficulty.NORMAL, List.of(
                    JUNGSIMSA_PARKING,
                    new Waypoint("새인봉", new Coordinate(35.121330, 126.965548), 400),
                    new Waypoint("서인봉", new Coordinate(35.117328, 126.981087), 610),
                    JANGBULJAE,
                    new Waypoint("입석대", new Coordinate(35.117551, 127.002573), 965))),
            new Course(3, "늦재-옛길 코스", 9.3, 256, Difficulty.NORMAL, List.of(
                    WONHYOSA_ENTRANCE,
                    new Waypoint("늦재", new Coordinate(35.143169, 126.981972), 631),
                    new Waypoint("동화사터", new Coordinate(35.133113, 126.985493), 799),
                    new Waypoint("용추삼거리", new Coordinate(35.119419, 126.994105), 760),
                    JANGBULJAE,
                    new Waypoint("옛길갈림길", new Coordinate(35.120759, 126.997949), 885),
                    WONHYOSA_ENTRANCE)),
            new Course(4, "시무지기폭포 코스", 10.1, 281, Difficulty.NORMAL, List.of(
                    new Waypoint("증심교", new Coordinate(35.131472, 126.963348), 145),
                    new Waypoint("토끼등", new Coordinate(35.131086, 126.977772), 454),
                    JUNGMEORIJAE,
                    JANGBULJAE,
                    new Waypoint("규봉암", new Coordinate(35.118311, 127.016173), 828),
                    new Waypoint("시무지기폭포", new Coordinate(35.127240, 127.025841), 522),
                    new Waypoint("인계리", new Coordinate(35.124816, 127.033643), 352)))
    ));

    @Override
    public Optional<Course> findById(long id) {
        return COURSES.findById(id);
    }
}
