package io.spbx.util.text;

import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.CheckReturnValue;
import com.google.errorprone.annotations.Immutable;
import io.spbx.util.base.BasicStrings;
import io.spbx.util.base.Pair;
import io.spbx.util.base.Triple;
import io.spbx.util.collect.Streamer;
import io.spbx.util.func.Chains;
import org.checkerframework.dataflow.qual.Pure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Immutable
public class BasicJoin {
    private final ImmutableList<String> items;

    private BasicJoin(@NotNull List<String> items) {
        this.items = ImmutableList.copyOf(items);
    }

    public static @NotNull BasicJoin singleOf(@Nullable Object item) {
        return new BasicJoin(ImmutableList.of(toString(item)));
    }

    public static @NotNull BasicJoin of(@Nullable Object item1, @Nullable Object item2) {
        return new BasicJoin(ImmutableList.of(toString(item1), toString(item2)));
    }

    public static @NotNull BasicJoin of(@Nullable Object item1, @Nullable Object item2, @Nullable Object item3) {
        return new BasicJoin(ImmutableList.of(toString(item1), toString(item2), toString(item3)));
    }

    public static @NotNull BasicJoin of(@Nullable Object item1, @Nullable Object item2, @Nullable Object item3,
                                        @Nullable Object @NotNull... items) {
        return new BasicJoin(
            Streamer.concat(Stream.of(item1, item2, item3), Stream.of(items)).map(BasicJoin::toString).toGuavaImmutableList()
        );
    }

    public static @NotNull BasicJoin of(@Nullable Object @NotNull[] items) {
        return new BasicJoin(Streamer.of(items).map(BasicJoin::toString).toGuavaImmutableList());
    }

    public static @NotNull BasicJoin of(@NotNull Iterable<? extends @Nullable Object> items) {
        return BasicJoin.of(items, BasicJoin::toString);
    }

    public static <T extends @Nullable Object> @NotNull BasicJoin of(@NotNull Iterable<T> items,
                                                                     @NotNull Function<T, String> toString) {
        return new BasicJoin(Streamer.of(items).map(Chains.nonNullify(toString, "")).toGuavaImmutableList());
    }

    public static @NotNull BasicJoin of(@NotNull Stream<? extends @Nullable Object> items) {
        return BasicJoin.of(items.map(BasicJoin::toString).collect(ImmutableList.toImmutableList()));
    }

    public static @NotNull BasicJoin of(@NotNull Pair<?, ?> pair) {
        return pair.map(BasicJoin::toString, BasicJoin::toString).mapToObj(BasicJoin::of);
    }

    public static @NotNull BasicJoin of(@NotNull Triple<?, ?, ?> triple) {
        return triple.map(BasicJoin::toString, BasicJoin::toString, BasicJoin::toString).mapToObj(BasicJoin::of);
    }

    @CheckReturnValue
    public @NotNull BasicJoin onlyNonEmpty() {
        return new BasicJoin(Streamer.of(items).filter(BasicStrings::isNotEmpty).toGuavaImmutableList());
    }

    @Pure
    @CheckReturnValue
    public @NotNull String join() {
        return String.join("", items);
    }

    @Pure
    @CheckReturnValue
    public @NotNull String join(@NotNull CharSequence separator) {
        return String.join(separator, items);
    }

    @Pure
    @CheckReturnValue
    public @NotNull String join(char separator) {
        return String.join(String.valueOf(separator), items);
    }

    @Pure
    @CheckReturnValue
    public @NotNull String join(@NotNull CharSequence sep, @NotNull CharSequence prefix, @NotNull CharSequence suffix) {
        return Streamer.of(items).join(sep, prefix, suffix);
    }

    @Pure
    @Override
    public String toString() {
        return join();
    }

    private static @NotNull String toString(@Nullable Object obj) {
        return BasicStrings.toStringOrEmpty(obj);
    }
}
