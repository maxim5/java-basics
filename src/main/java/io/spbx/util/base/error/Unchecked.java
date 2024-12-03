package io.spbx.util.base.error;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.func.ThrowBiConsumer;
import io.spbx.util.func.ThrowBoolSupplier;
import io.spbx.util.func.ThrowConsumer;
import io.spbx.util.func.ThrowFunction;
import io.spbx.util.func.ThrowIntSupplier;
import io.spbx.util.func.ThrowLongSupplier;
import io.spbx.util.func.ThrowRunnable;
import io.spbx.util.func.ThrowSupplier;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Stateless
public class Unchecked {
    @CanIgnoreReturnValue
    public static <T> T rethrow(@NotNull Throwable exception) {
        throw new RuntimeException(exception);
    }

    @CanIgnoreReturnValue
    public static <T> T rethrow(@NotNull String message, @NotNull Throwable exception) {
        throw new RuntimeException(message, exception);
    }

    @CanIgnoreReturnValue
    public static <T> T rethrow(@NotNull IOException exception) {
        throw new UncheckedIOException(exception);
    }

    @CanIgnoreReturnValue
    public static <T> T rethrow(@NotNull String message, @NotNull IOException exception) {
        throw new UncheckedIOException(message, exception);
    }

    // Idea taken from
    // https://stackoverflow.com/questions/4519557/is-there-a-way-to-throw-an-exception-without-adding-the-throws-declaration
    @SuppressWarnings("unchecked")
    @CanIgnoreReturnValue
    public static <T extends Throwable, R> R throwAny(Throwable exception) throws T {
        throw (T) exception;
    }

    public static class Runnables {
        public static <E extends Throwable> @NotNull Runnable rethrow(@NotNull ThrowRunnable<E> consumer) {
            return () -> {
                try {
                    consumer.run();
                } catch (RuntimeException | Error e) {
                    throw e;
                } catch (Throwable e) {
                    Unchecked.rethrow(e);
                }
            };
        }

        public static <E extends Throwable> void runRethrow(@NotNull ThrowRunnable<E> action) {
            try {
                action.run();
            } catch (RuntimeException | Error e) {
                throw e;
            } catch (Throwable e) {
                Unchecked.rethrow(e);
            }
        }

        public static <E extends Throwable> void runQuietly(@NotNull ThrowRunnable<E> action) {
            try {
                action.run();
            } catch (Throwable ignore) {
            }
        }
    }

    public static class Consumers {
        public static <T, E extends Throwable> @NotNull Consumer<T> rethrow(@NotNull ThrowConsumer<T, E> consumer) {
            return value -> {
                try {
                    consumer.accept(value);
                } catch (RuntimeException | Error e) {
                    throw e;
                } catch (Throwable e) {
                    Unchecked.rethrow(e);
                }
            };
        }

        public static <T, U, E extends Throwable>
                @NotNull BiConsumer<T, U> rethrow(@NotNull ThrowBiConsumer<T, U, E> consumer) {
            return (t, u) -> {
                try {
                    consumer.accept(t, u);
                } catch (RuntimeException | Error e) {
                    throw e;
                } catch (Throwable e) {
                    Unchecked.rethrow(e);
                }
            };
        }
    }

    public static class Suppliers {
        public static <T, E extends Throwable> @NotNull Supplier<T> rethrow(@NotNull ThrowSupplier<T, E> supplier) {
            return () -> {
                try {
                    return supplier.get();
                } catch (RuntimeException | Error e) {
                    throw e;
                } catch (Throwable e) {
                    return Unchecked.rethrow(e);
                }
            };
        }

        public static <T, E extends Throwable> T runRethrow(@NotNull ThrowSupplier<T, E> action) {
            try {
                return action.get();
            } catch (RuntimeException | Error e) {
                throw e;
            } catch (Throwable e) {
                return Unchecked.rethrow(e);
            }
        }

        public static <T, E extends Throwable> T runQuietlyOrDefault(@NotNull ThrowSupplier<T, E> action, T def) {
            try {
                return action.get();
            } catch (Throwable e) {
                return def;
            }
        }

        public static <T, E extends Throwable> T runQuietlyOrNull(@NotNull ThrowSupplier<T, E> action) {
            return runQuietlyOrDefault(action, null);
        }
    }

    public static class IntSuppliers {
        public static <E extends Throwable> int runRethrow(@NotNull ThrowIntSupplier<E> action) {
            try {
                return action.getAsInt();
            } catch (RuntimeException | Error e) {
                throw e;
            } catch (Throwable e) {
                return Unchecked.rethrow(e);
            }
        }

        public static <E extends Throwable> int runQuietlyOrDefault(@NotNull ThrowIntSupplier<E> action, int def) {
            try {
                return action.getAsInt();
            } catch (Throwable e) {
                return def;
            }
        }

        public static <E extends Throwable> int runQuietly(@NotNull ThrowIntSupplier<E> action) {
            return runQuietlyOrDefault(action, 0);
        }
    }

    public static class LongSuppliers {
        public static <E extends Throwable> long runRethrow(@NotNull ThrowLongSupplier<E> action) {
            try {
                return action.getAsLong();
            } catch (RuntimeException | Error e) {
                throw e;
            } catch (Throwable e) {
                return Unchecked.rethrow(e);
            }
        }

        public static <E extends Throwable> long runQuietlyOrDefault(@NotNull ThrowLongSupplier<E> action, long def) {
            try {
                return action.getAsLong();
            } catch (Throwable e) {
                return def;
            }
        }

        public static <E extends Throwable> long runQuietly(@NotNull ThrowLongSupplier<E> action) {
            return runQuietlyOrDefault(action, 0);
        }
    }

    public static class BoolSuppliers {
        public static <E extends Throwable> boolean runRethrow(@NotNull ThrowBoolSupplier<E> action) {
            try {
                return action.getAsBoolean();
            } catch (RuntimeException | Error e) {
                throw e;
            } catch (Throwable e) {
                return Unchecked.rethrow(e);
            }
        }

        public static <E extends Throwable> boolean runQuietlyOrDefault(@NotNull ThrowBoolSupplier<E> action, boolean def) {
            try {
                return action.getAsBoolean();
            } catch (Throwable e) {
                return def;
            }
        }

        public static <E extends Throwable> boolean runQuietly(@NotNull ThrowBoolSupplier<E> action) {
            return runQuietlyOrDefault(action, false);
        }
    }

    public static class Functions {
        public static <T, R, E extends Throwable> @NotNull Function<T, R> rethrow(@NotNull ThrowFunction<T, R, E> func) {
            return value -> {
                try {
                    return func.apply(value);
                } catch (RuntimeException | Error e) {
                    throw e;
                } catch (Throwable e) {
                    return Unchecked.rethrow(e);
                }
            };
        }

        public static <T, R, E extends Throwable>
                @NotNull Function<T, R> runQuietlyOrDefault(@NotNull ThrowFunction<T, R, E> func, R def) {
            return value -> {
                try {
                    return func.apply(value);
                } catch (Throwable e) {
                    return def;
                }
            };
        }

        public static <T, R, E extends Throwable> @NotNull Function<T, R> runQuietlyOrNull(@NotNull ThrowFunction<T, R, E> func) {
            return runQuietlyOrDefault(func, null);
        }
    }

    public static class Guava {
        public static @NotNull <T, R, E extends Throwable>
                com.google.common.base.Function<T, R> rethrow(@NotNull ThrowFunction<T, R, E> func) {
            return value -> {
                try {
                    return func.apply(value);
                } catch (RuntimeException | Error e) {
                    throw e;
                } catch (Throwable e) {
                    return Unchecked.rethrow(e);
                }
            };
        }

        public static @NotNull <T, R, E extends Throwable>
                com.google.common.base.Function<T, R> runQuietlyOrDefault(@NotNull ThrowFunction<T, R, E> func, R def) {
            return value -> {
                try {
                    return func.apply(value);
                } catch (Throwable e) {
                    return def;
                }
            };
        }

        public static @NotNull <T, R, E extends Throwable>
                com.google.common.base.Function<T, R> runQuietlyOrNull(@NotNull ThrowFunction<T, R, E> func) {
            return runQuietlyOrDefault(func, null);
        }
    }
}
