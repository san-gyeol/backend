package com.sangyeol.control.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sangyeol.control.domain.ControlSnapshot;
import com.sangyeol.control.domain.ControlStatus;
import com.sangyeol.control.domain.ControlZone;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class ControlServiceTest {

    @Test
    void 게이트웨이가_준_스냅샷을_그대로_반환한다() {
        // given — 가짜 어댑터: 고정 스냅샷 반환 (HTTP 없음)
        ControlSnapshot snapshot = new ControlSnapshot(ControlZone.MUDEUNGSAN,
                ControlStatus.PARTIAL, LocalDateTime.of(2026, 8, 16, 12, 0), "국립공원공단");
        ControlService service = new ControlService(() -> List.of(snapshot));

        // when
        List<ControlSnapshot> result = service.findControls();

        // then
        assertThat(result).containsExactly(snapshot);
    }

    @Test
    void 게이트웨이가_실패하면_ControlUnavailableException으로_변환한다() {
        // given — 가짜 어댑터: 항상 실패
        ControlService service = new ControlService(() -> {
            throw new IllegalArgumentException("무등산 통제 행이 2건이 아닙니다: 0");
        });

        // when & then
        assertThatThrownBy(service::findControls)
                .isInstanceOf(ControlUnavailableException.class);
    }
}
