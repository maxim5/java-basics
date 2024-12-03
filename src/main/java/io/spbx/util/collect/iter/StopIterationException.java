package io.spbx.util.collect.iter;

import io.spbx.util.base.error.BasicRuntimeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StopIterationException extends BasicRuntimeException {
    public StopIterationException() {
        super();
    }

    public StopIterationException(@NotNull String message) {
        super(message);
    }

    public StopIterationException(@NotNull String message, @Nullable Object @NotNull[] args) {
        super(message, args);
    }

    public StopIterationException(@NotNull String message, @Nullable Throwable cause) {
        super(message, cause);
    }

    public StopIterationException(@Nullable Throwable cause) {
        super(cause);
    }

    public static @NotNull StopIterationException newStopIterationException(@NotNull String message,
                                                                            @Nullable Object @NotNull... args) {
        return new StopIterationException(message, args);
    }

    public static void assure(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
        if (!cond) {
            throw newStopIterationException(message, args);
        }
    }

    public static void failIf(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
        if (cond) {
            throw newStopIterationException(message, args);
        }
    }
}
