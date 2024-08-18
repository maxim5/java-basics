package io.spbx.util.collect;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@CheckReturnValue
public abstract class AbstractStreamer<E, R extends AbstractStreamer<E, R>> implements ToStreamApi<E> {
    final Stream<E> stream;

    AbstractStreamer(@NotNull Stream<E> stream) {
        this.stream = stream;
    }

    abstract @NotNull R create(@NotNull Stream<E> stream);

    @Override
    public final @NotNull Stream<E> toStream() {
        return stream;
    }

    /* `Stream` API */

    public @NotNull IntStream mapToInt(@NotNull ToIntFunction<? super E> mapper) {
        return stream.mapToInt(mapper);
    }

    public @NotNull LongStream mapToLong(@NotNull ToLongFunction<? super E> mapper) {
        return stream.mapToLong(mapper);
    }

    public @NotNull DoubleStream mapToDouble(@NotNull ToDoubleFunction<? super E> mapper) {
        return stream.mapToDouble(mapper);
    }

    public @NotNull IntStream flatMapToInt(@NotNull Function<? super E, ? extends IntStream> mapper) {
        return stream.flatMapToInt(mapper);
    }

    public @NotNull LongStream flatMapToLong(@NotNull Function<? super E, ? extends LongStream> mapper) {
        return stream.flatMapToLong(mapper);
    }

    public @NotNull DoubleStream flatMapToDouble(@NotNull Function<? super E, ? extends DoubleStream> mapper) {
        return stream.flatMapToDouble(mapper);
    }

    public @NotNull R filter(@NotNull Predicate<? super E> predicate) {
        return this.create(stream.filter(predicate));
    }

    public @NotNull R skipIf(@NotNull Predicate<? super E> predicate) {
        return this.filter(predicate.negate());
    }

    public @NotNull R skipNulls() {
        return this.create(stream.filter(Objects::nonNull));
    }

    public @NotNull R skip(long n) {
        return this.create(stream.skip(n));
    }

    public @NotNull R limit(long n) {
        return this.create(stream.limit(n));
    }

    public @NotNull R peek(@NotNull Consumer<? super E> consumer) {
        return this.create(stream.peek(consumer));
    }

    public @NotNull R distinct() {
        return this.create(stream.distinct());
    }

    public @NotNull R sorted() {
        return this.create(stream.sorted());
    }

    public @NotNull R sorted(@NotNull Comparator<? super E> comparator) {
        return this.create(stream.sorted(comparator));
    }
}
