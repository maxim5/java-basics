package io.spbx.util.base;

import com.google.errorprone.annotations.Immutable;
import io.spbx.util.annotate.NegativeIndexingSupported;
import io.spbx.util.base.BasicExceptions.IllegalStateExceptions;
import io.spbx.util.buf.BaseBuf;
import io.spbx.util.func.Chains;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

import static io.spbx.util.base.EasyCast.castAny;
import static io.spbx.util.func.ScopeFunctions.with;

/**
 * Holds an immutable tuple of nullable objects.
 *
 * @see Pair
 * @see Triple
 * @see OneOf
 */
@Immutable
public final class Tuple extends BaseBuf implements Iterable<Object>, Serializable {
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

    @NegativeIndexingSupported
    public <T> T at(int i) {
        assert rangeCheck(i, BEFORE_TRANSLATION | OPEN_END_RANGE);
        return castAny(values[translateIndex(i)]);
    }

    @NegativeIndexingSupported
    public @NotNull Tuple slice(int i, int j) {
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

    @NegativeIndexingSupported
    public @NotNull Tuple withNth(int i, @Nullable Object value) {
        assert rangeCheck(i, BEFORE_TRANSLATION | OPEN_END_RANGE);
        Object[] copy = toArray();
        copy[translateIndex(i)] = value;
        return Tuple.of(copy);
    }

    @NegativeIndexingSupported
    public <T, R> @NotNull Tuple mapNth(int i, @NotNull Function<T, R> convert) {
        assert rangeCheck(i, BEFORE_TRANSLATION | OPEN_END_RANGE);
        i = translateIndex(i);
        return withNth(i, convert.apply(at(i)));
    }

    @NegativeIndexingSupported
    public <T> boolean testNth(int i, @NotNull Predicate<T> predicate) {
        assert rangeCheck(i, BEFORE_TRANSLATION | OPEN_END_RANGE);
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

    @NegativeIndexingSupported
    public Object @NotNull[] toArray(int i, int j) {
        assert rangeCheck(i, j, BEFORE_TRANSLATION | CLOSE_END_RANGE);
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
            Chains.chain(FixedArrayCollector::exactItems, Tuple::of)
        ));
    }

    public static <E> @NotNull Collector<E, ?, Tuple> toTupleSparse(int length) {
        assert length >= 0 : "Length can not be negative: " + length;
        return castAny(Collector.of(
            () -> new FixedArrayCollector(length),
            FixedArrayCollector::add,
            FixedArrayCollector::combine,
            Chains.chain(FixedArrayCollector::items, Tuple::of)
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
}
