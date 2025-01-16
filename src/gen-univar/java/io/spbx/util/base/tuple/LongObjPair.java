package io.spbx.util.base.tuple;

import io.spbx.util.func.BiLongObjConsumer;
import io.spbx.util.func.BiLongObjFunction;
import io.spbx.util.func.BiLongObjPredicate;
import io.spbx.util.func.BiLongObjToLongFunction;
import io.spbx.util.func.LongPredicate;
import io.spbx.util.func.LongUnaryOperator;
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
 * A simple immutable pair of two {@code long} values.
 */
@Immutable
@Generated(value = "$Type$ObjPair.java", date = "2025-01-14T10:07:33.424109300Z")
public record LongObjPair<T>(long first, T second) implements Map.Entry<Long, T>, Serializable {
    public static <T> @NotNull LongObjPair<T> of(long first, @Nullable T second) {
        return new LongObjPair(first, second);
    }

    public @NotNull LongObjPair<T> withFirst(long first) {
        return LongObjPair.of(first, second);
    }

    public @NotNull LongObjPair<T> withSecond(@Nullable T second) {
        return LongObjPair.of(first, second);
    }

    public <U> @NotNull LongObjPair<U> map(@NotNull LongUnaryOperator convertFirst, @NotNull Function<T, U> convertSecond) {
        return LongObjPair.of(convertFirst.applyToLong(first), convertSecond.apply(second));
    }

    public @NotNull LongObjPair<T> mapFirst(@NotNull LongUnaryOperator convert) {
        return this.map(convert, second -> second);
    }

    public @NotNull LongObjPair<T> mapFirst(@NotNull BiLongObjToLongFunction<T> convert) {
        return LongObjPair.of(convert.applyToLong(first, second), second);
    }

    public @NotNull LongObjPair<T> mapSecond(@NotNull UnaryOperator<T> convert) {
        return this.map(first -> first, convert);
    }

    public <U> @NotNull LongObjPair<U> mapSecond(@NotNull BiLongObjFunction<T, U> convert) {
        return LongObjPair.of(first, convert.apply(first, second));
    }

    public <R> @NotNull R mapToObj(@NotNull BiLongObjFunction<T, R> convert) {
        return convert.apply(first, second);
    }

    public long mapToLong(@NotNull BiLongObjToLongFunction<T> convert) {
        return convert.applyToLong(first, second);
    }

    public boolean testFirst(@NotNull LongPredicate predicate) {
        return predicate.test(first);
    }

    public boolean testSecond(@NotNull Predicate<T> predicate) {
        return predicate.test(second);
    }

    public boolean test(@NotNull BiLongObjPredicate<T> predicate) {
        return predicate.test(first, second);
    }

    public void apply(@NotNull BiLongObjConsumer<T> action) {
        action.accept(first, second);
    }

    @Override
    public Long getKey() {
        return first;
    }

    @Override
    public T getValue() {
        return second;
    }

    @Override
    public T setValue(T value) {
        throw new UnsupportedOperationException("LongObjPair is immutable");
    }

    public @NotNull Stream<Object> stream() {
        return Stream.of(first, second);
    }

    public @NotNull Pair<Long, T> toPair() {
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
