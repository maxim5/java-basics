package io.spbx.util.collect.iter;

import com.google.common.collect.ImmutableCollection;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.Unchecked;
import io.spbx.util.collect.array.ImmutableArray;
import io.spbx.util.collect.list.ImmutableArrayList;
import io.spbx.util.collect.stream.BasicStreams;
import io.spbx.util.collect.stream.Streamer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.spbx.util.collect.stream.BasicStreams.streamOf;

@Stateless
@Pure
@CheckReturnValue
public class BasicIterables {
    /* Standard `List` factory */

    public static <E> @NotNull ArrayList<E> newMutableList() {
        return new ArrayList<>();
    }

    public static <E> @NotNull ArrayList<E> newMutableList(int size) {
        return new ArrayList<>(size);
    }

    public static <E> @NotNull ArrayList<E> newMutableList(@Nullable Iterable<E> items) {
        return items == null
            ? newMutableList()
            : items instanceof Collection<E> c ? new ArrayList<>(c)
            : Streamer.of(items).toArrayList();
    }

    /* Conversions to standard collections */

    public static <E> @NotNull List<E> asList(@NotNull Iterable<E> items) {
        return items instanceof List<E> list
            ? list
            : items instanceof Collection<E> collection
            ? new ArrayList<>(collection)
            : StreamSupport.stream(items.spliterator(), false).toList();
    }

    public static <E> @NotNull ArrayList<E> asArrayList(@NotNull Iterable<E> items) {
        return items instanceof ArrayList<E> list
            ? list
            : items instanceof Collection<E> collection
            ? new ArrayList<>(collection)
            : Streamer.of(items).toArrayList();
    }

    public static <E> @NotNull Set<E> asSet(@NotNull Iterable<E> items) {
        return items instanceof Set<E> set
            ? set
            : Streamer.of(items).toSet();
    }

    public static <E> @NotNull HashSet<E> asHashSet(@NotNull Iterable<E> items) {
        return items instanceof HashSet<E> set
            ? set
            : Streamer.of(items).toHashSet();
    }

    public static <E> @NotNull LinkedHashSet<E> asLinkedHashSet(@NotNull Iterable<E> items) {
        return items instanceof LinkedHashSet<E> set
            ? set
            : Streamer.of(items).toLinkedHashSet();
    }

    public static <E> @NotNull Collection<E> asCollection(@NotNull Iterable<E> items) {
        return items instanceof Collection<E> collection ? collection : Streamer.of(items).toArrayList();
    }

    public static <E> @NotNull List<E> forEachToList(@NotNull Consumer<Consumer<E>> forEachFunc) {
        List<E> list = newMutableList();
        forEachFunc.accept(list::add);
        return list;
    }

    /* Iterations */

    public static <U, V> void forEachZipped(@Nullable U @NotNull [] left, @Nullable V @NotNull [] right,
                                            @NotNull BiConsumer<? super U, ? super V> action) {
        assert left.length == right.length : "Array length mismatch: %d vs %d".formatted(left.length, right.length);
        for (int i = 0, len = left.length; i < len; i++) {
            action.accept(left[i], right[i]);
        }
    }

    public static <U, V> void forEachZipped(@NotNull Iterable<U> left, @NotNull Iterable<V> right,
                                            @NotNull BiConsumer<? super U, ? super V> action) {
        Iterator<U> iterLeft = left.iterator();
        Iterator<V> iterRight = right.iterator();
        while (iterLeft.hasNext() && iterRight.hasNext()) {
            action.accept(iterLeft.next(), iterRight.next());
        }
        assert !iterLeft.hasNext() : "First iterable has more elements to iterate: %s vs %s".formatted(left, right);
        assert !iterRight.hasNext() : "Second iterable has more elements to iterate: %s vs %s".formatted(left, right);
    }

    /* List immutability */

    // Helpful for asserting immutability to justify @Immutable annotation
    public static boolean isImmutable(@NotNull Collection<?> collection) {
        return isGuavaImmutable(collection) ||
               isJavaUtilUnmodifiableOrImmutable(collection) ||
               isBasicsImmutable(collection);
    }

    // Helpful for asserting immutability to justify @Immutable annotation
    public static boolean isGuavaImmutable(@NotNull Collection<?> collection) {
        return collection instanceof ImmutableCollection<?>;
    }

    public static boolean isBasicsImmutable(@NotNull Collection<?> collection) {
        return collection instanceof ImmutableArray<?> || collection instanceof ImmutableArrayList<?>;
    }

    public static boolean isJavaUtilUnmodifiableOrImmutable(@NotNull Collection<?> collection) {
        return isJavaUtilUnmodifiableOrImmutable(collection.getClass());
    }

    private static boolean isJavaUtilUnmodifiableOrImmutable(@NotNull Class<?> cls) {
        return JAVA_UNMODIFIABLE_COLLECTION.isAssignableFrom(cls) ||
               JAVA_IMMUTABLE_COLLECTION.isAssignableFrom(cls) ||
               JAVA_COLLECTIONS_CLASSES.contains(cls);
    }

    private static final Class<?> JAVA_IMMUTABLE_COLLECTION = getClass("java.util.ImmutableCollections$AbstractImmutableCollection");
    private static final Class<?> JAVA_UNMODIFIABLE_COLLECTION = getClass("java.util.Collections$UnmodifiableCollection");
    private static final Set<Class<?>> JAVA_COLLECTIONS_CLASSES = Set.of(
        getClass("java.util.Collections$SingletonList"),
        getClass("java.util.Collections$SingletonSet"),
        getClass("java.util.Collections$EmptyList"),
        getClass("java.util.Collections$EmptySet"),
        getClass("java.util.Collections$UnmodifiableNavigableSet$EmptyNavigableSet")
    );

    private static @NotNull Class<?> getClass(@NotNull String className) {
        return Unchecked.Suppliers.runRethrow(() -> Class.forName(className));
    }

    /* Size estimates */

    public static <E> int sizeOf(@Nullable Collection<E> items) {
        return items == null ? 0 : items.size();
    }

    public static <E> int sizeOf(@Nullable Iterable<E> items, int def) {
        return items == null ? 0 : items instanceof Collection<E> c ? c.size() : def;
    }

    public static <E> long exactSizeOf(@Nullable Iterable<E> items) {
        int size = sizeOf(items, -1);
        return size >= 0 ? size : items == null ? 0 : countSize(items.iterator());
    }

    public static <E> long countSize(@NotNull Iterator<E> iterator) {
        long count = 0L;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }

    public static <E> boolean isEmpty(@Nullable Collection<E> items) {
        return items == null || items.isEmpty();
    }

    public static <E> boolean isEmpty(@Nullable Iterable<E> items) {
        return items instanceof Collection<E> c ? isEmpty(c) : items == null || !items.iterator().hasNext();
    }

    public static <E> long estimateSize(@Nullable Iterable<E> items, int def) {
        return items == null ? 0 : items instanceof Collection<?> c ? c.size() : estimateSize(items.spliterator(), def);
    }

    public static <E> long estimateSize(@NotNull Spliterator<E> spliterator, int def) {
        long estimatedSize = spliterator.estimateSize();
        return spliterator.hasCharacteristics(Spliterator.SIZED) || estimatedSize < Long.MAX_VALUE ? estimatedSize : def;
    }

    public static <E> int estimateSizeInt(@Nullable Iterable<E> items, int def) {
        return (int) estimateSize(items, def);
    }

    /* Null handling */

    public static <E> @NotNull Iterable<E> emptyIfNull(@Nullable Iterable<E> items) {
        return items != null ? items : List.of();
    }

    public static <E> @NotNull Collection<E> emptyIfNull(@Nullable Collection<E> items) {
        return items != null ? items : List.of();
    }

    public static <E> @NotNull Set<E> emptyIfNull(@Nullable Set<E> items) {
        return items != null ? items : Set.of();
    }

    public static <E> @NotNull List<E> emptyIfNull(@Nullable List<E> items) {
        return items != null ? items : List.of();
    }

    public static <E> @Nullable Iterable<E> nullIfEmpty(@Nullable Iterable<E> items) {
        return isEmpty(items) ? null : items;
    }

    public static <E, C extends Collection<E>> @Nullable C nullIfEmpty(@Nullable C items) {
        return isEmpty(items) ? null : items;
    }

    /* Manipulations */

    public static <E, C extends Collection<E>> @NotNull C replaceContent(@NotNull C mutable, @NotNull Collection<E> contents) {
        mutable.clear();
        mutable.addAll(contents);
        return mutable;
    }

    /* Distinct and duplicates */

    public static <E, C extends Collection<E>> @NotNull C distinctInPlace(@NotNull C mutable) {
        LinkedHashSet<E> distinct = new LinkedHashSet<>(mutable);
        if (distinct.size() < mutable.size()) {
            replaceContent(mutable, distinct);
        }
        return mutable;
    }

    public static <E> boolean isAllDistinct(@NotNull Iterable<E> items) {
        return streamOf(items).distinct().count() == exactSizeOf(items);
    }

    public static <E> @NotNull Set<E> duplicatesOf(@NotNull Iterable<E> items) {
        LinkedHashSet<E> distinct = new LinkedHashSet<>(sizeOf(items, 16));
        return Streamer.of(items).skipIf(distinct::add).toLinkedHashSet();
    }

    /* `List` concatenation */

    public static <E> @NotNull List<E> concatToList(@Nullable Stream<? extends E> first,
                                                    @Nullable Stream<? extends E> second) {
        return BasicStreams.concat(first, second).toList();
    }

    public static <E> @NotNull List<E> concatToList(@Nullable Stream<? extends E> first,
                                                    @Nullable Stream<? extends E> second,
                                                    @Nullable Stream<? extends E> third) {
        return BasicStreams.concat(first, second, third).toList();
    }

    public static <E> @NotNull List<E> concatToList(@Nullable Iterable<? extends E> first,
                                                    @Nullable Iterable<? extends E> second) {
        return concatToList(streamOf(first), streamOf(second));
    }

    public static <E> @NotNull List<E> concatToList(@Nullable Iterable<? extends E> first,
                                                    @Nullable Iterable<? extends E> second,
                                                    @Nullable Iterable<? extends E> third) {
        return concatToList(streamOf(first), streamOf(second), streamOf(third));
    }

    public static <E> @NotNull List<E> concatToList(@Nullable List<? extends E> first,
                                                    @Nullable List<? extends E> second) {
        ArrayList<E> result = new ArrayList<>(sizeOf(first) + sizeOf(second));
        result.addAll(emptyIfNull(first));
        result.addAll(emptyIfNull(second));
        return result;
    }

    public static <E> @NotNull List<E> concatToList(@Nullable List<? extends E> first,
                                                    @Nullable List<? extends E> second,
                                                    @Nullable List<? extends E> third) {
        ArrayList<E> result = new ArrayList<>(sizeOf(first) + sizeOf(second) + sizeOf(third));
        result.addAll(emptyIfNull(first));
        result.addAll(emptyIfNull(second));
        result.addAll(emptyIfNull(third));
        return result;
    }

    public static @SafeVarargs <E> @NotNull List<E> concatAllToList(@Nullable List<? extends E> @NotNull ... lists) {
        int capacity = streamOf(lists).mapToInt(BasicIterables::sizeOf).sum();
        ArrayList<E> result = new ArrayList<>(capacity);
        for (List<? extends E> list : lists) {
            result.addAll(emptyIfNull(list));
        }
        return result;
    }

    public static <E> @NotNull List<E> prependToList(@Nullable E first, @NotNull Iterable<? extends E> second) {
        return BasicStreams.prependToStream(first, second).toList();
    }

    public static <E> @NotNull List<E> appendToList(@NotNull Iterable<? extends E> first, @Nullable E second) {
        return BasicStreams.appendToStream(first, second).toList();
    }
}
