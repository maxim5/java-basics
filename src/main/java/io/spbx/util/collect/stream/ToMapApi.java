package io.spbx.util.collect.stream;

import com.google.common.collect.ImmutableMap;
import io.spbx.util.base.tuple.Pair;
import io.spbx.util.collect.stream.ToApiCommon.AcceptsNulls;
import io.spbx.util.collect.stream.ToApiCommon.DoesNotAcceptNulls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static io.spbx.util.collect.stream.ToApiCommon.assertNonNull;
import static io.spbx.util.func.ScopeFunctions.with;

public interface ToMapApi<E> extends ToStreamApi<E> {
    /* Collect into `Map` */

    default <K, V, M extends Map<K, V>> @NotNull M toMap(@NotNull Collector<E, ?, M> collector) {
        return toStream().collect(collector);
    }

    @AcceptsNulls
    default <K, V> @NotNull Map<K, V> toMap(
            @NotNull Function<? super E, ? extends K> keyFunc,
            @NotNull Function<? super E, ? extends V> valueFunc) {
        return toStream().collect(BasicCollectors.toMap(keyFunc, valueFunc, HashMap::new));
    }

    @AcceptsNulls
    default <K> @NotNull Map<K, E> toMapBy(@NotNull Function<? super E, ? extends K> keyFunc) {
        return toStream().collect(BasicCollectors.toMap(keyFunc, e -> e, HashMap::new));
    }

    @DoesNotAcceptNulls
    default <K, V> @NotNull ImmutableMap<K, V> toGuavaImmutableMap(
            @NotNull Function<? super E, ? extends K> keyFunc,
            @NotNull Function<? super E, ? extends V> valueFunc) {
        return with(toStream(), assertNonNull("toGuavaImmutableMap", keyFunc, valueFunc))
            .collect(ImmutableMap.toImmutableMap(keyFunc, valueFunc));
    }

    @DoesNotAcceptNulls
    default <K> @NotNull ImmutableMap<K, E> toGuavaImmutableMapBy(@NotNull Function<? super E, ? extends K> keyFunc) {
        return toGuavaImmutableMap(keyFunc, e -> e);
    }

    /* More collectors */

    default <K, V> @NotNull ToMapKvApi<K, V, Pair<K, V>> maps(
            @NotNull Function<? super E, ? extends K> keyFunc,
            @NotNull Function<? super E, ? extends V> valueFunc) {
        return ToMapKvApi.of(toStream().map(e -> toPair(e, keyFunc, valueFunc)));
    }

    default <K, V> @NotNull ToBiMapKvApi<K, V, Pair<K, V>> bimaps(
            @NotNull Function<? super E, ? extends K> keyFunc,
            @NotNull Function<? super E, ? extends V> valueFunc) {
        return ToBiMapKvApi.of(toStream().map(e -> toPair(e, keyFunc, valueFunc)));
    }

    default <K, V> @NotNull ToMultimapKvApi<K, V, Pair<K, V>> multimaps(
            @NotNull Function<? super E, ? extends K> keyFunc,
            @NotNull Function<? super E, ? extends V> valueFunc) {
        return ToMultimapKvApi.of(toStream().map(e -> toPair(e, keyFunc, valueFunc)));
    }

    /* Static factory methods */

    static @SafeVarargs <T> @NotNull ToMapApi<T> of(@Nullable T @Nullable ... items) {
        return () -> BasicStreams.streamOf(items);
    }

    static <T> @NotNull ToMapApi<T> of(@Nullable Iterable<@Nullable T> iterable) {
        return () -> BasicStreams.streamOf(iterable);
    }

    static <T> @NotNull ToMapApi<T> of(@Nullable Stream<@Nullable T> stream) {
        return () -> BasicStreams.emptyIfNull(stream);
    }

    /* Implementation */

    private static @Nullable <E, K, V> Pair<K, V> toPair(
            @Nullable E entry,
            @NotNull Function<? super E, ? extends K> keyFunc,
            @NotNull Function<? super E, ? extends V> valueFunc) {
        return entry == null ? null : Pair.of(keyFunc.apply(entry), valueFunc.apply(entry));
    }
}
