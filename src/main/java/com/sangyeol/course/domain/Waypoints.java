package com.sangyeol.course.domain;

import java.util.Comparator;
import java.util.List;

public class Waypoints {
    private final List<Waypoint> waypoints;

    public Waypoints(List<Waypoint> waypoints) {
        validateNotEmpty(waypoints);
        this.waypoints = List.copyOf(waypoints);
    }

    private static void validateNotEmpty(List<Waypoint> waypoints) {
        if (waypoints == null || waypoints.isEmpty()) {
            throw new IllegalArgumentException("코스에는 거점이 하나 이상 있어야 합니다");
        }
    }

    public Waypoint highest() {
        return waypoints.stream()
                .max(Comparator.comparingInt(Waypoint::altitude))
                .orElseThrow();
    }
}
