package io.spbx.util.collect.iter;

import io.spbx.util.base.tuple.IntObjPair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Inspired by <a href="https://github.com/fracpete/java-utils">Enumerate</a> util.
 */
@NotThreadSafe
public class Enumerator<T> implements Iterator<T> {
    private final Iterator<T> iterator;
    private int index = -1;

    Enumerator(@NotNull Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public static <T> @NotNull Enumerator<T> wrap(@NotNull Iterator<T> iterator) {
        return iterator instanceof Enumerator<T> enumerator ? enumerator : new Enumerator<>(iterator);
    }

    public static <T> @NotNull Enumerator<T> of(@NotNull Iterable<T> iterable) {
        return Enumerator.wrap(iterable.iterator());
    }

    public static <T> @NotNull Enumerator<T> of(@Nullable T @NotNull[] array) {
        return Enumerator.wrap(Stream.of(array).iterator());
    }

    public static <T> @NotNull Iterator<IntObjPair<T>> enumerate(@NotNull Iterator<T> iterator) {
        return new Iterator<>() {
            private int index = -1;
            @Override public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override public IntObjPair<T> next() {
                return IntObjPair.of(++index, iterator.next());
            }
            @Override public void remove() {
                iterator.remove();
            }
        };
    }

    public static <T> @NotNull Iterable<IntObjPair<T>> enumerate(@NotNull Iterable<T> iterator) {
        return () -> Enumerator.enumerate(iterator.iterator());
    }

    public static <T> @NotNull Iterable<IntObjPair<T>> enumerate(@Nullable T @NotNull[] array) {
        return () -> Enumerator.enumerate(Stream.of(array).iterator());
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        ++index;
        return iterator.next();
    }

    public int currentIndex() {
        return index;
    }

    public int nextIndex() {
        return index + 1;
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}
