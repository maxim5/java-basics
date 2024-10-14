package io.spbx.util.base;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CheckReturnValue;
import io.spbx.util.func.ThrowRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;

@CheckReturnValue
public class BasicExceptions {
    public static @NotNull AssertionError newAssertionError(@NotNull String message, @Nullable Object @NotNull... args) {
        return new AssertionError(formatMsg(message, args));
    }
    public static @NotNull AssertionError newAssertionError(@NotNull String message, @Nullable Object arg) {
        return new AssertionError(formatMsg(message, arg));
    }
    public static @NotNull AssertionError newAssertionError(@NotNull String message) {
        return new AssertionError(message);
    }

    public static @NotNull InternalError newInternalError(@NotNull String message, @Nullable Object @NotNull... args) {
        return new InternalError(formatMsg(message, args));
    }
    public static @NotNull InternalError newInternalError(@NotNull String message, @Nullable Object arg) {
        return new InternalError(formatMsg(message, arg));
    }
    public static @NotNull InternalError newInternalError(@NotNull String message) {
        return new InternalError(message);
    }

    public static @NotNull IllegalArgumentException newIllegalArgumentException(@NotNull String message,
                                                                                @Nullable Object @NotNull... args) {
        return new IllegalArgumentException(formatMsg(message, args));
    }
    public static @NotNull IllegalArgumentException newIllegalArgumentException(@NotNull String message,
                                                                                @Nullable Object arg) {
        return new IllegalArgumentException(formatMsg(message, arg));
    }
    public static @NotNull IllegalArgumentException newIllegalArgumentException(@NotNull String message) {
        return new IllegalArgumentException(message);
    }

    public static @NotNull IllegalStateException newIllegalStateException(@NotNull String message,
                                                                          @Nullable Object @NotNull... args) {
        return new IllegalStateException(formatMsg(message, args));
    }
    public static @NotNull IllegalStateException newIllegalStateException(@NotNull String message, @Nullable Object arg) {
        return new IllegalStateException(formatMsg(message, arg));
    }
    public static @NotNull IllegalStateException newIllegalStateException(@NotNull String message) {
        return new IllegalStateException(message);
    }

    public static @NotNull IOException newIOException(@NotNull String message, @Nullable Object @NotNull... args) {
        return new IOException(formatMsg(message, args));
    }
    public static @NotNull IOException newIOException(@NotNull String message, @Nullable Object arg) {
        return new IOException(formatMsg(message, arg));
    }
    public static @NotNull IOException newIOException(@NotNull String message) {
        return new IOException(message);
    }

    public static @NotNull UncheckedIOException newUncheckedIOException(@NotNull String message,
                                                                        @Nullable Object @NotNull... args) {
        return new UncheckedIOException(formatMsg(message, args), new IOException(formatMsg(message, args)));
    }
    public static @NotNull UncheckedIOException newUncheckedIOException(@NotNull String message, @Nullable Object arg) {
        return new UncheckedIOException(formatMsg(message, arg), new IOException(formatMsg(message, arg)));
    }
    public static @NotNull UncheckedIOException newUncheckedIOException(@NotNull String message) {
        return new UncheckedIOException(message, new IOException(message));
    }

    public static @NotNull UnsupportedOperationException newUnsupportedOperationException(
            @NotNull String message, @Nullable Object @NotNull... args) {
        return new UnsupportedOperationException(formatMsg(message, args));
    }
    public static @NotNull UnsupportedOperationException newUnsupportedOperationException(
            @NotNull String message, @Nullable Object arg) {
        return new UnsupportedOperationException(formatMsg(message, arg));
    }
    public static @NotNull UnsupportedOperationException newUnsupportedOperationException(@NotNull String message) {
        return new UnsupportedOperationException(message);
    }

    public static @NotNull UnsupportedOperationException notImplemented(@NotNull String message,
                                                                        @Nullable Object @NotNull... args) {
        return new UnsupportedOperationException("Not Implemented: " + formatMsg(message, args));
    }
    public static @NotNull UnsupportedOperationException notImplemented(@NotNull String message, @Nullable Object arg) {
        return new UnsupportedOperationException("Not Implemented: " + formatMsg(message, arg));
    }
    public static @NotNull UnsupportedOperationException notImplemented(@NotNull String message) {
        return new UnsupportedOperationException("Not Implemented: " + message);
    }

    // Usage:
    // assert runOnlyInDev(...);
    public static <E extends Throwable> boolean runOnlyInDev(@NotNull ThrowRunnable<E> runnable) {
        Unchecked.Runnables.runRethrow(runnable);
        return true;
    }

    @CanIgnoreReturnValue
    public static class InternalErrors {
        public static void assure(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
            if (!cond) {
                throw newInternalError(message, args);
            }
        }
        public static void assure(boolean cond, @NotNull String message, @Nullable Object arg) {
            if (!cond) {
                throw newInternalError(message, arg);
            }
        }
        public static void assure(boolean cond, @NotNull String message) {
            if (!cond) {
                throw newInternalError(message);
            }
        }

        public static <T> @NotNull T assureNonNull(@Nullable T value, @NotNull String message,
                                                   @Nullable Object @NotNull... args) {
            failIf(value == null, message, args);
            return value;
        }
        public static <T> @NotNull T assureNonNull(@Nullable T value, @NotNull String message, @Nullable Object arg) {
            failIf(value == null, message, arg);
            return value;
        }
        public static <T> @NotNull T assureNonNull(@Nullable T value, @NotNull String message) {
            failIf(value == null, message);
            return value;
        }
        public static <T> @NotNull T assureNonNull(@Nullable T value) {
            return assureNonNull(value, "Must not be null");
        }

        public static void failIf(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
            if (cond) {
                throw newInternalError(message, args);
            }
        }
        public static void failIf(boolean cond, @NotNull String message, @Nullable Object arg) {
            if (cond) {
                throw newInternalError(message, arg);
            }
        }
        public static void failIf(boolean cond, @NotNull String message) {
            if (cond) {
                throw newInternalError(message);
            }
        }

        public static <R> R fail(@NotNull String message, @Nullable Object @NotNull... args) {
            throw newInternalError(message, args);
        }
        public static <R> R fail(@NotNull String message, @Nullable Object arg) {
            throw newInternalError(message, arg);
        }
        public static <R> R fail(@NotNull String message) {
            throw newInternalError(message);
        }
    }

    @CanIgnoreReturnValue
    public static class IllegalArgumentExceptions {
        public static void assure(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
            if (!cond) {
                throw newIllegalArgumentException(message, args);
            }
        }
        public static void assure(boolean cond, @NotNull String message, @Nullable Object arg) {
            if (!cond) {
                throw newIllegalArgumentException(message, arg);
            }
        }
        public static void assure(boolean cond, @NotNull String message) {
            if (!cond) {
                throw newIllegalArgumentException(message);
            }
        }

        public static <T> @NotNull T assureNonNull(@Nullable T value, @NotNull String message,
                                                   @Nullable Object @NotNull... args) {
            failIf(value == null, message, args);
            return value;
        }
        public static <T> @NotNull T assureNonNull(@Nullable T value, @NotNull String message, @Nullable Object arg) {
            failIf(value == null, message, arg);
            return value;
        }
        public static <T> @NotNull T assureNonNull(@Nullable T value, @NotNull String message) {
            failIf(value == null, message);
            return value;
        }
        public static <T> @NotNull T assureNonNull(@Nullable T value) {
            return assureNonNull(value, "Must not be null");
        }

        public static void failIf(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
            if (cond) {
                throw newIllegalArgumentException(message, args);
            }
        }
        public static void failIf(boolean cond, @NotNull String message, @Nullable Object arg) {
            if (cond) {
                throw newIllegalArgumentException(message, arg);
            }
        }
        public static void failIf(boolean cond, @NotNull String message) {
            if (cond) {
                throw newIllegalArgumentException(message);
            }
        }

        public static <R> R fail(@NotNull String message, @Nullable Object @NotNull... args) {
            throw newIllegalArgumentException(message, args);
        }
        public static <R> R fail(@NotNull String message, @Nullable Object arg) {
            throw newIllegalArgumentException(message, arg);
        }
        public static <R> R fail(@NotNull String message) {
            throw newIllegalArgumentException(message);
        }
    }

    @CanIgnoreReturnValue
    public static class IllegalStateExceptions {
        public static void assure(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
            if (!cond) {
                throw newIllegalStateException(message, args);
            }
        }
        public static void assure(boolean cond, @NotNull String message, @Nullable Object arg) {
            if (!cond) {
                throw newIllegalStateException(message, arg);
            }
        }
        public static void assure(boolean cond, @NotNull String message) {
            if (!cond) {
                throw newIllegalStateException(message);
            }
        }

        public static <T> @NotNull T assureNonNull(@Nullable T value, @NotNull String message,
                                                   @Nullable Object @NotNull... args) {
            failIf(value == null, message, args);
            return value;
        }
        public static <T> @NotNull T assureNonNull(@Nullable T value, @NotNull String message, @Nullable Object arg) {
            failIf(value == null, message, arg);
            return value;
        }
        public static <T> @NotNull T assureNonNull(@Nullable T value, @NotNull String message) {
            failIf(value == null, message);
            return value;
        }
        public static <T> @NotNull T assureNonNull(@Nullable T value) {
            return assureNonNull(value, "Must not be null");
        }

        public static void failIf(boolean cond, @NotNull String message, @Nullable Object @NotNull... args) {
            if (cond) {
                throw newIllegalStateException(message, args);
            }
        }
        public static void failIf(boolean cond, @NotNull String message, @Nullable Object arg) {
            if (cond) {
                throw newIllegalStateException(message, arg);
            }
        }
        public static void failIf(boolean cond, @NotNull String message) {
            if (cond) {
                throw newIllegalStateException(message);
            }
        }

        public static <R> R fail(@NotNull String message, @Nullable Object @NotNull... args) {
            throw newIllegalStateException(message, args);
        }
        public static <R> R fail(@NotNull String message, @Nullable Object arg) {
            throw newIllegalStateException(message, arg);
        }
        public static <R> R fail(@NotNull String message) {
            throw newIllegalStateException(message);
        }
    }

    static @NotNull String formatMsg(@NotNull String message, @Nullable Object @NotNull[] args) {
        if (args.length == 0) {
            return message;
        }
        if (args.length == 1 && !message.contains("%")) {
            return BasicStrings.ensureSuffix(message, " ") + args[0];
        }
        return message.formatted(args);
    }

    static @NotNull String formatMsg(@NotNull String message, @Nullable Object arg) {
        if (!message.contains("%")) {
            return BasicStrings.ensureSuffix(message, " ") + arg;
        }
        return message.formatted(arg);
    }
}
