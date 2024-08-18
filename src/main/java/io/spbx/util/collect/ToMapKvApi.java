package io.spbx.util.collect;

import com.google.common.collect.ImmutableMap;
import io.spbx.util.collect.BasicCollectors.MapMergers;
import io.spbx.util.collect.ToApiCommon.AcceptsNulls;
import io.spbx.util.collect.ToApiCommon.DoesNotAcceptNulls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.spbx.util.collect.ToApiCommon.assertKvNonNull;
import static io.spbx.util.func.ScopeFunctions.with;

public interface ToMapKvApi<K, V, E extends Entry<K, V>> extends ToStreamApi<E> {
    /* Collect into `Map` */

    default <M extends Map<K, V>> @NotNull M toMap(@NotNull Collector<E, ?, M> collector) {
        return toStream().collect(collector);
    }

    @AcceptsNulls
    default <M extends Map<K, V>> @NotNull M toMap(@NotNull Supplier<M> supplier) {
        return toMap(BasicCollectors.toMap(Entry::getKey, Entry::getValue, supplier));
    }

    @AcceptsNulls
    default <M extends Map<K, V>> @NotNull M toMapIgnoreDuplicateKeys(@NotNull Supplier<M> supplier) {
        return toMap(BasicCollectors.toMap(Entry::getKey, Entry::getValue, MapMergers.ignoreDuplicates(), supplier));
    }

    @AcceptsNulls
    default <M extends Map<K, V>> @NotNull M toMap(@NotNull BinaryOperator<V> mergeFunc, @NotNull Supplier<M> supplier) {
        return toMap(BasicCollectors.toMap(Entry::getKey, Entry::getValue, mergeFunc, supplier));
    }

    @AcceptsNulls
    default @NotNull Map<K, V> toMap() {
        return this.toMap(HashMap::new);
    }

    @DoesNotAcceptNulls
    default <M extends ConcurrentMap<K, V>> @NotNull M toConcurrentMap(@NotNull Supplier<M> supplier) {
        return this.toConcurrentMap(MapMergers.throwing(), supplier);
    }

    @DoesNotAcceptNulls
    default <M extends ConcurrentMap<K, V>> @NotNull M toConcurrentMap(
            @NotNull BinaryOperator<V> mergeFunc,
            @NotNull Supplier<M> supplier) {
        return toMap(Collectors.toConcurrentMap(Entry::getKey, Entry::getValue, mergeFunc, supplier));
    }

    /* Collect into standard java `Map`s */

    @AcceptsNulls
    default @NotNull HashMap<K, V> toHashMap() {
        return this.toMap(HashMap::new);
    }

    @AcceptsNulls
    default @NotNull LinkedHashMap<K, V> toLinkedHashMap() {
        return this.toMap(LinkedHashMap::new);
    }

    // Needed?
    @AcceptsNulls
    default @NotNull LinkedHashMap<K, V> toOrderedMap() {
        return this.toLinkedHashMap();
    }

    @DoesNotAcceptNulls
    default @NotNull ConcurrentHashMap<K, V> toConcurrentHashMap() {
        return this.toNonNullMap("toConcurrentHashMap()", ConcurrentHashMap::new);
    }

    @DoesNotAcceptNulls
    default @NotNull TreeMap<K, V> toTreeMap() {
        return this.toNonNullMap("toTreeMap()", TreeMap::new);
    }

    @DoesNotAcceptNulls
    default @NotNull TreeMap<K, V> toTreeMap(@NotNull Comparator<K> comparator) {
        return this.toNonNullMap("toTreeMap(Comparator)", () -> new TreeMap<>(comparator));
    }

    /* Collect into `ImmutableMap` */

    @DoesNotAcceptNulls
    default @NotNull ImmutableMap<K, V> toGuavaImmutableMap() {
        return this.collectNonNull("toGuavaImmutableMap()", ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue));
    }

    @DoesNotAcceptNulls
    default @NotNull ImmutableMap<K, V> toGuavaImmutableMap(@NotNull BinaryOperator<V> mergeFunc) {
        return this.collectNonNull("toGuavaImmutableMap()", ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue, mergeFunc));
    }

    /* More collectors */

    default @NotNull ToBiMapKvApi<K, V, E> bimaps() {
        return ToBiMapKvApi.of(toStream());
    }

    default @NotNull ToMultimapKvApi<K, V, E> multimaps() {
        return ToMultimapKvApi.of(toStream());
    }

    /* Static factory methods */

    static @SafeVarargs <K, V, T extends Entry<K, V>> @NotNull ToMapKvApi<K, V, T> of(@Nullable T @Nullable ... items) {
        return () -> BasicStreams.streamOf(items);
    }

    static <K, V, T extends Entry<K, V>> @NotNull ToMapKvApi<K, V, T> of(@Nullable Iterable<@Nullable T> iterable) {
        return () -> BasicStreams.streamOf(iterable);
    }

    static <K, V, T extends Entry<K, V>> @NotNull ToMapKvApi<K, V, T> of(@Nullable Stream<@Nullable T> stream) {
        return () -> BasicStreams.emptyIfNull(stream);
    }

    /* Implementation */

    private <M extends Map<K, V>> @NotNull M toNonNullMap(@NotNull String methodName, @NotNull Supplier<M> supplier) {
        return collectNonNull(methodName, BasicCollectors.toMap(Entry::getKey, Entry::getValue, supplier));
    }

    private <M> @NotNull M collectNonNull(@NotNull String methodName, @NotNull Collector<E, ?, M> collector) {
        return with(toStream(), assertKvNonNull(methodName)).collect(collector);
    }
}
