package io.spbx.util.collect.array;

import io.spbx.util.collect.container.IntSize;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

/**
 * A base public version of a class backing {@link Arrays#asList(Object[])}.
 * <p>
 * Provides an {@link BaseArray.BaseBuilder} to support an incremental construction.
 * But after construction, the size of an {@link BaseArray} gets fixed,
 * and the methods {@code add}, {@code remove}, {@code clear} ... always throw.
 * But a mutable {@link Array} allows to {@code set}, {@code replaceAll}, {@code sort}.
 * <p>
 * Under the hood, stores the data in a typed array {@code E[]}, which makes all operations highly efficient, but
 * doesn't allow holding items of different types.
 * <p>
 * Allows null values.
 *
 * @see Array a mutable version
 * @see ImmutableArray an immutable version
 */
public abstract class BaseArray<E> extends AbstractList<E> implements IntSize {
    protected final E[] arr;

    protected BaseArray(@Nullable E @NotNull[] arr) {
        this.arr = arr;
    }

    @Override
    public int size() {
        return arr.length;
    }

    @Override
    public E get(int index) {
        assert index >= 0 && index < arr.length : "Index out of bounds: " + index;
        return arr[index];
    }

    @Override
    public int indexOf(@Nullable Object o) {
        E[] arr = this.arr;
        if (o == null) {
            for (int i = 0; i < arr.length; i++)
                if (arr[i] == null)
                    return i;
        } else {
            for (int i = 0; i < arr.length; i++)
                if (o.equals(arr[i]))
                    return i;
        }
        return -1;
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(arr, Spliterator.ORDERED);
    }

    @Override
    public void forEach(@NotNull Consumer<? super E> action) {
        for (E e : arr) {
            action.accept(e);
        }
    }

    public abstract @NotNull BaseBuilder<E> toBuilder();

    public abstract static class BaseBuilder<E> {
        protected E[] arr;

        protected BaseBuilder(@NotNull E[] arr) {
            this.arr = arr;
        }

        public @NotNull BaseBuilder<E> add(@Nullable E item) {
            grow(1);
            arr[arr.length - 1] = item;
            return this;
        }

        public @NotNull BaseBuilder<E> addAll(@NotNull Collection<? extends E> items) {
            int length = this.arr.length - 1;
            E[] arr = grow(items.size());
            for (E item : items) {
                arr[++length] = item;
            }
            return this;
        }

        @SuppressWarnings("unchecked")
        public @NotNull BaseBuilder<E> addAll(@Nullable E @NotNull ... items) {
            int length = this.arr.length - 1;
            E[] arr = grow(items.length);
            for (E item : items) {
                arr[++length] = item;
            }
            return this;
        }

        private @NotNull E[] grow(int extra) {
            return arr = Arrays.copyOf(arr, arr.length + extra);
        }
    }
}
