package io.spbx.util.collect;

import com.google.common.annotations.Beta;
import com.google.common.collect.Sets;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

@CanIgnoreReturnValue
public class MapBuilder<K, V> implements ToMapKvApi<K, V, Entry<K, V>> {
    private static final int DEFAULT_SIZE = 16;

    private final LinkedHashMap<K, V> map;

    private MapBuilder(@NotNull LinkedHashMap<K, V> map) {
        this.map = map;
    }

    public MapBuilder() {
        this(DEFAULT_SIZE);  // the builder is usually updated after creation, so lazy `HashMap` init is unnecessary
    }

    public MapBuilder(int size) {
        this(new LinkedHashMap<>(size));
    }

    /* Static builder constructors */

    public static <K, V> @NotNull MapBuilder<K, V> builder() {
        return new MapBuilder<>();
    }

    public static <K, V> @NotNull MapBuilder<K, V> builder(int size) {
        return new MapBuilder<>(size);
    }

    public static <K, V> @NotNull MapBuilder<K, V> of(@Nullable K key, @Nullable V val) {
        return new MapBuilder<K, V>().put(key, val);
    }

    public static <K, V> @NotNull MapBuilder<K, V> of(@Nullable K key1, @Nullable V val1,
                                                      @Nullable K key2, @Nullable V val2) {
        return new MapBuilder<K, V>().put(key1, val1, key2, val2);
    }

    public static <K, V> @NotNull MapBuilder<K, V> of(@Nullable K key1, @Nullable V val1,
                                                      @Nullable K key2, @Nullable V val2,
                                                      @Nullable K key3, @Nullable V val3) {
        return new MapBuilder<K, V>().put(key1, val1, key2, val2, key3, val3);
    }

    public static <K, V> @NotNull MapBuilder<K, V> of(@Nullable K key1, @Nullable V val1,
                                                      @Nullable K key2, @Nullable V val2,
                                                      @Nullable K key3, @Nullable V val3,
                                                      @Nullable K key4, @Nullable V val4) {
        return new MapBuilder<K, V>().put(key1, val1, key2, val2, key3, val3, key4, val4);
    }

    public static <K, V> @NotNull MapBuilder<K, V> of(@Nullable K key1, @Nullable V val1,
                                                      @Nullable K key2, @Nullable V val2,
                                                      @Nullable K key3, @Nullable V val3,
                                                      @Nullable K key4, @Nullable V val4,
                                                      @Nullable K key5, @Nullable V val5) {
        return new MapBuilder<K, V>().put(key1, val1, key2, val2, key3, val3, key4, val4, key5, val5);
    }

    public static <K, V> @NotNull MapBuilder<K, V> ofUnchecked(@Nullable K key1, @Nullable V val1,
                                                               @Nullable K key2, @Nullable V val2,
                                                               @Nullable K key3, @Nullable V val3,
                                                               @Nullable K key4, @Nullable V val4,
                                                               @Nullable K key5, @Nullable V val5,
                                                               @Nullable Object @NotNull... items) {
        return new MapBuilder<K, V>().putUnchecked(key1, val1, key2, val2, key3, val3, key4, val4, key5, val5, items);
    }

    public static <K, V> @NotNull MapBuilder<K, V> of(@Nullable Entry<K, V> entry) {
        return new MapBuilder<K, V>().put(entry);
    }

    public static <K, V> @NotNull MapBuilder<K, V> of(@Nullable Entry<K, V> entry1,
                                                      @Nullable Entry<K, V> entry2) {
        return new MapBuilder<K, V>().put(entry1).put(entry2);
    }

    public static <K, V> @NotNull MapBuilder<K, V> of(@Nullable Entry<K, V> entry1,
                                                      @Nullable Entry<K, V> entry2,
                                                      @Nullable Entry<K, V> entry3) {
        return new MapBuilder<K, V>().put(entry1).put(entry2).put(entry3);
    }

    public static <K, V> @NotNull MapBuilder<K, V> copyOf(@Nullable Map<K, V> items) {
        return new MapBuilder<K, V>(Math.max(DEFAULT_SIZE, BasicMaps.sizeOf(items))).putAll(items);
    }

    public static <K, V> @NotNull MapBuilder<K, V> copyOf(@Nullable Iterable<? extends Entry<K, V>> items) {
        return new MapBuilder<K, V>(Math.max(DEFAULT_SIZE, BasicIterables.sizeOf(items, DEFAULT_SIZE))).putAll(items);
    }

    /* `put()` methods */

    public @NotNull MapBuilder<K, V> put(@Nullable K key, @Nullable V val) {
        assert key != null || !map.containsKey(null) : "Key already exists: " + null;
        V existing = map.put(key, val);
        assert existing == null : "Key already exists: " + key;
        return this;
    }

    public @NotNull MapBuilder<K, V> put(@Nullable K key1, @Nullable V val1,
                                         @Nullable K key2, @Nullable V val2) {
        return put(key1, val1).put(key2, val2);
    }

    public @NotNull MapBuilder<K, V> put(@Nullable K key1, @Nullable V val1,
                                         @Nullable K key2, @Nullable V val2,
                                         @Nullable K key3, @Nullable V val3) {
        return put(key1, val1).put(key2, val2).put(key3, val3);
    }

    public @NotNull MapBuilder<K, V> put(@Nullable K key1, @Nullable V val1,
                                         @Nullable K key2, @Nullable V val2,
                                         @Nullable K key3, @Nullable V val3,
                                         @Nullable K key4, @Nullable V val4) {
        return put(key1, val1).put(key2, val2).put(key3, val3).put(key4, val4);
    }

    public @NotNull MapBuilder<K, V> put(@Nullable K key1, @Nullable V val1,
                                         @Nullable K key2, @Nullable V val2,
                                         @Nullable K key3, @Nullable V val3,
                                         @Nullable K key4, @Nullable V val4,
                                         @Nullable K key5, @Nullable V val5) {
        return put(key1, val1).put(key2, val2).put(key3, val3).put(key4, val4).put(key5, val5);
    }

    public @NotNull MapBuilder<K, V> putUnchecked(@Nullable K key1, @Nullable V val1,
                                                  @Nullable K key2, @Nullable V val2,
                                                  @Nullable K key3, @Nullable V val3,
                                                  @Nullable K key4, @Nullable V val4,
                                                  @Nullable K key5, @Nullable V val5,
                                                  @Nullable Object @NotNull... items) {
        return put(key1, val1, key2, val2, key3, val3, key4, val4, key5, val5).putAll(BasicMaps.toMapUnchecked(items));
    }

    public @NotNull MapBuilder<K, V> put(@Nullable Entry<K, V> entry) {
        return entry != null ? put(entry.getKey(), entry.getValue()) : this;
    }

    public @NotNull MapBuilder<K, V> putAll(@Nullable Map<K, V> items) {
        if (items == null) {
            return this;
        }
        Sets.SetView<K> existing;
        assert (existing = Sets.intersection(map.keySet(), items.keySet())).isEmpty() : "Keys already exist: " + existing;
        map.putAll(items);
        return this;
    }

    public @NotNull MapBuilder<K, V> putAll(@Nullable Iterable<? extends Entry<K, V>> items) {
        if (items != null) {
            items.forEach(this::put);
        }
        return this;
    }

    /* `overwrite()` methods */

    public @NotNull MapBuilder<K, V> overwrite(@Nullable K key, @Nullable V val) {
        map.put(key, val);
        return this;
    }

    public @NotNull MapBuilder<K, V> overwrite(@Nullable K key1, @Nullable V val1,
                                               @Nullable K key2, @Nullable V val2) {
        return overwrite(key1, val1).overwrite(key2, val2);
    }

    public @NotNull MapBuilder<K, V> overwrite(@Nullable K key1, @Nullable V val1,
                                               @Nullable K key2, @Nullable V val2,
                                               @Nullable K key3, @Nullable V val3) {
        return overwrite(key1, val1).overwrite(key2, val2).overwrite(key3, val3);
    }

    public @NotNull MapBuilder<K, V> overwrite(@Nullable K key1, @Nullable V val1,
                                               @Nullable K key2, @Nullable V val2,
                                               @Nullable K key3, @Nullable V val3,
                                               @Nullable K key4, @Nullable V val4) {
        return overwrite(key1, val1).overwrite(key2, val2).overwrite(key3, val3).overwrite(key4, val4);
    }

    public @NotNull MapBuilder<K, V> overwrite(@Nullable K key1, @Nullable V val1,
                                               @Nullable K key2, @Nullable V val2,
                                               @Nullable K key3, @Nullable V val3,
                                               @Nullable K key4, @Nullable V val4,
                                               @Nullable K key5, @Nullable V val5) {
        return overwrite(key1, val1).overwrite(key2, val2).overwrite(key3, val3).overwrite(key4, val4).overwrite(key5, val5);
    }

    public @NotNull MapBuilder<K, V> overwriteUnchecked(@Nullable K key1, @Nullable V val1,
                                                        @Nullable K key2, @Nullable V val2,
                                                        @Nullable K key3, @Nullable V val3,
                                                        @Nullable K key4, @Nullable V val4,
                                                        @Nullable K key5, @Nullable V val5,
                                                        @Nullable Object @NotNull... items) {
        return overwrite(key1, val1, key2, val2, key3, val3, key4, val4, key5, val5)
            .overwriteAll(BasicMaps.toMapUncheckedUsing(Arrays.asList(items), BasicMaps.MapPutMethod.overwrite()));
    }

    public @NotNull MapBuilder<K, V> overwrite(@Nullable Entry<K, V> entry) {
        return entry != null ? overwrite(entry.getKey(), entry.getValue()) : this;
    }

    public @NotNull MapBuilder<K, V> overwriteAll(@Nullable Map<K, V> items) {
        if (items != null) {
            map.putAll(items);
        }
        return this;
    }

    public @NotNull MapBuilder<K, V> overwriteAll(@Nullable Iterable<? extends Entry<K, V>> items) {
        if (items != null) {
            items.forEach(this::overwrite);
        }
        return this;
    }

    /* `update()` */

    @Beta
    @NotNull MapBuilder<K, V> update(@NotNull K key, @NotNull Supplier<V> value, @NotNull Consumer<V> consumer) {
        consumer.accept(map.computeIfAbsent(key, __ -> value.get()));
        return this;
    }

    /* Post-creation in-place manipulations */

    @Beta
    public @NotNull ToMapKvApi<K, V, Entry<K, V>> skipNulls() {
        map.entrySet().removeIf(e -> e.getKey() == null || e.getValue() == null);
        return this;
    }

    @Beta
    public @NotNull ToMapKvApi<K, V, Entry<K, V>> skipNullKeys() {
        map.keySet().removeIf(Objects::isNull);
        return this;
    }

    @Beta
    public @NotNull ToMapKvApi<K, V, Entry<K, V>> skipNullValues() {
        map.values().removeIf(Objects::isNull);
        return this;
    }

    /* Builders to various `Map` instances */

    @Override
    public @NotNull Stream<Entry<K, V>> toStream() {
        return map.entrySet().stream();
    }
}
