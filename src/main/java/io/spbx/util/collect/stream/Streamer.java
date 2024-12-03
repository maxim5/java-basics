package io.spbx.util.collect.stream;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Immutable
@CheckReturnValue
public final class Streamer<E> extends AbstractStreamer<E, Streamer<E>> implements ToCollectApi<E> {
    private Streamer(@NotNull Stream<E> stream) {
        super(stream);
    }

    @Override @NotNull Streamer<E> create(@NotNull Stream<E> stream) {
        return new Streamer<>(stream);
    }

    /* `Streamer` static factory methods */

    public static <E> @NotNull Streamer<E> of(@Nullable Stream<E> stream) {
        return new Streamer<>(BasicStreams.emptyIfNull(stream));
    }

    public static <E> @NotNull Streamer<E> of(@Nullable Iterable<@Nullable E> items) {
        return of(BasicStreams.streamOf(items));
    }

    public static <E> @NotNull Streamer<E> of() {
        return of(BasicStreams.empty());
    }

    public static <E> @NotNull Streamer<E> of(@Nullable E item) {
        return of(BasicStreams.single(item));
    }

    public static @SafeVarargs <E> @NotNull Streamer<E> of(@Nullable E @Nullable... items) {
        return of(BasicStreams.streamOf(items));
    }

    public static <E> @NotNull Streamer<E> concat(@Nullable Stream<E> first, @Nullable Stream<E> second) {
        return of(BasicStreams.concat(first, second));
    }

    public static <E> @NotNull Streamer<E> concat(@Nullable Iterable<E> first, @Nullable Iterable<E> second) {
        return of(BasicStreams.concat(first, second));
    }

    public static <E> @NotNull Streamer<E> concat(@Nullable E @Nullable[] first, @Nullable E @Nullable[] second) {
        return of(BasicStreams.concat(first, second));
    }

    public static <E> @NotNull Streamer<E> concat(@Nullable Streamer<E> first, @Nullable Streamer<E> second) {
        return of(BasicStreams.concat(first != null ? first.stream : null, second != null ? second.stream : null));
    }

    public static <E> @NotNull Streamer<E> repeat(@Nullable E item, int times) {
        return of(BasicStreams.repeat(item, times));
    }

    /* `BiStreamer` static factory methods */

    public static final class Bi {
        public static <K, V> @NotNull BiStreamer<K, V> of(@NotNull Stream<? extends Entry<? extends K, ? extends V>> stream) {
            return BiStreamer.of(stream.map(Bi::toPair));
        }

        public static <K, V> @NotNull BiStreamer<K, V> of(@Nullable Iterable<? extends Entry<? extends K, ? extends V>> items) {
            return BiStreamer.of(BasicStreams.streamOf(items).map(Bi::toPair));
        }

        public static @SafeVarargs <K, V> @NotNull BiStreamer<K, V> of(@Nullable Pair<? extends K, ? extends V> @Nullable... items) {
            return BiStreamer.of(BasicStreams.streamOf(items).map(Bi::toPair));
        }

        public static @SafeVarargs <K, V> @NotNull BiStreamer<K, V> of(@Nullable Entry<? extends K, ? extends V> @Nullable... items) {
            return BiStreamer.of(BasicStreams.streamOf(items).map(Bi::toPair));
        }

        private static <U, V> @NotNull Pair<U, V> toPair(@Nullable Entry<? extends U, ? extends V> entry) {
            return entry == null ? Pair.empty() : Pair.of(entry);
        }
    }

    public static <K, V> @NotNull BiStreamer<K, V> of(@Nullable Map<K, V> map) {
        return BiStreamer.of(map != null ? map.entrySet().stream().map(Pair::of) : Stream.empty());
    }

    public static <K, V> @NotNull BiStreamer<K, V> zip(@Nullable Stream<K> keys, @Nullable Stream<V> values) {
        return BiStreamer.of(BasicStreams.zip(keys, values, Pair::of));
    }

    public static <K, V> @NotNull BiStreamer<K, V> zip(@Nullable Iterable<K> keys, @Nullable Iterable<V> values) {
        return zip(BasicStreams.streamOf(keys), BasicStreams.streamOf(values));
    }

    public static <K, V> @NotNull BiStreamer<K, V> zip(@Nullable K @Nullable[] keys, @Nullable V @Nullable[] values) {
        return zip(BasicStreams.streamOf(keys), BasicStreams.streamOf(values));
    }

    /* `Stream` API */

    public <T> @NotNull Streamer<T> map(@NotNull Function<? super E, ? extends T> mapper) {
        return of(stream.map(mapper));
    }

    public <T> @NotNull Streamer<T> mapIfNonNull(@NotNull Function<? super E, ? extends T> mapper) {
        return mapIfPresent(e -> Optional.ofNullable(mapper.apply(e)));
    }

    public <T> @NotNull Streamer<T> mapIfPresent(@NotNull Function<? super E, ? extends Optional<? extends T>> mapper) {
        return of(stream.flatMap(e -> mapper.apply(e).stream()));
    }

    // Note: the index may be different from the index in the original container, if operations like `skip(n)` are applied.
    public <T> @NotNull Streamer<T> mapIndex(@NotNull IndexedFunction<? super E, ? extends T> mapper) {
        return of(stream.map(mapper.toFunction()));
    }

    public <T> @NotNull Streamer<T> flatMap(@NotNull Function<? super E, ? extends Stream<? extends T>> mapper) {
        return of(stream.flatMap(mapper));
    }

    // Note: the index may be different from the index in the original container, if operations like `skip(n)` are applied.
    public <T> @NotNull Streamer<T> flatMapIndex(@NotNull IndexedFunction<? super E, ? extends Stream<? extends T>> mapper) {
        return of(stream.flatMap(mapper.toFunction()));
    }

    public <T> @NotNull Streamer<T> mapMulti(@NotNull BiConsumer<? super E, ? super Consumer<T>> mapper) {
        return of(stream.mapMulti(mapper));
    }

    // FIX: Primitive-candidate
    public interface IndexedFunction<E, R> {
        R apply(E t, int value);

        private @NotNull Function<E, R> toFunction() {
            return new Function<>() {
                int index = 0;

                @Override public R apply(E e) {
                    return IndexedFunction.this.apply(e, index++);
                }
            };
        }
    }

    /* Conversion to `BiStreamer`*/

    public @NotNull BiStreamer<E, E> split() {
        return this.split(Pair::dupe);
    }

    public @NotNull <K, V> BiStreamer<K, V> split(
            @NotNull Function<? super E, ? extends Entry<? extends K, ? extends V>> mapper) {
        return Bi.of(stream.map(mapper));
    }

    public @NotNull <K, V> BiStreamer<K, V> split(@NotNull Function<? super E, ? extends K> keyMap,
                                                  @NotNull Function<? super E, ? extends V> valMap) {
        return BiStreamer.of(stream.map(e -> Pair.of(keyMap.apply(e), valMap.apply(e))));
    }

    public <K, V> @NotNull BiStreamer<K, V> flatSplit(@NotNull Function<? super E, ? extends Stream<Pair<K, V>>> mapper) {
        return BiStreamer.of(stream.flatMap(mapper));
    }

    public <K, V> @NotNull BiStreamer<K, V> flatSplit(@NotNull Function<? super E, ? extends Stream<? extends K>> keyMap,
                                                      @NotNull Function<? super E, ? extends Stream<? extends V>> valMap) {
        return BiStreamer.of(stream.flatMap(e -> Pair.of(e, e).map(keyMap, valMap).mapToObj(BasicStreams::zip)));
    }

    public @NotNull <K> BiStreamer<K, E> zipLeft(@NotNull Supplier<? extends K> supplier) {
        return this.split(e -> Pair.of(supplier.get(), e));
    }

    public @NotNull <K> BiStreamer<K, E> zipLeft(@NotNull Function<? super E, ? extends K> keyMap) {
        return this.split(e -> Pair.of(keyMap.apply(e), e));
    }

    public @NotNull <K> BiStreamer<K, E> flatZipLeft(@NotNull Function<? super E, ? extends Stream<K>> keyMap) {
        return this.flatSplit(e -> keyMap.apply(e).map(key -> Pair.of(key, e)));
    }

    public @NotNull <V> BiStreamer<E, V> zipRight(@NotNull Supplier<? extends V> supplier) {
        return this.split(e -> Pair.of(e, supplier.get()));
    }

    public @NotNull <V> BiStreamer<E, V> zipRight(@NotNull Function<? super E, ? extends V> func) {
        return this.split(e -> Pair.of(e, func.apply(e)));
    }

    public @NotNull <V> BiStreamer<E, V> flatZipRight(@NotNull Function<? super E, ? extends Stream<V>> func) {
        return this.flatSplit(e -> func.apply(e).map(val -> Pair.of(e, val)));
    }
}
