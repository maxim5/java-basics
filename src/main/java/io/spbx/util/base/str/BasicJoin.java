package io.spbx.util.base.str;

import com.google.common.collect.ImmutableList;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.tuple.Pair;
import io.spbx.util.base.tuple.Triple;
import io.spbx.util.collect.stream.Streamer;
import io.spbx.util.func.Functions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Immutable
@CheckReturnValue
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
        return new BasicJoin(Streamer.of(items).map(Functions.nonNullify(toString, "")).toGuavaImmutableList());
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

    @Pure public @NotNull BasicJoin onlyNonEmpty() {
        return new BasicJoin(Streamer.of(items).filter(BasicStrings::isNotEmpty).toGuavaImmutableList());
    }

    @Pure public @NotNull String join() {
        return String.join("", items);
    }

    @Pure public @NotNull String join(@NotNull CharSequence separator) {
        return String.join(separator, items);
    }

    @Pure public @NotNull String join(char separator) {
        return String.join(String.valueOf(separator), items);
    }

    @Pure public @NotNull String join(@NotNull CharSequence sep, @NotNull CharSequence prefix, @NotNull CharSequence suffix) {
        return Streamer.of(items).join(sep, prefix, suffix);
    }

    @Override
    @Pure public String toString() {
        return join();
    }

    private static @NotNull String toString(@Nullable Object obj) {
        return BasicStrings.toStringOrEmpty(obj);
    }
}
