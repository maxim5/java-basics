package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntContainer;
import com.carrotsearch.hppc.IntLookupContainer;
import com.carrotsearch.hppc.IntHashSet;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.IntIntHashMap;
import com.carrotsearch.hppc.IntIntMap;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.carrotsearch.hppc.predicates.IntPredicate;
import com.carrotsearch.hppc.procedures.IntIntProcedure;
import com.carrotsearch.hppc.procedures.IntObjectProcedure;

import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.classpath.RuntimeRequirement;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Helper utils for Hppc int classes:
 * {@link IntContainer}, {@link IntArrayList}, {@link IntHashSet}, etc.
 *
 * @link <a href='https://github.com/carrotsearch/hppc'>Hppc GitHub</a>
 */
@Stateless
@Generated(value = "Hppc$Type$.java", date = "2024-12-02T15:53:08.714570400Z")
public class HppcInt {
    /*
     * In case Hppc library is not in a classpath, try to fail early and produce a friendly error message.
     */
    static {
        RuntimeRequirement.verify("com.carrotsearch.hppc.IntArrayList");
    }

    /* `IntList` manipulations */

    public static @NotNull IntArrayList slice(@NotNull IntArrayList list, int fromIndex, int toIndex) {
        assert fromIndex >= 0 && toIndex >= 0 && fromIndex <= toIndex :
            "Invalid range: from=%d to=%d".formatted(fromIndex, toIndex);
        fromIndex = Math.min(fromIndex, list.elementsCount);
        toIndex = Math.min(toIndex, list.elementsCount);
        IntArrayList slice = new IntArrayList();
        slice.buffer = Arrays.copyOfRange(list.buffer, fromIndex, toIndex);
        slice.elementsCount = toIndex - fromIndex;
        return slice;
    }

    public static void iterateChunks(@NotNull IntContainer container,
                                     int chunkSize,
                                     @NotNull Consumer<IntArrayList> consumer) {
        assert chunkSize > 0 : "Invalid chunk size: " + chunkSize;
        IntArrayList arrayList = toIntArrayList(container);
        for (int i = 0; i < arrayList.size(); i += chunkSize) {
            IntArrayList chunk = HppcInt.slice(arrayList, i, i + chunkSize);
            consumer.accept(chunk);
        }
    }

    public static @NotNull IntArrayList toIntArrayList(@NotNull IntContainer container) {
        return container instanceof IntArrayList arrayList ? arrayList : new IntArrayList(container);
    }

    /* Java `List` <-> `IntList` conversions */

    public static @NotNull IntArrayList toIntList(@NotNull Iterable<Integer> list) {
        IntArrayList arrayList = new IntArrayList();
        list.forEach(arrayList::add);
        return arrayList;
    }

    public static @NotNull Stream<IntCursor> toJavaStream(@NotNull IntContainer container) {
        return StreamSupport.stream(container.spliterator(), false);
    }

    public static @NotNull ArrayList<Integer> toJavaList(@NotNull IntContainer container) {
        ArrayList<Integer> list = new ArrayList<>(container.size());
        for (IntCursor cursor : container) {
            list.add(cursor.value);
        }
        return list;
    }

    public static @NotNull IntArrayList collectToIntList(@NotNull java.util.stream.IntStream stream) {
        return stream.collect(IntArrayList::new, IntArrayList::add, IntArrayList::addAll);
    }

    /* `IntMap` manipulations */

    public static @NotNull IntIntMap slice(@NotNull IntIntMap map, int @NotNull[] keys) {
        IntIntHashMap result = new IntIntHashMap(keys.length);
        for (int key : keys) {
            result.put(key, map.get(key));
        }
        return result;
    }

    public static @NotNull IntIntMap slice(@NotNull IntIntMap map, @NotNull IntContainer keys) {
        IntIntHashMap result = new IntIntHashMap(keys.size());
        for (IntCursor cursor : keys) {
            result.put(cursor.value, map.get(cursor.value));
        }
        return result;
    }

    public static @NotNull Map<Integer, Integer> toJavaMap(@NotNull IntIntMap map) {
        HashMap<Integer, Integer> result = new HashMap<>(map.size());
        map.forEach((IntIntProcedure) result::put);
        return result;
    }

    public static <T> @NotNull Map<Integer, T> toJavaMap(@NotNull IntObjectMap<T> map) {
        HashMap<Integer, T> result = new HashMap<>(map.size());
        map.forEach((IntObjectProcedure<T>) result::put);
        return result;
    }

    // Note: not pure
    public static int computeIfAbsent(@NotNull IntIntMap map, int key, @NotNull java.util.function.IntSupplier def) {
        int result = map.get(key);
        if (result == 0 && !map.containsKey(key)) {
            result = def.getAsInt();
            map.put(key, result);
        }
        return result;
    }

    // Note: not pure
    public static <T> T computeIfAbsent(@NotNull IntObjectMap<T> map, int key, @NotNull Supplier<T> def) {
        T result = map.get(key);
        if (result == null && !map.containsKey(key)) {
            result = def.get();
            map.put(key, result);
        }
        return result;
    }

    /* `IntSet` manipulations */

    public static @NotNull IntHashSet union(@NotNull IntContainer lhs, @NotNull IntContainer rhs) {
        IntHashSet copy = new IntHashSet(lhs);
        copy.addAll(rhs);
        return copy;
    }

    public static @NotNull IntHashSet intersect(@NotNull IntContainer lhs, @NotNull IntLookupContainer rhs) {
        IntHashSet copy = new IntHashSet(lhs);
        copy.retainAll(rhs);
        return copy;
    }

    public static @NotNull IntHashSet subtract(@NotNull IntContainer lhs, @NotNull IntContainer rhs) {
        IntHashSet copy = new IntHashSet(lhs);
        copy.removeAll(rhs);
        return copy;
    }

    public static @NotNull IntHashSet retainAllCopy(@NotNull IntContainer container, @NotNull IntPredicate predicate) {
        IntHashSet copy = new IntHashSet(container);
        copy.retainAll(predicate);
        return copy;
    }

    public static @NotNull IntHashSet removeAllCopy(@NotNull IntContainer container, @NotNull IntPredicate predicate) {
        IntHashSet copy = new IntHashSet(container);
        copy.removeAll(predicate);
        return copy;
    }
}
