package com.sangyeol.common.domain;

public record Coordinate(double latitude, double longitude) {
    private static final double MAX_LATITUDE = 90;
    private static final double MAX_LONGITUDE = 180;

    public Coordinate {
        validateLatitude(latitude);
        validateLongitude(longitude);
    }

    private static void validateLatitude(double latitude) {
        if (Math.abs(latitude) > MAX_LATITUDE) {
            throw new IllegalArgumentException("위도는 -90 이상 90 이하여야 합니다: " + latitude);
        }
    }

    private static void validateLongitude(double longitude) {
        if (Math.abs(longitude) > MAX_LONGITUDE) {
            throw new IllegalArgumentException("경도는 -180 이상 180 이하여야 합니다: " + longitude);
        }
    }
}
