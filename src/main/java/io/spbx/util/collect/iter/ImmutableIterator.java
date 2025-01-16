package io.spbx.util.collect.iter;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.util.Iterator;

@Immutable
public class ImmutableIterator<T> implements Iterator<T> {
    private final Iterator<T> iterator;

    ImmutableIterator(@NotNull Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public static <T> @NotNull ImmutableIterator<T> wrap(@NotNull Iterator<T> iterator) {
        return iterator instanceof ImmutableIterator<T> immutable ? immutable : new ImmutableIterator<>(iterator);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("ImmutableIterator is immutable");
    }
}
