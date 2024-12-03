package io.spbx.util.collect.stream;

import com.google.common.collect.ImmutableSet;
import io.spbx.util.collect.stream.ToApiCommon.AcceptsNulls;
import io.spbx.util.collect.stream.ToApiCommon.DoesNotAcceptNulls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.spbx.util.collect.stream.ToApiCommon.assertNonNull;
import static io.spbx.util.func.ScopeFunctions.with;

public interface ToSetApi<E> extends ToStreamApi<E> {
    default <C extends Set<E>> @NotNull C toSet(@NotNull Supplier<C> supplier) {
        return toStream().collect(Collectors.toCollection(supplier));
    }

    @AcceptsNulls
    default @NotNull Set<E> toSet() {
        return toStream().collect(Collectors.toSet());
    }

    @AcceptsNulls
    default @NotNull HashSet<E> toHashSet() {
        return toSet(HashSet::new);
    }

    @AcceptsNulls
    default @NotNull LinkedHashSet<E> toLinkedHashSet() {
        return toSet(LinkedHashSet::new);
    }

    @DoesNotAcceptNulls
    default @NotNull TreeSet<E> toTreeSet() {
        return with(toStream(), assertNonNull("toTreeSet()")).collect(Collectors.toCollection(TreeSet::new));
    }

    @DoesNotAcceptNulls
    default @NotNull ImmutableSet<E> toGuavaImmutableSet() {
        return with(toStream(), assertNonNull("toGuavaImmutableSet()")).collect(ImmutableSet.toImmutableSet());
    }

    /* Static factory methods */

    static @SafeVarargs <E> @NotNull ToSetApi<E> of(@Nullable E @Nullable ... items) {
        return () -> BasicStreams.streamOf(items);
    }

    static <E> @NotNull ToSetApi<E> of(@NotNull Collection<@Nullable E> collection) {
        return collection::stream;
    }

    static <E> @NotNull ToSetApi<E> of(@NotNull Stream<@Nullable E> stream) {
        return () -> stream;
    }
}
