package control.infrastructure.knps;

import control.domain.ControlStatus;
import java.util.Map;

final class KnpsStatusMapper {

    private static final Map<String, ControlStatus> STATUS_BY_TEXT = Map.of(
            "정상", ControlStatus.NORMAL,
            "부분통제", ControlStatus.PARTIAL,
            "전면통제", ControlStatus.FULL
    );

    private KnpsStatusMapper() {
    }

    static ControlStatus toStatus(String text) {
        if (text == null || !STATUS_BY_TEXT.containsKey(text)) {
            throw new IllegalArgumentException("알 수 없는 통제 상태: " + text);
        }
        return STATUS_BY_TEXT.get(text);
    }
}
