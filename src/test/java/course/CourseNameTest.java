package course;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CourseNameTest {

    @Test
    void 정상_이름으로_생성된다() {
        // given
        String name = "당산나무 코스";

        // when
        CourseName courseName = new CourseName(name);

        // then
        assertThat(courseName.name()).isEqualTo("당산나무 코스");
    }

    @Test
    void 하이픈이_포함된_이름도_생성된다() {
        // given
        String name = "새인봉-입석대 코스";

        // when & then
        assertThatCode(() -> new CourseName(name))
                .doesNotThrowAnyException();
    }

    @Test
    void 코스_이름이_null이면_예외가_발생한다() {
        // given
        String name = null;

        // when & then
        assertThatThrownBy(() -> new CourseName(name))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 코스_이름이_공백뿐이면_예외가_발생한다() {
        // given
        String name = "   ";

        // when & then
        assertThatThrownBy(() -> new CourseName(name))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
