import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SanityTest {

    @Test
    void 환경_동작_확인() {
        assertThat(1 + 1).isEqualTo(2);
    }
}
