package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.ShortArrayList;
import com.carrotsearch.hppc.ShortContainer;
import com.carrotsearch.hppc.ShortLookupContainer;
import com.carrotsearch.hppc.ShortHashSet;
import com.carrotsearch.hppc.ShortObjectMap;
import com.carrotsearch.hppc.ShortShortHashMap;
import com.carrotsearch.hppc.ShortShortMap;
import com.carrotsearch.hppc.cursors.ShortCursor;
import com.carrotsearch.hppc.predicates.ShortPredicate;
import com.carrotsearch.hppc.procedures.ShortShortProcedure;
import com.carrotsearch.hppc.procedures.ShortObjectProcedure;

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
 * Helper utils for Hppc short classes:
 * {@link ShortContainer}, {@link ShortArrayList}, {@link ShortHashSet}, etc.
 *
 * @link <a href='https://github.com/carrotsearch/hppc'>Hppc GitHub</a>
 */
@Stateless
@Generated(value = "Hppc$Type$.java", date = "2025-01-14T10:07:33.441113700Z")
public class HppcShort {
    /*
     * In case Hppc library is not in a classpath, try to fail early and produce a friendly error message.
     */
    static {
        RuntimeRequirement.verify("com.carrotsearch.hppc.ShortArrayList");
    }

    /* `ShortList` manipulations */

    public static @NotNull ShortArrayList slice(@NotNull ShortArrayList list, int fromIndex, int toIndex) {
        assert fromIndex >= 0 && toIndex >= 0 && fromIndex <= toIndex :
            "Invalid range: from=%d to=%d".formatted(fromIndex, toIndex);
        fromIndex = Math.min(fromIndex, list.elementsCount);
        toIndex = Math.min(toIndex, list.elementsCount);
        ShortArrayList slice = new ShortArrayList();
        slice.buffer = Arrays.copyOfRange(list.buffer, fromIndex, toIndex);
        slice.elementsCount = toIndex - fromIndex;
        return slice;
    }

    public static void iterateChunks(@NotNull ShortContainer container,
                                     int chunkSize,
                                     @NotNull Consumer<ShortArrayList> consumer) {
        assert chunkSize > 0 : "Invalid chunk size: " + chunkSize;
        ShortArrayList arrayList = toShortArrayList(container);
        for (int i = 0; i < arrayList.size(); i += chunkSize) {
            ShortArrayList chunk = HppcShort.slice(arrayList, i, i + chunkSize);
            consumer.accept(chunk);
        }
    }

    public static @NotNull ShortArrayList toShortArrayList(@NotNull ShortContainer container) {
        return container instanceof ShortArrayList arrayList ? arrayList : new ShortArrayList(container);
    }

    /* Java `List` <-> `ShortList` conversions */

    public static @NotNull ShortArrayList toShortList(@NotNull Iterable<Short> list) {
        ShortArrayList arrayList = new ShortArrayList();
        list.forEach(arrayList::add);
        return arrayList;
    }

    public static @NotNull Stream<ShortCursor> toJavaStream(@NotNull ShortContainer container) {
        return StreamSupport.stream(container.spliterator(), false);
    }

    public static @NotNull ArrayList<Short> toJavaList(@NotNull ShortContainer container) {
        ArrayList<Short> list = new ArrayList<>(container.size());
        for (ShortCursor cursor : container) {
            list.add(cursor.value);
        }
        return list;
    }

    /* `ShortMap` manipulations */

    public static @NotNull ShortShortMap slice(@NotNull ShortShortMap map, short @NotNull[] keys) {
        ShortShortHashMap result = new ShortShortHashMap(keys.length);
        for (short key : keys) {
            result.put(key, map.get(key));
        }
        return result;
    }

    public static @NotNull ShortShortMap slice(@NotNull ShortShortMap map, @NotNull ShortContainer keys) {
        ShortShortHashMap result = new ShortShortHashMap(keys.size());
        for (ShortCursor cursor : keys) {
            result.put(cursor.value, map.get(cursor.value));
        }
        return result;
    }

    public static @NotNull Map<Short, Short> toJavaMap(@NotNull ShortShortMap map) {
        HashMap<Short, Short> result = new HashMap<>(map.size());
        map.forEach((ShortShortProcedure) result::put);
        return result;
    }

    public static <T> @NotNull Map<Short, T> toJavaMap(@NotNull ShortObjectMap<T> map) {
        HashMap<Short, T> result = new HashMap<>(map.size());
        map.forEach((ShortObjectProcedure<T>) result::put);
        return result;
    }

    // Note: not pure
    public static <T> T computeIfAbsent(@NotNull ShortObjectMap<T> map, short key, @NotNull Supplier<T> def) {
        T result = map.get(key);
        if (result == null && !map.containsKey(key)) {
            result = def.get();
            map.put(key, result);
        }
        return result;
    }

    /* `ShortSet` manipulations */

    public static @NotNull ShortHashSet union(@NotNull ShortContainer lhs, @NotNull ShortContainer rhs) {
        ShortHashSet copy = new ShortHashSet(lhs);
        copy.addAll(rhs);
        return copy;
    }

    public static @NotNull ShortHashSet intersect(@NotNull ShortContainer lhs, @NotNull ShortLookupContainer rhs) {
        ShortHashSet copy = new ShortHashSet(lhs);
        copy.retainAll(rhs);
        return copy;
    }

    public static @NotNull ShortHashSet subtract(@NotNull ShortContainer lhs, @NotNull ShortContainer rhs) {
        ShortHashSet copy = new ShortHashSet(lhs);
        copy.removeAll(rhs);
        return copy;
    }

    public static @NotNull ShortHashSet retainAllCopy(@NotNull ShortContainer container, @NotNull ShortPredicate predicate) {
        ShortHashSet copy = new ShortHashSet(container);
        copy.retainAll(predicate);
        return copy;
    }

    public static @NotNull ShortHashSet removeAllCopy(@NotNull ShortContainer container, @NotNull ShortPredicate predicate) {
        ShortHashSet copy = new ShortHashSet(container);
        copy.removeAll(predicate);
        return copy;
    }
}
