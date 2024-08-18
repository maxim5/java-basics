package io.spbx.util.collect;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import io.spbx.util.collect.ToApiCommon.AcceptsNulls;
import io.spbx.util.collect.ToApiCommon.DoesNotAcceptNulls;
import io.spbx.util.extern.guava.GuavaBiMaps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static io.spbx.util.collect.ToApiCommon.assertKvNonNull;
import static io.spbx.util.extern.guava.GuavaBiMaps.BiMapPutMethod.overwrite;
import static io.spbx.util.extern.guava.GuavaBiMaps.BiMapPutMethod.throwing;
import static io.spbx.util.func.ScopeFunctions.with;

public interface ToBiMapKvApi<K, V, E extends Entry<K, V>> extends ToStreamApi<E> {
    /* Collect into Guava `BiMap` */

    default <M extends BiMap<K, V>> @NotNull M toGuavaBiMap(@NotNull Collector<E, ?, M> collector) {
        return toStream().collect(collector);
    }

    @AcceptsNulls
    default @NotNull BiMap<K, V> toGuavaBiMap() {
        return this.toGuavaBiMap(GuavaBiMaps.toHashBiMap(Entry::getKey, Entry::getValue, throwing()));
    }

    @AcceptsNulls
    default @NotNull BiMap<K, V> toGuavaBiMapOverwriteDuplicates() {
        return this.toGuavaBiMap(GuavaBiMaps.toHashBiMap(Entry::getKey, Entry::getValue, overwrite()));
    }

    @DoesNotAcceptNulls
    default @NotNull ImmutableBiMap<K, V> toGuavaImmutableBiMap() {
        return with(toStream(), assertKvNonNull("toGuavaImmutableBiMap()"))
            .collect(ImmutableBiMap.toImmutableBiMap(Entry::getKey, Entry::getValue));
    }

    /* Static factory methods */

    static @SafeVarargs <K, V, T extends Entry<K, V>> @NotNull ToBiMapKvApi<K, V, T> of(@Nullable T @Nullable ... items) {
        return () -> BasicStreams.streamOf(items);
    }

    static <K, V, T extends Entry<K, V>> @NotNull ToBiMapKvApi<K, V, T> of(@Nullable Iterable<@Nullable T> iterable) {
        return () -> BasicStreams.streamOf(iterable);
    }

    static <K, V, T extends Entry<K, V>> @NotNull ToBiMapKvApi<K, V, T> of(@Nullable Stream<@Nullable T> stream) {
        return () -> BasicStreams.emptyIfNull(stream);
    }
}
