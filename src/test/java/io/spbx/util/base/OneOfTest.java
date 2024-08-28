package io.spbx.util.base;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.AssertTuples.assertOneOf;
import static io.spbx.util.base.AssertTuples.assertPair;
import static io.spbx.util.testing.MockFunction.failing;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("fast")
public class OneOfTest {
    @Test
    public void oneOf_simple() {
        assertOneOf(OneOf.ofFirst(1)).holds(1, null);
        assertOneOf(OneOf.ofSecond(2)).holds(null, 2);
        assertOneOf(OneOf.of(1, null)).holds(1, null);
        assertOneOf(OneOf.of(null, 2)).holds(null, 2);
    }

    @Test
    public void oneOf_invalid() {
        assertThrows(AssertionError.class, () -> OneOf.ofFirst(null));
        assertThrows(AssertionError.class, () -> OneOf.ofSecond(null));
        assertThrows(AssertionError.class, () -> OneOf.of(1, 2));
        assertThrows(AssertionError.class, () -> OneOf.of(null, null));
    }

    @Test
    public void oneOf_toPair() {
        assertPair(OneOf.ofFirst(1).toPair()).holds(1, null);
        assertPair(OneOf.ofSecond(2).toPair()).holds(null, 2);
    }

    @Test
    public void oneOf_swap() {
        assertOneOf(OneOf.ofFirst(1).swap()).holds(null, 1);
        assertOneOf(OneOf.ofSecond(2).swap()).holds(2, null);
    }

    @Test
    public void oneOf_map() {
        assertOneOf(OneOf.ofFirst(1).mapFirst(x1 -> -x1)).holds(-1, null);
        assertOneOf(OneOf.ofFirst(1).map(x1 -> -x1, String::valueOf)).holds(-1, null);
        assertOneOf(OneOf.ofSecond(2).mapSecond(x1 -> -x1)).holds(null, -2);
        assertOneOf(OneOf.ofSecond(2).map(String::valueOf, x -> -x)).holds(null, -2);
    }

    @Test
    public void oneOf_map_invalid() {
        assertThrows(AssertionError.class, () -> OneOf.of(1, null).mapSecond(String::valueOf));
        assertThrows(AssertionError.class, () -> OneOf.of(null, 2).mapFirst(String::valueOf));
    }

    @Test
    public void oneOf_mapToObj() {
        assertThat(OneOf.ofFirst(1).<String>mapToObj(String::valueOf, failing())).isEqualTo("1");
        assertThat(OneOf.ofSecond(2).<String>mapToObj(failing(), String::valueOf)).isEqualTo("2");
    }

    @Test
    public void oneOf_mapToInt() {
        assertThat(OneOf.ofFirst(1).mapToInt(x -> x + 1, failing())).isEqualTo(2);
        assertThat(OneOf.ofSecond(2).mapToInt(failing(), x -> x + 1)).isEqualTo(3);
    }

    @Test
    public void oneOf_mapToLong() {
        assertThat(OneOf.ofFirst(1).mapToLong(x -> x + 1L, failing())).isEqualTo(2L);
        assertThat(OneOf.ofSecond(2).mapToLong(failing(), x -> x + 1L)).isEqualTo(3L);
    }

    @Test
    public void oneOf_mapToDouble() {
        assertThat(OneOf.ofFirst(1).mapToDouble(x -> x + 1, failing())).isEqualTo(2.0);
        assertThat(OneOf.ofSecond(2).mapToDouble(failing(), x -> x + 1)).isEqualTo(3.0);
    }

    @Test
    public void oneOf_testFirst() {
        assertThat(OneOf.ofFirst(1).testFirstIfSet(x -> x > 0)).isTrue();
        assertThat(OneOf.ofFirst(-1).testFirstIfSet(x -> x > 0)).isFalse();
        assertThat(OneOf.ofSecond(1).testFirstIfSet(failing())).isFalse();
    }

    @Test
    public void oneOf_testSecond() {
        assertThat(OneOf.ofSecond(1).testSecondIfSet(x -> x > 0)).isTrue();
        assertThat(OneOf.ofSecond(-1).testSecondIfSet(x -> x > 0)).isFalse();
        assertThat(OneOf.ofFirst(1).testSecondIfSet(failing())).isFalse();
    }

    @Test
    public void oneOf_test() {
        assertThat(OneOf.ofFirst(1).test(x -> x > 0, failing())).isTrue();
        assertThat(OneOf.ofFirst(-1).test(x -> x > 0, failing())).isFalse();
        assertThat(OneOf.ofSecond(1).test(failing(), x -> x > 0)).isTrue();
        assertThat(OneOf.ofSecond(-1).test(failing(), x -> x > 0)).isFalse();
    }
}
