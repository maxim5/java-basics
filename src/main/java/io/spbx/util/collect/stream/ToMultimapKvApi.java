package io.spbx.util.collect.stream;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import io.spbx.util.collect.stream.ToApiCommon.AcceptsNulls;
import io.spbx.util.collect.stream.ToApiCommon.DoesNotAcceptNulls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static io.spbx.util.collect.stream.ToApiCommon.assertKvNonNull;
import static io.spbx.util.func.ScopeFunctions.with;

public interface ToMultimapKvApi<K, V, E extends Entry<K, V>> extends ToStreamApi<E> {
    /* Collect into Guava `Multimap` */

    default <M extends Multimap<K, V>> @NotNull M toGuavaMultimap(@NotNull Collector<E, ?, M> collector) {
        return toStream().collect(collector);
    }

    default <M extends Multimap<K, V>> @NotNull M toGuavaMultimap(@NotNull Supplier<M> supplier) {
        return this.toGuavaMultimap(Multimaps.toMultimap(Entry::getKey, Entry::getValue, supplier));
    }

    @AcceptsNulls
    default @NotNull HashMultimap<K, V> toGuavaMultimap() {
        return this.toGuavaMultimap(HashMultimap::create);
    }

    @DoesNotAcceptNulls
    default @NotNull ImmutableListMultimap<K, V> toGuavaImmutableListMultimap() {
        return with(toStream(), assertKvNonNull("toGuavaImmutableListMultimap"))
            .collect(ImmutableListMultimap.toImmutableListMultimap(Entry::getKey, Entry::getValue));
    }

    @DoesNotAcceptNulls
    default @NotNull ImmutableSetMultimap<K, V> toGuavaImmutableSetMultimap() {
        return with(toStream(), assertKvNonNull("toGuavaImmutableSetMultimap"))
            .collect(ImmutableSetMultimap.toImmutableSetMultimap(Entry::getKey, Entry::getValue));
    }

    /* Static factory methods */

    static @SafeVarargs <K, V, T extends Entry<K, V>> @NotNull ToMultimapKvApi<K, V, T> of(@Nullable T @Nullable ... items) {
        return () -> BasicStreams.streamOf(items);
    }

    static <K, V, T extends Entry<K, V>> @NotNull ToMultimapKvApi<K, V, T> of(@Nullable Iterable<@Nullable T> iterable) {
        return () -> BasicStreams.streamOf(iterable);
    }

    static <K, V, T extends Entry<K, V>> @NotNull ToMultimapKvApi<K, V, T> of(@Nullable Stream<@Nullable T> stream) {
        return () -> BasicStreams.emptyIfNull(stream);
    }
}
