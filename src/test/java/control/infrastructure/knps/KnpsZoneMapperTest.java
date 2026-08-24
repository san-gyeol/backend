package control.infrastructure.knps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import control.domain.ControlZone;
import org.junit.jupiter.api.Test;

class KnpsZoneMapperTest {

    @Test
    void 무등산_텍스트는_MUDEUNGSAN으로_매핑된다() {
        // given
        String text = "무등산";

        // when
        ControlZone zone = KnpsZoneMapper.toZone(text);

        // then
        assertThat(zone).isEqualTo(ControlZone.MUDEUNGSAN);
    }

    @Test
    void 무등산동부_텍스트는_MUDEUNGSAN_EAST로_매핑된다() {
        // given
        String text = "무등산동부";

        // when
        ControlZone zone = KnpsZoneMapper.toZone(text);

        // then
        assertThat(zone).isEqualTo(ControlZone.MUDEUNGSAN_EAST);
    }

    @Test
    void 알_수_없는_텍스트면_예외가_발생한다() {
        // given
        String text = "지리산";

        // when & then
        assertThatThrownBy(() -> KnpsZoneMapper.toZone(text))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("알 수 없는 통제 구역: 지리산");
    }

    @Test
    void 텍스트가_null이면_예외가_발생한다() {
        // given
        String text = null;

        // when & then
        assertThatThrownBy(() -> KnpsZoneMapper.toZone(text))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("알 수 없는 통제 구역: null");
    }
}
