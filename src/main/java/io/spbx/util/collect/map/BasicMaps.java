package io.spbx.util.collect.map;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.NonPure;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.collect.stream.Streamer;
import io.spbx.util.func.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static io.spbx.util.base.lang.EasyCast.castAny;
import static io.spbx.util.func.ScopeFunctions.also;

@Stateless
@Pure
@CheckReturnValue
public class BasicMaps {
    /* Standard `Map` factory methods */

    public static <K, V> @NotNull HashMap<K, V> newMutableMap() {
        return new HashMap<>();
    }

    public static <K, V> @NotNull HashMap<K, V> newMutableMap(int size) {
        return new HashMap<>(size);
    }

    public static <K, V> @NotNull HashMap<K, V> newMutableMap(@Nullable Map<K, V> map) {
        return map != null ? new HashMap<>(map) : newMutableMap();
    }

    public static <K, V> @NotNull HashMap<K, V> mutableMapOf(@Nullable K key, @Nullable V val) {
        return also(newMutableMap(), map -> map.put(key, val));
    }

    public static <K, V> @NotNull HashMap<K, V> mutableMapOf(@Nullable K key1, @Nullable V val1,
                                                             @Nullable K key2, @Nullable V val2) {
        return also(newMutableMap(), map -> putAll(map, key1, val1, key2, val2));
    }

    public static <K, V> @NotNull HashMap<K, V> mutableMapOf(@Nullable K key1, @Nullable V val1,
                                                             @Nullable K key2, @Nullable V val2,
                                                             @Nullable K key3, @Nullable V val3) {
        return also(newMutableMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3));
    }

    public static <K, V> @NotNull HashMap<K, V> mutableMapOf(@Nullable K key1, @Nullable V val1,
                                                             @Nullable K key2, @Nullable V val2,
                                                             @Nullable K key3, @Nullable V val3,
                                                             @Nullable K key4, @Nullable V val4) {
        return also(newMutableMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3, key4, val4));
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> newOrderedMap() {
        return new LinkedHashMap<>();
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> newOrderedMap(int size) {
        return new LinkedHashMap<>(size);
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> newOrderedMap(@Nullable Map<K, V> map) {
        return map != null ? new LinkedHashMap<>(map) : newOrderedMap();
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> orderedMapOf(@Nullable K key, @Nullable V val) {
        return also(newOrderedMap(), map -> map.put(key, val));
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> orderedMapOf(@Nullable K key1, @Nullable V val1,
                                                                   @Nullable K key2, @Nullable V val2) {
        return also(newOrderedMap(), map -> putAll(map, key1, val1, key2, val2));
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> orderedMapOf(@Nullable K key1, @Nullable V val1,
                                                                   @Nullable K key2, @Nullable V val2,
                                                                   @Nullable K key3, @Nullable V val3) {
        return also(newOrderedMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3));
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> orderedMapOf(@Nullable K key1, @Nullable V val1,
                                                                   @Nullable K key2, @Nullable V val2,
                                                                   @Nullable K key3, @Nullable V val3,
                                                                   @Nullable K key4, @Nullable V val4) {
        return also(newOrderedMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3, key4, val4));
    }

    public static <K, V> @NotNull ConcurrentHashMap<K, V> newConcurrentMap() {
        return new ConcurrentHashMap<>();
    }

    public static <K, V> @NotNull ConcurrentHashMap<K, V> newConcurrentMap(int size) {
        return new ConcurrentHashMap<>(size);
    }

    public static <K, V> @NotNull ConcurrentHashMap<K, V> newConcurrentMap(@Nullable Map<K, V> map) {
        return map != null ? new ConcurrentHashMap<>(map) : newConcurrentMap();
    }

    public static <K, V> @NotNull ConcurrentHashMap<K, V> concurrentMapOf(@NotNull K key, @NotNull V val) {
        return also(newConcurrentMap(), map -> map.put(key, val));
    }

    public static <K, V> @NotNull ConcurrentHashMap<K, V> concurrentMapOf(@NotNull K key1, @NotNull V val1,
                                                                          @NotNull K key2, @NotNull V val2) {
        return also(newConcurrentMap(), map -> putAll(map, key1, val1, key2, val2));
    }

    public static <K, V> @NotNull ConcurrentHashMap<K, V> concurrentMapOf(@NotNull K key1, @NotNull V val1,
                                                                          @NotNull K key2, @NotNull V val2,
                                                                          @NotNull K key3, @NotNull V val3) {
        return also(newConcurrentMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3));
    }

    public static <K, V> @NotNull ConcurrentHashMap<K, V> concurrentMapOf(@NotNull K key1, @NotNull V val1,
                                                                          @NotNull K key2, @NotNull V val2,
                                                                          @NotNull K key3, @NotNull V val3,
                                                                          @NotNull K key4, @NotNull V val4) {
        return also(newConcurrentMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3, key4, val4));
    }

    public static <K extends Comparable<K>, V> @NotNull TreeMap<K, V> newSortedMap() {
        return new TreeMap<>();
    }

    public static <K extends Comparable<K>, V> @NotNull TreeMap<K, V> newSortedMap(@Nullable Map<K, V> map) {
        return map != null ? new TreeMap<>(map) : newSortedMap();
    }

    public static <K extends Comparable<K>, V> @NotNull TreeMap<K, V> sortedMapOf(@NotNull K key, @NotNull V val) {
        return also(newSortedMap(), map -> map.put(key, val));
    }

    public static <K extends Comparable<K>, V> @NotNull TreeMap<K, V> sortedMapOf(@NotNull K key1, @NotNull V val1,
                                                                                  @NotNull K key2, @NotNull V val2) {
        return also(newSortedMap(), map -> putAll(map, key1, val1, key2, val2));
    }

    public static <K extends Comparable<K>, V> @NotNull TreeMap<K, V> sortedMapOf(@NotNull K key1, @NotNull V val1,
                                                                                  @NotNull K key2, @NotNull V val2,
                                                                                  @NotNull K key3, @NotNull V val3) {
        return also(newSortedMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3));
    }

    public static <K extends Comparable<K>, V> @NotNull TreeMap<K, V> sortedMapOf(@NotNull K key1, @NotNull V val1,
                                                                                  @NotNull K key2, @NotNull V val2,
                                                                                  @NotNull K key3, @NotNull V val3,
                                                                                  @NotNull K key4, @NotNull V val4) {
        return also(newSortedMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3, key4, val4));
    }

    /* Conversions to `Map` */

    public static <K, V> @NotNull LinkedHashMap<K, V> toMapUnchecked(@Nullable Object @Nullable ... items) {
        return items == null || items.length == 0 ? newOrderedMap() : toMapUnchecked(Arrays.asList(items));
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> toMapUnchecked(@NotNull List<?> items) {
        return toMapUncheckedUsing(items, MapPutMethod.throwing());
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> toMapUncheckedUsing(@NotNull List<?> items,
                                                                          @NotNull MapPutMethod<K, V> putMethod) {
        int size = items.size();
        assert size % 2 == 0 : "Invalid number of items to build Map from: %d".formatted(size);
        LinkedHashMap<K, V> result = newOrderedMap(size >> 1);
        for (int i = 0; i < size; i += 2) {
            putMethod.accept(result, castAny(items.get(i)), castAny(items.get(i + 1)));
        }
        return result;
    }

    public interface MapPutMethod<K, V> extends TriConsumer<Map<K, V>, K, V> {
        static <K, V> @NotNull MapPutMethod<K, V> overwrite() {
            return Map::put;
        }

        static <K, V> @NotNull MapPutMethod<K, V> throwing() {
            return (map, k, v) -> {
                V existing = map.put(k, v);
                assert existing == null : "Multiple entries with the same key: " + k;
            };
        }
    }

    /* Indexing */

    public static <K, E> @NotNull Map<K, E> indexBy(@Nullable Iterable<@Nullable E> items,
                                                    @NotNull Function<? super E, ? extends K> keyFunc) {
        return Streamer.of(items).toGuavaImmutableMapBy(keyFunc);
    }

    public static <K, E> @NotNull Map<K, E> indexBy(@Nullable E @Nullable [] items,
                                                    @NotNull Function<? super E, ? extends K> keyFunc) {
        return Streamer.of(items).toGuavaImmutableMapBy(keyFunc);
    }

    /* `Map` merging */

    public static <K, V> @NotNull Map<K, V> mergeToMap(@Nullable Map<? extends K, ? extends V> map1,
                                                       @Nullable Map<? extends K, ? extends V> map2) {
        LinkedHashMap<K, V> result = BasicMaps.newOrderedMap(sizeOf(map1) + sizeOf(map2));
        result.putAll(emptyIfNull(map1));
        result.putAll(emptyIfNull(map2));
        return result;
    }

    public static <K, V> @NotNull Map<K, V> mergeToMap(@Nullable Map<? extends K, ? extends V> map1,
                                                       @Nullable Map<? extends K, ? extends V> map2,
                                                       @Nullable Map<? extends K, ? extends V> map3) {
        LinkedHashMap<K, V> result = BasicMaps.newOrderedMap(sizeOf(map1) + sizeOf(map2) + sizeOf(map3));
        result.putAll(emptyIfNull(map1));
        result.putAll(emptyIfNull(map2));
        result.putAll(emptyIfNull(map3));
        return result;
    }

    /* `Map` manipulations */

    public static int sizeOf(@Nullable Map<?, ?> map) {
        return map != null ? map.size() : 0;
    }

    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static <K, V> @NotNull Map<K, V> emptyIfNull(@Nullable Map<K, V> map) {
        return map != null ? map : Map.of();
    }

    public static <M extends Map<?, ?>> @Nullable M nullIfEmpty(@Nullable M map) {
        return isEmpty(map) ? null : map;
    }

    public static <K, V> @NotNull Map<V, K> inverseMap(@NotNull Map<K, V> map) {
        return Streamer.of(map).inverse().toHashMap();
    }

    /* Implementation details */

    @NonPure
    private static <K, V> void putAll(Map<K, V> map, K key1, V val1, K key2, V val2) {
        map.put(key1, val1);
        map.put(key2, val2);
    }

    @NonPure
    private static <V, K> void putAll(Map<K, V> map, K key1, V val1, K key2, V val2, K key3, V val3) {
        map.put(key1, val1);
        map.put(key2, val2);
        map.put(key3, val3);
    }

    @NonPure
    private static <V, K> void putAll(Map<K, V> map, K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4) {
        map.put(key1, val1);
        map.put(key2, val2);
        map.put(key3, val3);
        map.put(key4, val4);
    }

    @NonPure
    @CanIgnoreReturnValue
    private static <K, V> @NotNull Map<K, V> putAll(@NotNull Map<K, V> map1, @NotNull Map<K, V> map2) {
        map1.putAll(map2);
        return map1;
    }
}
