package io.spbx.util.collect;

import com.google.common.truth.MapSubject;
import io.spbx.util.base.Pair;
import io.spbx.util.testing.AssertFailure;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.TestingBasics.*;

public class MapBuilderTest {
    private static final Integer NULL = null;

    @Test
    public void builder_of_order_simple() {
        assertBuilder(MapBuilder.builder()).isEmpty();
        assertBuilder(MapBuilder.builder(4)).isEmpty();
        assertBuilder(MapBuilder.of(1, 2)).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.of(1, 2, 3, 4)).isEqualTo(mapOf(1, 2, 3, 4));
        assertBuilder(MapBuilder.of(1, 2, 3, 4, 5, 6)).isEqualTo(mapOf(1, 2, 3, 4, 5, 6));
        assertBuilder(MapBuilder.of(1, 2, 3, 4, 5, 6, 7, 8)).isEqualTo(mapOf(1, 2, 3, 4, 5, 6, 7, 8));
        assertBuilder(MapBuilder.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)).isEqualTo(mapOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0));

        assertBuilder(MapBuilder.of(1, NULL)).isEqualTo(mapOf(1, NULL));
        assertBuilder(MapBuilder.of(NULL, 1)).isEqualTo(mapOf(NULL, 1));
        assertBuilder(MapBuilder.of(NULL, NULL)).isEqualTo(mapOf(NULL, NULL));
    }

    @Test
    public void builder_of_unchecked_order_simple() {
        assertBuilder(MapBuilder.ofUnchecked(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, (Object[]) arrayOf(11, 12)))
            .isEqualTo(mapOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        assertBuilder(MapBuilder.ofUnchecked(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, (Object[]) arrayOf(NULL, NULL)))
            .isEqualTo(mapOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, NULL, NULL));
    }

    @Test
    public void builder_of_entry_simple() {
        assertBuilder(MapBuilder.of(pairOf(1, 2))).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.of(pairOf(1, 2), pairOf(3, 4))).isEqualTo(mapOf(1, 2, 3, 4));
        assertBuilder(MapBuilder.of(pairOf(1, 2), pairOf(3, 4), pairOf(5, 6))).isEqualTo(mapOf(1, 2, 3, 4, 5, 6));
        assertBuilder(MapBuilder.of(pairOf(NULL, NULL))).isEqualTo(mapOf(NULL, NULL));
    }

    @Test
    public void builder_copyOf_simple() {
        assertBuilder(MapBuilder.copyOf(mapOf())).isEmpty();
        assertBuilder(MapBuilder.copyOf(mapOf(1, 2))).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.copyOf(mapOf(1, 2, 3, 4))).isEqualTo(mapOf(1, 2, 3, 4));
        assertBuilder(MapBuilder.copyOf(mapOf(NULL, NULL))).isEqualTo(mapOf(NULL, NULL));
    }

    @Test
    public void builder_copyOf_entry_list_simple() {
        assertBuilder(MapBuilder.copyOf(iterableOf())).isEmpty();
        assertBuilder(MapBuilder.copyOf((Iterable<Pair<Integer, Integer>>) null)).isEmpty();
        assertBuilder(MapBuilder.copyOf(iterableOf(pairOf(1, 2)))).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.copyOf(iterableOf(pairOf(1, 2), pairOf(3, 4)))).isEqualTo(mapOf(1, 2, 3, 4));
    }

    @Test
    public void builder_put_simple() {
        assertBuilder(MapBuilder.builder().put(1, 2)).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.builder().put(1, 2, 3, 4)).isEqualTo(mapOf(1, 2, 3, 4));
        assertBuilder(MapBuilder.builder().put(1, 2, 3, 4, 5, 6)).isEqualTo(mapOf(1, 2, 3, 4, 5, 6));
        assertBuilder(MapBuilder.builder().put(1, 2, 3, 4, 5, 6, 7, 8)).isEqualTo(mapOf(1, 2, 3, 4, 5, 6, 7, 8));
        assertBuilder(MapBuilder.builder().put(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)).isEqualTo(mapOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0));

        assertBuilder(MapBuilder.builder().put(1, NULL)).isEqualTo(mapOf(1, NULL));
        assertBuilder(MapBuilder.builder().put(NULL, 1)).isEqualTo(mapOf(NULL, 1));
        assertBuilder(MapBuilder.builder().put(NULL, NULL)).isEqualTo(mapOf(NULL, NULL));
        assertBuilder(MapBuilder.builder().put(1, NULL, 1, 2)).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.builder().put(1, NULL, 1, NULL)).isEqualTo(mapOf(1, NULL));
    }

    @Test
    public void builder_put_invalid() {
        assertFailure(() -> MapBuilder.builder().put(1, 2, 1, 2)).throwsAssertion();
        assertFailure(() -> MapBuilder.builder().put(1, 2, 1, 3)).throwsAssertion();

        assertFailure(() -> MapBuilder.builder().put(1, 2, 1, NULL)).throwsAssertion();
        // assertFailure(() -> MapBuilder.builder().put(1, NULL, 1, 2)).throwsAssertion();
        // assertFailure(() -> MapBuilder.builder().put(1, NULL, 1, NULL)).throwsAssertion();

        assertFailure(() -> MapBuilder.builder().put(NULL, 2, NULL, 2)).throwsAssertion();
        assertFailure(() -> MapBuilder.builder().put(NULL, 2, NULL, 3)).throwsAssertion();
        assertFailure(() -> MapBuilder.builder().put(NULL, NULL, NULL, NULL)).throwsAssertion();
        assertFailure(() -> MapBuilder.builder().put(NULL, NULL, NULL, 3)).throwsAssertion();
    }

    @Test
    public void builder_putUnchecked_simple() {
        assertBuilder(MapBuilder.builder().putUnchecked(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, (Object[]) arrayOf(11, 12)))
            .isEqualTo(mapOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        assertBuilder(MapBuilder.builder().putUnchecked(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, (Object[]) arrayOf(NULL, NULL)))
            .isEqualTo(mapOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, NULL, NULL));
    }

    @Test
    public void builder_put_entry_simple() {
        assertBuilder(MapBuilder.builder().put(pairOf(1, 2))).isEqualTo(mapOf(1, 2));
    }

    @Test
    public void builder_putAll_map_simple() {
        assertBuilder(MapBuilder.builder().putAll(mapOf())).isEmpty();
        assertBuilder(MapBuilder.builder().putAll(mapOf(1, 2))).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.builder().putAll(mapOf(1, 2, 3, 4))).isEqualTo(mapOf(1, 2, 3, 4));
        assertBuilder(MapBuilder.builder().putAll(mapOf(NULL, NULL))).isEqualTo(mapOf(NULL, NULL));
    }

    @Test
    public void builder_putAll_entry_list_simple() {
        assertBuilder(MapBuilder.builder().putAll(iterableOf())).isEmpty();
        assertBuilder(MapBuilder.builder().putAll((Iterable<Pair<Object, Object>>) null)).isEmpty();
        assertBuilder(MapBuilder.builder().putAll(iterableOf(pairOf(1, 2)))).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.builder().putAll(iterableOf(pairOf(1, 2), pairOf(3, 4)))).isEqualTo(mapOf(1, 2, 3, 4));
    }

    @Test
    public void builder_overwrite_simple() {
        assertBuilder(MapBuilder.builder().overwrite(1, 2)).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.builder().overwrite(1, 2, 3, 4)).isEqualTo(mapOf(1, 2, 3, 4));
        assertBuilder(MapBuilder.builder().overwrite(1, 2, 3, 4, 5, 6)).isEqualTo(mapOf(1, 2, 3, 4, 5, 6));
        assertBuilder(MapBuilder.builder().overwrite(1, 2, 3, 4, 5, 6, 7, 8)).isEqualTo(mapOf(1, 2, 3, 4, 5, 6, 7, 8));
        assertBuilder(MapBuilder.builder().overwrite(1, 2, 3, 4, 5, 6, 7, 8, 9, 0))
            .isEqualTo(mapOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0));

        assertBuilder(MapBuilder.builder().overwrite(1, NULL)).isEqualTo(mapOf(1, NULL));
        assertBuilder(MapBuilder.builder().overwrite(NULL, 1)).isEqualTo(mapOf(NULL, 1));
        assertBuilder(MapBuilder.builder().overwrite(NULL, NULL)).isEqualTo(mapOf(NULL, NULL));

        assertBuilder(MapBuilder.of(1, 1).overwrite(1, 2)).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.of(1, 1).overwrite(1, 2, 3, 4)).isEqualTo(mapOf(1, 2, 3, 4));
        assertBuilder(MapBuilder.of(1, 1).overwrite(1, 2, 3, 4, 5, 6)).isEqualTo(mapOf(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void builder_overwrite_duplicates() {
        assertBuilder(MapBuilder.builder().overwrite(1, 2, 1, 4)).isEqualTo(mapOf(1, 4));
        assertBuilder(MapBuilder.builder().overwrite(1, 2, 1, 4, 1, 6)).isEqualTo(mapOf(1, 6));
        assertBuilder(MapBuilder.builder().overwrite(1, 2, 1, 4, 1, 6, 1, 8)).isEqualTo(mapOf(1, 8));
        assertBuilder(MapBuilder.builder().overwrite(1, 2, 1, 4, 1, 6, 1, 8, 1, 0)).isEqualTo(mapOf(1, 0));

        assertBuilder(MapBuilder.builder().overwrite(1, 2, 1, NULL)).isEqualTo(mapOf(1, NULL));
        assertBuilder(MapBuilder.builder().overwrite(1, NULL, 1, NULL)).isEqualTo(mapOf(1, NULL));
        assertBuilder(MapBuilder.builder().overwrite(1, NULL, 1, 2)).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.builder().overwrite(NULL, 1, NULL, 2)).isEqualTo(mapOf(NULL, 2));
        assertBuilder(MapBuilder.builder().overwrite(NULL, NULL, NULL, 2)).isEqualTo(mapOf(NULL, 2));

        assertBuilder(MapBuilder.of(1, 2).overwrite(1, 4)).isEqualTo(mapOf(1, 4));
        assertBuilder(MapBuilder.of(1, 2).overwrite(1, 4, 1, 6)).isEqualTo(mapOf(1, 6));
        assertBuilder(MapBuilder.of(1, 2).overwrite(1, 4, 1, 6, 1, 8)).isEqualTo(mapOf(1, 8));
        assertBuilder(MapBuilder.of(1, 2).overwrite(1, 4, 1, 6, 1, 8, 1, 0)).isEqualTo(mapOf(1, 0));
    }

    @Test
    public void builder_overwriteUnchecked_simple() {
        assertBuilder(MapBuilder.builder().overwriteUnchecked(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, (Object[]) arrayOf(11, 12)))
            .isEqualTo(mapOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        assertBuilder(MapBuilder.builder().overwriteUnchecked(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, (Object[]) arrayOf(NULL, NULL)))
            .isEqualTo(mapOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, NULL, NULL));

        assertBuilder(MapBuilder.builder().overwriteUnchecked(1, 2, 1, 4, 1, 6, 1, 8, 1, 10, (Object[]) arrayOf(11, 12)))
            .isEqualTo(mapOf(1, 10, 11, 12));
        assertBuilder(MapBuilder.builder().overwriteUnchecked(1, 2, 1, 4, 1, 6, 1, 8, 1, 10, (Object[]) arrayOf(1, 12)))
            .isEqualTo(mapOf(1, 12));
        assertBuilder(MapBuilder.builder().overwriteUnchecked(1, 2, 1, 4, 1, 6, 1, 8, 1, 10, (Object[]) arrayOf(1, 12, 1, 14)))
            .isEqualTo(mapOf(1, 14));
    }

    @Test
    public void builder_overwrite_entry_simple() {
        assertBuilder(MapBuilder.builder().overwrite(pairOf(1, 2))).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.of(1, 1).overwrite(pairOf(1, 2))).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.of(1, 2).overwrite(pairOf(1, 2))).isEqualTo(mapOf(1, 2));
    }

    @Test
    public void builder_overwriteAll_map_simple() {
        assertBuilder(MapBuilder.builder().overwriteAll(mapOf())).isEmpty();
        assertBuilder(MapBuilder.builder().overwriteAll(mapOf(1, 2))).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.builder().overwriteAll(mapOf(1, 2, 3, 4))).isEqualTo(mapOf(1, 2, 3, 4));
        assertBuilder(MapBuilder.builder().overwriteAll(mapOf(NULL, NULL))).isEqualTo(mapOf(NULL, NULL));

        assertBuilder(MapBuilder.of(1, 1).overwriteAll(mapOf())).isEqualTo(mapOf(1, 1));
        assertBuilder(MapBuilder.of(1, 1).overwriteAll(mapOf(1, 2))).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.of(1, 1).overwriteAll(mapOf(1, 2, 3, 4))).isEqualTo(mapOf(1, 2, 3, 4));
        assertBuilder(MapBuilder.of(1, 1).overwriteAll(mapOf(NULL, NULL))).isEqualTo(mapOf(1, 1, NULL, NULL));
    }

    @Test
    public void builder_overwriteAll_entry_list_simple() {
        assertBuilder(MapBuilder.builder().overwriteAll(iterableOf())).isEmpty();
        assertBuilder(MapBuilder.builder().overwriteAll((Iterable<Pair<Object, Object>>) null)).isEmpty();
        assertBuilder(MapBuilder.builder().overwriteAll(iterableOf(pairOf(1, 2)))).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.builder().overwriteAll(iterableOf(pairOf(1, 2), pairOf(3, 4)))).isEqualTo(mapOf(1, 2, 3, 4));

        assertBuilder(MapBuilder.of(1, 1).overwriteAll(iterableOf())).isEqualTo(mapOf(1, 1));
        assertBuilder(MapBuilder.of(1, 1).overwriteAll((Iterable<Pair<Integer, Integer>>) null)).isEqualTo(mapOf(1, 1));
        assertBuilder(MapBuilder.of(1, 1).overwriteAll(iterableOf(pairOf(1, 2)))).isEqualTo(mapOf(1, 2));
        assertBuilder(MapBuilder.of(1, 1).overwriteAll(iterableOf(pairOf(1, 2), pairOf(3, 4)))).isEqualTo(mapOf(1, 2, 3, 4));
    }

    private static <K, V> @NotNull MapSubject assertBuilder(@NotNull MapBuilder<K, V> mapBuilder) {
        return assertThat(mapBuilder.toLinkedHashMap());
    }
}
