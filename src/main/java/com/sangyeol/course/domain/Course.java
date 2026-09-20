package com.sangyeol.course.domain;

import java.time.Duration;
import java.util.List;

public class Course {
    private final long id;
    private final CourseName name;
    private final Distance distance;
    private final Duration duration;
    private final Difficulty difficulty;
    private final Waypoints waypoints;

    public Course(long id, String name, double kilometers, long minutes, Difficulty difficulty,
                  List<Waypoint> waypoints) {
        this(id, new CourseName(name), new Distance(kilometers), Duration.ofMinutes(minutes), difficulty,
                new Waypoints(waypoints));
    }

    private Course(long id, CourseName name, Distance distance, Duration duration, Difficulty difficulty,
                   Waypoints waypoints) {
        validateId(id);
        validateDuration(duration);
        validateDifficulty(difficulty);
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.duration = duration;
        this.difficulty = difficulty;
        this.waypoints = waypoints;
    }

    private void validateId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("코스 id는 0보다 커야 합니다");
        }
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

    public boolean hasId(long id) {
        return this.id == id;
    }

    public Waypoint highestWaypoint() {
        return waypoints.highest();
    }
}
