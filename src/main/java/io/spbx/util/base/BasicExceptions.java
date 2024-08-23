package io.spbx.util.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CheckReturnValue;
import io.spbx.util.func.ThrowRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;

import static io.spbx.util.base.FmtRuntimeException.formatMsg;

@CheckReturnValue
public class BasicExceptions {
    public static @NotNull AssertionError newAssertionError(@NotNull String message, @Nullable Object @NotNull... args) {
        return new AssertionError(formatMsg(message, args));
    }

    public static @NotNull AssertionError newAssertionError(@NotNull String message) {
        return new AssertionError(message);
    }

    public static @NotNull InternalError newInternalError(@NotNull String message, @Nullable Object @NotNull... args) {
        return new InternalError(formatMsg(message, args));
    }

    public static @NotNull InternalError newInternalError(@NotNull String message) {
        return new InternalError(message);
    }

    public static @NotNull IllegalArgumentException newIllegalArgumentException(@NotNull String message,
                                                                                @Nullable Object @NotNull... args) {
        return new IllegalArgumentException(formatMsg(message, args));
    }

    public static @NotNull IllegalArgumentException newIllegalArgumentException(@NotNull String message) {
        return new IllegalArgumentException(message);
    }

    public static @NotNull IllegalStateException newIllegalStateException(@NotNull String message,
                                                                          @Nullable Object @NotNull... args) {
        return new IllegalStateException(formatMsg(message, args));
    }

    public static @NotNull IllegalStateException newIllegalStateException(@NotNull String message) {
        return new IllegalStateException(message);
    }

    public static @NotNull IOException newIOException(@NotNull String message, @Nullable Object @NotNull... args) {
        return new IOException(formatMsg(message, args));
    }

    public static @NotNull UncheckedIOException newUncheckedIOException(@NotNull String message,
                                                                        @Nullable Object @NotNull... args) {
        return new UncheckedIOException(formatMsg(message, args), new IOException(formatMsg(message, args)));
    }

    public static @NotNull UnsupportedOperationException newUnsupportedOperationException(
            @NotNull String message, @Nullable Object @NotNull... args) {
        return new UnsupportedOperationException(formatMsg(message, args));
    }

    public static @NotNull UnsupportedOperationException notImplemented(@NotNull String message,
                                                                        @Nullable Object @NotNull... args) {
        return new UnsupportedOperationException("Not Implemented: " + formatMsg(message, args));
    }

    // Usage:
    // assert runOnlyInDev(...);
    public static <E extends Throwable> boolean runOnlyInDev(@NotNull ThrowRunnable<E> runnable) {
        Unchecked.Runnables.runRethrow(runnable);
        return true;
    }

    public static class InternalErrors {
        public static void assure(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
            if (!cond) {
                throw newInternalError(message, args);
            }
        }

        @CanIgnoreReturnValue
        public static <T> @NotNull T assureNonNull(@Nullable T value, @NotNull String message,
                                                   @Nullable Object @NotNull... args) {
            failIf(value == null, message, args);
            return value;
        }

        public static void failIf(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
            if (cond) {
                throw newInternalError(message, args);
            }
        }

        @CanIgnoreReturnValue
        public static <R> R fail(@NotNull String message, @Nullable Object @NotNull... args) {
            throw newInternalError(message, args);
        }
    }

    public static class IllegalArgumentExceptions {
        public static void assure(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
            if (!cond) {
                throw newIllegalArgumentException(message, args);
            }
        }

        @CanIgnoreReturnValue
        public static <T> @NotNull T assureNonNull(@Nullable T value, @NotNull String message,
                                                   @Nullable Object @NotNull... args) {
            failIf(value == null, message, args);
            return value;
        }

        public static void failIf(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
            if (cond) {
                throw newIllegalArgumentException(message, args);
            }
        }

        @CanIgnoreReturnValue
        public static <R> R fail(@NotNull String message, @Nullable Object @NotNull... args) {
            throw newIllegalArgumentException(formatMsg(message, args));
        }
    }

    public static class IllegalStateExceptions {
        public static void assure(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
            if (!cond) {
                throw newIllegalStateException(message, args);
            }
        }

        @CanIgnoreReturnValue
        public static <T> @NotNull T assureNonNull(@Nullable T value, @NotNull String message,
                                                   @Nullable Object @NotNull... args) {
            failIf(value == null, message, args);
            return value;
        }

        public static void failIf(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
            if (cond) {
                throw newIllegalStateException(message, args);
            }
        }

        @CanIgnoreReturnValue
        public static <R> R fail(@NotNull String message, @Nullable Object @NotNull... args) {
            throw newIllegalStateException(message, args);
        }
    }
}
