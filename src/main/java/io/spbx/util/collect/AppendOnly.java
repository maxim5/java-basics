package io.spbx.util.collect;

import io.spbx.util.collect.LongSize.DistributedLongSize;
import io.spbx.util.func.Predicates;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A container which can only append values to the end and once appended they are immutable.
 *
 * @param <T>
 */
public interface AppendOnly<T> extends DistributedLongSize, Iterable<T> {
    /**
     * Appends the {@code value} to the end of collection.
     *
     * @throws IllegalArgumentException if the value is unsupported by the current implementation
     * @throws IllegalStateException if the current implementation can't be updated
     */
    void append(@NotNull T value) throws IllegalArgumentException, IllegalStateException;

    /**
     * Iterates over the values in sequence, from the first appended to the last.
     * Stops when the {@code predicate} returns {@code false}.
     */
    <P extends Predicate<? super T>> @NotNull P iterate(@NotNull P predicate);

    /**
     * Iterates over all values in sequence, from the first appended to the last.
     */
    @Override
    default void forEach(@NotNull Consumer<? super T> consumer) {
        iterate(Predicates.peekAndReturnTrue(consumer));
    }

    /**
     * Copies the entire collection to the {@link ArrayList}.
     * Only supports cases when the container stores less than {@link Integer#MAX_VALUE} items.
     */
    default @NotNull ArrayList<T> toArrayList() {
        ArrayList<T> result = new ArrayList<>(Math.max(this.exactIntSize(QueryOption.ONLY_IF_CACHED), 16));
        iterate(result::add);
        return result;
    }
}
