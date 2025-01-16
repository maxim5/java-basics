package io.spbx.util.base.tuple;

import io.spbx.util.func.BiIntConsumer;
import io.spbx.util.func.BiIntFunction;
import io.spbx.util.func.BiIntPredicate;
import io.spbx.util.func.IntBinaryOperator;
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
import java.util.stream.Stream;

/**
 * A simple immutable pair of two {@code int} values.
 */
@Immutable
@Generated(value = "$Type$Pair.java", date = "2025-01-14T10:07:33.427109800Z")
public record IntPair(int first, int second) implements Map.Entry<Integer, Integer>, Serializable {
    public static @NotNull IntPair of(int first, int second) {
        return new IntPair(first, second);
    }

    public static @NotNull IntPair of(int @NotNull[] array) {
        assert array.length == 2 : "Invalid array to create a pair from: length=%d".formatted(array.length);
        return new IntPair(array[0], array[1]);
    }

    public static @NotNull IntPair dupe(int item) {
        return IntPair.of(item, item);
    }

    public @NotNull IntPair withFirst(int first) {
        return IntPair.of(first, second);
    }

    public @NotNull IntPair withSecond(int second) {
        return IntPair.of(first, second);
    }

    public @NotNull IntPair swap() {
        return IntPair.of(second, first);
    }

    public @NotNull IntPair map(@NotNull IntUnaryOperator convertFirst, @NotNull IntUnaryOperator convertSecond) {
        return IntPair.of(convertFirst.applyToInt(first), convertSecond.applyToInt(second));
    }

    public @NotNull IntPair mapFirst(@NotNull IntUnaryOperator convert) {
        return this.map(convert, second -> second);
    }

    public @NotNull IntPair mapFirst(@NotNull IntBinaryOperator convert) {
        return IntPair.of(convert.applyToInt(first, second), second);
    }

    public @NotNull IntPair mapSecond(@NotNull IntUnaryOperator convert) {
        return this.map(first -> first, convert);
    }

    public @NotNull IntPair mapSecond(@NotNull IntBinaryOperator convert) {
        return IntPair.of(first, convert.applyToInt(first, second));
    }

    public <T> @NotNull T mapToObj(@NotNull BiIntFunction<T> convert) {
        return convert.apply(first, second);
    }

    public int mapToInt(@NotNull IntBinaryOperator convert) {
        return convert.applyToInt(first, second);
    }

    public boolean testFirst(@NotNull IntPredicate predicate) {
        return predicate.test(first);
    }

    public boolean testSecond(@NotNull IntPredicate predicate) {
        return predicate.test(second);
    }

    public boolean test(@NotNull BiIntPredicate predicate) {
        return predicate.test(first, second);
    }

    public void apply(@NotNull BiIntConsumer action) {
        action.accept(first, second);
    }

    @Override
    public Integer getKey() {
        return first;
    }

    @Override
    public Integer getValue() {
        return second;
    }

    @Override
    public Integer setValue(Integer value) {
        throw new UnsupportedOperationException("IntPair is immutable");
    }

    public @NotNull Stream<Integer> stream() {
        return Stream.of(first, second);
    }

    public @NotNull Pair<Integer, Integer> toPair() {
        return Pair.of(first, second);
    }

    public @NotNull IntObjPair<Integer> toIntObjPair() {
        return IntObjPair.of(first, second);
    }

    public @NotNull List<Integer> toList() {
        return Arrays.asList(first, second);
    }

    public int @NotNull[] toArray() {
        return new int[] { first, second };
    }

    public @NotNull Integer @NotNull[] toIntegerArray() {
        return new Integer[] { first, second };
    }

    @Override
    public String toString() {
        return "(%s, %s)".formatted(first, second);
    }
}
