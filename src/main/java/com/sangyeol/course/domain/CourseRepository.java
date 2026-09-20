package com.sangyeol.course.domain;

import java.util.Optional;

public interface CourseRepository {
    Optional<Course> findById(long id);
}
