package io.spbx.util.base.error;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BasicRuntimeException extends RuntimeException {
    protected BasicRuntimeException() {
    }

    protected BasicRuntimeException(@NotNull String message) {
        super(message);
    }

    protected BasicRuntimeException(@NotNull String message, @Nullable Object @NotNull[] args) {
        super(formatMsg(message, args));
    }

    protected BasicRuntimeException(@NotNull String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    protected BasicRuntimeException(@Nullable Throwable cause) {
        super(cause);
    }

    protected static @NotNull String formatMsg(@NotNull String message, @Nullable Object @NotNull[] args) {
        return BasicExceptions.formatMsg(message, args);
    }
}
