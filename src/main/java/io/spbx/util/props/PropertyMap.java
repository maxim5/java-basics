package io.spbx.util.props;

import io.spbx.util.func.Chains;
import io.spbx.util.func.Reversible;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

import static io.spbx.util.base.BasicNulls.firstNonNull;
import static io.spbx.util.base.BasicNulls.firstNonNullIfExist;
import static io.spbx.util.base.BasicParsing.*;
import static java.util.Objects.requireNonNull;

@CheckReturnValue
public interface PropertyMap {
    /* Default getters */

    @Nullable String getOrNull(@NotNull String key);
    default @Nullable String getOrNull(@NotNull PropertyKey property) { return getOrNull(property.key()); }
    default @Nullable String getOrDie(@NotNull String key) { return requireNonNull(getOrNull(key)); }
    default @Nullable String getOrDie(@NotNull PropertyKey property) { return getOrDie(property.key()); }
    default @NotNull Optional<String> getOptional(@NotNull String key) { return Optional.ofNullable(getOrNull(key)); }
    default @NotNull Optional<String> getOptional(@NotNull PropertyKey property) { return getOptional(property.key()); }
    default @NotNull String get(@NotNull String key, @NotNull String def) { return firstNonNull(getOrNull(key), def); }
    default @NotNull String get(@NotNull Property property) { return get(property.key(), property.def()); }

    default int getIntOrNull(@NotNull String key) { return getInt(key, 0); }
    default int getIntOrDie(@NotNull String key) { return parseIntSafe(getOrDie(key)); }
    default int getInt(@NotNull String key, int def) { return parseIntSafe(getOrNull(key), def); }
    default int getInt(@NotNull IntProperty property) { return getInt(property.key(), property.def()); }

    default long getLongOrNull(@NotNull String key) { return getLong(key, 0L); }
    default long getLongOrDie(@NotNull String key) { return parseLongSafe(getOrDie(key)); }
    default long getLong(@NotNull String key, long def) { return parseLongSafe(getOrNull(key), def); }
    default long getLong(@NotNull LongProperty property) { return getLong(property.key(), property.def()); }

    default byte getByteOrNull(@NotNull String key) { return getByte(key, (byte) 0); }
    default byte getByteOrDie(@NotNull String key) { return parseByteSafe(getOrDie(key)); }
    default byte getByte(@NotNull String key, byte def) { return parseByteSafe(getOrNull(key), def); }
    default byte getByte(@NotNull ByteProperty property) { return getByte(property.key(), property.def()); }

    default boolean getBoolOrFalse(@NotNull String key) { return getBool(key, false); }
    default boolean getBoolOrTrue(@NotNull String key) { return getBool(key, true); }
    default boolean getBoolOrDie(@NotNull String key) { return parseBoolSafe(getOrDie(key)); }
    default boolean getBool(@NotNull String key, boolean def) { return parseBoolSafe(getOrNull(key), def); }
    default boolean getBool(@NotNull BoolProperty property) { return getBool(property.key(), property.def()); }

    default double getDoubleOrNull(@NotNull String key) { return getDouble(key, 0.0); }
    default double getDoubleOrDie(@NotNull String key) { return parseDoubleSafe(getOrDie(key)); }
    default double getDouble(@NotNull String key, double def) { return parseDoubleSafe(getOrNull(key), def); }
    default double getDouble(@NotNull DoubleProperty property) { return getDouble(property.key(), property.def()); }

    default float getFloatOrNull(@NotNull String key) { return getFloat(key, 0.0f); }
    default float getFloatOrDie(@NotNull String key) { return parseFloatSafe(getOrDie(key)); }
    default float getFloat(@NotNull String key, float def) { return parseFloatSafe(getOrNull(key), def); }
    default float getFloat(@NotNull FloatProperty property) { return getFloat(property.key(), property.def()); }

    /* Typed getters */

    default <T> @NotNull TypedMap<T> via(@NotNull TypeExtractor<T> extractor) {
        // This below is an implementation of `TypedMap<T>`:
        //   public @NotNull Optional<T> getOptional(@NotNull String key) { ... }
        return key -> PropertyMap.this.getOptional(key).map(extractor);
    }

    interface TypeExtractor<T> extends Reversible<String, T> {
        default @Override @NotNull String backward(@NotNull T t) {
            return t.toString();
        }

        default @NotNull <U> TypeExtractor<U> map(@NotNull Reversible<T, U> reversible) {
            return TypeExtractor.of(Chains.chain(this::forward, reversible::forward),
                                    Chains.chain(reversible::backward, this::backward));
        }

        static <T> @NotNull TypeExtractor<T> of(@NotNull Reversible<String, T> reversible) {
            return TypeExtractor.of(reversible::forward, reversible::backward);
        }

        static <T> @NotNull TypeExtractor<T> of(@NotNull Function<String, T> forward, @NotNull Function<T, String> backward) {
            return new TypeExtractor<>() {
                @Override public @NotNull T forward(@NotNull String s) { return forward.apply(s); }
                @Override public @NotNull String backward(@NotNull T t) { return backward.apply(t); }
            };
        }
    }

    interface TypedMap<T> {
        default @Nullable T getOrNull(@NotNull String key) { return getOptional(key).orElse(null); }
        default @Nullable T getOrDie(@NotNull String key) { return requireNonNull(getOrNull(key)); }
        @NotNull Optional<T> getOptional(@NotNull String key);
        default @NotNull Optional<T> getOptional(@NotNull PropertyKey property) { return getOptional(property.key()); }
        default @NotNull T get(@NotNull String key, @NotNull T def) { return firstNonNull(getOrNull(key), def); }
        default @NotNull T get(@NotNull PropertyKeyDefault<T> property) { return get(property.key(), property.def()); }
    }

    /* Default `Property` classes */

    interface PropertyKey {
        @NotNull String key();
    }

    interface PropertyKeyDefault<T> extends PropertyKey {
        @NotNull T def();
    }

    record Property(@NotNull String key, @NotNull String def) implements PropertyKeyDefault<String> {
        public static @NotNull Property of(@NotNull String key, @NotNull String def) { return new Property(key, def); }
        public static @NotNull Property of(@NotNull String key) { return of(key, ""); }
    }

    record IntProperty(@NotNull String key, int def) implements PropertyKey {
        public static @NotNull IntProperty of(@NotNull String key, int def) { return new IntProperty(key, def); }
        public static @NotNull IntProperty of(@NotNull String key) { return of(key, 0); }
    }

    record LongProperty(@NotNull String key, long def) implements PropertyKey {
        public static @NotNull LongProperty of(@NotNull String key, long def) { return new LongProperty(key, def); }
        public static @NotNull LongProperty of(@NotNull String key) { return of(key, 0); }
    }

    record ByteProperty(@NotNull String key, byte def) implements PropertyKey {
        public static @NotNull ByteProperty of(@NotNull String key, byte def) { return new ByteProperty(key, def); }
        public static @NotNull ByteProperty of(@NotNull String key) { return of(key, (byte) 0); }
    }

    record BoolProperty(@NotNull String key, boolean def) implements PropertyKey {
        public static @NotNull BoolProperty of(@NotNull String key, boolean def) { return new BoolProperty(key, def); }
        public static @NotNull BoolProperty of(@NotNull String key) { return of(key, false); }
    }

    record DoubleProperty(@NotNull String key, double def) implements PropertyKey {
        public static @NotNull DoubleProperty of(@NotNull String key, double def) { return new DoubleProperty(key, def); }
        public static @NotNull DoubleProperty of(@NotNull String key) { return of(key, 0.0); }
    }

    record FloatProperty(@NotNull String key, float def) implements PropertyKey {
        public static @NotNull FloatProperty of(@NotNull String key, float def) { return new FloatProperty(key, def); }
        public static @NotNull FloatProperty of(@NotNull String key) { return of(key, 0.0f); }
    }

    record TypedProperty<T>(@NotNull String key, @NotNull T def) implements PropertyKeyDefault<T> {
        public static <T> @NotNull TypedProperty<T> of(@NotNull String key, @NotNull T def) {
            return new TypedProperty<>(key, def);
        }
    }

    /* Chaining */

    static @NotNull PropertyMap system() {
        return System::getProperty;
    }

    default @NotNull PropertyMap chainedWith(@NotNull PropertyMap backup) {
        return key -> firstNonNullIfExist(this.getOrNull(key), () -> backup.getOrNull(key));
    }

    default @NotNull StandardProperties standardize() {
        return StandardProperties.standardize(this);
    }
}
