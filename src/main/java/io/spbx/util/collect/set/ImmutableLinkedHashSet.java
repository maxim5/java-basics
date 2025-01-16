package io.spbx.util.collect.set;

import io.spbx.util.collect.container.IntSize;
import io.spbx.util.collect.iter.ImmutableIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static io.spbx.util.base.lang.EasyCast.castAny;
import static io.spbx.util.collect.stream.BasicStreams.streamOf;

/**
 * An immutable version of an {@code LinkedHashSet}.
 * <p>
 * Differences from other {@link java.util.Set} collections:
 * <ul>
 *     <li>Unlike a standard {@link LinkedHashSet}, does not allow modifications after construction.</li>
 *     <li>Unlike Guava's {@link com.google.common.collect.ImmutableSet}, allows null values.</li>
 * </ul>
 */
// FIX[minor]: optimize ImmutableLinkedHashSet1 (a single entry set)
@Immutable
public class ImmutableLinkedHashSet<E> extends LinkedHashSet<E> implements IntSize {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final ImmutableLinkedHashSet<?> EMPTY = new ImmutableLinkedHashSet<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final ImmutableLinkedHashSet<?> SINGLE_NULL = new ImmutableLinkedHashSet<>(1, add -> add.accept(null));

    private ImmutableLinkedHashSet() {
    }

    private ImmutableLinkedHashSet(int numElements) {
        super(calculateHashMapCapacity(numElements));
    }

    private ImmutableLinkedHashSet(int elementsNum, @NotNull Consumer<Consumer<E>> callback) {
        this(elementsNum);
        callback.accept(this::addInternal);
    }

    public static <E> @NotNull ImmutableLinkedHashSet<E> of() {
        return castAny(EMPTY);
    }

    public static <E> @NotNull ImmutableLinkedHashSet<E> of(@Nullable E item) {
        return item != null ? new ImmutableLinkedHashSet<>(1, add -> add.accept(item)) : castAny(SINGLE_NULL);
    }

    public static <E> @NotNull ImmutableLinkedHashSet<E> of(@Nullable E item1, @Nullable E item2) {
        return new ImmutableLinkedHashSet<>(2, add -> {
            add.accept(item1);
            add.accept(item2);
        });
    }

    public static <E> @NotNull ImmutableLinkedHashSet<E> of(@Nullable E item1, @Nullable E item2, @Nullable E item3) {
        return new ImmutableLinkedHashSet<>(3, add -> {
            add.accept(item1);
            add.accept(item2);
            add.accept(item3);
        });
    }

    @SafeVarargs
    public static <E> @NotNull ImmutableLinkedHashSet<E> of(@Nullable E item1, @Nullable E item2, @Nullable E item3,
                                                            @Nullable E @NotNull... rest) {
        return new ImmutableLinkedHashSet<>(rest.length + 3, adder -> {
            adder.accept(item1);
            adder.accept(item2);
            adder.accept(item3);
            for (E item : rest) {
                adder.accept(item);
            }
        });
    }

    public static <E> @NotNull ImmutableLinkedHashSet<E> copyOf(@Nullable E @NotNull[] items) {
        return new ImmutableLinkedHashSet<>(items.length, add -> {
            for (E item : items) {
                add.accept(item);
            }
        });
    }

    public static <E> @NotNull ImmutableLinkedHashSet<E> copyOf(@NotNull Collection<? extends E> items) {
        return items instanceof ImmutableLinkedHashSet<?> arrayList ?
            castAny(arrayList) :
            new ImmutableLinkedHashSet<>(items.size(), add -> {
                for (E item : items) {
                    add.accept(item);
                }
            });
    }

    public static <E> @NotNull ImmutableLinkedHashSet<E> copyOf(@NotNull Iterable<? extends E> items) {
        return items instanceof Collection<?> collection ?
            castAny(copyOf(collection)) :
            new ImmutableLinkedHashSet<>(8, add -> {
                for (E item : items) {
                    add.accept(item);
                }
            });
    }

    public static <E> @NotNull ImmutableLinkedHashSet<E> copyOf(@NotNull Iterator<? extends E> items) {
        return streamOf(items).collect(toImmutableLinkedHashSet());
    }

    public static <E> @NotNull Collector<E, ?, ImmutableLinkedHashSet<E>> toImmutableLinkedHashSet() {
        return castAny(COLLECTOR);
    }

    private static final Collector<Object, ?, ImmutableLinkedHashSet<Object>> COLLECTOR = Collector.of(
        ImmutableLinkedHashSet::new,
        ImmutableLinkedHashSet::addInternal,
        ImmutableLinkedHashSet::addAllInternal,
        set -> set
    );

    private void addInternal(E e) {
        super.add(e);
    }

    private @NotNull ImmutableLinkedHashSet<E> addAllInternal(@NotNull Collection<? extends E> c) {
        super.addAll(c);
        return this;
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return ImmutableIterator.wrap(super.iterator());
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.DISTINCT | Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("ImmutableLinkedHashSet is immutable");
    }

    @Override
    public void addFirst(E e) {
        throw new UnsupportedOperationException("ImmutableLinkedHashSet is immutable");
    }

    @Override
    public void addLast(E e) {
        throw new UnsupportedOperationException("ImmutableLinkedHashSet is immutable");
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        throw new UnsupportedOperationException("ImmutableLinkedHashSet is immutable");
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("ImmutableLinkedHashSet is immutable");
    }

    @Override
    public boolean removeIf(@NotNull Predicate<? super E> filter) {
        throw new UnsupportedOperationException("ImmutableLinkedHashSet is immutable");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("ImmutableLinkedHashSet is immutable");
    }

    @Override
    public E removeFirst() {
        throw new UnsupportedOperationException("ImmutableLinkedHashSet is immutable");
    }

    @Override
    public E removeLast() {
        throw new UnsupportedOperationException("ImmutableLinkedHashSet is immutable");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("ImmutableLinkedHashSet is immutable");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("ImmutableLinkedHashSet is immutable");
    }

    private static int calculateHashMapCapacity(int size) {
        return (int) Math.ceil(size / 0.75);
    }
}
