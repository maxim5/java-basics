package io.spbx.util.collect;

import com.google.errorprone.annotations.Immutable;
import io.spbx.util.base.Pair;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Immutable
@CheckReturnValue
public final class BiStreamer<K, V> extends AbstractStreamer<Pair<K, V>, BiStreamer<K, V>> implements ToCollectKvApi<K, V, Pair<K, V>> {
    private BiStreamer(@NotNull Stream<Pair<K, V>> stream) {
        super(stream);
    }

    @Override @NotNull BiStreamer<K, V> create(@NotNull Stream<Pair<K, V>> stream) {
        return new BiStreamer<>(stream);
    }

    static <K, V> @NotNull BiStreamer<K, V> of(@NotNull Stream<Pair<K, V>> stream) {
        return new BiStreamer<>(stream);
    }

    /* `Stream` API */

    public <K2, V2> @NotNull BiStreamer<K2, V2> map(@NotNull Function<? super Pair<K, V>, Pair<K2, V2>> mapper) {
        return of(stream.map(mapper));
    }

    public <K2, V2> @NotNull BiStreamer<K2, V2> map(@NotNull Function<? super K, ? extends K2> keyMap,
                                                    @NotNull Function<? super V, ? extends V2> valMap) {
        return of(stream.map(pair -> pair.map(keyMap, valMap)));
    }

    public <K2, V2> @NotNull BiStreamer<K2, V2> map(
            @NotNull BiFunction<? super K, ? super V, Pair<K2, V2>> mapper) {
        return of(stream.map(pair -> pair.mapToObj(mapper)));
    }

    public <K2, V2> @NotNull BiStreamer<K2, V2> flatMap(
            @NotNull Function<? super Pair<K, V>, ? extends Stream<? extends Pair<K2, V2>>> mapper) {
        return of(stream.flatMap(mapper));
    }

    public <K2, V2> @NotNull BiStreamer<K2, V2> flatMap(
            @NotNull BiFunction<? super K, ? super V, ? extends Stream<? extends Pair<K2, V2>>> mapper) {
        return of(stream.flatMap(pair -> pair.mapToObj(mapper)));
    }

    public <K2, V2> @NotNull BiStreamer<K2, V2> flatMap(
            @NotNull Function<? super K, ? extends Stream<? extends K2>> keyMap,
            @NotNull Function<? super V, ? extends Stream<? extends V2>> valMap) {
        return of(stream.flatMap(pair -> pair.map(keyMap, valMap).mapToObj(BasicStreams::zip)));
    }

    public <K2, V2> @NotNull BiStreamer<K2, V2> mapMulti(
            @NotNull BiConsumer<? super Pair<K, V>, ? super Consumer<Pair<K2, V2>>> mapper) {
        return of(stream.mapMulti(mapper));
    }

    public <K2> @NotNull BiStreamer<K2, V> mapKeys(@NotNull Function<? super K, ? extends K2> keyMap) {
        return of(stream.map(pair -> pair.mapFirst(keyMap)));
    }

    public <K2> @NotNull BiStreamer<K2, V> mapKeys(@NotNull BiFunction<? super K, ? super V, ? extends K2> keyMap) {
        return of(stream.map(pair -> pair.mapFirst(keyMap)));
    }

    public <V2> @NotNull BiStreamer<K, V2> mapValues(@NotNull Function<? super V, ? extends V2> valMap) {
        return of(stream.map(pair -> pair.mapSecond(valMap)));
    }

    public <V2> @NotNull BiStreamer<K, V2> mapValues(@NotNull BiFunction<? super K, ? super V, ? extends V2> valMap) {
        return of(stream.map(pair -> pair.mapSecond(valMap)));
    }

    public @NotNull BiStreamer<K, V> filter(@NotNull BiPredicate<? super K, ? super V> predicate) {
        return of(stream.filter(pair -> pair.test(predicate)));
    }

    public @NotNull BiStreamer<K, V> filterKeys(@NotNull Predicate<? super K> predicate) {
        return of(stream.filter(pair -> pair.testFirst(predicate)));
    }

    public @NotNull BiStreamer<K, V> filterValues(@NotNull Predicate<? super V> predicate) {
        return of(stream.filter(pair -> pair.testSecond(predicate)));
    }

    public @NotNull BiStreamer<K, V> skipIf(@NotNull BiPredicate<? super K, ? super V> predicate) {
        return of(stream.filter(pair -> pair.test(predicate.negate())));
    }

    public @NotNull BiStreamer<K, V> skipKeysIf(@NotNull Predicate<? super K> predicate) {
        return of(stream.filter(pair -> pair.testFirst(predicate.negate())));
    }

    public @NotNull BiStreamer<K, V> skipValuesIf(@NotNull Predicate<? super V> predicate) {
        return of(stream.filter(pair -> pair.testSecond(predicate)));
    }

    @Override
    public @NotNull BiStreamer<K, V> skipNulls() {
        return skipNullKeys().skipNullValues();
    }

    public @NotNull BiStreamer<K, V> skipNullKeys() {
        return filterKeys(Objects::nonNull);
    }

    public @NotNull BiStreamer<K, V> skipNullValues() {
        return filterValues(Objects::nonNull);
    }

    public @NotNull BiStreamer<V, K> inverse() {
        return of(stream.map(Pair::swap));
    }

    /* Conversion to `Streamer` */

    public @NotNull <T> Streamer<T> mapToObj(@NotNull Function<Pair<K, V>, T> func) {
        return Streamer.of(stream.map(func));
    }

    public @NotNull <T> Streamer<T> mapToObj(@NotNull BiFunction<K, V, T> func) {
        return Streamer.of(stream.map(pair -> pair.mapToObj(func)));
    }

    public @NotNull <T> Streamer<T> flatMapToObj(@NotNull Function<Pair<K, V>, ? extends Stream<T>> func) {
        return Streamer.of(stream.flatMap(func));
    }

    public @NotNull <T> Streamer<T> flatMapToObj(@NotNull BiFunction<K, V, ? extends Stream<T>> func) {
        return Streamer.of(stream.flatMap(pair -> pair.mapToObj(func)));
    }

    public @NotNull Streamer<Pair<K, V>> pairs() {
        return Streamer.of(stream);
    }
}
