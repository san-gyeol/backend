package com.sangyeol.weather.ui;

import com.sangyeol.weather.application.WeatherService;
import com.sangyeol.weather.ui.dto.WeatherResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<WeatherResponse> findHomeWeather() {
        return ResponseEntity.ok(WeatherResponse.from(weatherService.findHomeWeather()));
    }
}
