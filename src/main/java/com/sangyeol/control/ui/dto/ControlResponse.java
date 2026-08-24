package com.sangyeol.control.ui.dto;

import com.sangyeol.control.domain.ControlSnapshot;
import com.sangyeol.control.domain.ControlStatus;
import com.sangyeol.control.domain.ControlZone;
import java.time.LocalDateTime;
import java.util.Map;

public record ControlResponse(
        String zone,
        String zoneName,
        String status,
        String statusName,
        LocalDateTime referenceTime,
        String source
) {
    private static final Map<ControlZone, String> ZONE_NAMES = Map.of(
            ControlZone.MUDEUNGSAN, "무등산",
            ControlZone.MUDEUNGSAN_EAST, "무등산동부"
    );
    private static final Map<ControlStatus, String> STATUS_NAMES = Map.of(
            ControlStatus.NORMAL, "정상",
            ControlStatus.PARTIAL, "부분통제",
            ControlStatus.FULL, "전면통제"
    );

    public static ControlResponse from(ControlSnapshot snapshot) {
        return new ControlResponse(
                snapshot.zone().name(),
                ZONE_NAMES.get(snapshot.zone()),
                snapshot.status().name(),
                STATUS_NAMES.get(snapshot.status()),
                snapshot.referenceTime(),
                snapshot.source()
        );
    }
}
