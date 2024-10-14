package io.spbx.util.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

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

    public @NotNull Maybe not() {
        return switch (this) {
            case TRUE -> FALSE;
            case FALSE -> TRUE;
            case UNKNOWN -> UNKNOWN;
        };
    }

    public @NotNull Maybe and(boolean value) {
        return this == TRUE ? Maybe.of(value) : this;
    }

    public @NotNull Maybe and(@NotNull Maybe maybe) {
        return this == TRUE ? maybe : this;
    }

    public @NotNull Maybe or(boolean value) {
        return this == FALSE ? Maybe.of(value) : this;
    }

    public @NotNull Maybe or(@NotNull Maybe maybe) {
        return this == FALSE ? maybe : this;
    }

    public @Nullable Boolean toBoolean() {
        return this.isUnknown() ? null : this.isTrue();
    }

    public @NotNull Optional<Boolean> toOptional() {
        return Optional.ofNullable(this.toBoolean());
    }

    public void ifKnown(@NotNull Consumer<Boolean> consumer) {
        if (isKnown()) {
            consumer.accept(this.toBoolean());
        }
    }

    public boolean knownValue() {
        assert this.isKnown() : "The value must be known";
        return this.isTrue();
    }

    public boolean orElse(boolean def) {
        return this.isUnknown() ? def : this.isTrue();
    }
}
