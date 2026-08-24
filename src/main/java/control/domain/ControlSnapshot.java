package control.domain;

import java.time.LocalDateTime;

public record ControlSnapshot(
        ControlZone zone,
        ControlStatus status,
        LocalDateTime referenceTime,
        String source
) {
}
