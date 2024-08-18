package io.spbx.util.base;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.AssertTuples.*;
import static io.spbx.util.testing.TestingBasics.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("fast")
public class TripleTest {
    @Test
    public void triple_simple_constructors() {
        assertTriple(Triple.of(1, 2, 3)).holds(1, 2, 3);
        assertTriple(Triple.of(1, "2", '3')).holds(1, "2", '3');
        assertTriple(Triple.empty()).holds(null, null, null);
    }

    @Test
    public void triple_from_array() {
        assertTriple(Triple.from(arrayOf(1, 2, 3))).holds(1, 2, 3);
        assertTriple(Triple.from(arrayOf(0, 1, 2, 3, 4), 0)).holds(0, 1, 2);
        assertTriple(Triple.from(arrayOf(0, 1, 2, 3, 4), 1)).holds(1, 2, 3);
        assertTriple(Triple.from(arrayOf(0, 1, 2, 3, 4), 2)).holds(2, 3, 4);
        assertTriple(Triple.from(arrayOf(0, 1, 2, 3, 4), -3)).holds(2, 3, 4);
        assertTriple(Triple.from(arrayOf(0, 1, 2, 3, 4), -4)).holds(1, 2, 3);
        assertTriple(Triple.from(arrayOf(0, 1, 2, 3, 4), -5)).holds(0, 1, 2);
    }

    @Test
    public void triple_from_collection() {
        assertTriple(Triple.from(listOf(1, 2, 3))).holds(1, 2, 3);
        assertTriple(Triple.from(listOf(0, 1, 2, 3, 4), 0)).holds(0, 1, 2);
        assertTriple(Triple.from(listOf(0, 1, 2, 3, 4), 1)).holds(1, 2, 3);
        assertTriple(Triple.from(listOf(0, 1, 2, 3, 4), 2)).holds(2, 3, 4);
        assertTriple(Triple.from(listOf(0, 1, 2, 3, 4), -3)).holds(2, 3, 4);
        assertTriple(Triple.from(listOf(0, 1, 2, 3, 4), -4)).holds(1, 2, 3);
        assertTriple(Triple.from(listOf(0, 1, 2, 3, 4), -5)).holds(0, 1, 2);
    }

    @Test
    public void triple_simple_nulls() {
        assertTriple(Triple.of(1, 2, null)).holds(1, 2, null);
        assertTriple(Triple.of(1, null, 3)).holds(1, null, 3);
        assertTriple(Triple.of(null, 2, 3)).holds(null, 2, 3);
        assertTriple(Triple.of(1, null, null)).holds(1, null, null);
        assertTriple(Triple.of(null, 2, null)).holds(null, 2, null);
        assertTriple(Triple.of(null, null, 3)).holds(null, null, 3);
        assertTriple(Triple.of(null, null, null)).holds(null, null, null);

        assertTriple(Triple.from(arrayOf(null, null, null))).holds(null, null, null);
        assertTriple(Triple.from(listOf(null, null, null))).holds(null, null, null);
    }

    @Test
    public void triple_invalid_array() {
        assertThrows(AssertionError.class, () -> Triple.from(arrayOf()));
        assertThrows(AssertionError.class, () -> Triple.from(arrayOf(1)));
        assertThrows(AssertionError.class, () -> Triple.from(arrayOf(1, 2)));
        assertThrows(AssertionError.class, () -> Triple.from(arrayOf(1, 2, 3, 4)));

        assertThrows(AssertionError.class, () -> Triple.from(arrayOf(0, 1, 2, 3, 4), -1));
        assertThrows(AssertionError.class, () -> Triple.from(arrayOf(0, 1, 2, 3, 4), -2));
        assertThrows(AssertionError.class, () -> Triple.from(arrayOf(0, 1, 2, 3, 4), 3));
        assertThrows(AssertionError.class, () -> Triple.from(arrayOf(0, 1, 2, 3, 4), 4));
        assertThrows(AssertionError.class, () -> Triple.from(arrayOf(0, 1, 2, 3, 4), 5));
    }

    @Test
    public void triple_invalid_collection() {
        assertThrows(AssertionError.class, () -> Triple.from(listOf()));
        assertThrows(AssertionError.class, () -> Triple.from(listOf(1)));
        assertThrows(AssertionError.class, () -> Triple.from(listOf(1, 2)));
        assertThrows(AssertionError.class, () -> Triple.from(listOf(1, 2, 3, 4)));

        assertThrows(AssertionError.class, () -> Triple.from(listOf(0, 1, 2, 3, 4), -1));
        assertThrows(AssertionError.class, () -> Triple.from(listOf(0, 1, 2, 3, 4), -2));
        assertThrows(AssertionError.class, () -> Triple.from(listOf(0, 1, 2, 3, 4), 3));
        assertThrows(AssertionError.class, () -> Triple.from(listOf(0, 1, 2, 3, 4), 4));
        assertThrows(AssertionError.class, () -> Triple.from(listOf(0, 1, 2, 3, 4), 5));
    }

    @Test
    public void triple_with() {
        assertTriple(Triple.of(1, 2, 3).withFirst(0)).holds(0, 2, 3);
        assertTriple(Triple.of(1, 2, 3).withFirst(null)).holds(null, 2, 3);
        assertTriple(Triple.of(1, 2, 3).withSecond(0)).holds(1, 0, 3);
        assertTriple(Triple.of(1, 2, 3).withSecond(null)).holds(1, null, 3);
        assertTriple(Triple.of(1, 2, 3).withThird(0)).holds(1, 2, 0);
        assertTriple(Triple.of(1, 2, 3).withThird(null)).holds(1, 2, null);
    }

    @Test
    public void triple_toPairWithout() {
        assertPair(Triple.of(1, 2, 3).toPairWithoutFirst()).holds(2, 3);
        assertPair(Triple.of(1, 2, 3).toPairWithoutSecond()).holds(1, 3);
        assertPair(Triple.of(1, 2, 3).toPairWithoutThird()).holds(1, 2);
        assertPair(Triple.of(null, null, null).toPairWithoutFirst()).holds(null, null);
        assertPair(Triple.of(null, null, null).toPairWithoutSecond()).holds(null, null);
        assertPair(Triple.of(null, null, null).toPairWithoutThird()).holds(null, null);
    }

    @Test
    public void triple_toTuple() {
        assertTuple(Triple.of(1, 2, 3).toTuple()).holds(1, 2, 3);
        assertTuple(Triple.of(null, null, null).toTuple()).holds(null, null, null);
    }

    @Test
    public void triple_isOneOf() {
        assertThat(Triple.of(1, 2, 3).isOneOf()).isFalse();
        assertThat(Triple.of(1, 2, null).isOneOf()).isFalse();
        assertThat(Triple.of(1, null, 3).isOneOf()).isFalse();
        assertThat(Triple.of(null, 2, 3).isOneOf()).isFalse();
        assertThat(Triple.of(1, null, null).isOneOf()).isTrue();
        assertThat(Triple.of(null, 2, null).isOneOf()).isTrue();
        assertThat(Triple.of(null, null, 3).isOneOf()).isTrue();
        assertThat(Triple.of(null, null, null).isOneOf()).isFalse();
    }

    @Test
    public void triple_isAnyOf() {
        assertThat(Triple.of(1, 2, 3).isAnyOf()).isTrue();
        assertThat(Triple.of(1, 2, null).isAnyOf()).isTrue();
        assertThat(Triple.of(1, null, 3).isAnyOf()).isTrue();
        assertThat(Triple.of(null, 2, 3).isAnyOf()).isTrue();
        assertThat(Triple.of(null, null, 3).isAnyOf()).isTrue();
        assertThat(Triple.of(null, null, null).isAnyOf()).isFalse();
    }

    @Test
    public void triple_isAllOf() {
        assertThat(Triple.of(1, 2, 3).isAllOf()).isTrue();
        assertThat(Triple.of(1, 2, null).isAllOf()).isFalse();
        assertThat(Triple.of(1, null, 3).isAllOf()).isFalse();
        assertThat(Triple.of(null, 2, 3).isAllOf()).isFalse();
        assertThat(Triple.of(null, null, 3).isAllOf()).isFalse();
        assertThat(Triple.of(null, null, null).isAllOf()).isFalse();
    }

    @Test
    public void triple_isNoneOf() {
        assertThat(Triple.of(1, 2, 3).isNoneOf()).isFalse();
        assertThat(Triple.of(1, 2, null).isNoneOf()).isFalse();
        assertThat(Triple.of(1, null, 3).isNoneOf()).isFalse();
        assertThat(Triple.of(null, 2, 3).isNoneOf()).isFalse();
        assertThat(Triple.of(null, null, 3).isNoneOf()).isFalse();
        assertThat(Triple.of(null, null, null).isNoneOf()).isTrue();
    }

    @Test
    public void triple_countNulls() {
        assertThat(Triple.of(1, 2, 3).countNulls()).isEqualTo(0);
        assertThat(Triple.of(1, 2, null).countNulls()).isEqualTo(1);
        assertThat(Triple.of(1, null, 3).countNulls()).isEqualTo(1);
        assertThat(Triple.of(null, 2, 3).countNulls()).isEqualTo(1);
        assertThat(Triple.of(null, null, 3).countNulls()).isEqualTo(2);
        assertThat(Triple.of(null, null, null).countNulls()).isEqualTo(3);
    }

    @Test
    public void triple_countNonNulls() {
        assertThat(Triple.of(1, 2, 3).countNonNulls()).isEqualTo(3);
        assertThat(Triple.of(1, 2, null).countNonNulls()).isEqualTo(2);
        assertThat(Triple.of(1, null, 3).countNonNulls()).isEqualTo(2);
        assertThat(Triple.of(null, 2, 3).countNonNulls()).isEqualTo(2);
        assertThat(Triple.of(null, null, 3).countNonNulls()).isEqualTo(1);
        assertThat(Triple.of(null, null, null).countNonNulls()).isEqualTo(0);
    }

    @Test
    public void triple_simple_reverse() {
        assertTriple(Triple.of(1, 2, 3).reverse()).holds(3, 2, 1);
        assertTriple(Triple.of(1, "2", '3').reverse()).holds('3', "2", 1);
        assertTriple(Triple.of(1, null, null).reverse()).holds(null, null, 1);
        assertTriple(Triple.of(null, 2, null).reverse()).holds(null, 2, null);
        assertTriple(Triple.of(null, null, 3).reverse()).holds(3, null, null);
        assertTriple(Triple.of(null, null, null).reverse()).holds(null, null, null);
    }

    @Test
    public void triple_map() {
        assertTriple(Triple.of(1, "2", '3').map(x -> -x, String::length, x -> -x)).holds(-1, 1, -51);
        assertTriple(Triple.of(1, "2", '3').mapFirst(x -> -x)).holds(-1, "2", '3');
        assertTriple(Triple.of(1, "2", '3').mapFirst((x, y, z) -> x + y.length() + z)).holds(53, "2", '3');
        assertTriple(Triple.of(1, "2", '3').mapSecond(String::length)).holds(1, 1, '3');
        assertTriple(Triple.of(1, "2", '3').mapSecond((x, y, z) -> x + y.length() + z)).holds(1, 53, '3');
        assertTriple(Triple.of(1, "2", '3').mapThird(x -> -x)).holds(1, "2", -51);
        assertTriple(Triple.of(1, "2", '3').mapThird((x, y, z) -> x + y.length() + z)).holds(1, "2", 53);
    }

    @Test
    public void triple_map_nullable() {
        assertTriple(Triple.of(1, null, null).map(x -> -x, x -> x, x -> x)).holds(-1, null, null);
        assertTriple(Triple.of(1, null, null).map(x -> -x, Objects::toString, x -> x)).holds(-1, "null", null);
        assertTriple(Triple.of(1, null, null).map(x -> x, x -> x, Objects::toString)).holds(1, null, "null");

        assertTriple(Triple.of(null, 2, null).mapFirst(Objects::toString)).holds("null", 2, null);
        assertTriple(Triple.of(null, 2, null).mapSecond(Objects::toString)).holds(null, "2", null);
        assertTriple(Triple.of(null, 2, null).mapThird(Objects::toString)).holds(null, 2, "null");

        assertTriple(Triple.of(null, null, 3).mapFirst(Objects::toString)).holds("null", null, 3);
        assertTriple(Triple.of(null, null, 3).mapSecond(Objects::toString)).holds(null, "null", 3);
        assertTriple(Triple.of(null, null, 3).mapThird(Objects::toString)).holds(null, null, "3");

        assertTriple(Triple.of(null, null, null).mapFirst(Objects::toString)).holds("null", null, null);
        assertTriple(Triple.of(null, null, null).mapSecond(Objects::toString)).holds(null, "null", null);
        assertTriple(Triple.of(null, null, null).mapThird(Objects::toString)).holds(null, null, "null");
        assertTriple(Triple.of(null, null, null).map(Objects::toString, Objects::toString, Objects::toString))
            .holds("null", "null", "null");
    }

    @Test
    public void triple_mapToObj() {
        assertThat(Triple.of("1", "2", '3').<String>mapToObj("%s%s%s"::formatted)).isEqualTo("123");
        assertThat(Triple.of("1", "2", null).<String>mapToObj("%s%s%s"::formatted)).isEqualTo("12null");
        assertThat(Triple.of("1", null, '3').<String>mapToObj("%s%s%s"::formatted)).isEqualTo("1null3");
        assertThat(Triple.of(null, "2", '3').<String>mapToObj("%s%s%s"::formatted)).isEqualTo("null23");
        assertThat(Triple.of(null, null, '3').<String>mapToObj("%s%s%s"::formatted)).isEqualTo("nullnull3");
        assertThat(Triple.of(null, null, null).<String>mapToObj("%s%s%s"::formatted)).isEqualTo("nullnullnull");
    }

    @Test
    public void triple_testFirst() {
        assertThat(Triple.of(1, 2, 3).testFirst(x -> x == 1)).isTrue();
        assertThat(Triple.of(1, 2, 3).testFirst(x -> x != 1)).isFalse();
        assertThat(Triple.of(null, 2, 3).testFirst(java.util.Objects::isNull)).isTrue();
        assertThat(Triple.of(null, 2, 3).testFirst(java.util.Objects::nonNull)).isFalse();
    }

    @Test
    public void triple_testSecond() {
        assertThat(Triple.of(1, 2, 3).testSecond(x -> x == 2)).isTrue();
        assertThat(Triple.of(1, 2, 3).testSecond(x -> x != 2)).isFalse();
        assertThat(Triple.of(1, null, 3).testSecond(java.util.Objects::isNull)).isTrue();
        assertThat(Triple.of(1, null, 3).testSecond(java.util.Objects::nonNull)).isFalse();
    }

    @Test
    public void triple_testThird() {
        assertThat(Triple.of(1, 2, 3).testThird(x -> x == 3)).isTrue();
        assertThat(Triple.of(1, 2, 3).testThird(x -> x != 3)).isFalse();
        assertThat(Triple.of(1, 2, null).testThird(java.util.Objects::isNull)).isTrue();
        assertThat(Triple.of(1, 2, null).testThird(java.util.Objects::nonNull)).isFalse();
    }

    @Test
    public void triple_apply() {
        Triple.of(1, 2, 3).apply((first, second, third) -> {
            assertThat(first).isEqualTo(1);
            assertThat(second).isEqualTo(2);
            assertThat(third).isEqualTo(3);
        });
        Triple.of(null, 2, 3).apply((first, second, third) -> {
            assertThat(first).isNull();
            assertThat(second).isEqualTo(2);
            assertThat(third).isEqualTo(3);
        });
        Triple.of(1, null, 3).apply((first, second, third) -> {
            assertThat(first).isEqualTo(1);
            assertThat(second).isNull();
            assertThat(third).isEqualTo(3);
        });
        Triple.of(1, 2, null).apply((first, second, third) -> {
            assertThat(first).isEqualTo(1);
            assertThat(second).isEqualTo(2);
            assertThat(third).isNull();
        });
        Triple.of(null, null, null).apply((first, second, third) -> {
            assertThat(first).isNull();
            assertThat(second).isNull();
            assertThat(third).isNull();
        });
    }

    @Test
    public void triple_stream() {
        assertThat(Triple.of(1, 2, 3).stream()).containsExactly(1, 2, 3);
        assertThat(Triple.of(1, 2, null).stream()).containsExactly(1, 2, null);
        assertThat(Triple.of(1, null, 3).stream()).containsExactly(1, null, 3);
        assertThat(Triple.of(null, 2, 3).stream()).containsExactly(null, 2, 3);
        assertThat(Triple.of(null, null, null).stream()).containsExactly(null, null, null);
    }

    @Test
    public void triple_toList() {
        assertThat(Triple.of(1, 2, 3).toList()).containsExactly(1, 2, 3);
        assertThat(Triple.of(1, 2, null).toList()).containsExactly(1, 2, null);
        assertThat(Triple.of(1, null, 3).toList()).containsExactly(1, null, 3);
        assertThat(Triple.of(null, 2, 3).toList()).containsExactly(null, 2, 3);
        assertThat(Triple.of(null, null, null).toList()).containsExactly(null, null, null);
    }

    @Test
    public void triple_toArray() {
        assertThat(Triple.of(1, 2, 3).toArray()).asList().containsExactly(1, 2, 3);
        assertThat(Triple.of(1, 2, null).toArray()).asList().containsExactly(1, 2, null);
        assertThat(Triple.of(1, null, 3).toArray()).asList().containsExactly(1, null, 3);
        assertThat(Triple.of(null, 2, 3).toArray()).asList().containsExactly(null, 2, 3);
        assertThat(Triple.of(null, null, null).toArray()).asList().containsExactly(null, null, null);
    }

    @Test
    public void triple_compare_comparable() {
        Comparator<Triple<Integer, Integer, String>> comparator = Triple.comparator();

        assertThat(comparator.compare(Triple.of(0, 1, "a"), Triple.of(0, 2, "b"))).isAtMost(-1);
        assertThat(comparator.compare(Triple.of(0, 1, "a"), Triple.of(0, 2, "a"))).isAtMost(-1);
        assertThat(comparator.compare(Triple.of(0, 1, "a"), Triple.of(0, 2, ""))).isAtMost(-1);

        assertThat(comparator.compare(Triple.of(0, 1, "a"), Triple.of(0, 1, "b"))).isAtMost(-1);
        assertThat(comparator.compare(Triple.of(0, 1, "a"), Triple.of(0, 1, "a"))).isEqualTo(0);
        assertThat(comparator.compare(Triple.of(0, 1, "a"), Triple.of(0, 1, ""))).isAtLeast(1);

        assertThat(comparator.compare(Triple.of(0, 1, "a"), Triple.of(0, 0, "b"))).isAtLeast(1);
        assertThat(comparator.compare(Triple.of(0, 1, "a"), Triple.of(0, 0, "a"))).isAtLeast(1);
        assertThat(comparator.compare(Triple.of(0, 1, "a"), Triple.of(0, 0, ""))).isAtLeast(1);

        assertThat(comparator.compare(Triple.of(0, 2, "b"), Triple.of(1, 0, "a"))).isAtLeast(-1);
        assertThat(comparator.compare(Triple.of(0, 2, "b"), Triple.of(1, 2, "b"))).isAtLeast(-1);
        assertThat(comparator.compare(Triple.of(1, 0, "a"), Triple.of(0, 2, "b"))).isAtLeast(1);
        assertThat(comparator.compare(Triple.of(1, 2, "b"), Triple.of(0, 2, "b"))).isAtLeast(1);
    }

    @Test
    public void triple_compare_of_comparator() {
        Comparator<Triple<Integer, Integer, String>> comparator = Triple.comparator(Integer::compareTo,
                                                                                    Comparator.comparingInt(Math::abs),
                                                                                    Comparator.comparingInt(String::length));

        assertThat(comparator.compare(Triple.of(0, 1, "a"), Triple.of(0, 1, "a"))).isEqualTo(0);
        assertThat(comparator.compare(Triple.of(0, 1, "a"), Triple.of(0, -1, "b"))).isEqualTo(0);
        assertThat(comparator.compare(Triple.of(0, -1, "a"), Triple.of(0, 1, "b"))).isEqualTo(0);
        assertThat(comparator.compare(Triple.of(0, -1, "a"), Triple.of(0, -1, "b"))).isEqualTo(0);

        assertThat(comparator.compare(Triple.of(1, -1, "a"), Triple.of(0, -1, "b"))).isEqualTo(1);
        assertThat(comparator.compare(Triple.of(1, 0, "a"), Triple.of(0, -1, "aa"))).isEqualTo(1);
        assertThat(comparator.compare(Triple.of(0, 1, "aaa"), Triple.of(0, 1, "b"))).isEqualTo(1);
        assertThat(comparator.compare(Triple.of(0, 1, "aaa"), Triple.of(0, -1, "bb"))).isEqualTo(1);
        assertThat(comparator.compare(Triple.of(0, -1, "aaa"), Triple.of(0, 1, "bb"))).isEqualTo(1);
        assertThat(comparator.compare(Triple.of(0, 1, "aaa"), Triple.of(0, 0, "ccc"))).isEqualTo(1);

        assertThat(comparator.compare(Triple.of(0, 1, "aaa"), Triple.of(1, 0, "b"))).isEqualTo(-1);
        assertThat(comparator.compare(Triple.of(0, 1, "aaa"), Triple.of(1, 1, "aaa"))).isEqualTo(-1);
        assertThat(comparator.compare(Triple.of(0, 1, "aaa"), Triple.of(0, 2, "b"))).isEqualTo(-1);
        assertThat(comparator.compare(Triple.of(0, 1, "aaa"), Triple.of(0, 2, "bbb"))).isEqualTo(-1);
        assertThat(comparator.compare(Triple.of(0, 1, "aaa"), Triple.of(0, -2, "bbb"))).isEqualTo(-1);
        assertThat(comparator.compare(Triple.of(0, 1, "aaa"), Triple.of(0, -1, "bbb+"))).isEqualTo(-1);
    }

    @Test
    public void toTriple_simple() {
        assertThat(streamOf(1, 2, 3).collect(Triple.toTriple())).isEqualTo(Triple.of(1, 2, 3));
        assertThrows(IllegalStateException.class, () -> Stream.<Integer>of().collect(Triple.toTriple()));
        assertThrows(IllegalStateException.class, () -> streamOf(1).collect(Triple.toTriple()));
        assertThrows(IllegalStateException.class, () -> streamOf(1, 2).collect(Triple.toTriple()));
        assertThrows(IllegalStateException.class, () -> streamOf(1, 2, 3, 4).collect(Triple.toTriple()));

        assertThat(intStreamOf(1, 2, 3).boxed().collect(Triple.toTriple())).isEqualTo(Triple.of(1, 2, 3));
        assertThrows(IllegalStateException.class, () -> intStreamOf().boxed().collect(Triple.toTriple()));
        assertThrows(IllegalStateException.class, () -> intStreamOf(1).boxed().collect(Triple.toTriple()));
        assertThrows(IllegalStateException.class, () -> intStreamOf(1, 2).boxed().collect(Triple.toTriple()));
        assertThrows(IllegalStateException.class, () -> intStreamOf(1, 2, 3, 4).boxed().collect(Triple.toTriple()));
    }

    @Test
    public void toTripleSparse_simple() {
        assertThat(Stream.<Integer>of().collect(Triple.toTripleSparse())).isEqualTo(Triple.of(null, null, null));
        assertThat(streamOf(1).collect(Triple.toTripleSparse())).isEqualTo(Triple.of(1, null, null));
        assertThat(streamOf(1, 2).collect(Triple.toTripleSparse())).isEqualTo(Triple.of(1, 2, null));
        assertThat(streamOf(1, 2, 3).collect(Triple.toTripleSparse())).isEqualTo(Triple.of(1, 2, 3));
        assertThrows(IllegalStateException.class, () -> streamOf(1, 2, 3, 4).collect(Triple.toTripleSparse()));

        assertThat(intStreamOf().boxed().collect(Triple.toTripleSparse())).isEqualTo(Triple.of(null, null, null));
        assertThat(intStreamOf(1).boxed().collect(Triple.toTripleSparse())).isEqualTo(Triple.of(1, null, null));
        assertThat(intStreamOf(1, 2).boxed().collect(Triple.toTripleSparse())).isEqualTo(Triple.of(1, 2, null));
        assertThat(intStreamOf(1, 2, 3).boxed().collect(Triple.toTripleSparse())).isEqualTo(Triple.of(1, 2, 3));
        assertThrows(IllegalStateException.class, () -> intStreamOf(1, 2, 3, 4).boxed().collect(Triple.toTripleSparse()));
    }
}
