package io.spbx.util.collect.list;

import io.spbx.util.collect.array.Array;
import io.spbx.util.collect.array.ImmutableArray;
import io.spbx.util.collect.container.IntSize;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;

import static io.spbx.util.base.lang.EasyCast.castAny;
import static io.spbx.util.collect.stream.BasicStreams.streamOf;

/**
 * An immutable version of an {@code ArrayList}.
 * <p>
 * Differences from other collections backed by an array:
 * <ul>
 *     <li>Unlike a standard {@link ArrayList}, does not allow modifications after construction.</li>
 *     <li>Unlike Guava's {@link com.google.common.collect.ImmutableList}, allows null values.</li>
 *     <li>Unlike an {@link Array} and {@link ImmutableArray},
 *     under the hood, stores the data in an Object array type ({@code Object[]}),
 *     hence supports holding items of different types.</li>
 * </ul>
 *
 * @see ArrayList
 * @see ImmutableArray
 * @see com.google.common.collect.ImmutableList
 */
// FIX[minor]: optimize ImmutableArrayList1 (a single entry list)
@Immutable
public class ImmutableArrayList<E> extends ArrayList<E> implements IntSize {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final ImmutableArrayList<?> EMPTY = new ImmutableArrayList<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final ImmutableArrayList<?> SINGLE_NULL = new ImmutableArrayList<>(1, adder -> adder.accept(null));

    private ImmutableArrayList() {
        super(0);
    }

    private ImmutableArrayList(int capacity, @NotNull Consumer<Consumer<E>> callback) {
        super(capacity);
        callback.accept(this::addInternal);
    }

    /*package*/ ImmutableArrayList(@NotNull Collection<? extends E> c) {
        super(c.size());
        addAllInternal(c);
    }

    public static <E> @NotNull ImmutableArrayList<E> of() {
        return castAny(EMPTY);
    }

    public static <E> @NotNull ImmutableArrayList<E> of(@Nullable E item) {
        return item != null ? new ImmutableArrayList<>(1, adder -> adder.accept(item)) : castAny(SINGLE_NULL);
    }

    public static <E> @NotNull ImmutableArrayList<E> of(@Nullable E item1, @Nullable E item2) {
        return new ImmutableArrayList<>(2, adder -> {
            adder.accept(item1);
            adder.accept(item2);
        });
    }

    public static <E> @NotNull ImmutableArrayList<E> of(@Nullable E item1, @Nullable E item2, @Nullable E item3) {
        return new ImmutableArrayList<>(3, adder -> {
            adder.accept(item1);
            adder.accept(item2);
            adder.accept(item3);
        });
    }

    @SafeVarargs
    public static <E> @NotNull ImmutableArrayList<E> of(@Nullable E item1, @Nullable E item2, @Nullable E item3,
                                                        @Nullable E @NotNull... rest) {
        return new ImmutableArrayList<>(rest.length + 3, adder -> {
            adder.accept(item1);
            adder.accept(item2);
            adder.accept(item3);
            for (E item : rest) {
                adder.accept(item);
            }
        });
    }

    public static <E> @NotNull ImmutableArrayList<E> copyOf(@Nullable E @NotNull[] items) {
        return new ImmutableArrayList<>(items.length, adder -> {
            for (E item : items) {
                adder.accept(item);
            }
        });
    }

    public static <E> @NotNull ImmutableArrayList<E> copyOf(@NotNull Collection<? extends E> items) {
        return items instanceof ImmutableArrayList<?> arrayList ?
            castAny(arrayList) :
            new ImmutableArrayList<>(items);
    }

    public static <E> @NotNull ImmutableArrayList<E> copyOf(@NotNull Iterable<? extends E> items) {
        return items instanceof Collection<?> collection ?
            castAny(copyOf(collection)) :
            new ImmutableArrayList<>(8, adder -> {
                for (E item : items) {
                    adder.accept(item);
                }
            });
    }

    public static <E> @NotNull ImmutableArrayList<E> copyOf(@NotNull Iterator<? extends E> items) {
        return streamOf(items).collect(toImmutableArrayList());
    }

    public static <E> @NotNull Collector<E, ?, ImmutableArrayList<E>> toImmutableArrayList() {
        return castAny(COLLECTOR);
    }

    private static final Collector<Object, ?, ImmutableArrayList<Object>> COLLECTOR = Collector.of(
        ImmutableArrayList::new,
        ImmutableArrayList::addInternal,
        ImmutableArrayList::addAllInternal,
        list -> list
    );

    private void addInternal(E e) {
        super.add(e);
    }

    private @NotNull ImmutableArrayList<E> addAllInternal(@NotNull Collection<? extends E> c) {
        super.addAll(c);
        return this;
    }

    // FIX[minor]: override spliterator() to make it IMMUTABLE

    @Override
    public @NotNull Iterator<E> iterator() {
        return new ImmutableListIterator(size());
    }

    @Override
    public @NotNull ListIterator<E> listIterator() {
        return new ImmutableListIterator(size());
    }

    @Override
    public @NotNull ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds, list size: " + size());
        }
        return new ImmutableListIterator(size(), index);
    }

    @Override
    public @NotNull ImmutableArrayList<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("Index " + fromIndex + " is out of bounds, list size: " + size());
        }
        if (toIndex > size()) {
            throw new IndexOutOfBoundsException("Index " + toIndex + " is out of bounds, list size: " + size());
        }
        if (fromIndex == toIndex) {
            return castAny(EMPTY);
        }
        /* Arrays.copyOfRange(this.elementData, fromIndex, toIndex) */
        return new ImmutableArrayList<>(super.subList(fromIndex, toIndex));
    }

    @Override
    public E set(int index, @Nullable E element) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public void replaceAll(@NotNull UnaryOperator<E> operator) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public E removeFirst() {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public E removeLast() {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public void trimToSize() {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public void ensureCapacity(int minCapacity) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public boolean removeIf(@NotNull Predicate<? super E> filter) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    @Override
    public void sort(@NotNull Comparator<? super E> c) {
        throw new UnsupportedOperationException("ArrayList is immutable");
    }

    private class ImmutableListIterator implements ListIterator<E> {
        private int cursor;
        private final int size;

        ImmutableListIterator(int size, int position) {
            this.size = size;
            this.cursor = position;
        }

        ImmutableListIterator(int size) {
            this(size, 0);
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return get(cursor++);
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            return get(--cursor);
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("ArrayList is immutable");
        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException("ArrayList is immutable");
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException("ArrayList is immutable");
        }
    }
}
