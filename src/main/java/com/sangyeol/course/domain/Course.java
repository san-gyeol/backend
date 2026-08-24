package com.sangyeol.course.domain;

import java.time.Duration;
import java.util.Objects;

public class Course {
    private final CourseName name;
    private final Distance distance;
    private final Duration duration;
    private final Difficulty difficulty;

    public Course(CourseName name, Distance distance, Duration duration, Difficulty difficulty) {
        this.name = Objects.requireNonNull(name, "Course 생성에 CourseName이 필요합니다");
        this.distance = Objects.requireNonNull(distance, "Course 생성에 Distance가 필요합니다");
        this.duration = Objects.requireNonNull(duration, "Course 생성에 Duration이 필요합니다");
        this.difficulty = Objects.requireNonNull(difficulty, "Course 생성에 Difficulty가 필요합니다");
        validateDuration(duration);
    }

    private void validateDuration(Duration duration) {
        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("소요시간은 0보다 커야 합니다");
        }
    }
}
