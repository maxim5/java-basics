package io.spbx.util.collect;

import io.spbx.util.collect.ToApiCommon.AcceptsAllStreams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ToCollectKvApi<K, V, E extends Entry<K, V>> extends ToMapKvApi<K, V, E>, ToJoinApi<E>, AutoCloseable {
    default <R> R collect(@NotNull Collector<E, ?, R> collector) {
        return toStream().collect(collector);
    }

    default <R> R collect(@NotNull Collector<? super E, ?, R> collector, R def) {
        return collect(collector, (Supplier<R>) () -> def);
    }

    default <R> R collect(@NotNull Collector<? super E, ?, R> collector, @NotNull Supplier<R> def) {
        try {
            return toStream().collect(collector);
        } catch (RuntimeException | AssertionError e) {
            return def.get();
        }
    }

    default <C extends Collection<E>> @NotNull C toCollection(@NotNull Supplier<C> supplier) {
        return collect(Collectors.toCollection(supplier));
    }

    /* `Stream` terminal operations */

    @AcceptsAllStreams
    default boolean allMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        return toStream().allMatch(e -> predicate.test(e.getKey(), e.getValue()));
    }

    @AcceptsAllStreams
    default boolean anyMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        return toStream().anyMatch(e -> predicate.test(e.getKey(), e.getValue()));
    }

    @AcceptsAllStreams
    default boolean noneMatch(@NotNull BiPredicate<? super K, ? super V> predicate) {
        return toStream().noneMatch(e -> predicate.test(e.getKey(), e.getValue()));
    }

    @AcceptsAllStreams
    default void forEach(@NotNull BiConsumer<? super K, ? super V> consumer) {
        toStream().forEach(e -> consumer.accept(e.getKey(), e.getValue()));
    }

    @AcceptsAllStreams
    default void forEachOrdered(@NotNull BiConsumer<? super K, ? super V> consumer) {
        toStream().forEachOrdered(e -> consumer.accept(e.getKey(), e.getValue()));
    }

    @Override
    @AcceptsAllStreams
    default void close() {
        toStream().close();
    }

    /* Static factory methods */

    static @SafeVarargs <K, V, E extends Entry<K, V>> @NotNull ToCollectKvApi<K, V, E> of(@Nullable E @Nullable ... items) {
        return () -> BasicStreams.streamOf(items);
    }

    static <K, V, E extends Entry<K, V>> @NotNull ToCollectKvApi<K, V, E> of(@Nullable Iterable<@Nullable E> iterable) {
        return () -> BasicStreams.streamOf(iterable);
    }

    static <K, V, E extends Entry<K, V>> @NotNull ToCollectKvApi<K, V, E> of(@Nullable Stream<@Nullable E> stream) {
        return () -> BasicStreams.emptyIfNull(stream);
    }
}
