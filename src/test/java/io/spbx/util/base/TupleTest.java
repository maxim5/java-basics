package io.spbx.util.base;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.AssertTuples.*;
import static io.spbx.util.testing.TestingBasics.listOf;
import static io.spbx.util.testing.TestingBasics.streamOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TupleTest {
    private static final Integer NULL = null;
    
    @Test
    public void tuple_from_array() {
        assertTuple(Tuple.of()).holds();
        assertTuple(Tuple.of(0)).holds(0);
        assertTuple(Tuple.of(NULL)).holds(NULL);
        assertTuple(Tuple.of(1, 2)).holds(1, 2);
        assertTuple(Tuple.of(NULL, NULL)).holds(NULL, NULL);
        assertTuple(Tuple.of(1, '2', "3")).holds(1, '2', "3");
        assertTuple(Tuple.of(0, 1, 2, 3, 4)).holds(0, 1, 2, 3, 4);
    }

    @Test
    public void tuple_from_collection() {
        assertTuple(Tuple.from(listOf())).holds();
        assertTuple(Tuple.from(listOf(0))).holds(0);
        assertTuple(Tuple.from(listOf(NULL))).holds(NULL);
        assertTuple(Tuple.from(listOf(1, 2))).holds(1, 2);
        assertTuple(Tuple.from(listOf(NULL, NULL))).holds(NULL, NULL);
        assertTuple(Tuple.from(listOf(1, '2', "3"))).holds(1, '2', "3");
        assertTuple(Tuple.from(listOf(0, 1, 2, 3, 4))).holds(0, 1, 2, 3, 4);
    }

    @Test
    public void tuple_from_stream() {
        assertTuple(Tuple.from(streamOf())).holds();
        assertTuple(Tuple.from(streamOf(0))).holds(0);
        assertTuple(Tuple.from(streamOf(NULL))).holds(NULL);
        assertTuple(Tuple.from(streamOf(1, 2))).holds(1, 2);
        assertTuple(Tuple.from(streamOf(NULL, NULL))).holds(NULL, NULL);
        assertTuple(Tuple.from(streamOf(1, '2', "3"))).holds(1, '2', "3");
        assertTuple(Tuple.from(streamOf(0, 1, 2, 3, 4))).holds(0, 1, 2, 3, 4);
    }

    @Test
    public void tuple_slice() {
        assertTuple(Tuple.of(1, 2, 3).slice(1, 3)).holds(2, 3);
        assertTuple(Tuple.of(1, 2, 3).slice(1, 2)).holds(2);
        assertTuple(Tuple.of(1, 2, 3).slice(1, 1)).holds();
    }

    @Test
    public void tuple_toPair() {
        assertPair(Tuple.of(1, 2).toPair()).holds(1, 2);
        assertPair(Tuple.of(1, '2').toPair()).holds(1, '2');
        assertPair(Tuple.of(null, null).toPair()).holds(null, null);
        assertThrows(AssertionError.class, () -> Tuple.of(1).toPair());
        assertThrows(AssertionError.class, () -> Tuple.of(1, 2, 3).toPair());
    }

    @Test
    public void tuple_toTriple() {
        assertTriple(Tuple.of(1, 2, 3).toTriple()).holds(1, 2, 3);
        assertTriple(Tuple.of(1, '2', "3").toTriple()).holds(1, '2', "3");
        assertTriple(Tuple.of(1, 2, null).toTriple()).holds(1, 2, null);
        assertTriple(Tuple.of(null, null, null).toTriple()).holds(null, null, null);
    }

    @Test
    public void tuple_isOneOf() {
        assertThat(Tuple.of(1).isOneOf()).isTrue();
        assertThat(Tuple.of(NULL).isOneOf()).isFalse();

        assertThat(Tuple.of(1, 2).isOneOf()).isFalse();
        assertThat(Tuple.of(1, null).isOneOf()).isTrue();
        assertThat(Tuple.of(null, 2).isOneOf()).isTrue();
        assertThat(Tuple.of(null, null).isOneOf()).isFalse();

        assertThat(Tuple.of(1, 2, 3).isOneOf()).isFalse();
        assertThat(Tuple.of(1, 2, null).isOneOf()).isFalse();
        assertThat(Tuple.of(1, null, null).isOneOf()).isTrue();
        assertThat(Tuple.of(null, null, null).isOneOf()).isFalse();
    }

    @Test
    public void tuple_isAnyOf() {
        assertThat(Tuple.of(1).isAnyOf()).isTrue();
        assertThat(Tuple.of(NULL).isAnyOf()).isFalse();

        assertThat(Tuple.of(1, 2).isAnyOf()).isTrue();
        assertThat(Tuple.of(1, null).isAnyOf()).isTrue();
        assertThat(Tuple.of(null, 2).isAnyOf()).isTrue();
        assertThat(Tuple.of(null, null).isAnyOf()).isFalse();

        assertThat(Tuple.of(1, 2, 3).isAnyOf()).isTrue();
        assertThat(Tuple.of(1, 2, null).isAnyOf()).isTrue();
        assertThat(Tuple.of(1, null, null).isAnyOf()).isTrue();
        assertThat(Tuple.of(null, null, null).isAnyOf()).isFalse();
    }

    @Test
    public void tuple_isAllOf() {
        assertThat(Tuple.of(1, 2).isAllOf()).isTrue();
        assertThat(Tuple.of(1, null).isAllOf()).isFalse();
        assertThat(Tuple.of(null, 2).isAllOf()).isFalse();
        assertThat(Tuple.of(null, null).isAllOf()).isFalse();

        assertThat(Tuple.of(1, 2, 3).isAllOf()).isTrue();
        assertThat(Tuple.of(1, 2, null).isAllOf()).isFalse();
        assertThat(Tuple.of(1, null, null).isAllOf()).isFalse();
        assertThat(Tuple.of(null, null, null).isAllOf()).isFalse();
    }

    @Test
    public void tuple_isNoneOf() {
        assertThat(Tuple.of(1, 2).isNoneOf()).isFalse();
        assertThat(Tuple.of(1, null).isNoneOf()).isFalse();
        assertThat(Tuple.of(null, 2).isNoneOf()).isFalse();
        assertThat(Tuple.of(null, null).isNoneOf()).isTrue();

        assertThat(Tuple.of(1, 2, 3).isNoneOf()).isFalse();
        assertThat(Tuple.of(1, 2, null).isNoneOf()).isFalse();
        assertThat(Tuple.of(1, null, null).isNoneOf()).isFalse();
        assertThat(Tuple.of(null, null, null).isNoneOf()).isTrue();
    }

    @Test
    public void tuple_countNulls() {
        assertThat(Tuple.of(1, 2).countNulls()).isEqualTo(0);
        assertThat(Tuple.of(1, null).countNulls()).isEqualTo(1);
        assertThat(Tuple.of(null, 2).countNulls()).isEqualTo(1);
        assertThat(Tuple.of(null, null).countNulls()).isEqualTo(2);

        assertThat(Tuple.of(1, 2, 3).countNulls()).isEqualTo(0);
        assertThat(Tuple.of(1, 2, null).countNulls()).isEqualTo(1);
        assertThat(Tuple.of(1, null, null).countNulls()).isEqualTo(2);
        assertThat(Tuple.of(null, null, null).countNulls()).isEqualTo(3);
    }

    @Test
    public void tuple_countNonNulls() {
        assertThat(Tuple.of(1, 2).countNonNulls()).isEqualTo(2);
        assertThat(Tuple.of(1, null).countNonNulls()).isEqualTo(1);
        assertThat(Tuple.of(null, 2).countNonNulls()).isEqualTo(1);
        assertThat(Tuple.of(null, null).countNonNulls()).isEqualTo(0);

        assertThat(Tuple.of(1, 2, 3).countNonNulls()).isEqualTo(3);
        assertThat(Tuple.of(1, 2, null).countNonNulls()).isEqualTo(2);
        assertThat(Tuple.of(1, null, null).countNonNulls()).isEqualTo(1);
        assertThat(Tuple.of(null, null, null).countNonNulls()).isEqualTo(0);
    }

    @Test
    public void tuple_withNth() {
        assertTuple(Tuple.of(1, 2).withNth(0, -1)).holds(-1, 2);
        assertTuple(Tuple.of(1, 2).withNth(0, null)).holds(null, 2);
        assertTuple(Tuple.of(1, 2).withNth(1, -2)).holds(1, -2);
        assertTuple(Tuple.of(1, 2).withNth(1, null)).holds(1, null);
        assertThrows(IndexOutOfBoundsException.class, () -> Tuple.of(1).withNth(1, null));
    }

    @Test
    public void tuple_testNth() {
        assertThat(Tuple.of(1, 2).<Integer>testNth(0, x -> x == 1)).isTrue();
        assertThat(Tuple.of(1, 2).<Integer>testNth(0, x -> x != 1)).isFalse();
        assertThat(Tuple.of(1, null).testNth(1, java.util.Objects::isNull)).isTrue();
        assertThat(Tuple.of(1, null).testNth(1, java.util.Objects::nonNull)).isFalse();
        assertThrows(IndexOutOfBoundsException.class, () -> Tuple.of(1).testNth(1, java.util.Objects::isNull));
    }

    @Test
    public void tuple_mapNth() {
        assertTuple(Tuple.of(1, 2).<Integer, String>mapNth(0, String::valueOf)).holds("1", 2);
        assertTuple(Tuple.of(1, 2).<Integer, String>mapNth(1, String::valueOf)).holds(1, "2");
        assertTuple(Tuple.of(null, null).<Integer, String>mapNth(0, String::valueOf)).holds("null", null);
        assertTuple(Tuple.of(null, null).<Integer, String>mapNth(1, String::valueOf)).holds(null, "null");
        assertThrows(IndexOutOfBoundsException.class, () -> Tuple.of(1).mapNth(1, String::valueOf));
    }
}
