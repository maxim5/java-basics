package io.spbx.util.extern.guava;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.collect.stream.Streamer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static io.spbx.util.base.lang.EasyCast.castAny;
import static io.spbx.util.collect.map.BasicMaps.emptyIfNull;

@Stateless
@Pure
@CheckReturnValue
public class GuavaMaps {
    /* Conversions to `ImmutableMap` */

    public static <K, V> @NotNull ImmutableMap<K, V> immutableMapOf() {
        return ImmutableMap.of();
    }

    public static <K, V> @NotNull ImmutableMap<K, V> immutableMapOf(@NotNull K key, @NotNull V val) {
        return ImmutableMap.of(key, val);
    }

    public static <K, V> @NotNull ImmutableMap<K, V> immutableMapOf(@NotNull K key1, @NotNull V val1,
                                                                    @NotNull K key2, @NotNull V val2) {
        return ImmutableMap.of(key1, val1, key2, val2);
    }

    public static <K, V> @NotNull ImmutableMap<K, V> immutableMapOf(@NotNull K key1, @NotNull V val1,
                                                                    @NotNull K key2, @NotNull V val2,
                                                                    @NotNull K key3, @NotNull V val3) {
        return ImmutableMap.of(key1, val1, key2, val2, key3, val3);
    }

    public static <K, V> @NotNull ImmutableMap<K, V> immutableMapOf(@NotNull K key1, @NotNull V val1,
                                                                    @NotNull K key2, @NotNull V val2,
                                                                    @NotNull K key3, @NotNull V val3,
                                                                    @NotNull K key4, @NotNull V val4) {
        return ImmutableMap.of(key1, val1, key2, val2, key3, val3, key4, val4);
    }

    public static <K, V> @NotNull ImmutableMap<K, V> immutableMapOf(@NotNull K key1, @NotNull V val1,
                                                                    @NotNull K key2, @NotNull V val2,
                                                                    @NotNull K key3, @NotNull V val3,
                                                                    @NotNull K key4, @NotNull V val4,
                                                                    @NotNull K key5, @NotNull V val5) {
        return ImmutableMap.of(key1, val1, key2, val2, key3, val3, key4, val4, key5, val5);
    }

    public static <K, V> @NotNull ImmutableMap<K, V> immutableMapOf(@NotNull K key1, @NotNull V val1,
                                                                    @NotNull Object @NotNull... items) {
        assert items.length % 2 == 0 : "Invalid number of items: %d".formatted(items.length);
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builderWithExpectedSize(1 + items.length >> 1);
        builder.put(key1, val1);
        for (int i = 0; i < items.length; i += 2) {
            builder.put(castAny(items[i]), castAny(items[i + 1]));
        }
        return builder.buildOrThrow();
    }

    public static <K, V> @NotNull ImmutableMap<K, List<V>> deepImmutableMapOf(@NotNull Map<K, List<V>> map) {
        ImmutableMap.Builder<K, List<V>> builder = new ImmutableMap.Builder<>();
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            builder.put(entry.getKey(), ImmutableList.copyOf(entry.getValue()));
        }
        return builder.buildOrThrow();
    }

    /* `Map` merging */

    public static <K, V> @NotNull ImmutableMap<K, V> mergeToImmutableMap(@Nullable Map<? extends K, ? extends V> map1,
                                                                         @Nullable Map<? extends K, ? extends V> map2) {
        return ImmutableMap.<K, V>builder()
            .putAll(emptyIfNull(map1))
            .putAll(emptyIfNull(map2))
            .buildOrThrow();
    }

    public static <K, V> @NotNull ImmutableMap<K, V> mergeToImmutableMap(@Nullable Map<? extends K, ? extends V> map1,
                                                                         @Nullable Map<? extends K, ? extends V> map2,
                                                                         @Nullable Map<? extends K, ? extends V> map3) {
        return ImmutableMap.<K, V>builder()
            .putAll(emptyIfNull(map1))
            .putAll(emptyIfNull(map2))
            .putAll(emptyIfNull(map3))
            .buildOrThrow();
    }

    /* `Map` manipulations */

    public static <K, V> @NotNull ImmutableMap<V, K> inverseMap(@NotNull ImmutableMap<K, V> map) {
        return Streamer.of(map).inverse().toGuavaImmutableMap();
    }
}
