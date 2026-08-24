package course.domain;

import java.util.List;

public class Courses {
    private final List<Course> courses;

    public Courses(List<Course> courses) {
        this.courses = List.copyOf(courses);
    }
}
