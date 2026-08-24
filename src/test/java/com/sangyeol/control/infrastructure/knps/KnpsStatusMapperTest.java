package com.sangyeol.control.infrastructure.knps;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sangyeol.control.domain.ControlStatus;
import org.junit.jupiter.api.Test;

class KnpsStatusMapperTest {

    @Test
    void 정상_텍스트는_NORMAL로_매핑된다() {
        // given
        String text = "정상";

        // when
        ControlStatus status = KnpsStatusMapper.toStatus(text);

        // then
        assertThat(status).isEqualTo(ControlStatus.NORMAL);
    }

    @Test
    void 부분통제_텍스트는_PARTIAL로_매핑된다() {
        // given
        String text = "부분통제";

        // when
        ControlStatus status = KnpsStatusMapper.toStatus(text);

        // then
        assertThat(status).isEqualTo(ControlStatus.PARTIAL);
    }

    @Test
    void 전면통제_텍스트는_FULL로_매핑된다() {
        // given — 주의: 전면통제 문자열은 미실측(픽스처에 없음). 실제 관측 시 확인 필요
        String text = "전면통제";

        // when
        ControlStatus status = KnpsStatusMapper.toStatus(text);

        // then
        assertThat(status).isEqualTo(ControlStatus.FULL);
    }

    @Test
    void 알_수_없는_텍스트면_예외가_발생한다() {
        // given
        String text = "점검중";

        // when & then
        assertThatThrownBy(() -> KnpsStatusMapper.toStatus(text))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("알 수 없는 통제 상태: 점검중");
    }

    @Test
    void 텍스트가_null이면_예외가_발생한다() {
        // given
        String text = null;

        // when & then
        assertThatThrownBy(() -> KnpsStatusMapper.toStatus(text))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("알 수 없는 통제 상태: null");
    }
}
