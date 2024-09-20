package io.spbx.util.collect;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.EasyCast.castAny;
import static io.spbx.util.testing.TestingBasics.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("fast")
public class ListBuilderTest {
    private static final Integer NULL = null;

    @Test
    public void builder_empty() {
        ListBuilder<Integer> builder = ListBuilder.builder();
        assertBuilder(builder).isEmpty();
    }

    @Test
    public void builder_reserve_empty() {
        ListBuilder<Integer> builder = ListBuilder.builder(3);
        assertBuilder(builder).isEmpty();
    }

    @Test
    public void builder_add_one_not_null() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().add(1);
        assertBuilder(builder).containsExactly(1);
    }

    @Test
    public void builder_add_one_null() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().add(null);
        assertBuilder(builder).containsExactly(NULL);
    }

    @Test
    public void builder_add_two_not_nulls() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().add(1).add(2);
        assertBuilder(builder).containsExactly(1, 2);
    }

    @Test
    public void builder_add_two_null_and_not_null() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().add(1).add(null);
        assertBuilder(builder).containsExactly(1, null);
    }

    @Test
    public void builder_addIfPresent() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder()
            .addIfPresent(Optional.of(1))
            .addIfPresent(Optional.empty());
        assertBuilder(builder).containsExactly(1);
    }

    @Test
    public void builder_addAll_one_not_null() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().addAll(1);
        assertBuilder(builder).containsExactly(1);
    }

    @Test
    public void builder_addAll_one_null() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().addAll(NULL);
        assertBuilder(builder).containsExactly(NULL);
    }

    @Test
    public void builder_addAll_two_not_nulls() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().addAll(1, 2);
        assertBuilder(builder).containsExactly(1, 2);
    }

    @Test
    public void builder_addAll_two_nulls() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().addAll(null, null);
        assertBuilder(builder).containsExactly(null, null);
    }

    @Test
    public void builder_addAll_three_not_nulls() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().addAll(1, 2, 3);
        assertBuilder(builder).containsExactly(1, 2, 3);
    }

    @Test
    public void builder_addAll_three_nulls() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().addAll(null, null, null);
        assertBuilder(builder).containsExactly(null, null, null);
    }

    @Test
    public void builder_addAll_vararg_not_nulls() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().addAll(1, 2, 3, 4, 5);
        assertBuilder(builder).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    public void builder_addAll_vararg_null_and_not_nulls() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().addAll(1, null, 2, null);
        assertBuilder(builder).containsExactly(1, null, 2, null);
    }

    @Test
    public void builder_addAll_collection_not_nulls() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().addAll(listOf(1, 2, 3, 4));
        assertBuilder(builder).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void builder_addAll_collection_null_and_not_null() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().addAll(listOf(null, 1, null));
        assertBuilder(builder).containsExactly(null, 1, null);
    }

    @Test
    public void builder_addAll_iterable_not_nulls() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().addAll(iterableOf(1, 2, 3, 4));
        assertBuilder(builder).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void builder_addAll_iterable_null_and_not_null() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().addAll(iterableOf(null, 1));
        assertBuilder(builder).containsExactly(null, 1);
    }

    @Test
    public void builder_of_varargs() {
        assertBuilder(ListBuilder.<Integer>of()).isEmpty();
        assertBuilder(ListBuilder.of(1)).containsExactly(1);
        assertBuilder(ListBuilder.of(1, 2)).containsExactly(1, 2);
        assertBuilder(ListBuilder.of(1, 2, 3)).containsExactly(1, 2, 3);
        assertBuilder(ListBuilder.of(1, 2, null)).containsExactly(1, 2, null);
        assertBuilder(ListBuilder.of(null, null, null, 0)).containsExactly(null, null, null, 0);
        assertBuilder(ListBuilder.of(null, null, null, 0, 1)).containsExactly(null, null, null, 0, 1);
    }

    @Test
    public void builder_copyOf_list() {
        assertBuilder(ListBuilder.copyOf(listOf())).isEmpty();
        assertBuilder(ListBuilder.copyOf(listOf(1))).containsExactly(1);
        assertBuilder(ListBuilder.copyOf(listOf(1, 2))).containsExactly(1, 2);
    }

    @Test
    public void builder_skipNulls() {
        assertBuilder(ListBuilder.<Integer>of().skipNulls()).isEmpty();
        assertBuilder(ListBuilder.of(1).skipNulls()).containsExactly(1);
        assertBuilder(ListBuilder.of(1, 2).skipNulls()).containsExactly(1, 2);
        assertBuilder(ListBuilder.of(1, 2, null).skipNulls()).containsExactly(1, 2);
        assertBuilder(ListBuilder.of(null, null, null, 0).skipNulls()).containsExactly(0);
        assertBuilder(ListBuilder.of(NULL).skipNulls()).isEmpty();
    }

    @Test
    public void builder_distinct() {
        assertBuilder(ListBuilder.<Integer>of().distinct()).isEmpty();
        assertBuilder(ListBuilder.of(1).distinct()).containsExactly(1);
        assertBuilder(ListBuilder.of(1, 1, 1).distinct()).containsExactly(1);
        assertBuilder(ListBuilder.of(1, 2).distinct()).containsExactly(1, 2);
        assertBuilder(ListBuilder.of(1, 2, 1, 2).distinct()).containsExactly(1, 2);
        assertBuilder(ListBuilder.of(1, 2, null).distinct()).containsExactly(1, 2, null);
        assertBuilder(ListBuilder.of(NULL).distinct()).containsExactly(NULL);
        assertBuilder(ListBuilder.of(null, null, null, 0).distinct()).containsExactly(null, 0);
        assertBuilder(ListBuilder.of(1, 1, 1, 2, 2, 2, 1, 1, 3, 3, 1, 2, 3).distinct()).containsExactly(1, 2, 3);
    }

    @Test
    public void builder_sorted() {
        Comparator<Integer> cmp = Integer::compareTo;
        assertBuilder(ListBuilder.<Integer>of().sorted(cmp)).isEmpty();
        assertBuilder(ListBuilder.of(1).sorted(cmp)).containsExactly(1);
        assertBuilder(ListBuilder.of(1, 1, 1).sorted(cmp)).containsExactly(1, 1, 1);
        assertBuilder(ListBuilder.of(2, 1).sorted(cmp)).containsExactly(1, 2);
        assertBuilder(ListBuilder.of(1, 2, 1, 2).sorted(cmp)).containsExactly(1, 1, 2, 2);
        assertBuilder(ListBuilder.of(4, 3, 2, 1).sorted(cmp)).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void builder_combine() {
        ListBuilder<Integer> builder = ListBuilder.<Integer>builder().addAll(1, 2)
            .combine(ListBuilder.<Integer>builder().addAll(3));
        assertBuilder(builder).containsExactly(1, 2, 3);
    }

    @Test
    public void builder_makeCollector() {
        Collector<Integer, ?, ArrayList<Integer>> collector = ListBuilder.makeCollector(ToListApi::toArrayList);
        assertThat(streamOf(1, 2, 3).collect(collector)).containsExactly(1, 2, 3);
        assertThat(streamOf(1, null, null).collect(collector)).containsExactly(1, null, null);
    }

    @CheckReturnValue
    private static <T> @NotNull ListBuilderSubject<T> assertBuilder(@NotNull ToListApi<T> builder) {
        return new ListBuilderSubject<>(builder);
    }

    private record ListBuilderSubject<T>(@NotNull ToListApi<T> builder) {
        public void isEmpty() {
            containsExactly();
        }

        public final @SafeVarargs void containsExactly(@Nullable T @NotNull ... expected) {
            boolean hasNulls = streamOf(expected).anyMatch(Objects::isNull);

            if (hasNulls) {
                assertThrows(AssertionError.class, builder::toList);
                assertThrows(AssertionError.class, builder::toGuavaImmutableList);
            } else {
                assertThat(builder.toList()).containsExactlyElementsIn(expected).inOrder();
                assertThat(builder.toGuavaImmutableList()).containsExactlyElementsIn(expected).inOrder();
            }

            assertThat(builder.toList(() -> new LinkedList<>())).containsExactlyElementsIn(expected).inOrder();
            assertThat(builder.toArrayList()).containsExactlyElementsIn(expected).inOrder();
            assertThat(builder.toBasicsMutableArray()).containsExactlyElementsIn(expected).inOrder();
            assertThat(builder.toBasicsImmutableArray()).containsExactlyElementsIn(expected).inOrder();
            assertThat(builder.toBasicsImmutableArrayList()).containsExactlyElementsIn(expected).inOrder();
            assertThat(builder.toBasicsTuple()).containsExactlyElementsIn(expected).inOrder();
            assertThat(builder.toNativeArray()).asList().containsExactlyElementsIn(expected).inOrder();
            assertThat(builder.toNativeArray(len -> castAny(new Object[len]))).asList().containsExactlyElementsIn(expected).inOrder();
        }
    }
}
