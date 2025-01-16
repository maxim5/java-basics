package io.spbx.util.base.tuple;

import io.spbx.util.base.annotate.AllowPythonIndexing;
import io.spbx.util.base.annotate.PyIndex;
import io.spbx.util.base.error.BasicExceptions.IllegalStateExceptions;
import io.spbx.util.base.error.RangeCheck;
import io.spbx.util.base.lang.IntLength;
import io.spbx.util.func.Functions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static io.spbx.util.base.lang.EasyCast.castAny;
import static io.spbx.util.func.ScopeFunctions.with;

/**
 * Holds an immutable tuple of nullable objects.
 *
 * @see Pair
 * @see Triple
 * @see OneOf
 */
@Immutable
public final class Tuple implements IntLength, Iterable<Object>, Serializable {
    private static final Tuple EMPTY = new Tuple(new Object[0]);

    private final Object[] values;

    private Tuple(@Nullable Object @NotNull[] values) {
        this.values = values;
    }

    public static @NotNull Tuple of(@Nullable Object @NotNull ... values) {
        return values.length == 0 ? EMPTY : new Tuple(Arrays.copyOf(values, values.length));
    }

    public static @NotNull Tuple from(@NotNull Collection<?> collection) {
        return collection.isEmpty() ? EMPTY : new Tuple(collection.toArray());
    }

    public static @NotNull Tuple from(@NotNull Stream<?> stream) {
        return with(stream.toArray(), array -> array.length == 0 ? EMPTY : new Tuple(array));
    }

    @Override
    public int length() {
        return values.length;
    }

    public <T> T first() {
        assert length() > 0 : "Tuple is empty";
        return castAny(values[0]);
    }

    public <T> T last() {
        assert length() > 0 : "Tuple is empty";
        return castAny(values[values.length - 1]);
    }

    @AllowPythonIndexing
    public <T> T at(@PyIndex int i) {
        assert rangeCheck(i);
        return castAny(values[translateIndex(i)]);
    }

    @AllowPythonIndexing
    public @NotNull Tuple slice(@PyIndex int i, @PyIndex int j) {
        Object[] slice = toArray(i, j);
        return Tuple.of(slice);
    }

    public <U, V> @NotNull Pair<U, V> toPair() {
        assert values.length == 2 : "Tuple can't be converted to a pair: " + this;
        return Pair.of(at(0), at(1));
    }

    public <U, V, W> @NotNull Triple<U, V, W> toTriple() {
        assert values.length == 3 : "Tuple can't be converted to a triple: " + this;
        return Triple.of(at(0), at(1), at(2));
    }

    public boolean isOneOf() {
        return stream().filter(Objects::nonNull).count() == 1;
    }

    public boolean isAnyOf() {
        return stream().anyMatch(Objects::nonNull);
    }

    public boolean isAllOf() {
        return stream().allMatch(Objects::nonNull);
    }

    public boolean isNoneOf() {
        return stream().allMatch(Objects::isNull);
    }

    public int countNulls() {
        return stream().mapToInt(val -> val == null ? 1 : 0).sum();
    }

    public int countNonNulls() {
        return stream().mapToInt(val -> val == null ? 0 : 1).sum();
    }

    @AllowPythonIndexing
    public @NotNull Tuple withNth(@PyIndex int i, @Nullable Object value) {
        assert rangeCheck(i);
        Object[] copy = toArray();
        copy[translateIndex(i)] = value;
        return Tuple.of(copy);
    }

    @AllowPythonIndexing
    public <T, R> @NotNull Tuple mapNth(@PyIndex int i, @NotNull Function<T, R> convert) {
        assert rangeCheck(i);
        i = translateIndex(i);
        return withNth(i, convert.apply(at(i)));
    }

    @AllowPythonIndexing
    public <T> boolean testNth(@PyIndex int i, @NotNull Predicate<T> predicate) {
        assert rangeCheck(i);
        i = translateIndex(i);
        return predicate.test(at(i));
    }

    @Override
    public @NotNull Iterator<Object> iterator() {
        return stream().iterator();
    }

    public @NotNull Stream<Object> stream() {
        return Stream.of(values);
    }

    public @NotNull List<Object> toList() {
        return Arrays.asList(values);
    }

    public Object @NotNull[] toArray() {
        return Arrays.copyOf(values, values.length);
    }

    @AllowPythonIndexing
    public Object @NotNull[] toArray(@PyIndex int i, @PyIndex int j) {
        assert rangeCheck(i, j);
        return Arrays.copyOfRange(values, translateIndex(i), translateIndex(j));
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Tuple that && Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    public static <E> @NotNull Collector<E, ?, Tuple> toTuple(int length) {
        assert length >= 0 : "Length can not be negative: " + length;
        return castAny(Collector.of(
            () -> new FixedArrayCollector(length),
            FixedArrayCollector::add,
            FixedArrayCollector::combine,
            Functions.chain(FixedArrayCollector::exactItems, Tuple::of)
        ));
    }

    public static <E> @NotNull Collector<E, ?, Tuple> toTupleSparse(int length) {
        assert length >= 0 : "Length can not be negative: " + length;
        return castAny(Collector.of(
            () -> new FixedArrayCollector(length),
            FixedArrayCollector::add,
            FixedArrayCollector::combine,
            Functions.chain(FixedArrayCollector::items, Tuple::of)
        ));
    }

    // Reused also by `Pair` and `Triple`
    static class FixedArrayCollector {
        private final Object[] items;
        private int count = 0;

        protected FixedArrayCollector(int len) {
            assert len >= 0 : "FixedArrayCollector length can not be negative: " + len;
            items = new Object[len];
        }

        protected @NotNull Object[] items() {
            return items;
        }

        protected @NotNull Object[] exactItems() {
            IllegalStateExceptions.assure(count == items.length, "Too few objects collected than a tuple can hold");
            return items;
        }

        protected void add(@Nullable Object obj) {
            IllegalStateExceptions.assure(count < items.length, "Too many objects collected than a tuple can hold");
            items[count++] = obj;
        }

        protected @NotNull FixedArrayCollector combine(@NotNull FixedArrayCollector that) {
            for (int i = 0; i < that.count; i++)
                this.add(that.items[i]);
            return this;
        }
    }

    /* Negative index translation and Range check */

    private int translateIndex(@PyIndex int i) {
        return RangeCheck.translateIndex(this, i);
    }

    private boolean rangeCheck(@PyIndex int i) {
        return RangeCheck.rangeCheck(this, i, RangeCheck.BEFORE_TRANSLATION | RangeCheck.OPEN_END_RANGE);
    }

    private boolean rangeCheck(@PyIndex int i, @PyIndex int j) {
        return RangeCheck.rangeCheck(this, i, j, RangeCheck.BEFORE_TRANSLATION | RangeCheck.OPEN_END_RANGE);
    }
}
