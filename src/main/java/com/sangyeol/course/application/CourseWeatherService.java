package com.sangyeol.course.application;

import com.sangyeol.course.domain.Course;
import com.sangyeol.course.domain.CourseRepository;
import com.sangyeol.weather.application.WeatherService;
import com.sangyeol.weather.domain.WeatherSnapshot;
import org.springframework.stereotype.Service;

@Service
public class CourseWeatherService {

    private final CourseRepository courseRepository;
    private final WeatherService weatherService;

    public CourseWeatherService(CourseRepository courseRepository, WeatherService weatherService) {
        this.courseRepository = courseRepository;
        this.weatherService = weatherService;
    }

    // 코스 날씨 기준 지점: 코스 거점 중 가장 높은 고도의 위치 (기능명세, 2026-08-23 팀 합의)
    public WeatherSnapshot findWeather(long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        return weatherService.findWeather(course.highestWaypoint().coordinate());
    }
}
