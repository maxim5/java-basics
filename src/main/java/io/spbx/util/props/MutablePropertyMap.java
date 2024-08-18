package io.spbx.util.props;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static io.spbx.util.base.BasicNulls.firstNonNullIfExist;

@CanIgnoreReturnValue
public interface MutablePropertyMap extends PropertyMap {
    /* Default setters */

    @Nullable String setString(@NotNull String key, @NotNull String val);
    default @Nullable String setString(@NotNull Property prop, @NotNull String val) { return setString(prop.key(), val); }

    default @Nullable String setInt(@NotNull String key, int val) { return setString(key, Integer.toString(val)); }
    default @Nullable String setInt(@NotNull IntProperty prop, int val) { return setInt(prop.key(), val); }

    default @Nullable String setLong(@NotNull String key, long val) { return setString(key, Long.toString(val)); }
    default @Nullable String setLong(@NotNull LongProperty prop, long val) { return setLong(prop.key(), val); }

    default @Nullable String setByte(@NotNull String key, byte val) { return setString(key, Byte.toString(val)); }
    default @Nullable String setByte(@NotNull ByteProperty prop, byte val) { return setByte(prop.key(), val); }

    default @Nullable String setBool(@NotNull String key, boolean val) { return setString(key, Boolean.toString(val)); }
    default @Nullable String setBool(@NotNull BoolProperty prop, boolean val) { return setBool(prop.key(), val); }

    default @Nullable String setDouble(@NotNull String key, double val) { return setString(key, Double.toString(val)); }
    default @Nullable String setDouble(@NotNull DoubleProperty prop, double val) { return setDouble(prop.key(), val); }

    default @Nullable String setFloat(@NotNull String key, float val) { return setString(key, Float.toString(val)); }
    default @Nullable String setFloat(@NotNull FloatProperty prop, float val) { return setFloat(prop.key(), val); }

    /* Typed setters */

    @Override
    default <T> @NotNull MutableTypedMap<T> via(@NotNull TypeExtractor<T> extractor) {
        return new MutableTypedMap<>() {
            @Override public void setTyped(@NotNull String key, @NotNull T val) {
                MutablePropertyMap.this.setString(key, extractor.backward(val));
            }
            @Override public @NotNull Optional<T> getOptional(@NotNull String key) {
                return MutablePropertyMap.this.getOptional(key).map(extractor);
            }
        };
    }

    interface MutableTypedMap<T> extends TypedMap<T> {
        void setTyped(@NotNull String key, @NotNull T val);
        default void setTyped(@NotNull PropertyKeyDefault<T> prop, @NotNull T val) { setTyped(prop.key(), val); }
    }

    /* Chaining */

    static @NotNull MutablePropertyMap system() {
        return new MutablePropertyMap() {
            @Override public @Nullable String getOrNull(@NotNull String key) {
                return System.getProperty(key);
            }
            @Override public @Nullable String setString(@NotNull String key, @NotNull String val) {
                return System.setProperty(key, val);
            }
        };
    }

    @Override
    default @NotNull MutablePropertyMap chainedWith(@NotNull PropertyMap backup) {
        return new MutablePropertyMap() {
            @Override public @Nullable String setString(@NotNull String key, @NotNull String val) {
                return MutablePropertyMap.this.setString(key, val);
            }
            @Override public @Nullable String getOrNull(@NotNull String key) {
                return firstNonNullIfExist(MutablePropertyMap.this.getOrNull(key), () -> backup.getOrNull(key));
            }
        };
    }

    @Override
    default @NotNull StandardMutableProperties standardize() {
        return StandardMutableProperties.standardize(this);
    }
}
