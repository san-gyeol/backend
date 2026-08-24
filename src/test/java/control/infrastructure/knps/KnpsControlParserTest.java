package control.infrastructure.knps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import control.domain.ControlSnapshot;
import control.domain.ControlStatus;
import control.domain.ControlZone;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class KnpsControlParserTest {

    private String loadFixture() throws IOException {
        return Files.readString(Path.of("src/test/resources/knps-control-list.html"));
    }

    @Test
    void 실제_응답에서_무등산과_무등산동부_두_건을_뽑아낸다() throws IOException {
        // given — 스파이크 때 저장한 실제 공단 응답
        String html = loadFixture();

        // when
        List<ControlSnapshot> snapshots = KnpsControlParser.parse(html);

        // then — 저장 시점의 실측값과 대조
        assertThat(snapshots).hasSize(2);
        assertThat(snapshots.get(0).zone()).isEqualTo(ControlZone.MUDEUNGSAN);
        assertThat(snapshots.get(0).status()).isEqualTo(ControlStatus.PARTIAL);
        assertThat(snapshots.get(0).referenceTime()).isEqualTo(LocalDateTime.of(2026, 8, 16, 12, 0));
        assertThat(snapshots.get(0).source()).isEqualTo("국립공원공단");
        assertThat(snapshots.get(1).zone()).isEqualTo(ControlZone.MUDEUNGSAN_EAST);
        assertThat(snapshots.get(1).referenceTime()).isEqualTo(LocalDateTime.of(2026, 8, 16, 12, 32));
    }

    @Test
    void 무등산_행이_없으면_예외가_발생한다() throws IOException {
        // given — 페이지 개편으로 무등산 행이 사라진 상황을 재현
        String html = loadFixture().replace("무등산", "없는산");

        // when & then — 조용히 빈 결과를 주지 않고 시끄럽게 죽는다
        assertThatThrownBy(() -> KnpsControlParser.parse(html))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상태_텍스트가_바뀌면_예외가_발생한다() throws IOException {
        // given — 공단이 상태 문구를 바꾼 상황을 재현
        String html = loadFixture().replace("부분통제", "점검중");

        // when & then — 매퍼의 미지 텍스트 방어가 파서까지 전파된다
        assertThatThrownBy(() -> KnpsControlParser.parse(html))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("알 수 없는 통제 상태: 점검중");
    }
}
