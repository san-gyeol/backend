package com.sangyeol.course.domain;

import java.util.List;
import java.util.Optional;

public class Courses {
    private final List<Course> courses;

    public Courses(List<Course> courses) {
        this.courses = List.copyOf(courses);
    }

    public Optional<Course> findById(long id) {
        return courses.stream()
                .filter(course -> course.hasId(id))
                .findFirst();
    }
}
