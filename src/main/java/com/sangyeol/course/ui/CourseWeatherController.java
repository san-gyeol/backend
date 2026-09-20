package com.sangyeol.course.ui;

import com.sangyeol.course.application.CourseWeatherService;
import com.sangyeol.weather.ui.dto.WeatherResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseWeatherController {

    private final CourseWeatherService courseWeatherService;

    public CourseWeatherController(CourseWeatherService courseWeatherService) {
        this.courseWeatherService = courseWeatherService;
    }

    @GetMapping("/{courseId}/weather")
    public ResponseEntity<WeatherResponse> findWeather(@PathVariable long courseId) {
        return ResponseEntity.ok(WeatherResponse.from(courseWeatherService.findWeather(courseId)));
    }
}
