package com.sangyeol.control.infrastructure.knps;

import com.sangyeol.control.domain.ControlZone;
import java.util.Map;

final class KnpsZoneMapper {

    private static final Map<String, ControlZone> ZONE_BY_TEXT = Map.of(
            "무등산", ControlZone.MUDEUNGSAN,
            "무등산동부", ControlZone.MUDEUNGSAN_EAST
    );

    private KnpsZoneMapper() {
    }

    static ControlZone toZone(String text) {
        if (text == null || !ZONE_BY_TEXT.containsKey(text)) {
            throw new IllegalArgumentException("알 수 없는 통제 구역: " + text);
        }
        return ZONE_BY_TEXT.get(text);
    }
}
