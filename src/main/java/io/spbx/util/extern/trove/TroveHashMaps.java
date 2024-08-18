package io.spbx.util.extern.trove;

import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;
import io.spbx.util.base.BasicStrings;
import io.spbx.util.collect.StringContentMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class TroveHashMaps {
    public static class StringHashMap<V> extends TCustomHashMap<String, V> implements Map<String, V> {
        public StringHashMap() {
            this(MapOptions.of());
        }
        public StringHashMap(int initialCapacity) {
            this(MapOptions.of(), initialCapacity);
        }
        public StringHashMap(int initialCapacity, float loadFactor) {
            this(MapOptions.of(), initialCapacity, loadFactor);
        }
        public StringHashMap(@NotNull Map<? extends String, ? extends V> map) {
            this(MapOptions.of(), map);
        }
        public StringHashMap(@NotNull TCustomHashMap<? extends String, ? extends V> map) {
            this(MapOptions.of(), map);
        }

        public StringHashMap(@NotNull MapOptions options) {
            super(options.toHashingStrategyForStrings());
        }
        public StringHashMap(@NotNull MapOptions options, int initialCapacity) {
            super(options.toHashingStrategyForStrings(), initialCapacity);
        }
        public StringHashMap(@NotNull MapOptions options, int initialCapacity, float loadFactor) {
            super(options.toHashingStrategyForStrings(), initialCapacity, loadFactor);
        }
        public StringHashMap(@NotNull MapOptions options, @NotNull Map<? extends String, ? extends V> map) {
            super(options.toHashingStrategyForStrings(), map);
        }
        public StringHashMap(@NotNull MapOptions options, @NotNull TCustomHashMap<? extends String, ? extends V> map) {
            super(options.toHashingStrategyForStrings(), map);
        }

        public static <V> @NotNull StringHashMap<V> newMap() {
            return new StringHashMap<>(MapOptions.of());
        }
        public static <V> @NotNull StringHashMap<V> newIgnoreCaseMap() {
            return new StringHashMap<>(MapOptions.of(false, true));
        }
        public static <V> @NotNull StringHashMap<V> newAllowingNullsMap() {
            return new StringHashMap<>(MapOptions.of(true, false));
        }

        @Override public @Nullable V put(String key, V value) {
            return super.put(key == null ? null : key.intern(), value);
        }
        @Override public @Nullable V putIfAbsent(String key, V value) {
            return super.putIfAbsent(key == null ? null : key.intern(), value);
        }
    }

    public static class StringContentHashMap<V> extends TCustomHashMap<CharSequence, V> implements StringContentMap<V> {
        public StringContentHashMap() {
            this(MapOptions.of());
        }
        public StringContentHashMap(int initialCapacity) {
            this(MapOptions.of(), initialCapacity);
        }
        public StringContentHashMap(int initialCapacity, float loadFactor) {
            this(MapOptions.of(), initialCapacity, loadFactor);
        }
        public StringContentHashMap(@NotNull Map<? extends CharSequence, ? extends V> map) {
            this(MapOptions.of(), map);
        }
        public StringContentHashMap(@NotNull TCustomHashMap<? extends CharSequence, ? extends V> map) {
            this(MapOptions.of(), map);
        }

        public StringContentHashMap(@NotNull MapOptions options) {
            super(options.toHashingStrategyForContent());
        }
        public StringContentHashMap(@NotNull MapOptions options, int initialCapacity) {
            super(options.toHashingStrategyForContent(), initialCapacity);
        }
        public StringContentHashMap(@NotNull MapOptions options, int initialCapacity, float loadFactor) {
            super(options.toHashingStrategyForContent(), initialCapacity, loadFactor);
        }
        public StringContentHashMap(@NotNull MapOptions options, @NotNull Map<? extends CharSequence, ? extends V> map) {
            super(options.toHashingStrategyForContent(), map);
        }
        public StringContentHashMap(@NotNull MapOptions options, @NotNull TCustomHashMap<? extends CharSequence, ? extends V> map) {
            super(options.toHashingStrategyForContent(), map);
        }

        public static <V> @NotNull StringContentHashMap<V> newMap() {
            return new StringContentHashMap<>(MapOptions.of());
        }
        public static <V> @NotNull StringContentHashMap<V> newIgnoreCaseMap() {
            return new StringContentHashMap<>(MapOptions.of(false, true));
        }
        public static <V> @NotNull StringContentHashMap<V> newAllowingNullsMap() {
            return new StringContentHashMap<>(MapOptions.of(true, false));
        }

        @Override public @Nullable V get(Object key) {
            return super.get(StringContentMap.toCharSequence(key));
        }
        @Override public @Nullable V put(CharSequence key, V value) {
            return super.put(BasicStrings.intern(key), value);
        }
        @Override public @Nullable V putIfAbsent(CharSequence key, V value) {
            return super.putIfAbsent(BasicStrings.intern(key), value);
        }
        @Override public V getOrDefault(Object key, V defaultValue) {
            return super.getOrDefault(StringContentMap.toCharSequence(key), defaultValue);
        }
        @Override public boolean containsKey(Object key) {
            return super.containsKey(StringContentMap.toCharSequence(key));
        }
        @Override public @Nullable V remove(Object key) {
            return super.remove(StringContentMap.toCharSequence(key));
        }
    }

    public record MapOptions(boolean allowNullKeys, boolean ignoreCase) {
        public static @NotNull MapOptions of(boolean allowNullKeys, boolean ignoreCase) {
            return new MapOptions(allowNullKeys, ignoreCase);
        }
        public static @NotNull MapOptions of() {
            return new MapOptions(false, false);
        }
        public @NotNull MapOptions withNullKeys() {
            return new MapOptions(true, ignoreCase);
        }
        public @NotNull MapOptions withIgnoreCase() {
            return new MapOptions(allowNullKeys, true);
        }

        @NotNull HashingStrategy<String> toHashingStrategyForStrings() {
            return ignoreCase
                ? allowNullKeys ? TroveHashing.STRING_IGNORE_CASE_NULL_EQUIVALENCE : TroveHashing.STRING_IGNORE_CASE_EQUIVALENCE
                : TroveHashing.DefaultEquivalence.instance();
        }

        @NotNull HashingStrategy<CharSequence> toHashingStrategyForContent() {
            return ignoreCase
                ? allowNullKeys ? TroveHashing.CONTENT_IGNORE_CASE_NULL_EQUIVALENCE : TroveHashing.CONTENT_IGNORE_CASE_EQUIVALENCE
                : allowNullKeys ? TroveHashing.CONTENT_NULL_EQUIVALENCE : TroveHashing.CONTENT_EQUIVALENCE;
        }
    }
}
