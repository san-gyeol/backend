package com.sangyeol.control.infrastructure.knps;

import static org.assertj.core.api.Assertions.assertThat;

import com.sangyeol.control.domain.ControlSnapshot;
import com.sangyeol.control.domain.ControlZone;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Disabled("실제 공단 서버 호출 — 수동 검증용. 마지막 통과: 2026-08-25")
class KnpsControlGatewayTest {

    @Test
    void 실제_공단_페이지에서_무등산_통제정보_두_건을_가져온다() {
        // given — Spring 컨텍스트 없이 직접 조립 (실제 네트워크 사용)
        RestClient client = RestClient.builder()
                .baseUrl("https://www.knps.or.kr")
                .defaultHeader(HttpHeaders.USER_AGENT, "sangyeol-backend/0.1")
                .build();
        KnpsControlGateway adapter = new KnpsControlGateway(client);

        // when
        List<ControlSnapshot> snapshots = adapter.fetch();

        // then — 상태 값은 시점마다 다르므로 구조(개수·구역·출처)만 검증
        assertThat(snapshots).hasSize(2);
        assertThat(snapshots).extracting(ControlSnapshot::zone)
                .containsExactly(ControlZone.MUDEUNGSAN, ControlZone.MUDEUNGSAN_EAST);
        assertThat(snapshots).allSatisfy(snapshot -> {
            assertThat(snapshot.status()).isNotNull();
            assertThat(snapshot.referenceTime()).isNotNull();
            assertThat(snapshot.source()).isEqualTo("국립공원공단");
        });
    }
}
