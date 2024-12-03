package io.spbx.util.collect.stream;

import io.spbx.util.collect.stream.ToApiCommon.AcceptsAllStreams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ToJoinApi<E> extends ToStreamApi<E> {
    default @NotNull String join(@NotNull Collector<CharSequence, ?, String> collector) {
        return toStringStream().collect(collector);
    }

    @AcceptsAllStreams
    default @NotNull String join() {
        return join(Collectors.joining());
    }

    @AcceptsAllStreams
    default @NotNull String join(char sep) {
        return join(Collectors.joining(String.valueOf(sep)));
    }

    @AcceptsAllStreams
    default @NotNull String join(@NotNull CharSequence separator) {
        return join(Collectors.joining(separator));
    }

    @AcceptsAllStreams
    default @NotNull String join(char sep, char prefix, char suffix) {
        return join(Collectors.joining(String.valueOf(sep), String.valueOf(prefix), String.valueOf(suffix)));
    }

    @AcceptsAllStreams
    default @NotNull String join(@NotNull CharSequence sep, @NotNull CharSequence prefix, @NotNull CharSequence suffix) {
        return join(Collectors.joining(sep, prefix, suffix));
    }

    @AcceptsAllStreams
    default @NotNull String joinLines() {
        return join("\n");
    }

    /* Static factory methods */

    static @SafeVarargs <E> @NotNull ToJoinApi<E> of(@Nullable E @Nullable ... items) {
        return () -> BasicStreams.streamOf(items);
    }

    static <E> @NotNull ToJoinApi<E> of(@Nullable Iterable<@Nullable E> iterable) {
        return () -> BasicStreams.streamOf(iterable);
    }

    static <E> @NotNull ToJoinApi<E> of(@Nullable Stream<@Nullable E> stream) {
        return () -> BasicStreams.emptyIfNull(stream);
    }

    /* Implementation */

    private @NotNull Stream<String> toStringStream() {
        return toStream().map(Objects::toString);
    }
}
