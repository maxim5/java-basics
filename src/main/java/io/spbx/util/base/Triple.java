package io.spbx.util.base;

import com.google.errorprone.annotations.Immutable;
import io.spbx.util.base.Tuple.FixedArrayCollector;
import io.spbx.util.func.TriConsumer;
import io.spbx.util.func.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static io.spbx.util.base.EasyCast.castAny;
import static io.spbx.util.base.EasyCast.castToInt;

/**
 * Holds an immutable triple of nullable objects.
 *
 * @see Pair
 * @see Tuple
 * @see OneOf
 */
@Immutable
public final class Triple<U, V, W> implements Serializable {
    private static final Triple<?, ?, ?> EMPTY = new Triple<>(null, null, null);

    private final U first;
    private final V second;
    private final W third;

    private Triple(U first, V second, W third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <U, V, W> @NotNull Triple<U, V, W> of(@Nullable U first, @Nullable V second, @Nullable W third) {
        return first == null && second == null && third == null ? Triple.empty() : new Triple<>(first, second, third);
    }

    public static <U, V, W> @NotNull Triple<U, V, W> empty() {
        return castAny(EMPTY);
    }

    public static <T> @NotNull Triple<T, T, T> from(@Nullable T @NotNull[] array) {
        assert array.length == 3 : "Invalid array to create a triple from: length=%d".formatted(array.length);
        return Triple.of(array[0], array[1], array[2]);
    }

    public static <T> @NotNull Triple<T, T, T> from(@Nullable T @NotNull[] array, int fromIndex) {
        fromIndex = fromIndex < 0 ? fromIndex + array.length : fromIndex;
        assert array.length >= fromIndex + 3 :
            "Invalid array to create a triple from: length=%d, index=%d".formatted(array.length, fromIndex);
        return Triple.of(array[fromIndex], array[fromIndex + 1], array[fromIndex + 2]);
    }

    public static <T> @NotNull Triple<T, T, T> from(@NotNull Collection<? extends T> collection) {
        assert collection.size() == 3 : "Invalid list to create a triple from: size=%d".formatted(collection.size());
        Iterator<? extends T> iterator = collection.iterator();
        // JLS 15.7.4 Argument Lists are Evaluated Left-to-Right
        return Triple.of(iterator.next(), iterator.next(), iterator.next());
    }

    public static <T> @NotNull Triple<T, T, T> from(@NotNull Collection<? extends T> collection, int fromIndex) {
        fromIndex = fromIndex < 0 ? fromIndex + collection.size() : fromIndex;
        assert collection.size() >= fromIndex + 3 :
            "Invalid collection to create a triple from: size=%d, index=%d".formatted(collection.size(), fromIndex);
        return collection.stream().skip(fromIndex).limit(3).collect(toTriple());
    }

    public U first() {
        return first;
    }

    public V second() {
        return second;
    }

    public W third() {
        return third;
    }

    public @NotNull Triple<U, V, W> withFirst(@Nullable U first) {
        return Triple.of(first, second, third);
    }

    public @NotNull Triple<U, V, W> withSecond(@Nullable V second) {
        return Triple.of(first, second, third);
    }

    public @NotNull Triple<U, V, W> withThird(@Nullable W third) {
        return Triple.of(first, second, third);
    }

    public @NotNull Pair<V, W> toPairWithoutFirst() {
        return Pair.of(second, third);
    }

    public @NotNull Pair<U, W> toPairWithoutSecond() {
        return Pair.of(first, third);
    }

    public @NotNull Pair<U, V> toPairWithoutThird() {
        return Pair.of(first, second);
    }

    public @NotNull Tuple toTuple() {
        return Tuple.of(first, second, third);
    }

    public boolean isOneOf() {
        return first == null ? second == null ^ third == null : second == null && third == null;
    }

    public boolean isAnyOf() {
        return first != null || second != null || third != null;
    }

    public boolean isAllOf() {
        return first != null && second != null && third != null;
    }

    public boolean isNoneOf() {
        return this == EMPTY;
    }

    public int countNulls() {
        return castToInt(first == null) + castToInt(second == null) + castToInt(third == null);
    }

    public int countNonNulls() {
        return castToInt(first != null) + castToInt(second != null) + castToInt(third != null);
    }

    public @NotNull Triple<W, V, U> reverse() {
        return Triple.of(third, second, first);
    }

    public <T, S, R> @NotNull Triple<T, S, R> map(@NotNull Function<? super U, ? extends T> convertFirst,
                                                  @NotNull Function<? super V, ? extends S> convertSecond,
                                                  @NotNull Function<? super W, ? extends R> convertThird) {
        return Triple.of(convertFirst.apply(first), convertSecond.apply(second), convertThird.apply(third));
    }

    public <T> @NotNull Triple<T, V, W> mapFirst(@NotNull Function<? super U, ? extends T> convert) {
        return map(convert, second -> second, third -> third);
    }

    public <T> @NotNull Triple<T, V, W> mapFirst(@NotNull TriFunction<? super U, ? super V, ? super W, ? extends T> convert) {
        return Triple.of(convert.apply(first, second, third), second, third);
    }

    public <T> @NotNull Triple<U, T, W> mapSecond(@NotNull Function<? super V, ? extends T> convert) {
        return map(first -> first, convert, third -> third);
    }

    public <T> @NotNull Triple<U, T, W> mapSecond(@NotNull TriFunction<? super U, ? super V, ? super W, ? extends T> convert) {
        return Triple.of(first, convert.apply(first, second, third), third);
    }

    public <T> @NotNull Triple<U, V, T> mapThird(@NotNull Function<? super W, ? extends T> convert) {
        return map(first -> first, second -> second, convert);
    }

    public <T> @NotNull Triple<U, V, T> mapThird(@NotNull TriFunction<? super U, ? super V, ? super W, ? extends T> convert) {
        return Triple.of(first, second, convert.apply(first, second, third));
    }

    public <T> @NotNull T mapToObj(@NotNull TriFunction<? super U, ? super V, ? super W, ? extends T> convert) {
        return convert.apply(first, second, third);
    }

    public boolean testFirst(@NotNull Predicate<? super U> predicate) {
        return predicate.test(first);
    }

    public boolean testSecond(@NotNull Predicate<? super V> predicate) {
        return predicate.test(second);
    }

    public boolean testThird(@NotNull Predicate<? super W> predicate) {
        return predicate.test(third);
    }

    public void apply(@NotNull TriConsumer<? super U, ? super V, ? super W> action) {
        action.accept(first, second, third);
    }

    public @NotNull Stream<Object> stream() {
        return Stream.of(first, second, third);
    }

    public @NotNull List<Object> toList() {
        return Arrays.asList(first, second, third);
    }

    public Object @NotNull[] toArray() {
        return new Object[] { first, second, third };
    }

    public static <U extends Comparable<U>, V extends Comparable<V>, W extends Comparable<W>>
            @NotNull Comparator<Triple<U, V, W>> comparator() {
        return Comparator.<Triple<U, V, W>, U>comparing(Triple::first)
            .thenComparing(Triple::second)
            .thenComparing(Triple::third);
    }

    public static <U, V, W> @NotNull Comparator<Triple<U, V, W>> comparator(@NotNull Comparator<? super U> cmpFirst,
                                                                            @NotNull Comparator<? super V> cmpSecond,
                                                                            @NotNull Comparator<? super W> cmpThird) {
        return Comparator.<Triple<U, V, W>, U>comparing(Triple::first, cmpFirst)
            .thenComparing(Triple::second, cmpSecond)
            .thenComparing(Triple::third, cmpThird);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Triple<?, ?, ?> that &&
               Objects.equals(this.first, that.first) &&
               Objects.equals(this.second, that.second) &&
               Objects.equals(this.third, that.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "(%s, %s, %s)".formatted(first, second, third);
    }

    public static <E> @NotNull Collector<E, ?, Triple<E, E, E>> toTriple() {
        return castAny(Collector.of(
            () -> new FixedArrayCollector(3),
            FixedArrayCollector::add,
            FixedArrayCollector::combine,
            c -> Triple.from(c.exactItems())
        ));
    }

    public static <E> @NotNull Collector<E, ?, Triple<E, E, E>> toTripleSparse() {
        return castAny(Collector.of(
            () -> new FixedArrayCollector(3),
            FixedArrayCollector::add,
            FixedArrayCollector::combine,
            c -> Triple.from(c.items())
        ));
    }
}
