package io.spbx.util.collect.iter;

import io.spbx.util.func.ThrowConsumer;
import org.jetbrains.annotations.NotNull;

/**
 * Implementing this interface allows an object to be the target of the enhanced
 * {@code for} statement (sometimes called the "for-each loop" statement).
 *
 * @param <T> the type of elements returned by the iterator
 * @see Iterable
 * @see ThrowIterator
 */
public interface ThrowIterable<T, E extends Throwable> extends Iterable<T> {
    /**
     * Returns a {@link ThrowIterator} over elements of type {@code T}.
     */
    @Override
    @NotNull ThrowIterator<T, E> iterator();

    /**
     * Performs the given action for each element of the {@code ThrowIterable}
     * until all elements have been processed or the action throws an
     * exception. Actions are performed in the order of iteration, if that
     * order is specified.
     * <p>
     * The behavior of this method is unspecified if the action performs
     * side-effects that modify the underlying source of elements, unless an
     * overriding class has specified a concurrent modification policy.
     */
    default void forEach(@NotNull ThrowConsumer<? super T, E> action) throws E {
        this.iterator().forEachRemaining(action);
    }
}
