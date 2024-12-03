package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.LongArrayList;
import com.carrotsearch.hppc.LongContainer;
import com.carrotsearch.hppc.LongLookupContainer;
import com.carrotsearch.hppc.LongHashSet;
import com.carrotsearch.hppc.LongObjectMap;
import com.carrotsearch.hppc.LongLongHashMap;
import com.carrotsearch.hppc.LongLongMap;
import com.carrotsearch.hppc.cursors.LongCursor;
import com.carrotsearch.hppc.predicates.LongPredicate;
import com.carrotsearch.hppc.procedures.LongLongProcedure;
import com.carrotsearch.hppc.procedures.LongObjectProcedure;

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
 * Helper utils for Hppc long classes:
 * {@link LongContainer}, {@link LongArrayList}, {@link LongHashSet}, etc.
 *
 * @link <a href='https://github.com/carrotsearch/hppc'>Hppc GitHub</a>
 */
@Stateless
@Generated(value = "Hppc$Type$.java", date = "2024-12-02T15:53:08.714570400Z")
public class HppcLong {
    /*
     * In case Hppc library is not in a classpath, try to fail early and produce a friendly error message.
     */
    static {
        RuntimeRequirement.verify("com.carrotsearch.hppc.LongArrayList");
    }

    /* `LongList` manipulations */

    public static @NotNull LongArrayList slice(@NotNull LongArrayList list, int fromIndex, int toIndex) {
        assert fromIndex >= 0 && toIndex >= 0 && fromIndex <= toIndex :
            "Invalid range: from=%d to=%d".formatted(fromIndex, toIndex);
        fromIndex = Math.min(fromIndex, list.elementsCount);
        toIndex = Math.min(toIndex, list.elementsCount);
        LongArrayList slice = new LongArrayList();
        slice.buffer = Arrays.copyOfRange(list.buffer, fromIndex, toIndex);
        slice.elementsCount = toIndex - fromIndex;
        return slice;
    }

    public static void iterateChunks(@NotNull LongContainer container,
                                     int chunkSize,
                                     @NotNull Consumer<LongArrayList> consumer) {
        assert chunkSize > 0 : "Invalid chunk size: " + chunkSize;
        LongArrayList arrayList = toLongArrayList(container);
        for (int i = 0; i < arrayList.size(); i += chunkSize) {
            LongArrayList chunk = HppcLong.slice(arrayList, i, i + chunkSize);
            consumer.accept(chunk);
        }
    }

    public static @NotNull LongArrayList toLongArrayList(@NotNull LongContainer container) {
        return container instanceof LongArrayList arrayList ? arrayList : new LongArrayList(container);
    }

    /* Java `List` <-> `LongList` conversions */

    public static @NotNull LongArrayList toLongList(@NotNull Iterable<Long> list) {
        LongArrayList arrayList = new LongArrayList();
        list.forEach(arrayList::add);
        return arrayList;
    }

    public static @NotNull Stream<LongCursor> toJavaStream(@NotNull LongContainer container) {
        return StreamSupport.stream(container.spliterator(), false);
    }

    public static @NotNull ArrayList<Long> toJavaList(@NotNull LongContainer container) {
        ArrayList<Long> list = new ArrayList<>(container.size());
        for (LongCursor cursor : container) {
            list.add(cursor.value);
        }
        return list;
    }

    public static @NotNull LongArrayList collectToLongList(@NotNull java.util.stream.LongStream stream) {
        return stream.collect(LongArrayList::new, LongArrayList::add, LongArrayList::addAll);
    }

    /* `LongMap` manipulations */

    public static @NotNull LongLongMap slice(@NotNull LongLongMap map, long @NotNull[] keys) {
        LongLongHashMap result = new LongLongHashMap(keys.length);
        for (long key : keys) {
            result.put(key, map.get(key));
        }
        return result;
    }

    public static @NotNull LongLongMap slice(@NotNull LongLongMap map, @NotNull LongContainer keys) {
        LongLongHashMap result = new LongLongHashMap(keys.size());
        for (LongCursor cursor : keys) {
            result.put(cursor.value, map.get(cursor.value));
        }
        return result;
    }

    public static @NotNull Map<Long, Long> toJavaMap(@NotNull LongLongMap map) {
        HashMap<Long, Long> result = new HashMap<>(map.size());
        map.forEach((LongLongProcedure) result::put);
        return result;
    }

    public static <T> @NotNull Map<Long, T> toJavaMap(@NotNull LongObjectMap<T> map) {
        HashMap<Long, T> result = new HashMap<>(map.size());
        map.forEach((LongObjectProcedure<T>) result::put);
        return result;
    }

    // Note: not pure
    public static long computeIfAbsent(@NotNull LongLongMap map, long key, @NotNull java.util.function.LongSupplier def) {
        long result = map.get(key);
        if (result == 0 && !map.containsKey(key)) {
            result = def.getAsLong();
            map.put(key, result);
        }
        return result;
    }

    // Note: not pure
    public static <T> T computeIfAbsent(@NotNull LongObjectMap<T> map, long key, @NotNull Supplier<T> def) {
        T result = map.get(key);
        if (result == null && !map.containsKey(key)) {
            result = def.get();
            map.put(key, result);
        }
        return result;
    }

    /* `LongSet` manipulations */

    public static @NotNull LongHashSet union(@NotNull LongContainer lhs, @NotNull LongContainer rhs) {
        LongHashSet copy = new LongHashSet(lhs);
        copy.addAll(rhs);
        return copy;
    }

    public static @NotNull LongHashSet intersect(@NotNull LongContainer lhs, @NotNull LongLookupContainer rhs) {
        LongHashSet copy = new LongHashSet(lhs);
        copy.retainAll(rhs);
        return copy;
    }

    public static @NotNull LongHashSet subtract(@NotNull LongContainer lhs, @NotNull LongContainer rhs) {
        LongHashSet copy = new LongHashSet(lhs);
        copy.removeAll(rhs);
        return copy;
    }

    public static @NotNull LongHashSet retainAllCopy(@NotNull LongContainer container, @NotNull LongPredicate predicate) {
        LongHashSet copy = new LongHashSet(container);
        copy.retainAll(predicate);
        return copy;
    }

    public static @NotNull LongHashSet removeAllCopy(@NotNull LongContainer container, @NotNull LongPredicate predicate) {
        LongHashSet copy = new LongHashSet(container);
        copy.removeAll(predicate);
        return copy;
    }
}
