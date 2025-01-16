package io.spbx.util.props;

import io.spbx.util.security.Secret;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static io.spbx.util.base.lang.BasicNulls.firstNonNullIfExists;

public interface StandardMutableProperties extends StandardProperties, MutablePropertyMap {
    /* Specialized shortcuts for setters */

    default <T extends Enum<T>> @Nullable String setEnum(@NotNull String key, @NotNull T val) {
        return setString(key, val.name());
    }
    default <T extends Enum<T>> @Nullable String setEnum(@NotNull PropertyKeyDefault<T> prop, @NotNull T val) {
        return setEnum(prop.key(), val);
    }

    default void setPath(@NotNull String key, @NotNull Path val) { via(PATH).setTyped(key, val); }
    default void setPath(@NotNull PropertyKeyDefault<Path> property, @NotNull Path val) {
        via(PATH).setTyped(property, val);
    }

    default void setPaths(@NotNull String key, @NotNull List<Path> val) { via(PATHS).setTyped(key, val); }
    default void setPaths(@NotNull PropertyKeyDefault<List<Path>> property, @NotNull List<Path> val) {
        via(PATHS).setTyped(property, val);
    }

    default void setSecret(@NotNull String key, @NotNull Secret val) { via(SECRET).setTyped(key, val); }
    default void setSecret(@NotNull PropertyKeyDefault<Secret> property, @NotNull Secret val) {
        via(SECRET).setTyped(property, val);
    }

    default void setDuration(@NotNull String key, @NotNull Duration val) { via(DURATION).setTyped(key, val); }
    default void setDuration(@NotNull PropertyKeyDefault<Duration> property, @NotNull Duration val) {
        via(DURATION).setTyped(property, val);
    }

    /* Standardization */

    static @NotNull StandardMutableProperties system() {
        return new StandardMutableProperties() {
            @Override public @Nullable String getOrNull(@NotNull String key) {
                return System.getProperty(key);
            }
            @Override public @Nullable String setString(@NotNull String key, @NotNull String val) {
                return System.setProperty(key, val);
            }
        };
    }

    @Override default @NotNull StandardMutableProperties chainedWith(@NotNull PropertyMap backup) {
        return new StandardMutableProperties() {
            @Override public @Nullable String setString(@NotNull String key, @NotNull String val) {
                return StandardMutableProperties.this.setString(key, val);
            }
            @Override public @Nullable String getOrNull(@NotNull String key) {
                return firstNonNullIfExists(StandardMutableProperties.this.getOrNull(key), () -> backup.getOrNull(key));
            }
        };
    }

    @Override default @NotNull StandardMutableProperties standardize() {
        return this;
    }

    static @NotNull StandardMutableProperties standardize(@NotNull MutablePropertyMap propertyMap) {
        return propertyMap instanceof StandardMutableProperties standard ? standard : new StandardMutableProperties() {
            @Override public @Nullable String getOrNull(@NotNull String key) {
                return propertyMap.getOrNull(key);
            }
            @Override public @Nullable String setString(@NotNull String key, @NotNull String val) {
                return propertyMap.setString(key, val);
            }
        };
    }
}
