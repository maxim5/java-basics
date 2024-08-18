package io.spbx.util.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.spbx.util.func.TriConsumer;
import org.checkerframework.dataflow.qual.Pure;
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

import static io.spbx.util.base.EasyCast.castAny;
import static io.spbx.util.func.ScopeFunctions.alsoApply;

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
        return alsoApply(newMutableMap(), map -> map.put(key, val));
    }

    public static <K, V> @NotNull HashMap<K, V> mutableMapOf(@Nullable K key1, @Nullable V val1,
                                                             @Nullable K key2, @Nullable V val2) {
        return alsoApply(newMutableMap(), map -> putAll(map, key1, val1, key2, val2));
    }

    public static <K, V> @NotNull HashMap<K, V> mutableMapOf(@Nullable K key1, @Nullable V val1,
                                                             @Nullable K key2, @Nullable V val2,
                                                             @Nullable K key3, @Nullable V val3) {
        return alsoApply(newMutableMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3));
    }

    public static <K, V> @NotNull HashMap<K, V> mutableMapOf(@Nullable K key1, @Nullable V val1,
                                                             @Nullable K key2, @Nullable V val2,
                                                             @Nullable K key3, @Nullable V val3,
                                                             @Nullable K key4, @Nullable V val4) {
        return alsoApply(newMutableMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3, key4, val4));
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
        return alsoApply(newOrderedMap(), map -> map.put(key, val));
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> orderedMapOf(@Nullable K key1, @Nullable V val1,
                                                                   @Nullable K key2, @Nullable V val2) {
        return alsoApply(newOrderedMap(), map -> putAll(map, key1, val1, key2, val2));
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> orderedMapOf(@Nullable K key1, @Nullable V val1,
                                                                   @Nullable K key2, @Nullable V val2,
                                                                   @Nullable K key3, @Nullable V val3) {
        return alsoApply(newOrderedMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3));
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> orderedMapOf(@Nullable K key1, @Nullable V val1,
                                                                   @Nullable K key2, @Nullable V val2,
                                                                   @Nullable K key3, @Nullable V val3,
                                                                   @Nullable K key4, @Nullable V val4) {
        return alsoApply(newOrderedMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3, key4, val4));
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
        return alsoApply(newConcurrentMap(), map -> map.put(key, val));
    }

    public static <K, V> @NotNull ConcurrentHashMap<K, V> concurrentMapOf(@NotNull K key1, @NotNull V val1,
                                                                          @NotNull K key2, @NotNull V val2) {
        return alsoApply(newConcurrentMap(), map -> putAll(map, key1, val1, key2, val2));
    }

    public static <K, V> @NotNull ConcurrentHashMap<K, V> concurrentMapOf(@NotNull K key1, @NotNull V val1,
                                                                          @NotNull K key2, @NotNull V val2,
                                                                          @NotNull K key3, @NotNull V val3) {
        return alsoApply(newConcurrentMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3));
    }

    public static <K, V> @NotNull ConcurrentHashMap<K, V> concurrentMapOf(@NotNull K key1, @NotNull V val1,
                                                                          @NotNull K key2, @NotNull V val2,
                                                                          @NotNull K key3, @NotNull V val3,
                                                                          @NotNull K key4, @NotNull V val4) {
        return alsoApply(newConcurrentMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3, key4, val4));
    }

    public static <K extends Comparable<K>, V> @NotNull TreeMap<K, V> newSortedMap() {
        return new TreeMap<>();
    }

    public static <K extends Comparable<K>, V> @NotNull TreeMap<K, V> newSortedMap(@Nullable Map<K, V> map) {
        return map != null ? new TreeMap<>(map) : newSortedMap();
    }

    public static <K extends Comparable<K>, V> @NotNull TreeMap<K, V> sortedMapOf(@NotNull K key, @NotNull V val) {
        return alsoApply(newSortedMap(), map -> map.put(key, val));
    }

    public static <K extends Comparable<K>, V> @NotNull TreeMap<K, V> sortedMapOf(@NotNull K key1, @NotNull V val1,
                                                                                  @NotNull K key2, @NotNull V val2) {
        return alsoApply(newSortedMap(), map -> putAll(map, key1, val1, key2, val2));
    }

    public static <K extends Comparable<K>, V> @NotNull TreeMap<K, V> sortedMapOf(@NotNull K key1, @NotNull V val1,
                                                                                  @NotNull K key2, @NotNull V val2,
                                                                                  @NotNull K key3, @NotNull V val3) {
        return alsoApply(newSortedMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3));
    }

    public static <K extends Comparable<K>, V> @NotNull TreeMap<K, V> sortedMapOf(@NotNull K key1, @NotNull V val1,
                                                                                  @NotNull K key2, @NotNull V val2,
                                                                                  @NotNull K key3, @NotNull V val3,
                                                                                  @NotNull K key4, @NotNull V val4) {
        return alsoApply(newSortedMap(), map -> putAll(map, key1, val1, key2, val2, key3, val3, key4, val4));
    }

    /* Conversions to `Map` */

    @Pure
    public static <K, V> @NotNull LinkedHashMap<K, V> toMapUnchecked(@Nullable Object @Nullable ... items) {
        return items == null || items.length == 0 ? newOrderedMap() : toMapUnchecked(Arrays.asList(items));
    }

    @Pure
    public static <K, V> @NotNull LinkedHashMap<K, V> toMapUnchecked(@NotNull List<?> items) {
        return toMapUncheckedUsing(items, MapPutMethod.throwing());
    }

    @Pure
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

    @Pure
    public static <K, E> @NotNull Map<K, E> indexBy(@Nullable Iterable<@Nullable E> items,
                                                    @NotNull Function<? super E, ? extends K> keyFunc) {
        return Streamer.of(items).toGuavaImmutableMapBy(keyFunc);
    }

    @Pure
    public static <K, E> @NotNull Map<K, E> indexBy(@Nullable E @Nullable[] items,
                                                    @NotNull Function<? super E, ? extends K> keyFunc) {
        return Streamer.of(items).toGuavaImmutableMapBy(keyFunc);
    }

    /* `Map` merging */

    @Pure
    public static <K, V> @NotNull Map<K, V> mergeToMap(@Nullable Map<? extends K, ? extends V> map1,
                                                       @Nullable Map<? extends K, ? extends V> map2) {
        LinkedHashMap<K, V> result = BasicMaps.newOrderedMap(sizeOf(map1) + sizeOf(map2));
        result.putAll(emptyIfNull(map1));
        result.putAll(emptyIfNull(map2));
        return result;
    }

    @Pure
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

    @Pure
    public static int sizeOf(@Nullable Map<?, ?> map) {
        return map != null ? map.size() : 0;
    }

    @Pure
    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    @Pure
    public static <K, V> @NotNull Map<K, V> emptyIfNull(@Nullable Map<K, V> map) {
        return map != null ? map : Map.of();
    }

    @Pure
    public static <M extends Map<?, ?>> @Nullable M nullIfEmpty(@Nullable M map) {
        return isEmpty(map) ? null : map;
    }

    @Pure
    public static <K, V> @NotNull Map<V, K> inverseMap(@NotNull Map<K, V> map) {
        return Streamer.of(map).inverse().toHashMap();
    }

    /* Implementation details */

    private static <K, V> void putAll(Map<K, V> map, K key1, V val1, K key2, V val2) {
        map.put(key1, val1);
        map.put(key2, val2);
    }

    private static <V, K> void putAll(Map<K, V> map, K key1, V val1, K key2, V val2, K key3, V val3) {
        map.put(key1, val1);
        map.put(key2, val2);
        map.put(key3, val3);
    }

    private static <V, K> void putAll(Map<K, V> map, K key1, V val1, K key2, V val2, K key3, V val3, K key4, V val4) {
        map.put(key1, val1);
        map.put(key2, val2);
        map.put(key3, val3);
        map.put(key4, val4);
    }

    @CanIgnoreReturnValue
    private static <K, V> @NotNull Map<K, V> putAll(@NotNull Map<K, V> map1, @NotNull Map<K, V> map2) {
        map1.putAll(map2);
        return map1;
    }
}
