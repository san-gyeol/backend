package com.sangyeol.course.domain;

import java.time.Duration;

public class Course {
    private final CourseName name;
    private final Distance distance;
    private final Duration duration;
    private final Difficulty difficulty;

    public Course(String name, double kilometers, long minutes, Difficulty difficulty) {
        this(new CourseName(name), new Distance(kilometers), Duration.ofMinutes(minutes), difficulty);
    }

    private Course(CourseName name, Distance distance, Duration duration, Difficulty difficulty) {
        validateDuration(duration);
        validateDifficulty(difficulty);
        this.name = name;
        this.distance = distance;
        this.duration = duration;
        this.difficulty = difficulty;
    }

    private void validateDuration(Duration duration) {
        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("소요시간은 0보다 커야 합니다");
        }
    }

    private void validateDifficulty(Difficulty difficulty) {
        if (difficulty == null) {
            throw new IllegalArgumentException("난이도는 비어 있을 수 없습니다");
        }
    }
}
