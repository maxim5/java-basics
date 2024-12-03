package io.spbx.util.collect.stream;

import com.google.common.collect.ImmutableList;
import io.spbx.util.base.tuple.Tuple;
import io.spbx.util.collect.array.Array;
import io.spbx.util.collect.array.ImmutableArray;
import io.spbx.util.collect.list.ImmutableArrayList;
import io.spbx.util.collect.stream.ToApiCommon.AcceptsNulls;
import io.spbx.util.collect.stream.ToApiCommon.DoesNotAcceptNulls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.spbx.util.base.lang.EasyCast.castAny;
import static io.spbx.util.collect.stream.ToApiCommon.assertNonNull;
import static io.spbx.util.func.ScopeFunctions.with;

public interface ToListApi<E> extends ToStreamApi<E> {
    default <L extends List<E>> @NotNull L toList(@NotNull Supplier<L> supplier) {
        return toStream().collect(Collectors.toCollection(supplier));
    }

    @DoesNotAcceptNulls
    default @NotNull List<E> toList() {
        return with(toStream(), assertNonNull("toList()")).toList();
    }

    @AcceptsNulls
    default @NotNull ArrayList<E> toArrayList() {
        return toList(ArrayList::new);
    }

    @AcceptsNulls
    default @NotNull List<E> toFixedSizeList() {
        return Arrays.asList(castAny(toNativeArray()));
    }

    @AcceptsNulls
    default @Nullable Object @NotNull[] toNativeArray() {
        return toStream().toArray();
    }

    @AcceptsNulls
    default @Nullable E @NotNull[] toNativeArray(@NotNull IntFunction<E[]> generator) {
        return toStream().toArray(generator);
    }

    @DoesNotAcceptNulls
    default @NotNull ImmutableList<E> toGuavaImmutableList() {
        return with(toStream(), assertNonNull("toGuavaImmutableList()")).collect(ImmutableList.toImmutableList());
    }

    @AcceptsNulls
    default @NotNull ImmutableArrayList<E> toBasicsImmutableArrayList() {
        return toStream().collect(ImmutableArrayList.toImmutableArrayList());
    }

    @AcceptsNulls
    default @NotNull Array<E> toBasicsMutableArray() {
        return Array.<E>builder().addAll(toFixedSizeList()).toArray();
    }

    @AcceptsNulls
    default @NotNull ImmutableArray<E> toBasicsImmutableArray() {
        return ImmutableArray.<E>builder().addAll(toFixedSizeList()).toArray();
    }

    @AcceptsNulls
    default @NotNull Tuple toBasicsTuple() {
        return Tuple.of(toNativeArray());
    }

    /* Static factory methods */

    static @SafeVarargs <E> @NotNull ToListApi<E> of(@Nullable E @Nullable ... items) {
        return () -> BasicStreams.streamOf(items);
    }

    static <E> @NotNull ToListApi<E> of(@Nullable Iterable<@Nullable E> iterable) {
        return () -> BasicStreams.streamOf(iterable);
    }

    static <E> @NotNull ToListApi<E> of(@Nullable Stream<@Nullable E> stream) {
        return () -> BasicStreams.emptyIfNull(stream);
    }
}
