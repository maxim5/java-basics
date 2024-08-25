package io.spbx.util.base;

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
        if (args.length == 0) {
            return message;
        }
        if (args.length == 1 && !message.contains("%")) {
            return BasicStrings.ensureSuffix(message, " ") + args[0];
        }
        return message.formatted(args);
    }
}
