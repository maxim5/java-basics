package io.spbx.util.base;

import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.Predicate;

/**
 * Note: all ops are in-place for performance reasons.
 */
public class BasicArrays {
    @Pure public static <T> int indexOf(@Nullable T @NotNull[] array, @Nullable T value) {
        return indexOf(array, i -> i == value);
    }

    @Pure public static <T> int indexOf(@Nullable T @NotNull[] array, @NotNull Predicate<T> check) {
        return indexOf(array, 0, array.length, check, -1);
    }

    @Pure public static <T> int indexOf(@Nullable T @NotNull[] array, int from, int to, @NotNull Predicate<T> check, int def) {
        assert from >= 0 && from <= array.length : "From index is out of array bounds: %d".formatted(from);
        assert to >= 0 && to <= array.length : "To index is out of array bounds: %d".formatted(to);
        assert def < 0 || def >= array.length : "Default index can't be within array bounds: %d".formatted(def);
        for (int i = from; i < to; i++) {
            if (check.test(array[i])) {
                return i;
            }
        }
        return def;
    }

    @Pure public static <T> int lastIndexOf(@Nullable T @NotNull[] array, @Nullable T value) {
        return lastIndexOf(array, i -> i == value);
    }

    @Pure public static <T> int lastIndexOf(@Nullable T @NotNull[] array, @NotNull Predicate<T> check) {
        return lastIndexOf(array, 0, array.length, check, -1);
    }

    @Pure public static <T> int lastIndexOf(@Nullable T @NotNull[] array, int from, int to, @NotNull Predicate<T> check, int def) {
        assert from >= 0 && from <= array.length : "From index is out of array bounds: %d".formatted(from);
        assert to >= 0 && to <= array.length : "To index is out of array bounds: %d".formatted(to);
        assert def < 0 || def >= array.length : "Default index can't be within array bounds: %d".formatted(def);
        for (int i = to - 1; i >= from; --i) {
            if (check.test(array[i])) {
                return i;
            }
        }
        return def;
    }

    @Pure public static <T> boolean contains(@Nullable T @NotNull[] array, @Nullable T value) {
        return indexOf(array, value) >= 0;
    }

    @Pure public static <T> boolean contains(@Nullable T @NotNull[] array, @NotNull Predicate<T> check) {
        return indexOf(array, check) >= 0;
    }

    @Pure public static <T> T[] reverse(@Nullable T @NotNull[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            T tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    @Pure public static <T> T[] fill(@Nullable T @NotNull[] array, @Nullable T value) {
        Arrays.fill(array, value);
        return array;
    }

    @Pure public static <T> T[] fill(@Nullable T @NotNull[] array, @NotNull IntFunction<T> value) {
        for (int i = 0; i < array.length; i++) {
            array[i] = value.apply(i);
        }
        return array;
    }
}
