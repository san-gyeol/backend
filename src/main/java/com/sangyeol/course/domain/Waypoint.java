package com.sangyeol.course.domain;

import com.sangyeol.common.domain.Coordinate;

public record Waypoint(String name, Coordinate coordinate, int altitude) {
    public Waypoint {
        validateName(name);
        validateCoordinate(coordinate);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("거점 이름은 비어 있을 수 없습니다");
        }
    }

    private static void validateCoordinate(Coordinate coordinate) {
        if (coordinate == null) {
            throw new IllegalArgumentException("거점 좌표는 비어 있을 수 없습니다");
        }
    }
}
