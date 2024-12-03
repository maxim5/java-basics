package io.spbx.util.base.tuple;

import io.spbx.util.base.tuple.Tuple.FixedArrayCollector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SequencedCollection;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static io.spbx.util.base.lang.EasyCast.castAny;
import static io.spbx.util.base.lang.EasyCast.castToInt;

/**
 * Holds an immutable pair of nullable objects.
 *
 * @see Triple
 * @see Tuple
 * @see OneOf
 */
@Immutable
public final class Pair<U, V> implements Map.Entry<U, V>, Serializable {
    private static final Pair<?, ?> EMPTY = new Pair<>(null, null);

    private final U first;
    private final V second;

    private Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    public static <U, V> @NotNull Pair<U, V> of(@Nullable U first, @Nullable V second) {
        return first == null && second == null ? Pair.empty() : new Pair<>(first, second);
    }

    public static <U, V> @NotNull Pair<U, V> of(@NotNull Map.Entry<? extends U, ? extends V> entry) {
        return entry instanceof Pair<?, ?> pair ? castAny(pair) : Pair.of(entry.getKey(), entry.getValue());
    }

    public static <U, V> @NotNull Pair<U, V> empty() {
        return castAny(EMPTY);
    }

    public static <T> @NotNull Pair<T, T> from(@Nullable T @NotNull[] array) {
        assert array.length == 2 : "Invalid array to create a pair from: length=%d".formatted(array.length);
        return Pair.of(array[0], array[1]);
    }

    public static <T> @NotNull Pair<T, T> from(@Nullable T @NotNull[] array, int fromIndex) {
        fromIndex = fromIndex < 0 ? fromIndex + array.length : fromIndex;
        assert array.length >= fromIndex + 2 :
            "Invalid array to create a pair from: length=%d, index=%d".formatted(array.length, fromIndex);
        return Pair.of(array[fromIndex], array[fromIndex + 1]);
    }

    public static <T> @NotNull Pair<T, T> from(@NotNull SequencedCollection<? extends T> collection) {
        assert collection.size() == 2 : "Invalid collection to create a pair from: size=%d".formatted(collection.size());
        return Pair.of(collection.getFirst(), collection.getLast());
    }

    public static <T> @NotNull Pair<T, T> from(@NotNull Collection<? extends T> collection, int fromIndex) {
        fromIndex = fromIndex < 0 ? fromIndex + collection.size() : fromIndex;
        assert collection.size() >= fromIndex + 2 :
            "Invalid collection to create a pair from: size=%d, index=%d".formatted(collection.size(), fromIndex);
        return collection.stream().skip(fromIndex).limit(2).collect(toPair());
    }

    public static <T> @NotNull Pair<T, T> dupe(@Nullable T item) {
        return Pair.of(item, item);
    }

    public U first() {
        return first;
    }

    public V second() {
        return second;
    }

    @Override
    public U getKey() {
        return first;
    }

    @Override
    public V getValue() {
        return second;
    }

    @Override
    public V setValue(V value) {
        throw new UnsupportedOperationException("Pair is immutable");
    }

    public @NotNull Pair<U, V> withFirst(@Nullable U first) {
        return Pair.of(first, second);
    }

    public @NotNull Pair<U, V> withSecond(@Nullable V second) {
        return Pair.of(first, second);
    }

    public @NotNull <W> Triple<U, V, W> toTripleWith(@Nullable W third) {
        return Triple.of(first, second, third);
    }

    public @NotNull Tuple toTuple() {
        return Tuple.of(first, second);
    }

    public boolean isOneOf() {
        return first == null ^ second == null;
    }

    public @NotNull OneOf<U, V> toOneOf() {
        assert isOneOf() : "Can't convert a pair to a one-of: " + this;
        return OneOf.of(first, second);
    }

    public boolean isAnyOf() {
        return first != null || second != null;
    }

    public boolean isAllOf() {
        return first != null && second != null;
    }

    public boolean isNoneOf() {
        return this == EMPTY;
    }

    public int countNulls() {
        return castToInt(first == null) + castToInt(second == null);
    }

    public int countNonNulls() {
        return castToInt(first != null) + castToInt(second != null);
    }

    public @NotNull Pair<V, U> swap() {
        return Pair.of(second, first);
    }

    public <T, S> @NotNull Pair<T, S> map(@NotNull Function<? super U, ? extends T> convertFirst,
                                          @NotNull Function<? super V, ? extends S> convertSecond) {
        return Pair.of(convertFirst.apply(first), convertSecond.apply(second));
    }

    public <T> @NotNull Pair<T, V> mapFirst(@NotNull Function<? super U, ? extends T> convert) {
        return map(convert, second -> second);
    }

    public <T> @NotNull Pair<T, V> mapFirst(@NotNull BiFunction<? super U, ? super V, ? extends T> convert) {
        return Pair.of(convert.apply(first, second), second);
    }

    public <T> @NotNull Pair<U, T> mapSecond(@NotNull Function<? super V, ? extends T> convert) {
        return map(first -> first, convert);
    }

    public <T> @NotNull Pair<U, T> mapSecond(@NotNull BiFunction<? super U, ? super V, ? extends T> convert) {
        return Pair.of(first, convert.apply(first, second));
    }

    public <T> @NotNull T mapToObj(@NotNull BiFunction<? super U, ? super V, ? extends T> convert) {
        return convert.apply(first, second);
    }

    public int mapToInt(@NotNull ToIntBiFunction<? super U, ? super V> convert) {
        return convert.applyAsInt(first, second);
    }

    public long mapToLong(@NotNull ToLongBiFunction<? super U, ? super V> convert) {
        return convert.applyAsLong(first, second);
    }

    public double mapToDouble(@NotNull ToDoubleBiFunction<? super U, ? super V> convert) {
        return convert.applyAsDouble(first, second);
    }

    public boolean testFirst(@NotNull Predicate<? super U> predicate) {
        return predicate.test(first);
    }

    public boolean testSecond(@NotNull Predicate<? super V> predicate) {
        return predicate.test(second);
    }

    public boolean test(@NotNull BiPredicate<? super U, ? super V> predicate) {
        return predicate.test(first, second);
    }

    public void apply(@NotNull BiConsumer<? super U, ? super V> action) {
        action.accept(first, second);
    }

    public @NotNull Stream<Object> stream() {
        return Stream.of(first, second);
    }

    public @NotNull List<Object> toList() {
        return Arrays.asList(first, second);
    }

    public Object @NotNull[] toArray() {
        return new Object[] { first, second };
    }

    public static <U extends Comparable<U>, V extends Comparable<V>> @NotNull Comparator<Pair<U, V>> comparator() {
        return Comparator.<Pair<U, V>, U>comparing(Pair::first).thenComparing(Pair::second);
    }

    public static <U, V> @NotNull Comparator<Pair<U, V>> comparator(@NotNull Comparator<? super U> cmpFirst,
                                                                    @NotNull Comparator<? super V> cmpSecond) {
        return Comparator.<Pair<U, V>, U>comparing(Pair::first, cmpFirst).thenComparing(Pair::second, cmpSecond);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Pair<?, ?> that && Objects.equals(first, that.first) && Objects.equals(second, that.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(%s, %s)".formatted(first, second);
    }

    public static <E> @NotNull Collector<E, ?, Pair<E, E>> toPair() {
        return castAny(Collector.of(
            () -> new FixedArrayCollector(2),
            FixedArrayCollector::add,
            FixedArrayCollector::combine,
            c -> Pair.from(c.exactItems())
        ));
    }

    public static <E> @NotNull Collector<E, ?, Pair<E, E>> toPairSparse() {
        return castAny(Collector.of(
            () -> new FixedArrayCollector(2),
            FixedArrayCollector::add,
            FixedArrayCollector::combine,
            c -> Pair.from(c.items())
        ));
    }
}
