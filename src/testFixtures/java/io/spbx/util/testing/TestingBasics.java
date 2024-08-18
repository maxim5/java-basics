package io.spbx.util.testing;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.spbx.util.base.Pair;
import io.spbx.util.collect.BasicMaps;
import io.spbx.util.collect.BasicStreams;
import io.spbx.util.collect.ListBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.TreeSet;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static io.spbx.util.base.EasyCast.castAny;
import static io.spbx.util.collect.BasicMaps.newOrderedMap;
import static io.spbx.util.collect.BasicMaps.orderedMapOf;

public class TestingBasics {
    public static final Integer[] NULL_ARRAY = null;
    public static final List<Integer> NULL_LIST = null;
    public static final Set<Integer> NULL_SET = null;
    public static final Iterable<Integer> NULL_ITERABLE = null;
    public static final Collection<Integer> NULL_COLLECTION = null;
    public static final Map<Integer, Integer> NULL_MAP = null;
    public static final Stream<Integer> NULL_STREAM = null;
    public static final IntStream NULL_INT_STREAM = null;
    public static final LongStream NULL_LONG_STREAM = null;
    public static final Pair<Integer, Integer> NULL_PAIR = null;

    public static @SafeVarargs <T> T[] arrayOf(@Nullable T @NotNull... items) {
        return items;
    }

    public static @SafeVarargs <T> @NotNull List<T> listOf(@Nullable T @NotNull... items) {
        return Arrays.asList(items);
    }

    public static @SafeVarargs <T> @NotNull Set<T> setOf(@Nullable T @NotNull... items) {
        return new LinkedHashSet<>(listOf(items));
    }

    public static @SafeVarargs <T> @NotNull TreeSet<T> sortedSetOf(@NotNull T @NotNull... items) {
        return new TreeSet<>(listOf(items));
    }

    public static @SafeVarargs <T> @NotNull Iterable<T> iterableOf(@Nullable T @NotNull... items) {
        return Stream.of(items)::iterator;
    }

    public static @SafeVarargs <T> @NotNull Iterator<T> iteratorOf(@Nullable T @NotNull... items) {
        return Stream.of(items).iterator();
    }

    public static @SafeVarargs <T> @NotNull Spliterator<T> spliteratorOf(@Nullable T @NotNull... items) {
        return Stream.of(items).spliterator();
    }

    public static @SafeVarargs <T> @NotNull Spliterator<T> spliteratorOfUnknownSize(@Nullable T @NotNull... items) {
        return Spliterators.spliteratorUnknownSize(iteratorOf(items), Spliterator.ORDERED);
    }

    public static @SafeVarargs <T> @NotNull Stream<T> streamOf(@Nullable T @NotNull... items) {
        return Stream.of(items);
    }

    public static <T> @NotNull Stream<T> streamOf(@NotNull Iterable<@Nullable T> items) {
        return BasicStreams.streamOf(items);
    }

    public static @SafeVarargs <T> @NotNull Stream<T> parallelOf(@Nullable T @NotNull... items) {
        return Stream.of(items).parallel();
    }

    public static @NotNull IntStream intStreamOf(int @NotNull... items) {
        return IntStream.of(items);
    }

    public static @NotNull LongStream longStreamOf(long @NotNull... items) {
        return LongStream.of(items);
    }

    public static <E> @NotNull List<E> flatListOf(@Nullable Object @NotNull... items) {
        ListBuilder<E> builder = ListBuilder.builder(2 * items.length);
        for (Object item : items) {
            addRecursive(builder, item);
        }
        return builder.toList();
    }

    private static <T> void addRecursive(@NotNull ListBuilder<T> builder, @Nullable Object item) {
        switch (item) {
            case Iterable<?> iterable -> iterable.forEach(it -> addRecursive(builder, it));
            case Stream<?> stream -> stream.forEach(it -> addRecursive(builder, it));
            case Object[] array -> streamOf(array).forEach(it -> addRecursive(builder, it));
            case null, default -> builder.add(castAny(item));
        }
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> mapOf() {
        return newOrderedMap();
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> mapOf(@Nullable K key, @Nullable V val) {
        return orderedMapOf(key, val);
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> mapOf(@Nullable K key1, @Nullable V val1,
                                                            @Nullable K key2, @Nullable V val2) {
        return orderedMapOf(key1, val1, key2, val2);
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> mapOf(@Nullable K key1, @Nullable V val1,
                                                            @Nullable K key2, @Nullable V val2,
                                                            @Nullable K key3, @Nullable V val3) {
        return orderedMapOf(key1, val1, key2, val2, key3, val3);
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> mapOf(@Nullable K key1, @Nullable V val1,
                                                            @Nullable K key2, @Nullable V val2,
                                                            @Nullable K key3, @Nullable V val3,
                                                            @Nullable K key4, @Nullable V val4) {
        return orderedMapOf(key1, val1, key2, val2, key3, val3, key4, val4);
    }

    public static <K, V> @NotNull LinkedHashMap<K, V> mapOf(@Nullable Object @NotNull... items) {
        return BasicMaps.toMapUncheckedUsing(listOf(items), BasicMaps.MapPutMethod.overwrite());
    }

    public static <K, V> @NotNull Pair<K, V> pairOf(@Nullable K key, @Nullable V val) {
        return Pair.of(key, val);
    }

    public static @SafeVarargs <T> @NotNull T[] appendVarArg(@Nullable T arg, @Nullable T @NotNull... args) {
        T[] result = Arrays.copyOf(args, args.length + 1);
        result[args.length] = arg;
        return result;
    }

    public static @SafeVarargs <T> @NotNull T[] prependVarArg(@Nullable T arg, @Nullable T @NotNull... args) {
        T[] result = Arrays.copyOf(args, args.length + 1);
        result[0] = arg;
        System.arraycopy(args, 0, result, 1, args.length);
        return result;
    }

    public static @NotNull Path toPath(@NotNull String path) {
        return Path.of(path);
    }

    @CanIgnoreReturnValue
    public static boolean waitFor(long millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException ignore) {
            return false;
        }
    }

    public static class ArrayIterator<T> implements Iterator<T> {
        private final T[] array;
        private int cur = 0;

        public @SafeVarargs ArrayIterator(@Nullable T @NotNull... array) {
            this.array = array;
        }

        @Override public boolean hasNext() {
            return cur < array.length;
        }

        @Override public T next() {
            return array[cur++];
        }
    }

    public static class ArrayIterable<T> implements Iterable<T> {
        private final T[] array;

        public @SafeVarargs ArrayIterable(@Nullable T @NotNull... array) {
            this.array = array;
        }

        public static @SafeVarargs <T> @NotNull ArrayIterable<T> of(@Nullable T @NotNull... array) {
            return new ArrayIterable<>(array);
        }

        @Override public @NotNull Iterator<T> iterator() {
            return new ArrayIterator<>(array);
        }
    }

    public static class SimpleBitSet<T extends SimpleBitSet<T>> {
        protected final BitSet bitSet = new BitSet();

        protected @NotNull T setBit(int bitIndex) {
            bitSet.set(bitIndex);
            return castAny(this);
        }

        protected @NotNull T unsetBit(int bitIndex) {
            bitSet.clear(bitIndex);
            return castAny(this);
        }
    }
}
