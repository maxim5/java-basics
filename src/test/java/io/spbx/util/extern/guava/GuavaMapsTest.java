package io.spbx.util.extern.guava;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.arrayOf;
import static io.spbx.util.testing.TestingBasics.mapOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GuavaMapsTest {
    @Test
    public void immutableMapOf_simple() {
        assertThat(GuavaMaps.immutableMapOf()).containsExactly();
        assertThat(GuavaMaps.immutableMapOf("a", "b")).containsExactly("a", "b").inOrder();
        assertThat(GuavaMaps.immutableMapOf("a", "b", "c", "b")).containsExactly("a", "b", "c", "b").inOrder();
        assertThat(GuavaMaps.immutableMapOf(1, 2, 3, 4)).containsExactly(1, 2, 3, 4).inOrder();
        assertThat(GuavaMaps.immutableMapOf(1, 2, 3, 4, 5, 6)).containsExactly(1, 2, 3, 4, 5, 6).inOrder();
        assertThat(GuavaMaps.immutableMapOf(1, 2, 3, 4, 5, 6, 7, 8)).containsExactly(1, 2, 3, 4, 5, 6, 7, 8).inOrder();
    }

    @Test
    @SuppressWarnings("ConfusingArgumentToVarargsMethod")
    public void immutableMapOf_vararg() {
        assertThat(GuavaMaps.immutableMapOf(1, 2, arrayOf())).containsExactly(1, 2).inOrder();
        assertThat(GuavaMaps.immutableMapOf(1, 2, arrayOf(3, 4, 5, 6))).containsExactly(1, 2, 3, 4, 5, 6).inOrder();
        assertThat(GuavaMaps.immutableMapOf(1, 2, arrayOf(3, 2))).containsExactly(1, 2, 3, 2).inOrder();
    }

    @Test
    @SuppressWarnings("ConfusingArgumentToVarargsMethod")
    public void immutableMapOf_invalid() {
        assertThrows(AssertionError.class, () -> GuavaMaps.immutableMapOf("foo", "bar", "baz"));
        assertThrows(AssertionError.class, () -> GuavaMaps.immutableMapOf("foo", "bar", arrayOf("baz")));
        assertThrows(IllegalArgumentException.class, () -> GuavaMaps.immutableMapOf("foo", "bar", "foo", "bar"));
        assertThrows(IllegalArgumentException.class, () -> GuavaMaps.immutableMapOf("foo", "bar", arrayOf("foo", "bar")));
    }

    @Test
    public void mergeToImmutableMap_two_simple() {
        assertThat(GuavaMaps.mergeToImmutableMap(mapOf(), mapOf())).isEmpty();
        assertThat(GuavaMaps.mergeToImmutableMap(mapOf("foo", "bar"), mapOf())).containsExactly("foo", "bar").inOrder();
        assertThat(GuavaMaps.mergeToImmutableMap(mapOf(), mapOf("foo", "bar"))).containsExactly("foo", "bar").inOrder();

        assertThat(GuavaMaps.mergeToImmutableMap(mapOf(1, 2), mapOf(2, 3))).containsExactly(1, 2, 2, 3).inOrder();
        assertThat(GuavaMaps.mergeToImmutableMap(mapOf(1, 2), mapOf(2, 1))).containsExactly(1, 2, 2, 1).inOrder();

        assertThrows(IllegalArgumentException.class,
                     () -> GuavaMaps.mergeToImmutableMap(mapOf("foo", "bar"), mapOf("foo", "baz")));
        assertThrows(IllegalArgumentException.class,
                     () -> GuavaMaps.mergeToImmutableMap(mapOf(1, 2, 3, 4), mapOf(1, 5, 3, 5)));
        assertThrows(IllegalArgumentException.class,
                     () -> GuavaMaps.mergeToImmutableMap(mapOf(1, 2, 3, 4, 5, 6), mapOf(1, 5, 3, 5)));
    }

    @Test
    public void mergeToImmutableMap_two_nulls() {
        assertThat(GuavaMaps.mergeToImmutableMap(mapOf("foo", "bar"), null)).containsExactly("foo", "bar").inOrder();
        assertThat(GuavaMaps.mergeToImmutableMap(null, mapOf("foo", "bar"))).containsExactly("foo", "bar").inOrder();
        assertThat(GuavaMaps.mergeToImmutableMap(null, null)).isEmpty();
    }

    @Test
    public void mergeToImmutableMap_three_simple() {
        assertThat(GuavaMaps.mergeToImmutableMap(mapOf(), mapOf(), mapOf())).isEmpty();
        assertThat(GuavaMaps.mergeToImmutableMap(mapOf("foo", "bar"), mapOf(), mapOf())).containsExactly("foo", "bar").inOrder();
        assertThat(GuavaMaps.mergeToImmutableMap(mapOf(), mapOf("foo", "bar"), mapOf())).containsExactly("foo", "bar").inOrder();

        assertThat(GuavaMaps.mergeToImmutableMap(mapOf(1, 2), mapOf(3, 4), mapOf(5, 6))).containsExactly(1, 2, 3, 4, 5, 6).inOrder();
        assertThat(GuavaMaps.mergeToImmutableMap(mapOf(1, 2), mapOf(2, 3), mapOf(3, 4))).containsExactly(1, 2, 2, 3, 3, 4).inOrder();

        assertThrows(IllegalArgumentException.class,
                     () -> GuavaMaps.mergeToImmutableMap(mapOf(1, 2), mapOf(1, 3), mapOf(1, 4)));
        assertThrows(IllegalArgumentException.class,
                     () -> GuavaMaps.mergeToImmutableMap(mapOf(1, 2), mapOf(1, 5), mapOf(1, 6, 3, 6)));
        assertThrows(IllegalArgumentException.class,
                     () -> GuavaMaps.mergeToImmutableMap(mapOf(1, 2, 3, 4), mapOf(7, 5), mapOf(3, 8)));
    }

    @Test
    public void mergeToImmutableMap_three_nulls() {
        assertThat(GuavaMaps.mergeToImmutableMap(mapOf("foo", "bar"), null, null)).containsExactly("foo", "bar").inOrder();
        assertThat(GuavaMaps.mergeToImmutableMap(null, mapOf("foo", "bar"), null)).containsExactly("foo", "bar").inOrder();
        assertThat(GuavaMaps.mergeToImmutableMap(null, null, mapOf("foo", "bar"))).containsExactly("foo", "bar").inOrder();
        assertThat(GuavaMaps.mergeToImmutableMap(null, null, null)).isEmpty();
    }
}
