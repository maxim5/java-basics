package io.spbx.util.base.tuple;

import io.spbx.util.func.BiLongConsumer;
import io.spbx.util.func.BiLongFunction;
import io.spbx.util.func.BiLongPredicate;
import io.spbx.util.func.LongBinaryOperator;
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
import java.util.stream.Stream;

/**
 * A simple immutable pair of two {@code long} values.
 */
@Immutable
@Generated(value = "$Type$Pair.java", date = "2025-01-14T10:07:33.427109800Z")
public record LongPair(long first, long second) implements Map.Entry<Long, Long>, Serializable {
    public static @NotNull LongPair of(long first, long second) {
        return new LongPair(first, second);
    }

    public static @NotNull LongPair of(long @NotNull[] array) {
        assert array.length == 2 : "Invalid array to create a pair from: length=%d".formatted(array.length);
        return new LongPair(array[0], array[1]);
    }

    public static @NotNull LongPair dupe(long item) {
        return LongPair.of(item, item);
    }

    public @NotNull LongPair withFirst(long first) {
        return LongPair.of(first, second);
    }

    public @NotNull LongPair withSecond(long second) {
        return LongPair.of(first, second);
    }

    public @NotNull LongPair swap() {
        return LongPair.of(second, first);
    }

    public @NotNull LongPair map(@NotNull LongUnaryOperator convertFirst, @NotNull LongUnaryOperator convertSecond) {
        return LongPair.of(convertFirst.applyToLong(first), convertSecond.applyToLong(second));
    }

    public @NotNull LongPair mapFirst(@NotNull LongUnaryOperator convert) {
        return this.map(convert, second -> second);
    }

    public @NotNull LongPair mapFirst(@NotNull LongBinaryOperator convert) {
        return LongPair.of(convert.applyToLong(first, second), second);
    }

    public @NotNull LongPair mapSecond(@NotNull LongUnaryOperator convert) {
        return this.map(first -> first, convert);
    }

    public @NotNull LongPair mapSecond(@NotNull LongBinaryOperator convert) {
        return LongPair.of(first, convert.applyToLong(first, second));
    }

    public <T> @NotNull T mapToObj(@NotNull BiLongFunction<T> convert) {
        return convert.apply(first, second);
    }

    public long mapToLong(@NotNull LongBinaryOperator convert) {
        return convert.applyToLong(first, second);
    }

    public boolean testFirst(@NotNull LongPredicate predicate) {
        return predicate.test(first);
    }

    public boolean testSecond(@NotNull LongPredicate predicate) {
        return predicate.test(second);
    }

    public boolean test(@NotNull BiLongPredicate predicate) {
        return predicate.test(first, second);
    }

    public void apply(@NotNull BiLongConsumer action) {
        action.accept(first, second);
    }

    @Override
    public Long getKey() {
        return first;
    }

    @Override
    public Long getValue() {
        return second;
    }

    @Override
    public Long setValue(Long value) {
        throw new UnsupportedOperationException("LongPair is immutable");
    }

    public @NotNull Stream<Long> stream() {
        return Stream.of(first, second);
    }

    public @NotNull Pair<Long, Long> toPair() {
        return Pair.of(first, second);
    }

    public @NotNull LongObjPair<Long> toLongObjPair() {
        return LongObjPair.of(first, second);
    }

    public @NotNull List<Long> toList() {
        return Arrays.asList(first, second);
    }

    public long @NotNull[] toArray() {
        return new long[] { first, second };
    }

    public @NotNull Long @NotNull[] toLongArray() {
        return new Long[] { first, second };
    }

    @Override
    public String toString() {
        return "(%s, %s)".formatted(first, second);
    }
}
