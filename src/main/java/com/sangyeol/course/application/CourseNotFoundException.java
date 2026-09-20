package com.sangyeol.course.application;

import com.sangyeol.common.exception.BusinessException;

public class CourseNotFoundException extends BusinessException {

    private final long courseId;

    public CourseNotFoundException(long courseId) {
        super(CourseErrorType.COURSE_NOT_FOUND);
        this.courseId = courseId;
    }

    @Override
    public String getMessage() {
        return "코스를 찾을 수 없습니다: id=" + courseId;
    }
}
