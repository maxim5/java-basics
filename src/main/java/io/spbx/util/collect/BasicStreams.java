package io.spbx.util.collect;

import com.google.common.collect.Streams;
import io.spbx.util.base.Pair;
import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BasicStreams {
    /* `Stream` factory */

    public static <T> @NotNull Stream<T> empty() {
        return Stream.empty();
    }

    public static <T> @NotNull Stream<T> single(@Nullable T item) {
        return Stream.of(item);
    }

    public static <T> @NotNull Stream<T> streamOf() {
        return empty();
    }

    public static <T> @NotNull Stream<T> streamOf(@Nullable T item) {
        return single(item);
    }

    public static @SafeVarargs <T> @NotNull Stream<T> streamOf(@Nullable T @Nullable... items) {
        return items != null ? Stream.of(items) : Stream.empty();
    }

    public static <T> @NotNull Stream<T> streamOf(@Nullable T @Nullable[] items, int start) {
        return streamOf(items).skip(start);
    }

    public static <T> @NotNull Stream<T> streamOf(@Nullable T @Nullable[] items, int start, int length) {
        return streamOf(items).skip(start).limit(length);
    }

    public static <T> @NotNull Stream<T> streamOf(@Nullable Iterable<@Nullable T> iterable) {
        return iterable == null ? Stream.empty() :
            iterable instanceof Collection<T> collection
                ? collection.stream()
                : StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> @NotNull Stream<T> streamOf(@NotNull Iterator<@Nullable T> iterator) {
        return streamOf(Spliterators.spliteratorUnknownSize(iterator, 0));
    }

    public static <T> @NotNull Stream<T> streamOf(@NotNull Spliterator<@Nullable T> spliterator) {
        return StreamSupport.stream(spliterator, false);
    }

    public static <T> @NotNull Stream<T> repeat(@Nullable T item, int times) {
        return times == 0 ? empty() : times == 1 ? single(item) : IntStream.range(0, times).mapToObj(i -> item);
    }

    /* Null handling */

    @Pure
    public static <E> @NotNull Stream<E> emptyIfNull(@Nullable Stream<E> stream) {
        return stream != null ? stream : Stream.empty();
    }

    @Pure
    public static @NotNull IntStream emptyIfNull(@Nullable IntStream stream) {
        return stream != null ? stream : IntStream.empty();
    }

    @Pure
    public static @NotNull LongStream emptyIfNull(@Nullable LongStream stream) {
        return stream != null ? stream : LongStream.empty();
    }

    /* `Stream` concatenation */

    @Pure
    public static <E> @NotNull Stream<E> concat(@Nullable Stream<? extends E> first,
                                                @Nullable Stream<? extends E> second) {
        return Stream.concat(emptyIfNull(first), emptyIfNull(second));
    }

    @Pure
    public static <E> @NotNull Stream<E> concat(@Nullable Iterable<? extends E> first,
                                                @Nullable Iterable<? extends E> second) {
        return Stream.concat(streamOf(first), streamOf(second));
    }

    @Pure
    public static <E> @NotNull Stream<E> concat(@Nullable E @Nullable[] first,
                                                @Nullable E @Nullable[] second) {
        return Stream.concat(streamOf(first), streamOf(second));
    }

    @Pure
    public static <E> @NotNull Stream<E> concat(@Nullable Stream<? extends E> first,
                                                @Nullable Stream<? extends E> second,
                                                @Nullable Stream<? extends E> third) {
        return Stream.concat(Stream.concat(emptyIfNull(first), emptyIfNull(second)), emptyIfNull(third));
    }

    @Pure
    public static <E> @NotNull Stream<E> concat(@Nullable Iterable<? extends E> first,
                                                @Nullable Iterable<? extends E> second,
                                                @Nullable Iterable<? extends E> third) {
        return Stream.concat(Stream.concat(streamOf(first), streamOf(second)), streamOf(third));
    }

    @Pure
    public static <E> @NotNull Stream<E> concat(@Nullable E @Nullable[] first,
                                                @Nullable E @Nullable[] second,
                                                @Nullable E @Nullable[] third) {
        return Stream.concat(Stream.concat(streamOf(first), streamOf(second)), streamOf(third));
    }

    /* `Stream` concatenation */

    @Pure
    public static <A, B, R> @NotNull Stream<R> zip(@Nullable Stream<? extends A> first,
                                                   @Nullable Stream<? extends B> second,
                                                   @NotNull BiFunction<? super A, ? super B, R> function) {
        return Streams.zip(emptyIfNull(first), emptyIfNull(second), function);
    }

    @Pure
    public static <A, B> @NotNull Stream<Pair<A, B>> zip(@Nullable Stream<? extends A> first,
                                                         @Nullable Stream<? extends B> second) {
        return zip(first, second, Pair::of);
    }
}
