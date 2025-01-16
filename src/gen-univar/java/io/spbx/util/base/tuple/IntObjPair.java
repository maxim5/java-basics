package io.spbx.util.base.tuple;

import io.spbx.util.func.BiIntObjConsumer;
import io.spbx.util.func.BiIntObjFunction;
import io.spbx.util.func.BiIntObjPredicate;
import io.spbx.util.func.BiIntObjToIntFunction;
import io.spbx.util.func.IntPredicate;
import io.spbx.util.func.IntUnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import javax.annotation.processing.Generated;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * A simple immutable pair of two {@code int} values.
 */
@Immutable
@Generated(value = "$Type$ObjPair.java", date = "2025-01-14T10:07:33.424109300Z")
public record IntObjPair<T>(int first, T second) implements Map.Entry<Integer, T>, Serializable {
    public static <T> @NotNull IntObjPair<T> of(int first, @Nullable T second) {
        return new IntObjPair(first, second);
    }

    public @NotNull IntObjPair<T> withFirst(int first) {
        return IntObjPair.of(first, second);
    }

    public @NotNull IntObjPair<T> withSecond(@Nullable T second) {
        return IntObjPair.of(first, second);
    }

    public <U> @NotNull IntObjPair<U> map(@NotNull IntUnaryOperator convertFirst, @NotNull Function<T, U> convertSecond) {
        return IntObjPair.of(convertFirst.applyToInt(first), convertSecond.apply(second));
    }

    public @NotNull IntObjPair<T> mapFirst(@NotNull IntUnaryOperator convert) {
        return this.map(convert, second -> second);
    }

    public @NotNull IntObjPair<T> mapFirst(@NotNull BiIntObjToIntFunction<T> convert) {
        return IntObjPair.of(convert.applyToInt(first, second), second);
    }

    public @NotNull IntObjPair<T> mapSecond(@NotNull UnaryOperator<T> convert) {
        return this.map(first -> first, convert);
    }

    public <U> @NotNull IntObjPair<U> mapSecond(@NotNull BiIntObjFunction<T, U> convert) {
        return IntObjPair.of(first, convert.apply(first, second));
    }

    public <R> @NotNull R mapToObj(@NotNull BiIntObjFunction<T, R> convert) {
        return convert.apply(first, second);
    }

    public int mapToInt(@NotNull BiIntObjToIntFunction<T> convert) {
        return convert.applyToInt(first, second);
    }

    public boolean testFirst(@NotNull IntPredicate predicate) {
        return predicate.test(first);
    }

    public boolean testSecond(@NotNull Predicate<T> predicate) {
        return predicate.test(second);
    }

    public boolean test(@NotNull BiIntObjPredicate<T> predicate) {
        return predicate.test(first, second);
    }

    public void apply(@NotNull BiIntObjConsumer<T> action) {
        action.accept(first, second);
    }

    @Override
    public Integer getKey() {
        return first;
    }

    @Override
    public T getValue() {
        return second;
    }

    @Override
    public T setValue(T value) {
        throw new UnsupportedOperationException("IntObjPair is immutable");
    }

    public @NotNull Stream<Object> stream() {
        return Stream.of(first, second);
    }

    public @NotNull Pair<Integer, T> toPair() {
        return Pair.of(first, second);
    }

    public @NotNull List<Object> toList() {
        return Arrays.asList(first, second);
    }

    public Object @NotNull[] toArray() {
        return new Object[] { first, second };
    }

    @Override
    public String toString() {
        return "(%s, %s)".formatted(first, second);
    }
}
