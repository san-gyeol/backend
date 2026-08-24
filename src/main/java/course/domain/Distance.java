package course.domain;

public record Distance(double kilometers) {
    public Distance {
        if (kilometers <= 0) {
            throw new IllegalArgumentException("거리는 0보다 커야 합니다");
        }
    }
}
