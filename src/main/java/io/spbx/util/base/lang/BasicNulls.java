package io.spbx.util.base.lang;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Stateless
@Pure
@CheckReturnValue
public class BasicNulls {
    public static <T> @NotNull T firstNonNull(@Nullable T first, @Nullable T second) {
        return first != null ? first : Objects.requireNonNull(second, "Both arguments are null");
    }

    public static <T> @NotNull T firstNonNull(@Nullable T first, @NotNull Supplier<@Nullable T> second) {
        return first != null ? first : Objects.requireNonNull(second.get(), "Both arguments are null");
    }

    public static <T> @NotNull T firstNonNull(@NotNull Supplier<@Nullable T> first,
                                              @NotNull Supplier<@Nullable T> second) {
        return BasicNulls.firstNonNull(first.get(), second);
    }

    public static <T> @NotNull T firstNonNull(@NotNull Supplier<@Nullable T> first,
                                              @NotNull Supplier<@Nullable T> second,
                                              @NotNull T def) {
        return BasicNulls.firstNonNull(BasicNulls.firstNonNullIfExists(first, second), def);
    }

    public static <T> @NotNull T firstNonNull(@NotNull Iterable<@NotNull Supplier<@Nullable T>> suppliers) {
        return Objects.requireNonNull(BasicNulls.firstNonNullIfExists(suppliers, (T) null), "All arguments are null");
    }

    public static <T> @NotNull T firstNonNull(@NotNull Iterable<@NotNull Supplier<@Nullable T>> suppliers,
                                              @NotNull T def) {
        return Objects.requireNonNull(BasicNulls.firstNonNullIfExists(suppliers, def), "All arguments are null");
    }

    public static @SafeVarargs <T> @NotNull T firstNonNull(@NotNull Supplier<@Nullable T> @NotNull... suppliers) {
        for (Supplier<T> supplier : suppliers) {
            T result = supplier.get();
            if (result != null) {
                return result;
            }
        }
        throw new NullPointerException("All arguments are null");
    }

    public static <T> @Nullable T firstNonNullIfExists(@Nullable T first, @Nullable T second) {
        return first != null ? first : second;
    }

    public static <T> @Nullable T firstNonNullIfExists(@Nullable T first, @NotNull Supplier<@Nullable T> second) {
        return first != null ? first : second.get();
    }

    public static <T> @Nullable T firstNonNullIfExists(@NotNull Supplier<@Nullable T> first,
                                                       @NotNull Supplier<@Nullable T> second) {
        return BasicNulls.firstNonNullIfExists(first.get(), second);
    }

    public static <T> @Nullable T firstNonNullIfExists(@NotNull Supplier<@Nullable T> first,
                                                       @NotNull Supplier<@Nullable T> second,
                                                       @Nullable T def) {
        return BasicNulls.firstNonNullIfExists(BasicNulls.firstNonNullIfExists(first, second), def);
    }

    public static <T> @Nullable T firstNonNullIfExists(@NotNull Iterable<Supplier<@Nullable T>> suppliers) {
        return BasicNulls.firstNonNullIfExists(suppliers, (T) null);
    }

    public static <T> @Nullable T firstNonNullIfExists(@NotNull Iterable<Supplier<@Nullable T>> suppliers,
                                                       @Nullable T def) {
        for (Supplier<T> supplier : suppliers) {
            T value = supplier.get();
            if (value != null) {
                return value;
            }
        }
        return def;
    }

    public static @SafeVarargs <T> @Nullable T firstNonNullIfExists(@NotNull Supplier<@Nullable T> @NotNull... suppliers) {
        for (Supplier<T> supplier : suppliers) {
            T result = supplier.get();
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public static <T, U> @Nullable T firstNonNullIfExists(@NotNull Iterable<U> items,
                                                          @NotNull Function<U, @Nullable T> func) {
        return firstNonNullIfExists(items, func, null);
    }

    public static <T, U> @Nullable T firstNonNullIfExists(@NotNull Iterable<U> items,
                                                          @NotNull Function<U, @Nullable T> func,
                                                          @Nullable T def) {
        for (U item : items) {
            T value = func.apply(item);
            if (value != null) {
                return value;
            }
        }
        return def;
    }

    public static <T> @Nullable T nullifyIf(@Nullable T value, @NotNull Predicate<T> predicate) {
        return value == null || predicate.test(value) ? null : value;
    }
}
