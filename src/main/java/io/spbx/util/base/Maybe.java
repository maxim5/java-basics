package io.spbx.util.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public enum Maybe {
    TRUE,
    FALSE,
    UNKNOWN;

    public static @NotNull Maybe of(boolean bool) {
        return bool ? TRUE : FALSE;
    }

    public static @NotNull Maybe ofNullable(@Nullable Boolean bool) {
        return bool == null ? UNKNOWN : Maybe.of(bool);
    }

    public static @NotNull Maybe ofOptional(@NotNull Optional<Boolean> optional) {
        return optional.map(Maybe::of).orElse(UNKNOWN);
    }

    public boolean isTrue() {
        return this == TRUE;
    }

    public boolean isFalse() {
        return this == FALSE;
    }

    public boolean isKnown() {
        return this != UNKNOWN;
    }

    public boolean isUnknown() {
        return this == UNKNOWN;
    }

    public @Nullable Boolean toBoolean() {
        return this.isUnknown() ? null : this.isTrue();
    }

    public @NotNull Optional<Boolean> toOptional() {
        return Optional.ofNullable(this.toBoolean());
    }

    public boolean knownValue() {
        assert this.isKnown();
        return this.isTrue();
    }

    public boolean orElse(boolean def) {
        return this.isUnknown() ? def : this.isTrue();
    }
}
