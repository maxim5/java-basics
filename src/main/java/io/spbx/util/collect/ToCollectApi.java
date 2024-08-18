package io.spbx.util.collect;

import com.google.common.collect.MoreCollectors;
import io.spbx.util.base.Pair;
import io.spbx.util.base.Triple;
import io.spbx.util.base.Tuple;
import io.spbx.util.collect.ToApiCommon.Contract;
import io.spbx.util.collect.ToApiCommon.DoesNotAcceptNulls;
import io.spbx.util.collect.ToApiCommon.AcceptsAllStreams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ToCollectApi<E> extends ToListApi<E>, ToSetApi<E>, ToMapApi<E>, ToJoinApi<E>, AutoCloseable {
    default <R> R collect(@NotNull Collector<? super E, ?, R> collector) {
        return toStream().collect(collector);
    }

    default <R> R collect(@NotNull Collector<? super E, ?, R> collector, R def) {
        return collect(collector, (Supplier<R>) () -> def);
    }

    default <R> R collect(@NotNull Collector<? super E, ?, R> collector, @NotNull Supplier<R> def) {
        try {
            return collect(collector);
        } catch (RuntimeException | AssertionError e) {
            return def.get();
        }
    }

    default <C extends Collection<E>> @NotNull C toCollection(@NotNull Supplier<C> supplier) {
        return collect(Collectors.toCollection(supplier));
    }

    // accepts: 0 or 1 non-null element
    @DoesNotAcceptNulls
    default @NotNull Optional<E> toOptional() {
        return toStream().collect(MoreCollectors.toOptional());
    }

    // accepts: 0 or 1 nullable element
    @Contract(comments = "Fails if more than one item", acceptsNulls = true)
    default @Nullable E toAtMostOne() {
        return toAtMostN(1).first();
    }

    // accepts: exactly 1 nullable element
    @Contract(comments = "Fails if zero or more than one item", acceptsNulls = true)
    default @Nullable E toExactlyOne() {
        return toStream().collect(MoreCollectors.onlyElement());
    }

    // accepts: 0, 1 or 2 nullable elements
    @Contract(comments = "Fails if more than two items", acceptsNulls = true)
    default @NotNull Pair<E, E> toAtMostTwo() {
        return toStream().collect(Pair.toPairSparse());
    }

    // accepts: exactly 2 nullable elements
    @Contract(comments = "Fails if not exactly two items", acceptsNulls = true)
    default @NotNull Pair<E, E> toExactlyTwo() {
        return toStream().collect(Pair.toPair());
    }

    @Contract(comments = "Fails if more than three items", acceptsNulls = true)
    default @NotNull Triple<E, E, E> toAtMostThree() {
        return toStream().collect(Triple.toTripleSparse());
    }

    @Contract(comments = "Fails if not exactly three items", acceptsNulls = true)
    default @NotNull Triple<E, E, E> toExactlyThree() {
        return toStream().collect(Triple.toTriple());
    }

    @Contract(comments = "Fails if more than `n` items", acceptsNulls = true)
    default @NotNull Tuple toAtMostN(int n) {
        return toStream().collect(Tuple.toTupleSparse(n));
    }

    @Contract(comments = "Fails if not exactly `n` items", acceptsNulls = true)
    default @NotNull Tuple toExactlyN(int n) {
        return toStream().collect(Tuple.toTuple(n));
    }

    // accepts: any sequence, captures the non-null if all others are null
    @AcceptsAllStreams
    default @NotNull Optional<E> toAtMostOneIgnoreNulls() {
        return toStream().collect(BasicCollectors.toOnlyNonNullOrEmpty());
    }

    // accepts: any sequence, captures if exactly one
    // toOneIfSingle()

    /* `Stream` terminal operations */

    @AcceptsAllStreams
    default boolean allMatch(@NotNull Predicate<? super E> predicate) {
        return toStream().allMatch(predicate);
    }

    @AcceptsAllStreams
    default boolean anyMatch(@NotNull Predicate<? super E> predicate) {
        return toStream().anyMatch(predicate);
    }

    @AcceptsAllStreams
    default boolean noneMatch(@NotNull Predicate<? super E> predicate) {
        return toStream().noneMatch(predicate);
    }

    @AcceptsAllStreams
    default @NotNull Optional<E> findFirst() {
        return toStream().findFirst();
    }

    @AcceptsAllStreams
    default @NotNull Optional<E> findAny() {
        return toStream().findAny();
    }

    @AcceptsAllStreams
    default boolean allEqual() {
        return BasicCollectors.allEqual(toStream());
    }

    @AcceptsAllStreams
    default @NotNull Iterator<E> iterator() {
        return toStream().iterator();
    }

    @AcceptsAllStreams
    default @NotNull Spliterator<E> spliterator() {
        return toStream().spliterator();
    }

    @AcceptsAllStreams
    default void forEach(@NotNull Consumer<? super E> consumer) {
        toStream().forEach(consumer);
    }

    @AcceptsAllStreams
    default void forEachOrdered(@NotNull Consumer<? super E> consumer) {
        toStream().forEachOrdered(consumer);
    }

    default long count() {
        return toStream().count();
    }

    @AcceptsAllStreams
    default @NotNull Optional<E> min(@NotNull Comparator<? super E> comparator) {
        return toStream().min(comparator);
    }

    @AcceptsAllStreams
    default @NotNull Optional<E> max(@NotNull Comparator<? super E> comparator) {
        return toStream().max(comparator);
    }

    @Override
    @AcceptsAllStreams
    default void close() {
        toStream().close();
    }

    /* Static factory methods */

    static @SafeVarargs <E> @NotNull ToCollectApi<E> of(@Nullable E @Nullable ... items) {
        return () -> BasicStreams.streamOf(items);
    }

    static <E> @NotNull ToCollectApi<E> of(@Nullable Iterable<@Nullable E> iterable) {
        return () -> BasicStreams.streamOf(iterable);
    }

    static <E> @NotNull ToCollectApi<E> of(@Nullable Stream<@Nullable E> stream) {
        return () -> BasicStreams.emptyIfNull(stream);
    }
}
