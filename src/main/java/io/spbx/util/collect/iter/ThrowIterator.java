package io.spbx.util.collect.iter;

import io.spbx.util.base.error.Unchecked;
import io.spbx.util.func.ThrowConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static io.spbx.util.base.error.BasicExceptions.newUnsupportedOperationException;
import static io.spbx.util.base.error.Unchecked.Runnables;
import static io.spbx.util.base.error.Unchecked.Suppliers;

/**
 * Same as {@link Iterator} but allows to throw check exceptions during iteration.
 * Also adapts to {@link Iterator} API but converting checked exceptions into unchecked.
 *
 * @param <T> the type of elements returned by the iterator
 * @see Iterator
 * @see ThrowIterable
 */
public interface ThrowIterator<T, E extends Throwable> extends Iterator<T> {
    boolean hasNextThrow() throws E;

    T nextThrow() throws E;

    default void removeThrow() throws E {
        throw newUnsupportedOperationException("removeThrow() not supported for", this);
    }

    default boolean hasNext() {
        try {
            return hasNextThrow();
        } catch (Throwable e) {
            return Unchecked.rethrow(e);
        }
    }

    default T next() {
        return Suppliers.runRethrow(this::nextThrow);
    }

    default void remove() {
        Runnables.runRethrow(this::removeThrow);
    }

    default void forEachRemaining(@NotNull ThrowConsumer<? super T, E> action) throws E {
        while (hasNextThrow()) {
            action.accept(nextThrow());
        }
    }
}
