package io.spbx.util.base.ops;

import com.google.common.primitives.Ints;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.ScopeFunctions.also;
import static io.spbx.util.testing.AssertReverse.assertRoundtrip;
import static io.spbx.util.testing.TestingPrimitives.ints;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("fast")
public class IntOpsTest {
    /** {@link IntOps#range} **/

    @Test
    public void range_of_one() {
        assertThat(IntOps.range(0)).asList().isEmpty();

        assertThat(IntOps.range(1)).asList().containsExactly(0).inOrder();
        assertThat(IntOps.range(2)).asList().containsExactly(0, 1).inOrder();
        assertThat(IntOps.range(3)).asList().containsExactly(0, 1, 2).inOrder();

        assertThat(IntOps.range(-1)).asList().containsExactly(0).inOrder();
        assertThat(IntOps.range(-2)).asList().containsExactly(0, -1).inOrder();
        assertThat(IntOps.range(-3)).asList().containsExactly(0, -1, -2).inOrder();
    }

    @Test
    public void range_of_two() {
        assertThat(IntOps.range(0, 0)).asList().isEmpty();
        assertThat(IntOps.range(1, 1)).asList().isEmpty();
        assertThat(IntOps.range(2, 2)).asList().isEmpty();

        assertThat(IntOps.range(0, 1)).asList().containsExactly(0).inOrder();
        assertThat(IntOps.range(0, 2)).asList().containsExactly(0, 1).inOrder();
        assertThat(IntOps.range(0, 3)).asList().containsExactly(0, 1, 2).inOrder();

        assertThat(IntOps.range(1, 0)).asList().containsExactly(1).inOrder();
        assertThat(IntOps.range(2, 0)).asList().containsExactly(2, 1).inOrder();
        assertThat(IntOps.range(3, 0)).asList().containsExactly(3, 2, 1).inOrder();

        assertThat(IntOps.range(0, -1)).asList().containsExactly(0).inOrder();
        assertThat(IntOps.range(0, -2)).asList().containsExactly(0, -1).inOrder();
        assertThat(IntOps.range(0, -3)).asList().containsExactly(0, -1, -2).inOrder();

        assertThat(IntOps.range(-1, 0)).asList().containsExactly(-1).inOrder();
        assertThat(IntOps.range(-2, 0)).asList().containsExactly(-2, -1).inOrder();
        assertThat(IntOps.range(-3, 0)).asList().containsExactly(-3, -2, -1).inOrder();
    }

    /** {@link IntOps#reverse(int[])} **/

    @Test
    public void reverse_simple() {
        assertThat(IntOps.reverse(ints())).asList().isEmpty();
        assertThat(IntOps.reverse(ints(1))).asList().containsExactly(1).inOrder();
        assertThat(IntOps.reverse(ints(1, 2))).asList().containsExactly(2, 1).inOrder();
        assertThat(IntOps.reverse(ints(1, 2, 3))).asList().containsExactly(3, 2, 1).inOrder();
        assertThat(IntOps.reverse(ints(1, 2, 3, 4))).asList().containsExactly(4, 3, 2, 1).inOrder();
    }

    /** {@link IntOps#fill} **/

    @Test
    public void fill_simple() {
        assertThat(IntOps.fill(new int[0], 1)).asList().isEmpty();
        assertThat(IntOps.fill(new int[1], 1)).asList().containsExactly(1).inOrder();
        assertThat(IntOps.fill(new int[2], 1)).asList().containsExactly(1, 1).inOrder();

        assertThat(IntOps.fill(0, 1)).asList().isEmpty();
        assertThat(IntOps.fill(1, 1)).asList().containsExactly(1).inOrder();
        assertThat(IntOps.fill(2, 1)).asList().containsExactly(1, 1).inOrder();

        assertThat(IntOps.fill(new int[0], i -> i)).asList().isEmpty();
        assertThat(IntOps.fill(new int[1], i -> i)).asList().containsExactly(0).inOrder();
        assertThat(IntOps.fill(new int[2], i -> i)).asList().containsExactly(0, 1).inOrder();

        assertThat(IntOps.fill(0, i -> i)).asList().isEmpty();
        assertThat(IntOps.fill(1, i -> i)).asList().containsExactly(0).inOrder();
        assertThat(IntOps.fill(2, i -> i)).asList().containsExactly(0, 1).inOrder();
    }

    /** {@link IntOps#map} **/

    @Test
    public void map_unary_simple() {
        assertThat(IntOps.map(ints(), a -> a + 1)).asList().isEmpty();
        assertThat(IntOps.map(ints(1), a -> a + 1)).asList().containsExactly(2);
        assertThat(IntOps.map(ints(1, 2), a -> a + 1)).asList().containsExactly(2, 3);
    }

    @Test
    public void map_binary_simple() {
        assertThat(IntOps.map(ints(), (a, i) -> a * i)).asList().isEmpty();
        assertThat(IntOps.map(ints(1), (a, i) -> a * i)).asList().containsExactly(0);
        assertThat(IntOps.map(ints(1, 2, 3), (a, i) -> a * i)).asList().containsExactly(0, 2, 6);
    }

    /** {@link IntOps#mapInPlace} **/

    @Test
    public void mapInPlace_unary_simple() {
        assertThat(IntOps.mapInPlace(ints(), a -> a + 1)).asList().isEmpty();
        assertThat(IntOps.mapInPlace(ints(1), a -> a + 1)).asList().containsExactly(2);
        assertThat(IntOps.mapInPlace(ints(1, 2), a -> a + 1)).asList().containsExactly(2, 3);
    }

    @Test
    public void mapInPlace_binary_simple() {
        assertThat(IntOps.mapInPlace(ints(), (a, i) -> a * i)).asList().isEmpty();
        assertThat(IntOps.mapInPlace(ints(1), (a, i) -> a * i)).asList().containsExactly(0);
        assertThat(IntOps.mapInPlace(ints(1, 2, 3), (a, i) -> a * i)).asList().containsExactly(0, 2, 6);
    }

    /** {@link IntOps#filter} **/

    @Test
    public void filter_unary_simple() {
        assertThat(IntOps.filter(ints(), a -> a > 1)).asList().isEmpty();
        assertThat(IntOps.filter(ints(1), a -> a > 1)).asList().isEmpty();
        assertThat(IntOps.filter(ints(2), a -> a > 1)).asList().containsExactly(2);
        assertThat(IntOps.filter(ints(1, 2), a -> a > 1)).asList().containsExactly(2);
        assertThat(IntOps.filter(ints(3, 1, 2), a -> a > 1)).asList().containsExactly(3, 2);
    }

    @Test
    public void filter_binary_simple() {
        assertThat(IntOps.filter(ints(), (a, i) -> a > i)).asList().isEmpty();
        assertThat(IntOps.filter(ints(0), (a, i) -> a > i)).asList().isEmpty();
        assertThat(IntOps.filter(ints(1), (a, i) -> a > i)).asList().containsExactly(1);
        assertThat(IntOps.filter(ints(1, 1), (a, i) -> a > i)).asList().containsExactly(1);
        assertThat(IntOps.filter(ints(1, 2), (a, i) -> a > i)).asList().containsExactly(1, 2);
    }

    /** {@link IntOps#filterInPlace} **/

    @Test
    public void filterInPlace_unary_simple() {
        also(ints(), array -> {
            int n = IntOps.filterInPlace(array, a -> a > 1);
            assertThat(n).isEqualTo(0);
            assertThat(IntOps.realloc(array, n)).asList().isEmpty();
        });
        also(ints(1), array -> {
            int n = IntOps.filterInPlace(array, a -> a > 1);
            assertThat(n).isEqualTo(0);
            assertThat(IntOps.realloc(array, n)).asList().isEmpty();
        });
        also(ints(0, 1), array -> {
            int n = IntOps.filterInPlace(array, a -> a > 1);
            assertThat(n).isEqualTo(0);
            assertThat(IntOps.realloc(array, n)).asList().isEmpty();
        });
        also(ints(2), array -> {
            int n = IntOps.filterInPlace(array, a -> a > 1);
            assertThat(n).isEqualTo(1);
            assertThat(IntOps.realloc(array, n)).asList().containsExactly(2);
        });
        also(ints(1, 2), array -> {
            int n = IntOps.filterInPlace(array, a -> a > 1);
            assertThat(n).isEqualTo(1);
            assertThat(IntOps.realloc(array, n)).asList().containsExactly(2);
        });
        also(ints(0, 1, 2, 3), array -> {
            int n = IntOps.filterInPlace(array, a -> a > 1);
            assertThat(n).isEqualTo(2);
            assertThat(IntOps.realloc(array, n)).asList().containsExactly(2, 3);
        });
        also(ints(0, 1, 2, 3, 1, 2), array -> {
            int n = IntOps.filterInPlace(array, a -> a > 1);
            assertThat(n).isEqualTo(3);
            assertThat(IntOps.realloc(array, n)).asList().containsExactly(2, 3, 2);
        });
        also(ints(3, 2, 1, 0, 1, 2, 1, 3, 2), array -> {
            int n = IntOps.filterInPlace(array, a -> a > 1);
            assertThat(n).isEqualTo(5);
            assertThat(IntOps.realloc(array, n)).asList().containsExactly(3, 2, 2, 3, 2);
        });
    }

    @Test
    public void filterInPlace_binary_simple() {
        also(ints(), array -> {
            int n = IntOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(0);
            assertThat(IntOps.realloc(array, n)).asList().isEmpty();
        });
        also(ints(0), array -> {
            int n = IntOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(0);
            assertThat(IntOps.realloc(array, n)).asList().isEmpty();
        });
        also(ints(0, 1), array -> {
            int n = IntOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(0);
            assertThat(IntOps.realloc(array, n)).asList().isEmpty();
        });
        also(ints(2), array -> {
            int n = IntOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(1);
            assertThat(IntOps.realloc(array, n)).asList().containsExactly(2);
        });
        also(ints(0, 2), array -> {
            int n = IntOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(1);
            assertThat(IntOps.realloc(array, n)).asList().containsExactly(2);
        });
        also(ints(1, 0, 3, 2), array -> {
            int n = IntOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(2);
            assertThat(IntOps.realloc(array, n)).asList().containsExactly(1, 3);
        });
        also(ints(0, 1, 3, 4, 1, 6), array -> {
            int n = IntOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(3);
            assertThat(IntOps.realloc(array, n)).asList().containsExactly(3, 4, 6);
        });
        also(ints(1, 2, 0, 4, 0, 6, 0, 0), array -> {
            int n = IntOps.filterInPlace(array, (a, i) -> a > i);
            assertThat(n).isEqualTo(4);
            assertThat(IntOps.realloc(array, n)).asList().containsExactly(1, 2, 4, 6);
        });
    }

    /** {@link IntOps#indexOf} **/

    @Test
    public void indexOf_predicate_simple() {
        assertThat(IntOps.indexOf(ints(), i -> i == 0)).isEqualTo(-1);

        assertThat(IntOps.indexOf(ints(0), i -> i == 0)).isEqualTo(0);
        assertThat(IntOps.indexOf(ints(0), i -> i == 777)).isEqualTo(-1);

        assertThat(IntOps.indexOf(ints(0, 0, 0), i -> i == 0)).isEqualTo(0);
        assertThat(IntOps.indexOf(ints(0, 0, 0), i -> i == 777)).isEqualTo(-1);

        assertThat(IntOps.indexOf(ints(0, 1), i -> i == 0)).isEqualTo(0);
        assertThat(IntOps.indexOf(ints(0, 1), i -> i == 1)).isEqualTo(1);
        assertThat(IntOps.indexOf(ints(0, 1), i -> i == 777)).isEqualTo(-1);

        assertThat(IntOps.indexOf(ints(0, 1, 2, 3), i -> i == 0)).isEqualTo(0);
        assertThat(IntOps.indexOf(ints(0, 1, 2, 3), i -> i == 1)).isEqualTo(1);
        assertThat(IntOps.indexOf(ints(0, 1, 2, 3), i -> i == 2)).isEqualTo(2);
        assertThat(IntOps.indexOf(ints(0, 1, 2, 3), i -> i == 777)).isEqualTo(-1);

        assertThat(IntOps.indexOf(ints(0, 1, 2, 3, 0), i -> i == 0)).isEqualTo(0);
    }

    @Test
    public void indexOf_value_simple() {
        assertThat(IntOps.indexOf(ints(), 0)).isEqualTo(-1);

        assertThat(IntOps.indexOf(ints(0), 0)).isEqualTo(0);
        assertThat(IntOps.indexOf(ints(0), 777)).isEqualTo(-1);

        assertThat(IntOps.indexOf(ints(0, 0, 0), 0)).isEqualTo(0);
        assertThat(IntOps.indexOf(ints(0, 0, 0), 777)).isEqualTo(-1);

        assertThat(IntOps.indexOf(ints(0, 1), 0)).isEqualTo(0);
        assertThat(IntOps.indexOf(ints(0, 1), 1)).isEqualTo(1);
        assertThat(IntOps.indexOf(ints(0, 1), 777)).isEqualTo(-1);

        assertThat(IntOps.indexOf(ints(0, 1, 2, 3), 0)).isEqualTo(0);
        assertThat(IntOps.indexOf(ints(0, 1, 2, 3), 1)).isEqualTo(1);
        assertThat(IntOps.indexOf(ints(0, 1, 2, 3), 2)).isEqualTo(2);
        assertThat(IntOps.indexOf(ints(0, 1, 2, 3), 777)).isEqualTo(-1);

        assertThat(IntOps.indexOf(ints(0, 1, 2, 3, 0), 0)).isEqualTo(0);
    }

    @Test
    public void indexOf_predicate_subarray() {
        assertThat(IntOps.indexOf(ints(1, 2, 3), i -> i == 0, 0, -1, -777)).isEqualTo(-777);
        assertThat(IntOps.indexOf(ints(1, 2, 0), i -> i == 0, 0, -1, -777)).isEqualTo(-777);
        assertThat(IntOps.indexOf(ints(1, 0, 0), i -> i == 0, 0, -1, -777)).isEqualTo(1);

        assertThat(IntOps.indexOf(ints(1, 2, 3), i -> i == 0, -2, -1, -777)).isEqualTo(-777);
        assertThat(IntOps.indexOf(ints(1, 0, 3), i -> i == 0, -2, -1, -777)).isEqualTo(1);
        assertThat(IntOps.indexOf(ints(0, 2, 3), i -> i == 0, -2, -1, -777)).isEqualTo(-777);

        assertThrows(AssertionError.class, () -> IntOps.indexOf(ints(1, 2, 3), i -> i == 0, -1, 0, -777));
        assertThrows(AssertionError.class, () -> IntOps.indexOf(ints(1, 2, 3), i -> i == 0, -1, -2, -777));
        assertThrows(AssertionError.class, () -> IntOps.indexOf(ints(1, 2, 3), i -> i == 0, 0, 0, 0));
    }

    /** {@link IntOps#lastIndexOf} **/

    @Test
    public void lastIndexOf_predicate_simple() {
        assertThat(IntOps.lastIndexOf(ints(), i -> i == 0)).isEqualTo(-1);

        assertThat(IntOps.lastIndexOf(ints(0), i -> i == 0)).isEqualTo(0);
        assertThat(IntOps.lastIndexOf(ints(0), i -> i == 777)).isEqualTo(-1);

        assertThat(IntOps.lastIndexOf(ints(0, 0, 0), i -> i == 0)).isEqualTo(2);
        assertThat(IntOps.lastIndexOf(ints(0, 0, 0), i -> i == 777)).isEqualTo(-1);

        assertThat(IntOps.lastIndexOf(ints(0, 1), i -> i == 0)).isEqualTo(0);
        assertThat(IntOps.lastIndexOf(ints(0, 1), i -> i == 1)).isEqualTo(1);
        assertThat(IntOps.lastIndexOf(ints(0, 1), i -> i == 777)).isEqualTo(-1);

        assertThat(IntOps.lastIndexOf(ints(0, 1, 2, 3), i -> i == 0)).isEqualTo(0);
        assertThat(IntOps.lastIndexOf(ints(0, 1, 2, 3), i -> i == 1)).isEqualTo(1);
        assertThat(IntOps.lastIndexOf(ints(0, 1, 2, 3), i -> i == 2)).isEqualTo(2);
        assertThat(IntOps.lastIndexOf(ints(0, 1, 2, 3), i -> i == 777)).isEqualTo(-1);

        assertThat(IntOps.lastIndexOf(ints(0, 1, 2, 3, 0), i -> i == 0)).isEqualTo(4);
    }

    @Test
    public void lastIndexOf_value_simple() {
        assertThat(IntOps.lastIndexOf(ints(), 0)).isEqualTo(-1);

        assertThat(IntOps.lastIndexOf(ints(0), 0)).isEqualTo(0);
        assertThat(IntOps.lastIndexOf(ints(0), 777)).isEqualTo(-1);

        assertThat(IntOps.lastIndexOf(ints(0, 0, 0), 0)).isEqualTo(2);
        assertThat(IntOps.lastIndexOf(ints(0, 0, 0), 777)).isEqualTo(-1);

        assertThat(IntOps.lastIndexOf(ints(0, 1), 0)).isEqualTo(0);
        assertThat(IntOps.lastIndexOf(ints(0, 1), 1)).isEqualTo(1);
        assertThat(IntOps.lastIndexOf(ints(0, 1), 777)).isEqualTo(-1);

        assertThat(IntOps.lastIndexOf(ints(0, 1, 2, 3), 0)).isEqualTo(0);
        assertThat(IntOps.lastIndexOf(ints(0, 1, 2, 3), 1)).isEqualTo(1);
        assertThat(IntOps.lastIndexOf(ints(0, 1, 2, 3), 2)).isEqualTo(2);
        assertThat(IntOps.lastIndexOf(ints(0, 1, 2, 3), 777)).isEqualTo(-1);

        assertThat(IntOps.lastIndexOf(ints(0, 1, 2, 3, 0), 0)).isEqualTo(4);
    }

    @Test
    public void lastIndexOf_predicate_subarray() {
        assertThat(IntOps.lastIndexOf(ints(1, 2, 3), i -> i == 0, 0, -1, -777)).isEqualTo(-777);
        assertThat(IntOps.lastIndexOf(ints(1, 2, 0), i -> i == 0, 0, -1, -777)).isEqualTo(-777);
        assertThat(IntOps.lastIndexOf(ints(1, 0, 0), i -> i == 0, 0, -1, -777)).isEqualTo(1);

        assertThat(IntOps.lastIndexOf(ints(1, 2, 3), i -> i == 0, -2, -1, -777)).isEqualTo(-777);
        assertThat(IntOps.lastIndexOf(ints(1, 0, 3), i -> i == 0, -2, -1, -777)).isEqualTo(1);
        assertThat(IntOps.lastIndexOf(ints(0, 2, 3), i -> i == 0, -2, -1, -777)).isEqualTo(-777);

        assertThrows(AssertionError.class, () -> IntOps.lastIndexOf(ints(1, 2, 3), i -> i == 0, -1, 0, -777));
        assertThrows(AssertionError.class, () -> IntOps.lastIndexOf(ints(1, 2, 3), i -> i == 0, -1, -2, -777));
        assertThrows(AssertionError.class, () -> IntOps.lastIndexOf(ints(1, 2, 3), i -> i == 0, 0, 0, 0));
    }

    /** {@link IntOps#contains} **/

    @Test
    public void contains_value_simple() {
        assertThat(IntOps.contains(ints(), 0)).isFalse();
        assertThat(IntOps.contains(ints(1), 0)).isFalse();
        assertThat(IntOps.contains(ints(1, 2), 0)).isFalse();

        assertThat(IntOps.contains(ints(0), 0)).isTrue();
        assertThat(IntOps.contains(ints(0, 1), 0)).isTrue();
        assertThat(IntOps.contains(ints(0, 0, 0), 0)).isTrue();
    }

    @Test
    public void contains_predicate_simple() {
        assertThat(IntOps.contains(ints(), i -> i == 0)).isFalse();
        assertThat(IntOps.contains(ints(1), i -> i == 0)).isFalse();
        assertThat(IntOps.contains(ints(1, 2), i -> i == 0)).isFalse();

        assertThat(IntOps.contains(ints(0), i -> i == 0)).isTrue();
        assertThat(IntOps.contains(ints(0, 1), i -> i == 0)).isTrue();
        assertThat(IntOps.contains(ints(0, 0, 0), i -> i == 0)).isTrue();
    }

    /** {@link IntOps#firstNonNegative} **/

    @Test
    public void firstNonNegative_of_two() {
        assertThat(IntOps.firstNonNegative(1, 2)).isEqualTo(1);
        assertThat(IntOps.firstNonNegative(0, 1)).isEqualTo(0);
        assertThat(IntOps.firstNonNegative(0, -1)).isEqualTo(0);
        assertThat(IntOps.firstNonNegative(-1, 0)).isEqualTo(0);
        assertThrows(IllegalArgumentException.class, () -> IntOps.firstNonNegative(-1, -2));
    }

    @Test
    public void firstNonNegative_of_three() {
        assertThat(IntOps.firstNonNegative(1, 2, 3)).isEqualTo(1);
        assertThat(IntOps.firstNonNegative(0, 1, 2)).isEqualTo(0);
        assertThat(IntOps.firstNonNegative(0, -1, -2)).isEqualTo(0);
        assertThat(IntOps.firstNonNegative(-1, 0, 1)).isEqualTo(0);
        assertThat(IntOps.firstNonNegative(-1, 0, -2)).isEqualTo(0);
        assertThat(IntOps.firstNonNegative(-1, -2, 0)).isEqualTo(0);
        assertThrows(IllegalArgumentException.class, () -> IntOps.firstNonNegative(-1, -2, -1));
    }

    @Test
    public void firstNonNegative_of_vararg() {
        assertThat(IntOps.firstNonNegative(1, 2, 3, 4, 5)).isEqualTo(1);
        assertThat(IntOps.firstNonNegative(0, 1, 2, 0, 1)).isEqualTo(0);
        assertThat(IntOps.firstNonNegative(0, -1, -2, -3, -4)).isEqualTo(0);
        assertThat(IntOps.firstNonNegative(-1, 0, -2, -3, -4)).isEqualTo(0);
        assertThat(IntOps.firstNonNegative(-1, -2, 0, -3, -4)).isEqualTo(0);
        assertThat(IntOps.firstNonNegative(-1, -2, -3, 0, -4)).isEqualTo(0);
        assertThat(IntOps.firstNonNegative(-1, -2, -3, -4, 0)).isEqualTo(0);
        assertThrows(IllegalArgumentException.class, () -> IntOps.firstNonNegative(-1, -2, -1, -2, -3));
    }

    /** {@link IntOps#requirePositive(int)} **/

    @Test
    public void requirePositive_simple() {
        assertThat(IntOps.requirePositive(1)).isEqualTo(1);
        assertThat(IntOps.requirePositive(Integer.MAX_VALUE)).isEqualTo(Integer.MAX_VALUE);
        assertThrows(IllegalArgumentException.class, () -> IntOps.requirePositive(0));
        assertThrows(IllegalArgumentException.class, () -> IntOps.requirePositive(-1));
    }

    /** {@link IntOps#requireNonNegative(int)} **/

    @Test
    public void requireNonNegative_simple() {
        assertThat(IntOps.requireNonNegative(0)).isEqualTo(0);
        assertThat(IntOps.requireNonNegative(1)).isEqualTo(1);
        assertThat(IntOps.requireNonNegative(Integer.MAX_VALUE)).isEqualTo(Integer.MAX_VALUE);
        assertThrows(IllegalArgumentException.class, () -> IntOps.requirePositive(-1));
    }

    /** {@link IntOps#concat(int[], int[])} **/

    @Test
    public void concat_simple() {
        assertThat(IntOps.concat(ints(), ints())).asList().isEmpty();
        assertThat(IntOps.concat(ints(1), ints())).asList().containsExactly(1);
        assertThat(IntOps.concat(ints(), ints(2))).asList().containsExactly(2);
        assertThat(IntOps.concat(ints(1), ints(2))).asList().containsExactly(1, 2);
        assertThat(IntOps.concat(ints(1, 2, 3), ints())).asList().containsExactly(1, 2, 3);
        assertThat(IntOps.concat(ints(), ints(4, 5, 6))).asList().containsExactly(4, 5, 6);
        assertThat(IntOps.concat(ints(1, 2, 3), ints(4, 5))).asList().containsExactly(1, 2, 3, 4, 5);

    }

    /** {@link IntOps#append(int[], int)} **/

    @Test
    public void append_simple() {
        assertThat(IntOps.append(ints(), 7)).asList().containsExactly(7);
        assertThat(IntOps.append(ints(1), 7)).asList().containsExactly(1, 7);
        assertThat(IntOps.append(ints(1, 2), 7)).asList().containsExactly(1, 2, 7);
    }

    /** {@link IntOps#prepend(int, int[])} **/

    @Test
    public void prepend_simple() {
        assertThat(IntOps.prepend(7, ints())).asList().containsExactly(7);
        assertThat(IntOps.prepend(7, ints(1))).asList().containsExactly(7, 1);
        assertThat(IntOps.prepend(7, ints(1, 2))).asList().containsExactly(7, 1, 2);
    }

    /** {@link IntOps#slice} **/

    @Test
    public void slice_simple() {
        assertThat(IntOps.slice(ints(), 0, 0)).asList().isEmpty();
        assertThat(IntOps.slice(ints(1), 0, 0)).asList().isEmpty();
        assertThat(IntOps.slice(ints(1), 1, 1)).asList().isEmpty();
        assertThat(IntOps.slice(ints(1), 0, 1)).asList().containsExactly(1);

        assertThat(IntOps.slice(ints(1, 2, 3), 1, 2)).asList().containsExactly(2);
        assertThat(IntOps.slice(ints(1, 2, 3), 1, 3)).asList().containsExactly(2, 3);
        assertThat(IntOps.slice(ints(1, 2, 3), 0, 2)).asList().containsExactly(1, 2);
        assertThat(IntOps.slice(ints(1, 2, 3), 0, 1)).asList().containsExactly(1);

        assertThat(IntOps.slice(ints(1, 2, 3), 0)).asList().containsExactly(1, 2, 3);
        assertThat(IntOps.slice(ints(1, 2, 3), 1)).asList().containsExactly(2, 3);
        assertThat(IntOps.slice(ints(1, 2, 3), 2)).asList().containsExactly(3);
        assertThat(IntOps.slice(ints(1, 2, 3), 3)).asList().isEmpty();
    }

    /** {@link IntOps#realloc(int[], int)} **/

    @Test
    public void realloc_simple() {
        assertThat(IntOps.realloc(ints(), 0)).asList().isEmpty();
        assertThat(IntOps.realloc(ints(), 1)).asList().containsExactly(0);

        assertThat(IntOps.realloc(ints(1, 2, 3), 1)).asList().containsExactly(1);
        assertThat(IntOps.realloc(ints(1, 2, 3), 2)).asList().containsExactly(1, 2);
        assertThat(IntOps.realloc(ints(1, 2, 3), 3)).asList().containsExactly(1, 2, 3);
        assertThat(IntOps.realloc(ints(1, 2, 3), 4)).asList().containsExactly(1, 2, 3, 0);
        assertThat(IntOps.realloc(ints(1, 2, 3), 5)).asList().containsExactly(1, 2, 3, 0, 0);
    }

    /** {@link IntOps#ensureCapacity(int[], int)} **/

    @Test
    public void ensureCapacity_simple() {
        assertThat(IntOps.ensureCapacity(ints(), 0)).asList().isEmpty();
        assertThat(IntOps.ensureCapacity(ints(), 1)).asList().containsExactly(0);

        assertThat(IntOps.ensureCapacity(ints(1, 2, 3), 1)).asList().containsExactly(1, 2, 3);
        assertThat(IntOps.ensureCapacity(ints(1, 2, 3), 2)).asList().containsExactly(1, 2, 3);
        assertThat(IntOps.ensureCapacity(ints(1, 2, 3), 3)).asList().containsExactly(1, 2, 3);
        assertThat(IntOps.ensureCapacity(ints(1, 2, 3), 4)).asList().containsExactly(1, 2, 3, 0);
        assertThat(IntOps.ensureCapacity(ints(1, 2, 3), 5)).asList().containsExactly(1, 2, 3, 0, 0);
    }

    /** {@link IntOps#toBigEndianBytes} {@link IntOps#fromBigEndianBytes} {@link IntOps#valueOfBigEndianBytes} **/

    private static final int[] EDGE_CASE_INTS = {
        0, 1, -1, 1 << 15, -1 << 15, 1 << 31, -1 << 31, Integer.MIN_VALUE, Integer.MAX_VALUE
    };

    @Test
    public void toBigEndianBytes_ultimate() {
        for (int value : EDGE_CASE_INTS) {
            byte[] expected = Ints.toByteArray(value);
            assertThat(IntOps.toBigEndianBytes(new int[] { value })).isEqualTo(expected);
            assertThat(IntOps.toBigEndianBytes(value)).isEqualTo(expected);
            assertThat(IntOps.toBigEndianBytes(value, new byte[4])).isEqualTo(expected);
            assertThat(IntOps.toBigEndianBytes(value, new byte[4], 0)).isEqualTo(expected);
            assertThat(IntOps.toBigEndianBytes(value, new byte[5], 0)).isEqualTo(ByteOps.append(expected, (byte) 0));
            assertThat(IntOps.toBigEndianBytes(value, new byte[5], 1)).isEqualTo(ByteOps.prepend((byte) 0, expected));
        }
    }

    @Test
    public void fromBigEndianBytes_ultimate() {
        for (int value : EDGE_CASE_INTS) {
            byte[] bytes = Ints.toByteArray(value);
            assertThat(IntOps.fromBigEndianBytes(new byte[0])).asList().isEmpty();
            assertThat(IntOps.fromBigEndianBytes(bytes)).asList().containsExactly(value);
            assertThat(IntOps.fromBigEndianBytes(ByteOps.concat(bytes, bytes))).asList().containsExactly(value, value);
        }
    }

    @Test
    public void valueOfBigEndianBytes_ultimate() {
        for (int value : EDGE_CASE_INTS) {
            byte[] bytes = Ints.toByteArray(value);
            assertThat(IntOps.valueOfBigEndianBytes(bytes)).isEqualTo(value);
            assertThat(IntOps.valueOfBigEndianBytes(bytes, 0)).isEqualTo(value);
            assertThat(IntOps.valueOfBigEndianBytes(ByteOps.append(bytes, (byte) 77), 0)).isEqualTo(value);
            assertThat(IntOps.valueOfBigEndianBytes(ByteOps.prepend((byte) 77, bytes), 1)).isEqualTo(value);
            assertThat(IntOps.valueOfBigEndianBytes(bytes[0], bytes[1], bytes[2], bytes[3])).isEqualTo(value);
        }
    }

    @Test
    public void toBigEndianBytes_valueOfBigEndianBytes_roundtrip_ultimate() {
        for (int value : EDGE_CASE_INTS) {
            assertRoundtrip(IntOps::toBigEndianBytes, IntOps::valueOfBigEndianBytes, value);
        }
    }

    /** {@link IntOps#avg(int, int)} **/

    @Test
    public void avg_simple() {
        assertThat(IntOps.avg(0, 1)).isEqualTo(0);
        assertThat(IntOps.avg(0, 2)).isEqualTo(1);
        assertThat(IntOps.avg(1, 2)).isEqualTo(1);
        assertThat(IntOps.avg(1, 3)).isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(ints = { 1 << 30, Integer.MAX_VALUE, Integer.MAX_VALUE - 1 })
    public void avg_edge_cases_positive(int val) {
        assertThat(IntOps.avg(val, val)).isEqualTo(val);
        assertThat(IntOps.avg(val - 1, val - 1)).isEqualTo(val - 1);
        assertThat(IntOps.avg(val - 1, val)).isEqualTo(val - 1);
        assertThat(IntOps.avg(val - 2, val)).isEqualTo(val - 1);
        assertThat(IntOps.avg(0, val)).isEqualTo(val >> 1);
        assertThat(IntOps.avg(-val, val)).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(ints = { -1 << 30, Integer.MIN_VALUE + 1 })
    public void avg_edge_cases_negative(int val) {
        assertThat(IntOps.avg(val, val)).isEqualTo(val);
        assertThat(IntOps.avg(val + 1, val + 1)).isEqualTo(val + 1);
        assertThat(IntOps.avg(val + 1, val)).isEqualTo(val);
        assertThat(IntOps.avg(val + 2, val)).isEqualTo(val + 1);
        assertThat(IntOps.avg(-val, val)).isEqualTo(0);
    }
}
