package io.spbx.util.props;

import io.spbx.util.props.PropertyMap.TypeExtractor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static io.spbx.util.base.lang.BasicNulls.firstNonNull;
import static io.spbx.util.base.str.BasicParsing.*;
import static java.util.Objects.requireNonNull;

public interface PropertyList {
    /* Default getters */

    @Nullable String getOrNull(int key);
    default @Nullable String getOrDie(int key) { return requireNonNull(getOrNull(key)); }
    default @NotNull Optional<String> getOptional(int key) { return Optional.ofNullable(getOrNull(key)); }
    default @NotNull String get(int key, @NotNull String def) { return firstNonNull(getOrNull(key), def); }
    default boolean contains(int key) { return getOrNull(key) != null; }

    default int getIntOrNull(int key) { return getInt(key, 0); }
    default int getIntOrDie(int key) { return parseIntSafe(getOrDie(key)); }
    default int getInt(int key, int def) { return parseIntSafe(getOrNull(key), def); }

    default long getLongOrNull(int key) { return getLong(key, 0L); }
    default long getLongOrDie(int key) { return parseLongSafe(getOrDie(key)); }
    default long getLong(int key, long def) { return parseLongSafe(getOrNull(key), def); }

    default byte getByteOrNull(int key) { return getByte(key, (byte) 0); }
    default byte getByteOrDie(int key) { return parseByteSafe(getOrDie(key)); }
    default byte getByte(int key, byte def) { return parseByteSafe(getOrNull(key), def); }

    default char getCharOrNull(int key) { return getChar(key, (char) 0); }
    default char getCharOrDie(int key) { return parseCharSafe(getOrDie(key)); }
    default char getChar(int key, char def) { return parseCharSafe(getOrNull(key), def); }

    default boolean getBoolOrFalse(int key) { return getBool(key, false); }
    default boolean getBoolOrTrue(int key) { return getBool(key, true); }
    default boolean getBoolOrDie(int key) { return parseBoolSafe(getOrDie(key)); }
    default boolean getBool(int key, boolean def) { return parseBoolSafe(getOrNull(key), def); }

    default double getDoubleOrNull(int key) { return getDouble(key, 0.0); }
    default double getDoubleOrDie(int key) { return parseDoubleSafe(getOrDie(key)); }
    default double getDouble(int key, double def) { return parseDoubleSafe(getOrNull(key), def); }

    default float getFloatOrNull(int key) { return getFloat(key, 0.0f); }
    default float getFloatOrDie(int key) { return parseFloatSafe(getOrDie(key)); }
    default float getFloat(int key, float def) { return parseFloatSafe(getOrNull(key), def); }

    /* Typed getters */

    default <T> @NotNull TypedList<T> via(@NotNull TypeExtractor<T> extractor) {
        // This below is an implementation of `TypedMap<T>`:
        //   public @NotNull Optional<T> getOptional(int key) { ... }
        return key -> PropertyList.this.getOptional(key).map(extractor);
    }

    interface TypedList<T> {
        default @Nullable T getOrNull(int key) { return getOptional(key).orElse(null); }
        default @Nullable T getOrDie(int key) { return requireNonNull(getOrNull(key)); }
        @NotNull Optional<T> getOptional(int key);
        default @NotNull T get(int key, @NotNull T def) { return firstNonNull(getOrNull(key), def); }
    }
}
