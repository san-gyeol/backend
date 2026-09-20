package com.sangyeol.common.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CoordinateTest {

    @Test
    void 위도와_경도가_범위_안이면_정상_생성된다() {
        // given — 무등산 주소지 좌표
        double latitude = 35.124386;
        double longitude = 127.009131;

        // when & then
        assertThatCode(() -> new Coordinate(latitude, longitude))
                .doesNotThrowAnyException();
    }

    @Test
    void 경계값_90과_180은_허용한다() {
        assertThatCode(() -> new Coordinate(90, -180))
                .doesNotThrowAnyException();
    }

    @Test
    void 위도가_90을_넘으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Coordinate(90.1, 127.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("위도는 -90 이상 90 이하여야 합니다: 90.1");
    }

    @Test
    void 위도가_마이너스_90보다_작으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Coordinate(-90.1, 127.0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 경도가_180을_넘으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Coordinate(35.0, 180.1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("경도는 -180 이상 180 이하여야 합니다: 180.1");
    }

    @Test
    void 경도가_마이너스_180보다_작으면_예외가_발생한다() {
        assertThatThrownBy(() -> new Coordinate(35.0, -180.1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
