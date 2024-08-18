package io.spbx.util.collect;

import io.spbx.util.base.Pair;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.collect.BasicMaps.MapPutMethod.overwrite;
import static io.spbx.util.testing.TestingBasics.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BasicMapsTest {
    @Test
    public void toMapUnchecked_vararg_simple() {
        assertThat(BasicMaps.toMapUnchecked()).isEmpty();
        assertThat(BasicMaps.toMapUnchecked((Object[]) null)).isEmpty();
        assertThat(BasicMaps.toMapUnchecked("foo", "bar")).containsExactly("foo", "bar").inOrder();
        assertThat(BasicMaps.toMapUnchecked("foo", "bar", "baz", "bar")).containsExactly("foo", "bar", "baz", "bar").inOrder();
        assertThat(BasicMaps.toMapUnchecked("foo", null, "baz", null)).containsExactly("foo", null, "baz", null).inOrder();
        assertThat(BasicMaps.toMapUnchecked(null, "foo")).containsExactly(null, "foo").inOrder();
        assertThat(BasicMaps.toMapUnchecked(null, null)).containsExactly(null, null).inOrder();
    }

    @Test
    public void toMapUnchecked_list_simple() {
        assertThat(BasicMaps.toMapUnchecked(listOf())).isEmpty();
        assertThat(BasicMaps.toMapUnchecked(listOf("foo", "bar"))).containsExactly("foo", "bar").inOrder();
        assertThat(BasicMaps.toMapUnchecked(listOf(null, "foo"))).containsExactly(null, "foo").inOrder();
        assertThat(BasicMaps.toMapUnchecked(listOf(null, null))).containsExactly(null, null).inOrder();
    }

    @Test
    public void toMapUnchecked_invalid() {
        assertThrows(AssertionError.class, () -> BasicMaps.toMapUnchecked("foo"));
        assertThrows(AssertionError.class, () -> BasicMaps.toMapUnchecked("foo", "bar", "baz"));
        assertThrows(AssertionError.class, () -> BasicMaps.toMapUnchecked("foo", "bar", "foo", "bar"));
    }

    @Test
    public void toMapUncheckedUsing_overwrite() {
        assertThat(BasicMaps.toMapUncheckedUsing(listOf(), overwrite())).isEmpty();
        assertThat(BasicMaps.toMapUncheckedUsing(listOf(1, 2), overwrite())).containsExactly(1, 2).inOrder();
        assertThat(BasicMaps.toMapUncheckedUsing(listOf(1, 2, 1, 3), overwrite())).containsExactly(1, 3).inOrder();
        assertThat(BasicMaps.toMapUncheckedUsing(listOf(null, 2, 1, 3), overwrite())).containsExactly(null, 2, 1, 3).inOrder();
        assertThat(BasicMaps.toMapUncheckedUsing(listOf(null, 2, null, 3), overwrite())).containsExactly(null, 3).inOrder();
    }

    @Test
    public void indexBy_iterable_simple() {
        assertThat(BasicMaps.indexBy(listOf(), Integer::toHexString)).isEmpty();
        assertThat(BasicMaps.indexBy(setOf(), Integer::toHexString)).isEmpty();
        assertThat(BasicMaps.indexBy(iterableOf(), Integer::toHexString)).isEmpty();
        assertThat(BasicMaps.indexBy(listOf(1, 2), Integer::toHexString)).containsExactly("1", 1, "2", 2).inOrder();
        assertThat(BasicMaps.indexBy(iterableOf(1, 2), Integer::toHexString)).containsExactly("1", 1, "2", 2).inOrder();
        assertThat(BasicMaps.indexBy(listOf(Pair.of(1, 2)), Pair::first)).containsExactly(1, Pair.of(1, 2)).inOrder();
        assertThat(BasicMaps.indexBy(listOf(Pair.of(1, 2)), Pair::second)).containsExactly(2, Pair.of(1, 2)).inOrder();
    }

    @Test
    public void indexBy_array_simple() {
        assertThat(BasicMaps.indexBy(arrayOf(), Integer::toHexString)).isEmpty();
        assertThat(BasicMaps.indexBy(arrayOf(1, 2), Integer::toHexString)).containsExactly("1", 1, "2", 2).inOrder();
        assertThat(BasicMaps.indexBy(arrayOf(Pair.of(1, 2)), Pair::first)).containsExactly(1, Pair.of(1, 2)).inOrder();
        assertThat(BasicMaps.indexBy(arrayOf(Pair.of(1, 2)), Pair::second)).containsExactly(2, Pair.of(1, 2)).inOrder();
    }

    @Test
    public void mergeToMap_two_simple() {
        assertThat(BasicMaps.mergeToMap(mapOf(), mapOf())).isEmpty();
        assertThat(BasicMaps.mergeToMap(mapOf("foo", "bar"), mapOf())).containsExactly("foo", "bar").inOrder();
        assertThat(BasicMaps.mergeToMap(mapOf(), mapOf("foo", "bar"))).containsExactly("foo", "bar").inOrder();

        assertThat(BasicMaps.mergeToMap(mapOf("foo", "bar"), mapOf("bar", "baz"))).containsExactly("foo", "bar", "bar", "baz").inOrder();
        assertThat(BasicMaps.mergeToMap(mapOf("foo", "bar"), mapOf("foo", "baz"))).containsExactly("foo", "baz").inOrder();
        assertThat(BasicMaps.mergeToMap(mapOf("foo", "bar"), mapOf("bar", "foo"))).containsExactly("foo", "bar", "bar", "foo").inOrder();

        assertThat(BasicMaps.mergeToMap(mapOf(1, 2, 3, 4), mapOf(1, 5, 3, 5))).containsExactly(1, 5, 3, 5).inOrder();
        assertThat(BasicMaps.mergeToMap(mapOf(1, 2, 3, 4, 5, 6), mapOf(1, 5, 3, 5))).containsExactly(1, 5, 3, 5, 5, 6).inOrder();
    }

    @Test
    public void mergeToMap_two_nulls() {
        assertThat(BasicMaps.mergeToMap(mapOf("foo", "bar"), null)).containsExactly("foo", "bar").inOrder();
        assertThat(BasicMaps.mergeToMap(null, mapOf("foo", "bar"))).containsExactly("foo", "bar").inOrder();
        assertThat(BasicMaps.mergeToMap(null, null)).isEmpty();
    }

    @Test
    public void mergeToMap_three_simple() {
        assertThat(BasicMaps.mergeToMap(mapOf(), mapOf(), mapOf())).isEmpty();
        assertThat(BasicMaps.mergeToMap(mapOf("foo", "bar"), mapOf(), mapOf())).containsExactly("foo", "bar").inOrder();
        assertThat(BasicMaps.mergeToMap(mapOf(), mapOf("foo", "bar"), mapOf())).containsExactly("foo", "bar").inOrder();

        assertThat(BasicMaps.mergeToMap(mapOf(1, 2), mapOf(3, 4), mapOf(5, 6))).containsExactly(1, 2, 3, 4, 5, 6).inOrder();
        assertThat(BasicMaps.mergeToMap(mapOf(1, 2), mapOf(2, 3), mapOf(3, 4))).containsExactly(1, 2, 2, 3, 3, 4).inOrder();
        assertThat(BasicMaps.mergeToMap(mapOf(1, 2), mapOf(1, 3), mapOf(1, 4))).containsExactly(1, 4).inOrder();

        assertThat(BasicMaps.mergeToMap(mapOf(1, 2, 3, 4), mapOf(1, 5, 3, 5), mapOf(1, 6, 3, 6))).containsExactly(1, 6, 3, 6).inOrder();
        assertThat(BasicMaps.mergeToMap(mapOf(1, 2, 3, 4, 5, 6), mapOf(1, 5, 3, 5), mapOf(5, 8))).containsExactly(1, 5, 3, 5, 5, 8).inOrder();
    }

    @Test
    public void mergeToMap_three_nulls() {
        assertThat(BasicMaps.mergeToMap(mapOf("foo", "bar"), null, null)).containsExactly("foo", "bar").inOrder();
        assertThat(BasicMaps.mergeToMap(null, mapOf("foo", "bar"), null)).containsExactly("foo", "bar").inOrder();
        assertThat(BasicMaps.mergeToMap(null, null, mapOf("foo", "bar"))).containsExactly("foo", "bar").inOrder();
        assertThat(BasicMaps.mergeToMap(null, null, null)).isEmpty();
    }

    @Test
    public void sizeOf_simple() {
        assertThat(BasicMaps.sizeOf(null)).isEqualTo(0);
        assertThat(BasicMaps.sizeOf(mapOf())).isEqualTo(0);
        assertThat(BasicMaps.sizeOf(mapOf(1, 2))).isEqualTo(1);
    }

    @Test
    @SuppressWarnings("ConstantValue")
    public void isEmpty_simple() {
        assertThat(BasicMaps.isEmpty(null)).isTrue();
        assertThat(BasicMaps.isEmpty(mapOf())).isTrue();
        assertThat(BasicMaps.isEmpty(mapOf(1, 2))).isFalse();
    }

    @Test
    public void emptyIfNull_simple() {
        assertThat(BasicMaps.emptyIfNull(null)).isEmpty();
        assertThat(BasicMaps.emptyIfNull(mapOf())).isEmpty();
        assertThat(BasicMaps.emptyIfNull(mapOf(1, 2))).containsExactly(1, 2).inOrder();
    }

    @Test
    public void nullIfEmpty_simple() {
        assertThat(BasicMaps.nullIfEmpty(NULL_MAP)).isNull();
        assertThat(BasicMaps.nullIfEmpty(mapOf())).isNull();
        assertThat(BasicMaps.nullIfEmpty(mapOf(1, 2))).containsExactly(1, 2).inOrder();
    }

    @Test
    public void inverseMap_simple() {
        assertThat(BasicMaps.inverseMap(mapOf())).isEmpty();
        assertThat(BasicMaps.inverseMap(mapOf("foo", "bar"))).containsExactly("bar", "foo").inOrder();
        assertThat(BasicMaps.inverseMap(mapOf("foo", "bar", "bar", "baz"))).containsExactly("bar", "foo", "baz", "bar").inOrder();
    }

    @Test
    public void inverseMap_invalid() {
        assertThrows(IllegalStateException.class, () -> BasicMaps.inverseMap(mapOf("foo", "bar", "baz", "bar")));
    }
}
