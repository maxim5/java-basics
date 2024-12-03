package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.FloatArrayList;
import com.carrotsearch.hppc.FloatContainer;
import com.carrotsearch.hppc.FloatLookupContainer;
import com.carrotsearch.hppc.cursors.FloatCursor;
import com.carrotsearch.hppc.predicates.FloatPredicate;
import com.carrotsearch.hppc.procedures.FloatFloatProcedure;
import com.carrotsearch.hppc.procedures.FloatObjectProcedure;

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
 * Helper utils for Hppc float classes:
 * {@link FloatContainer}, {@link FloatArrayList}, {@link FloatHashSet}, etc.
 *
 * @link <a href='https://github.com/carrotsearch/hppc'>Hppc GitHub</a>
 */
@Stateless
@Generated(value = "Hppc$Type$.java", date = "2024-12-02T15:53:08.714570400Z")
public class HppcFloat {
    /*
     * In case Hppc library is not in a classpath, try to fail early and produce a friendly error message.
     */
    static {
        RuntimeRequirement.verify("com.carrotsearch.hppc.FloatArrayList");
    }

    /* `FloatList` manipulations */

    public static @NotNull FloatArrayList slice(@NotNull FloatArrayList list, int fromIndex, int toIndex) {
        assert fromIndex >= 0 && toIndex >= 0 && fromIndex <= toIndex :
            "Invalid range: from=%d to=%d".formatted(fromIndex, toIndex);
        fromIndex = Math.min(fromIndex, list.elementsCount);
        toIndex = Math.min(toIndex, list.elementsCount);
        FloatArrayList slice = new FloatArrayList();
        slice.buffer = Arrays.copyOfRange(list.buffer, fromIndex, toIndex);
        slice.elementsCount = toIndex - fromIndex;
        return slice;
    }

    public static void iterateChunks(@NotNull FloatContainer container,
                                     int chunkSize,
                                     @NotNull Consumer<FloatArrayList> consumer) {
        assert chunkSize > 0 : "Invalid chunk size: " + chunkSize;
        FloatArrayList arrayList = toFloatArrayList(container);
        for (int i = 0; i < arrayList.size(); i += chunkSize) {
            FloatArrayList chunk = HppcFloat.slice(arrayList, i, i + chunkSize);
            consumer.accept(chunk);
        }
    }

    public static @NotNull FloatArrayList toFloatArrayList(@NotNull FloatContainer container) {
        return container instanceof FloatArrayList arrayList ? arrayList : new FloatArrayList(container);
    }

    /* Java `List` <-> `FloatList` conversions */

    public static @NotNull FloatArrayList toFloatList(@NotNull Iterable<Float> list) {
        FloatArrayList arrayList = new FloatArrayList();
        list.forEach(arrayList::add);
        return arrayList;
    }

    public static @NotNull Stream<FloatCursor> toJavaStream(@NotNull FloatContainer container) {
        return StreamSupport.stream(container.spliterator(), false);
    }

    public static @NotNull ArrayList<Float> toJavaList(@NotNull FloatContainer container) {
        ArrayList<Float> list = new ArrayList<>(container.size());
        for (FloatCursor cursor : container) {
            list.add(cursor.value);
        }
        return list;
    }
}
