package io.spbx.util.base.ops;

import io.spbx.util.base.annotate.AllowPythonIndexing;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.InPlace;
import io.spbx.util.base.annotate.NonPure;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.PyIndex;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.RangeCheck;
import io.spbx.util.base.error.RangeCheck.LowLevel;
import io.spbx.util.func.BiIntObjFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static io.spbx.util.base.error.RangeCheck.BEFORE_TRANSLATION;
import static io.spbx.util.base.error.RangeCheck.CLOSE_END_RANGE;

@Stateless
@CheckReturnValue
public class ObjArrayOps {
    @Pure
    public static <T> boolean hasNull(@Nullable T @NotNull[] array) {
        return indexOf(array, Objects::isNull) >= 0;
    }

    @Pure
    public static <T> boolean hasNonNull(@Nullable T @NotNull[] array) {
        return indexOf(array, Objects::nonNull) >= 0;
    }

    @Pure
    public static <T> int indexOf(@Nullable T @NotNull[] array, @Nullable T value) {
        return indexOf(array, i -> i == value);
    }

    @Pure
    public static <T> int indexOf(@Nullable T @NotNull[] array, @NotNull Predicate<T> check) {
        return indexOf(array, check, 0, array.length, -1);
    }

    @Pure
    @AllowPythonIndexing
    public static <T> int indexOf(@Nullable T @NotNull[] array, @NotNull Predicate<T> check,
                                  @PyIndex int from, @PyIndex int to, int def) {
        assert RangeCheck.with(array.length, array).rangeCheck(from, to, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        assert RangeCheck.with(array.length, array).outOfRangeCheck(def);
        from = LowLevel.translateIndex(from, array.length);
        to = LowLevel.translateIndex(to, array.length);
        for (int i = from; i < to; ++i) {
            if (check.test(array[i])) {
                return i;
            }
        }
        return def;
    }

    @Pure
    public static <T> int lastIndexOf(@Nullable T @NotNull[] array, @Nullable T value) {
        return lastIndexOf(array, i -> i == value);
    }

    @Pure
    public static <T> int lastIndexOf(@Nullable T @NotNull[] array, @NotNull Predicate<T> check) {
        return lastIndexOf(array, check, 0, array.length, -1);
    }

    @Pure
    @AllowPythonIndexing
    public static <T> int lastIndexOf(@Nullable T @NotNull[] array, @NotNull Predicate<T> check,
                                      @PyIndex int from, @PyIndex int to, int def) {
        assert RangeCheck.with(array.length, array).rangeCheck(from, to, BEFORE_TRANSLATION | CLOSE_END_RANGE);
        assert RangeCheck.with(array.length, array).outOfRangeCheck(def);
        from = LowLevel.translateIndex(from, array.length);
        to = LowLevel.translateIndex(to, array.length);
        for (int i = to - 1; i >= from; --i) {
            if (check.test(array[i])) {
                return i;
            }
        }
        return def;
    }

    @Pure
    public static <T> boolean contains(@Nullable T @NotNull[] array, @Nullable T value) {
        return indexOf(array, value) >= 0;
    }

    @Pure
    public static <T> boolean contains(@Nullable T @NotNull[] array, @NotNull Predicate<T> check) {
        return indexOf(array, check) >= 0;
    }

    @NonPure
    public static <T> @Nullable T @NotNull[] reverse(@InPlace @Nullable T @NotNull[] array) {
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            T tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    @NonPure
    public static <T> @Nullable T @NotNull[] fill(@InPlace @Nullable T @NotNull[] array, @Nullable T value) {
        Arrays.fill(array, value);
        return array;
    }

    @NonPure
    public static <T> @Nullable T @NotNull[] fill(@InPlace @Nullable T @NotNull[] array, @NotNull IntFunction<T> value) {
        for (int i = 0; i < array.length; i++) {
            array[i] = value.apply(i);
        }
        return array;
    }

    @NonPure
    public static <T> @Nullable T @NotNull[] map(@InPlace @Nullable T @NotNull[] array, @NotNull UnaryOperator<T> value) {
        for (int i = 0; i < array.length; i++) {
            array[i] = value.apply(array[i]);
        }
        return array;
    }

    @NonPure
    public static <T> @Nullable T @NotNull[] map(@InPlace @Nullable T @NotNull[] array, @NotNull BiIntObjFunction<T, T> value) {
        for (int i = 0; i < array.length; i++) {
            array[i] = value.apply(i, array[i]);
        }
        return array;
    }

    @Pure
    public static <T> @Nullable T @NotNull[] concat(@Nullable T @NotNull[] array1, @Nullable T @NotNull[] array2) {
        T[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    @Pure
    public static <T> @Nullable T @NotNull[] append(@Nullable T @NotNull[] array, @Nullable T value) {
        T[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = value;
        return result;
    }

    @Pure
    public static <T> @Nullable T @NotNull[] prepend(@Nullable T value, @Nullable T @NotNull[] array) {
        T[] result = Arrays.copyOf(array, array.length + 1);
        result[0] = value;
        System.arraycopy(array, 0, result, 1, array.length);
        return result;
    }

    @Pure
    public static <T> @Nullable T @NotNull[] slice(@Nullable T @NotNull[] array, int from, int to) {
        assert 0 <= from && from <= to && to <= array.length : "Invalid range: from=%d to=%d".formatted(from, to);
        return Arrays.copyOfRange(array, from, to);
    }

    @Pure
    public static <T> @Nullable T @NotNull[] slice(@Nullable T @NotNull[] array, int from) {
        return slice(array, from, array.length);
    }

    @Pure
    public static <T> @Nullable T @NotNull[] ensureCapacity(@Nullable T @NotNull[] array, int minLen) {
        return array.length < minLen ? Arrays.copyOf(array, minLen) : array;
    }
}
