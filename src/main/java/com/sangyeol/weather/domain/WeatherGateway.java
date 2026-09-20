package com.sangyeol.weather.domain;

import com.sangyeol.common.domain.Coordinate;

public interface WeatherGateway {
    WeatherSnapshot fetch(Coordinate point);
}
