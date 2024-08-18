package io.spbx.util.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
        return BasicNulls.firstNonNull(BasicNulls.firstNonNullIfExist(first, second), def);
    }

    public static <T> @NotNull T firstNonNull(@NotNull Iterable<@NotNull Supplier<@Nullable T>> suppliers) {
        return Objects.requireNonNull(BasicNulls.firstNonNullIfExist(suppliers, (T) null));
    }

    public static <T> @NotNull T firstNonNull(@NotNull Iterable<@NotNull Supplier<@Nullable T>> suppliers,
                                              @NotNull T def) {
        return Objects.requireNonNull(BasicNulls.firstNonNullIfExist(suppliers, def));
    }

    public static <T> @Nullable T firstNonNullIfExist(@Nullable T first, @Nullable T second) {
        return first != null ? first : second;
    }

    public static <T> @Nullable T firstNonNullIfExist(@Nullable T first, @NotNull Supplier<@Nullable T> second) {
        return first != null ? first : second.get();
    }

    public static <T> @Nullable T firstNonNullIfExist(@NotNull Supplier<@Nullable T> first,
                                                      @NotNull Supplier<@Nullable T> second) {
        return BasicNulls.firstNonNullIfExist(first.get(), second);
    }

    public static <T> @Nullable T firstNonNullIfExist(@NotNull Supplier<@Nullable T> first,
                                                      @NotNull Supplier<@Nullable T> second,
                                                      @Nullable T def) {
        return BasicNulls.firstNonNullIfExist(BasicNulls.firstNonNullIfExist(first, second), def);
    }

    public static <T> @Nullable T firstNonNullIfExist(@NotNull Iterable<Supplier<@Nullable T>> suppliers) {
        return BasicNulls.firstNonNullIfExist(suppliers, (T) null);
    }

    public static <T> @Nullable T firstNonNullIfExist(@NotNull Iterable<Supplier<@Nullable T>> suppliers,
                                                      @Nullable T def) {
        for (Supplier<T> supplier : suppliers) {
            T value = supplier.get();
            if (value != null) {
                return value;
            }
        }
        return def;
    }

    public static <T, U> @Nullable T firstNonNullIfExist(@NotNull Iterable<U> items,
                                                         @NotNull Function<U, @Nullable T> func) {
        return firstNonNullIfExist(items, func, null);
    }

    public static <T, U> @Nullable T firstNonNullIfExist(@NotNull Iterable<U> items,
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
