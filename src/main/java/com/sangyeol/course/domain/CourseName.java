package com.sangyeol.course.domain;

public record CourseName(String name) {
    public CourseName {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("코스 이름은 비어 있을 수 없습니다");
        }
    }
}
