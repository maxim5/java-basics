package io.spbx.util.base.tuple;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.tuple.AssertTuples.assertOneOf;
import static io.spbx.util.base.tuple.AssertTuples.assertTriple;
import static io.spbx.util.testing.func.MockFunction.failing;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("fast")
public class OneOfThreeTest {
    @Test
    public void oneOfThree_simple() {
        assertOneOf(OneOfThree.ofFirst(1)).holds(1, null, null);
        assertOneOf(OneOfThree.ofSecond(2)).holds(null, 2, null);
        assertOneOf(OneOfThree.ofThird(3)).holds(null, null, 3);
        assertOneOf(OneOfThree.of(1, null, null)).holds(1, null, null);
        assertOneOf(OneOfThree.of(null, 2, null)).holds(null, 2, null);
        assertOneOf(OneOfThree.of(null, null, 3)).holds(null, null, 3);
    }

    @Test
    public void oneOfThree_invalid() {
        assertThrows(AssertionError.class, () -> OneOfThree.ofFirst(null));
        assertThrows(AssertionError.class, () -> OneOfThree.ofSecond(null));
        assertThrows(AssertionError.class, () -> OneOfThree.ofThird(null));
        assertThrows(AssertionError.class, () -> OneOfThree.of(1, 2, 3));
        assertThrows(AssertionError.class, () -> OneOfThree.of(1, 2, null));
        assertThrows(AssertionError.class, () -> OneOfThree.of(1, null, 3));
        assertThrows(AssertionError.class, () -> OneOfThree.of(null, 2, 3));
        assertThrows(AssertionError.class, () -> OneOfThree.of(null, null, null));
    }

    @Test
    public void oneOfThree_toPair() {
        assertTriple(OneOfThree.ofFirst(1).toTriple()).holds(1, null, null);
        assertTriple(OneOfThree.ofSecond(2).toTriple()).holds(null, 2, null);
        assertTriple(OneOfThree.ofThird(3).toTriple()).holds(null, null, 3);
    }

    @Test
    public void oneOfThree_map() {
        assertOneOf(OneOfThree.ofFirst(1).mapFirst(x1 -> -x1)).holds(-1, null, null);
        assertOneOf(OneOfThree.ofFirst(1).map(x1 -> -x1, String::valueOf, String::valueOf)).holds(-1, null, null);
        assertOneOf(OneOfThree.ofSecond(2).mapSecond(x1 -> -x1)).holds(null, -2, null);
        assertOneOf(OneOfThree.ofSecond(2).map(String::valueOf, x -> -x, String::valueOf)).holds(null, -2, null);
        assertOneOf(OneOfThree.ofThird(3).mapThird(x3 -> -x3)).holds(null, null, -3);
        assertOneOf(OneOfThree.ofThird(3).map(String::valueOf, String::valueOf, x3 -> -x3)).holds(null, null, -3);
    }

    @Test
    public void oneOfThree_map_invalid() {
        assertThrows(AssertionError.class, () -> OneOfThree.of(1, null, null).mapSecond(String::valueOf));
        assertThrows(AssertionError.class, () -> OneOfThree.of(1, null, null).mapThird(String::valueOf));
        assertThrows(AssertionError.class, () -> OneOfThree.of(null, 2, null).mapFirst(String::valueOf));
        assertThrows(AssertionError.class, () -> OneOfThree.of(null, 2, null).mapThird(String::valueOf));
        assertThrows(AssertionError.class, () -> OneOfThree.of(null, null, 3).mapFirst(String::valueOf));
        assertThrows(AssertionError.class, () -> OneOfThree.of(null, null, 3).mapSecond(String::valueOf));
    }

    @Test
    public void oneOfThree_mapToObj() {
        assertThat(OneOfThree.ofFirst(1).<String>mapToObj(String::valueOf, failing(), failing())).isEqualTo("1");
        assertThat(OneOfThree.ofSecond(2).<String>mapToObj(failing(), String::valueOf, failing())).isEqualTo("2");
        assertThat(OneOfThree.ofThird(3).<String>mapToObj(failing(), failing(), String::valueOf)).isEqualTo("3");
    }

    @Test
    public void oneOfThree_mapToInt() {
        assertThat(OneOfThree.ofFirst(1).mapToInt(x -> x + 1, failing(), failing())).isEqualTo(2);
        assertThat(OneOfThree.ofSecond(2).mapToInt(failing(), x -> x + 1, failing())).isEqualTo(3);
        assertThat(OneOfThree.ofThird(3).mapToInt(failing(), failing(), x -> x + 1)).isEqualTo(4);
    }

    @Test
    public void oneOfThree_mapToLong() {
        assertThat(OneOfThree.ofFirst(1).mapToLong(x -> x + 1, failing(), failing())).isEqualTo(2L);
        assertThat(OneOfThree.ofSecond(2).mapToLong(failing(), x -> x + 1, failing())).isEqualTo(3L);
        assertThat(OneOfThree.ofThird(3).mapToLong(failing(), failing(), x -> x + 1)).isEqualTo(4L);
    }

    @Test
    public void oneOfThree_mapToDouble() {
        assertThat(OneOfThree.ofFirst(1).mapToDouble(x -> x + 1, failing(), failing())).isEqualTo(2.0);
        assertThat(OneOfThree.ofSecond(2).mapToDouble(failing(), x -> x + 1, failing())).isEqualTo(3.0);
        assertThat(OneOfThree.ofThird(3).mapToDouble(failing(), failing(), x -> x + 1)).isEqualTo(4.0);
    }

    @Test
    public void oneOfThree_testFirst() {
        assertThat(OneOfThree.ofFirst(1).testFirstIfSet(x -> x > 0)).isTrue();
        assertThat(OneOfThree.ofFirst(-1).testFirstIfSet(x -> x > 0)).isFalse();
        assertThat(OneOfThree.ofSecond(1).testFirstIfSet(failing())).isFalse();
        assertThat(OneOfThree.ofThird(1).testFirstIfSet(failing())).isFalse();
    }

    @Test
    public void oneOfThree_testSecond() {
        assertThat(OneOfThree.ofSecond(1).testSecondIfSet(x -> x > 0)).isTrue();
        assertThat(OneOfThree.ofSecond(-1).testSecondIfSet(x -> x > 0)).isFalse();
        assertThat(OneOfThree.ofFirst(1).testSecondIfSet(failing())).isFalse();
        assertThat(OneOfThree.ofThird(1).testSecondIfSet(failing())).isFalse();
    }

    @Test
    public void oneOfThree_testThird() {
        assertThat(OneOfThree.ofThird(1).testThirdIfSet(x -> x > 0)).isTrue();
        assertThat(OneOfThree.ofThird(-1).testThirdIfSet(x -> x > 0)).isFalse();
        assertThat(OneOfThree.ofFirst(1).testThirdIfSet(failing())).isFalse();
        assertThat(OneOfThree.ofSecond(1).testThirdIfSet(failing())).isFalse();
    }

    @Test
    public void oneOfThree_test() {
        assertThat(OneOfThree.ofFirst(1).test(x -> x > 0, failing(), failing())).isTrue();
        assertThat(OneOfThree.ofFirst(-1).test(x -> x > 0, failing(), failing())).isFalse();
        assertThat(OneOfThree.ofSecond(1).test(failing(), x -> x > 0, failing())).isTrue();
        assertThat(OneOfThree.ofSecond(-1).test(failing(), x -> x > 0, failing())).isFalse();
        assertThat(OneOfThree.ofThird(1).test(failing(), failing(), x -> x > 0)).isTrue();
        assertThat(OneOfThree.ofThird(-1).test(failing(), failing(), x -> x > 0)).isFalse();
    }
}
