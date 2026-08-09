package course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class DistanceTest {

    @Test
    void 정상_거리로_생성된다() {
        // given
        double kilometers = 9.4;

        // when
        Distance distance = new Distance(kilometers);

        // then
        assertThat(distance.kilometers()).isEqualTo(9.4);
    }

    @Test
    void 거리가_음수면_예외가_발생한다() {
        // given
        double kilometers = -4.3;

        // when & then
        assertThatThrownBy(() -> new Distance(kilometers))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 거리가_0이면_예외가_발생한다() {
        // given
        double kilometers = 0.0;

        // when & then
        assertThatThrownBy(() -> new Distance(kilometers))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
