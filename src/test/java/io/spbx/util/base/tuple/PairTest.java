package io.spbx.util.base.tuple;

import com.google.common.base.Objects;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.tuple.AssertTuples.*;
import static io.spbx.util.testing.TestingBasics.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("fast")
public class PairTest {
    @Test
    public void pair_constructor_simple() {
        assertPair(Pair.of(1, 2)).holds(1, 2);
        assertPair(Pair.of(1, "2")).holds(1, "2");
        assertPair(Pair.of(Map.entry("1", "2"))).holds("1", "2");
        assertPair(Pair.of(new AbstractMap.SimpleEntry<>(1, "2"))).holds(1, "2");
        assertPair(Pair.empty()).holds(null, null);
        assertPair(Pair.dupe(1)).holds(1, 1);

        assertThat(Pair.of(null, null)).isSameInstanceAs(Pair.empty());
        assertThat(Pair.dupe(null)).isSameInstanceAs(Pair.empty());
    }

    @Test
    public void pair_constructor_from_entry() {
        Pair<CharSequence, CharSequence> pair = Pair.of(Map.entry("1", "2"));
        assertPair(pair).holds("1", "2");
    }

    @Test
    public void pair_constructor_from_pair() {
        Pair<Integer, String> pair = Pair.of(1, "2");
        assertPair(Pair.of(pair)).holds(1, "2");
        assertThat(Pair.of(pair)).isSameInstanceAs(pair);
    }

    @Test
    public void pair_from_array() {
        assertPair(Pair.from(arrayOf(1, 2))).holds(1, 2);
        assertPair(Pair.from(arrayOf(0, 1, 2, 3, 4), 0)).holds(0, 1);
        assertPair(Pair.from(arrayOf(0, 1, 2, 3, 4), 1)).holds(1, 2);
        assertPair(Pair.from(arrayOf(0, 1, 2, 3, 4), 2)).holds(2, 3);
        assertPair(Pair.from(arrayOf(0, 1, 2, 3, 4), 3)).holds(3, 4);
        assertPair(Pair.from(arrayOf(0, 1, 2, 3, 4), -2)).holds(3, 4);
        assertPair(Pair.from(arrayOf(0, 1, 2, 3, 4), -3)).holds(2, 3);
        assertPair(Pair.from(arrayOf(0, 1, 2, 3, 4), -4)).holds(1, 2);
        assertPair(Pair.from(arrayOf(0, 1, 2, 3, 4), -5)).holds(0, 1);
    }

    @Test
    public void pair_from_collection() {
        assertPair(Pair.from(listOf(1, 2))).holds(1, 2);
        assertPair(Pair.from(listOf(0, 1, 2, 3, 4), 0)).holds(0, 1);
        assertPair(Pair.from(listOf(0, 1, 2, 3, 4), 1)).holds(1, 2);
        assertPair(Pair.from(listOf(0, 1, 2, 3, 4), 2)).holds(2, 3);
        assertPair(Pair.from(listOf(0, 1, 2, 3, 4), 3)).holds(3, 4);
        assertPair(Pair.from(listOf(0, 1, 2, 3, 4), -2)).holds(3, 4);
        assertPair(Pair.from(listOf(0, 1, 2, 3, 4), -3)).holds(2, 3);
        assertPair(Pair.from(listOf(0, 1, 2, 3, 4), -4)).holds(1, 2);
        assertPair(Pair.from(listOf(0, 1, 2, 3, 4), -5)).holds(0, 1);
    }
    
    @Test
    public void pair_simple_nulls() {
        assertPair(Pair.of(1, null)).holds(1, null);
        assertPair(Pair.of(null, 2)).holds(null, 2);
        assertPair(Pair.of(null, null)).holds(null, null);

        assertPair(Pair.from(arrayOf(null, null))).holds(null, null);
        assertPair(Pair.from(listOf(null, null))).holds(null, null);
        assertPair(Pair.of(new AbstractMap.SimpleEntry<>(null, null))).holds(null, null);
    }

    @Test
    public void pair_invalid_array() {
        assertThrows(AssertionError.class, () -> Pair.from(arrayOf()));
        assertThrows(AssertionError.class, () -> Pair.from(arrayOf(1)));
        assertThrows(AssertionError.class, () -> Pair.from(arrayOf(1, 2, 3)));

        assertThrows(AssertionError.class, () -> Pair.from(arrayOf(0, 1, 2, 3, 4), -1));
        assertThrows(AssertionError.class, () -> Pair.from(arrayOf(0, 1, 2, 3, 4), 4));
        assertThrows(AssertionError.class, () -> Pair.from(arrayOf(0, 1, 2, 3, 4), 5));
    }

    @Test
    public void pair_invalid_collection() {
        assertThrows(AssertionError.class, () -> Pair.from(listOf()));
        assertThrows(AssertionError.class, () -> Pair.from(listOf(1)));
        assertThrows(AssertionError.class, () -> Pair.from(listOf(1, 2, 3)));

        assertThrows(AssertionError.class, () -> Pair.from(listOf(0, 1, 2, 3, 4), -1));
        assertThrows(AssertionError.class, () -> Pair.from(listOf(0, 1, 2, 3, 4), 4));
        assertThrows(AssertionError.class, () -> Pair.from(listOf(0, 1, 2, 3, 4), 5));
    }

    @Test
    public void pair_with() {
        assertPair(Pair.of(1, 2).withFirst(0)).holds(0, 2);
        assertPair(Pair.of(1, 2).withFirst(null)).holds(null, 2);
        assertPair(Pair.of(1, 2).withSecond(0)).holds(1, 0);
        assertPair(Pair.of(1, 2).withSecond(null)).holds(1, null);
    }

    @Test
    public void pair_toTripleWith() {
        assertTriple(Pair.of(1, 2).toTripleWith(3)).holds(1, 2, 3);
        assertTriple(Pair.of(1, 2).toTripleWith(null)).holds(1, 2, null);
        assertTriple(Pair.of(null, null).toTripleWith(null)).holds(null, null, null);
    }

    @Test
    public void pair_toTuple() {
        assertTuple(Pair.of(1, 2).toTuple()).holds(1, 2);
        assertTuple(Pair.of(null, null).toTuple()).holds(null, null);
    }

    @Test
    public void pair_isOneOf() {
        assertThat(Pair.of(1, 2).isOneOf()).isFalse();
        assertThat(Pair.of(1, null).isOneOf()).isTrue();
        assertThat(Pair.of(null, 2).isOneOf()).isTrue();
        assertThat(Pair.of(null, null).isOneOf()).isFalse();
    }

    @Test
    public void pair_toOneOf() {
        assertOneOf(Pair.of(1, null).toOneOf()).holds(1, null);
        assertOneOf(Pair.of(null, 2).toOneOf()).holds(null, 2);
        assertThrows(AssertionError.class, () -> Pair.of(1, 2).toOneOf());
        assertThrows(AssertionError.class, () -> Pair.of(null, null).toOneOf());
    }

    @Test
    public void pair_isAnyOf() {
        assertThat(Pair.of(1, 2).isAnyOf()).isTrue();
        assertThat(Pair.of(1, null).isAnyOf()).isTrue();
        assertThat(Pair.of(null, 2).isAnyOf()).isTrue();
        assertThat(Pair.of(null, null).isAnyOf()).isFalse();
    }

    @Test
    public void pair_isAllOf() {
        assertThat(Pair.of(1, 2).isAllOf()).isTrue();
        assertThat(Pair.of(1, null).isAllOf()).isFalse();
        assertThat(Pair.of(null, 2).isAllOf()).isFalse();
        assertThat(Pair.of(null, null).isAllOf()).isFalse();
    }

    @Test
    public void pair_isNoneOf() {
        assertThat(Pair.of(1, 2).isNoneOf()).isFalse();
        assertThat(Pair.of(1, null).isNoneOf()).isFalse();
        assertThat(Pair.of(null, 2).isNoneOf()).isFalse();
        assertThat(Pair.of(null, null).isNoneOf()).isTrue();
    }

    @Test
    public void pair_countNulls() {
        assertThat(Pair.of(1, 2).countNulls()).isEqualTo(0);
        assertThat(Pair.of(1, null).countNulls()).isEqualTo(1);
        assertThat(Pair.of(null, 2).countNulls()).isEqualTo(1);
        assertThat(Pair.of(null, null).countNulls()).isEqualTo(2);
    }

    @Test
    public void pair_countNonNulls() {
        assertThat(Pair.of(1, 2).countNonNulls()).isEqualTo(2);
        assertThat(Pair.of(1, null).countNonNulls()).isEqualTo(1);
        assertThat(Pair.of(null, 2).countNonNulls()).isEqualTo(1);
        assertThat(Pair.of(null, null).countNonNulls()).isEqualTo(0);
    }

    @Test
    public void pair_simple_swap() {
        assertPair(Pair.of(1, 2).swap()).holds(2, 1);
        assertPair(Pair.of(1, "2").swap()).holds("2", 1);
        assertPair(Pair.of(1, null).swap()).holds(null, 1);
        assertPair(Pair.of(null, 2).swap()).holds(2, null);
        assertPair(Pair.of(null, null).swap()).holds(null, null);
    }

    @Test
    public void pair_map() {
        assertPair(Pair.of(1, "2").map(x -> -x, String::length)).holds(-1, 1);
        assertPair(Pair.of(1, "2").mapFirst(x -> -x)).holds(-1, "2");
        assertPair(Pair.of(1, "2").mapFirst((x, s) -> x + s.length())).holds(2, "2");
        assertPair(Pair.of(1, "2").mapSecond(String::length)).holds(1, 1);
        assertPair(Pair.of(1, "2").mapSecond((x, s) -> x + s.length())).holds(1, 2);
    }

    @Test
    public void pair_map_nullable() {
        assertPair(Pair.of(1, null).map(x -> -x, x -> x)).holds(-1, null);
        assertPair(Pair.of(1, null).map(x -> -x, String::valueOf)).holds(-1, "null");

        assertPair(Pair.of(null, 2).mapFirst(String::valueOf)).holds("null", 2);
        assertPair(Pair.of(null, 2).mapSecond(String::valueOf)).holds(null, "2");

        assertPair(Pair.of(null, null).mapFirst(String::valueOf)).holds("null", null);
        assertPair(Pair.of(null, null).mapSecond(String::valueOf)).holds(null, "null");
        assertPair(Pair.of(null, null).map(String::valueOf, String::valueOf)).holds("null", "null");
    }

    @Test
    public void pair_mapToObj() {
        assertThat(Pair.of("1", "2").<String>mapToObj("%s%s"::formatted)).isEqualTo("12");
        assertThat(Pair.of("1", null).<String>mapToObj("%s%s"::formatted)).isEqualTo("1null");
        assertThat(Pair.of(null, "2").<String>mapToObj("%s%s"::formatted)).isEqualTo("null2");
        assertThat(Pair.of(null, null).<String>mapToObj("%s%s"::formatted)).isEqualTo("nullnull");
    }

    @Test
    public void pair_mapToInt() {
        assertThat(Pair.of(1, 2).mapToInt(Integer::sum)).isEqualTo(3);
        assertThat(Pair.of(1, null).mapToInt(Objects::hashCode)).isEqualTo(Objects.hashCode(1, null));
        assertThat(Pair.of(null, 2).mapToInt(Objects::hashCode)).isEqualTo(Objects.hashCode(null, 2));
        assertThat(Pair.of(null, null).mapToInt(Objects::hashCode)).isEqualTo(Objects.hashCode(null, null));
    }

    @Test
    public void pair_mapToLong() {
        assertThat(Pair.of(1, 2).mapToLong(Integer::sum)).isEqualTo(3);
        assertThat(Pair.of(1, null).mapToLong(Objects::hashCode)).isEqualTo(Objects.hashCode(1, null));
        assertThat(Pair.of(null, 2).mapToLong(Objects::hashCode)).isEqualTo(Objects.hashCode(null, 2));
        assertThat(Pair.of(null, null).mapToLong(Objects::hashCode)).isEqualTo(Objects.hashCode(null, null));
    }

    @Test
    public void pair_mapToDouble() {
        assertThat(Pair.of(1, 2).mapToDouble(Integer::sum)).isEqualTo(3);
        assertThat(Pair.of(1, null).mapToDouble(Objects::hashCode)).isEqualTo(Objects.hashCode(1, null));
        assertThat(Pair.of(null, 2).mapToDouble(Objects::hashCode)).isEqualTo(Objects.hashCode(null, 2));
        assertThat(Pair.of(null, null).mapToDouble(Objects::hashCode)).isEqualTo(Objects.hashCode(null, null));
    }

    @Test
    public void pair_testFirst() {
        assertThat(Pair.of(1, 2).testFirst(x -> x == 1)).isTrue();
        assertThat(Pair.of(1, 2).testFirst(x -> x != 1)).isFalse();
        assertThat(Pair.of(null, 2).testFirst(java.util.Objects::isNull)).isTrue();
        assertThat(Pair.of(null, 2).testFirst(java.util.Objects::nonNull)).isFalse();
    }

    @Test
    public void pair_testSecond() {
        assertThat(Pair.of(1, 2).testSecond(x -> x == 2)).isTrue();
        assertThat(Pair.of(1, 2).testSecond(x -> x != 2)).isFalse();
        assertThat(Pair.of(1, null).testSecond(java.util.Objects::isNull)).isTrue();
        assertThat(Pair.of(1, null).testSecond(java.util.Objects::nonNull)).isFalse();
    }

    @Test
    public void pair_test() {
        assertThat(Pair.of(1, 1).test((x, y) -> x - y == 0)).isTrue();
        assertThat(Pair.of(1, 2).test((x, y) -> x - y == 0)).isFalse();
        assertThat(Pair.of(1, null).test((x, y) -> x != null && y == null)).isTrue();
        assertThat(Pair.of(null, 1).test((x, y) -> x == null && y != null)).isTrue();
        assertThat(Pair.of(null, null).test((x, y) -> x == null && y == null)).isTrue();
    }

    @Test
    public void pair_apply() {
        Pair.of(1, 2).apply((first, second) -> {
            assertThat(first).isEqualTo(1);
            assertThat(second).isEqualTo(2);
        });
        Pair.of(1, null).apply((first, second) -> {
            assertThat(first).isEqualTo(1);
            assertThat(second).isNull();
        });
        Pair.of(null, 2).apply((first, second) -> {
            assertThat(first).isNull();
            assertThat(second).isEqualTo(2);
        });
        Pair.of(null, null).apply((first, second) -> {
            assertThat(first).isNull();
            assertThat(second).isNull();
        });
    }

    @Test
    public void pair_stream() {
        assertThat(Pair.of(1, 2).stream()).containsExactly(1, 2);
        assertThat(Pair.of(1, null).stream()).containsExactly(1, null);
        assertThat(Pair.of(null, 2).stream()).containsExactly(null, 2);
        assertThat(Pair.of(null, null).stream()).containsExactly(null, null);
    }

    @Test
    public void pair_toList() {
        assertThat(Pair.of(1, 2).toList()).containsExactly(1, 2);
        assertThat(Pair.of(1, null).toList()).containsExactly(1, null);
        assertThat(Pair.of(null, 2).toList()).containsExactly(null, 2);
        assertThat(Pair.of(null, null).toList()).containsExactly(null, null);
    }

    @Test
    public void pair_toArray() {
        assertThat(Pair.of(1, 2).toArray()).asList().containsExactly(1, 2);
        assertThat(Pair.of(1, null).toArray()).asList().containsExactly(1, null);
        assertThat(Pair.of(null, 2).toArray()).asList().containsExactly(null, 2);
        assertThat(Pair.of(null, null).toArray()).asList().containsExactly(null, null);
    }

    @Test
    public void pair_compare_comparable() {
        assertThat(Pair.<Integer, String>comparator().compare(Pair.of(1, "a"), Pair.of(2, "b"))).isAtMost(-1);
        assertThat(Pair.<Integer, String>comparator().compare(Pair.of(1, "a"), Pair.of(2, "a"))).isAtMost(-1);
        assertThat(Pair.<Integer, String>comparator().compare(Pair.of(1, "a"), Pair.of(2, ""))).isAtMost(-1);

        assertThat(Pair.<Integer, String>comparator().compare(Pair.of(1, "a"), Pair.of(1, "b"))).isAtMost(-1);
        assertThat(Pair.<Integer, String>comparator().compare(Pair.of(1, "a"), Pair.of(1, "a"))).isEqualTo(0);
        assertThat(Pair.<Integer, String>comparator().compare(Pair.of(1, "a"), Pair.of(1, ""))).isAtLeast(1);

        assertThat(Pair.<Integer, String>comparator().compare(Pair.of(1, "a"), Pair.of(0, "b"))).isAtLeast(1);
        assertThat(Pair.<Integer, String>comparator().compare(Pair.of(1, "a"), Pair.of(0, "a"))).isAtLeast(1);
        assertThat(Pair.<Integer, String>comparator().compare(Pair.of(1, "a"), Pair.of(0, ""))).isAtLeast(1);
    }

    @Test
    public void pair_compare_of_comparator() {
        Comparator<Integer> absCmp = Comparator.comparingInt(Math::abs);
        Comparator<String> lenCmp = Comparator.comparingInt(String::length);

        assertThat(Pair.comparator(absCmp, lenCmp).compare(Pair.of(1, "a"), Pair.of(1, "a"))).isEqualTo(0);
        assertThat(Pair.comparator(absCmp, lenCmp).compare(Pair.of(1, "a"), Pair.of(-1, "b"))).isEqualTo(0);
        assertThat(Pair.comparator(absCmp, lenCmp).compare(Pair.of(-1, "a"), Pair.of(1, "b"))).isEqualTo(0);
        assertThat(Pair.comparator(absCmp, lenCmp).compare(Pair.of(-1, "a"), Pair.of(-1, "b"))).isEqualTo(0);

        assertThat(Pair.comparator(absCmp, lenCmp).compare(Pair.of(1, "aaa"), Pair.of(1, "b"))).isEqualTo(1);
        assertThat(Pair.comparator(absCmp, lenCmp).compare(Pair.of(1, "aaa"), Pair.of(-1, "bb"))).isEqualTo(1);
        assertThat(Pair.comparator(absCmp, lenCmp).compare(Pair.of(-1, "aaa"), Pair.of(1, "bb"))).isEqualTo(1);
        assertThat(Pair.comparator(absCmp, lenCmp).compare(Pair.of(1, "aaa"), Pair.of(0, "ccc"))).isEqualTo(1);

        assertThat(Pair.comparator(absCmp, lenCmp).compare(Pair.of(1, "aaa"), Pair.of(2, "b"))).isEqualTo(-1);
        assertThat(Pair.comparator(absCmp, lenCmp).compare(Pair.of(1, "aaa"), Pair.of(2, "bbb"))).isEqualTo(-1);
        assertThat(Pair.comparator(absCmp, lenCmp).compare(Pair.of(1, "aaa"), Pair.of(-2, "bbb"))).isEqualTo(-1);
        assertThat(Pair.comparator(absCmp, lenCmp).compare(Pair.of(1, "aaa"), Pair.of(-1, "bbb+"))).isEqualTo(-1);
    }

    @Test
    public void toPair_simple() {
        assertThat(streamOf(1, 2).collect(Pair.toPair())).isEqualTo(Pair.of(1, 2));
        assertThrows(IllegalStateException.class, () -> Stream.<Integer>of().collect(Pair.toPair()));
        assertThrows(IllegalStateException.class, () -> streamOf(1).collect(Pair.toPair()));
        assertThrows(IllegalStateException.class, () -> streamOf(1, 2, 3).collect(Pair.toPair()));

        assertThat(intStreamOf(1, 2).boxed().collect(Pair.toPair())).isEqualTo(Pair.of(1, 2));
        assertThrows(IllegalStateException.class, () -> intStreamOf().boxed().collect(Pair.toPair()));
        assertThrows(IllegalStateException.class, () -> intStreamOf(1).boxed().collect(Pair.toPair()));
        assertThrows(IllegalStateException.class, () -> intStreamOf(1, 2, 3).boxed().collect(Pair.toPair()));
    }

    @Test
    public void toPairSparse_simple() {
        assertThat(Stream.<Integer>of().collect(Pair.toPairSparse())).isEqualTo(Pair.of(null, null));
        assertThat(streamOf(1).collect(Pair.toPairSparse())).isEqualTo(Pair.of(1, null));
        assertThat(streamOf(1, 2).collect(Pair.toPairSparse())).isEqualTo(Pair.of(1, 2));
        assertThrows(IllegalStateException.class, () -> streamOf(1, 2, 3).collect(Pair.toPairSparse()));

        assertThat(intStreamOf().boxed().collect(Pair.toPairSparse())).isEqualTo(Pair.of(null, null));
        assertThat(intStreamOf(1).boxed().collect(Pair.toPairSparse())).isEqualTo(Pair.of(1, null));
        assertThat(intStreamOf(1, 2).boxed().collect(Pair.toPairSparse())).isEqualTo(Pair.of(1, 2));
        assertThrows(IllegalStateException.class, () -> intStreamOf(1, 2, 3).boxed().collect(Pair.toPairSparse()));
    }
}
