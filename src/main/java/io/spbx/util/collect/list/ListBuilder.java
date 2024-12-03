package io.spbx.util.collect.list;

import io.spbx.util.base.annotate.Beta;
import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.collect.iter.BasicIterables;
import io.spbx.util.collect.stream.ToCollectApi;
import io.spbx.util.collect.stream.ToListApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static io.spbx.util.collect.iter.BasicIterables.emptyIfNull;
import static io.spbx.util.collect.iter.BasicIterables.sizeOf;

/**
 * A universal {@link List} builder. Supports null values.
 */
@CanIgnoreReturnValue
public class ListBuilder<T> implements ToListApi<T> {
    private static final int DEFAULT_SIZE = 8;

    private final ArrayList<T> list;

    public ListBuilder() {
        this(DEFAULT_SIZE);  // the builder is usually updated after creation, so lazy `ArrayList` init is unnecessary
    }

    public ListBuilder(int size) {
        list = new ArrayList<>(size);
    }

    /* Static builder constructors */

    public static <T> @NotNull ListBuilder<T> builder() {
        return new ListBuilder<>();
    }

    public static <T> @NotNull ListBuilder<T> builder(int size) {
        return new ListBuilder<>(size);
    }

    public static <T> @NotNull ListBuilder<T> of(@Nullable T item) {
        return ListBuilder.<T>builder().add(item);
    }

    public static <T> @NotNull ListBuilder<T> of(@Nullable T item1, @Nullable T item2) {
        return ListBuilder.<T>builder().addAll(item1, item2);
    }

    public static <T> @NotNull ListBuilder<T> of(@Nullable T item1, @Nullable T item2, @Nullable T item3) {
        return ListBuilder.<T>builder().addAll(item1, item2, item3);
    }

    public static @SafeVarargs <T> @NotNull ListBuilder<T> of(@Nullable T @NotNull ... items) {
        return ListBuilder.<T>builder(items.length).addAll(items);
    }

    public static <T> @NotNull ListBuilder<T> copyOf(@Nullable Iterable<? extends T> items) {
        return ListBuilder.<T>builder(sizeOf(items, DEFAULT_SIZE)).addAll(items);
    }

    /* `add()` and `addAll()` methods */

    public @NotNull ListBuilder<T> add(@Nullable T item) {
        list.add(item);
        return this;
    }

    public @NotNull ListBuilder<T> addAll(@Nullable Iterable<? extends T> items) {
        for (T item : emptyIfNull(items)) {
            list.add(item);
        }
        return this;
    }

    public @NotNull ListBuilder<T> addAll(@Nullable Collection<? extends T> items) {
        list.addAll(emptyIfNull(items));
        return this;
    }

    @SuppressWarnings("unchecked")
    public @NotNull ListBuilder<T> addAll(@Nullable T @NotNull ... items) {
        return addAll(Arrays.asList(items));
    }

    public @NotNull ListBuilder<T> addAll(@Nullable T item) {
        return add(item);
    }

    public @NotNull ListBuilder<T> addAll(@Nullable T item1, @Nullable T item2) {
        list.add(item1);
        list.add(item2);
        return this;
    }

    public @NotNull ListBuilder<T> addAll(@Nullable T item1, @Nullable T item2, @Nullable T item3) {
        list.add(item1);
        list.add(item2);
        list.add(item3);
        return this;
    }

    @Beta
    public @NotNull ListBuilder<T> addIfPresent(@NotNull Optional<T> item) {
        item.ifPresent(list::add);
        return this;
    }

    /* Post-creation in-place manipulations */

    @Beta
    public @NotNull ToListApi<T> skipNulls() {
        list.removeIf(Objects::isNull);
        return this;
    }

    @Beta
    public @NotNull ToListApi<T> distinct() {
        BasicIterables.distinctInPlace(list);
        return this;
    }

    @Beta
    public @NotNull ToListApi<T> sorted(@NotNull Comparator<? super T> comparator) {
        list.sort(comparator);
        return this;
    }

    /* Builders to various representations */

    @Override
    public @NotNull Stream<T> toStream() {
        return list.stream();
    }

    // Needs override, otherwise there is an infinite recursion, because `ImmutableArrayList` collector uses `ListBuilder`.
    @Override
    public @NotNull ImmutableArrayList<T> toBasicsImmutableArrayList() {
        return new ImmutableArrayList<>(list);
    }

    public @NotNull ToCollectApi<T> collectors() {
        return ToCollectApi.of(list);
    }

    /* Collector helper */

    @NotNull ListBuilder<T> combine(@NotNull ListBuilder<T> builder) {
        addAll(builder.list);
        return this;
    }

    public static <E, L extends List<E>> @NotNull Collector<E, ?, L> makeCollector(@NotNull Function<ListBuilder<E>, L> toListFunc) {
        return Collector.of(
            ListBuilder::new,
            ListBuilder::add,
            ListBuilder::combine,
            toListFunc
        );
    }
}
