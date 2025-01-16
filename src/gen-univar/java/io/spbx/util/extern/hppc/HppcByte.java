package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.ByteArrayList;
import com.carrotsearch.hppc.ByteContainer;
import com.carrotsearch.hppc.ByteLookupContainer;
import com.carrotsearch.hppc.cursors.ByteCursor;
import com.carrotsearch.hppc.predicates.BytePredicate;
import com.carrotsearch.hppc.procedures.ByteByteProcedure;
import com.carrotsearch.hppc.procedures.ByteObjectProcedure;

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
 * Helper utils for Hppc byte classes:
 * {@link ByteContainer}, {@link ByteArrayList}, {@link ByteHashSet}, etc.
 *
 * @link <a href='https://github.com/carrotsearch/hppc'>Hppc GitHub</a>
 */
@Stateless
@Generated(value = "Hppc$Type$.java", date = "2025-01-14T10:07:33.441113700Z")
public class HppcByte {
    /*
     * In case Hppc library is not in a classpath, try to fail early and produce a friendly error message.
     */
    static {
        RuntimeRequirement.verify("com.carrotsearch.hppc.ByteArrayList");
    }

    /* `ByteList` manipulations */

    public static @NotNull ByteArrayList slice(@NotNull ByteArrayList list, int fromIndex, int toIndex) {
        assert fromIndex >= 0 && toIndex >= 0 && fromIndex <= toIndex :
            "Invalid range: from=%d to=%d".formatted(fromIndex, toIndex);
        fromIndex = Math.min(fromIndex, list.elementsCount);
        toIndex = Math.min(toIndex, list.elementsCount);
        ByteArrayList slice = new ByteArrayList();
        slice.buffer = Arrays.copyOfRange(list.buffer, fromIndex, toIndex);
        slice.elementsCount = toIndex - fromIndex;
        return slice;
    }

    public static void iterateChunks(@NotNull ByteContainer container,
                                     int chunkSize,
                                     @NotNull Consumer<ByteArrayList> consumer) {
        assert chunkSize > 0 : "Invalid chunk size: " + chunkSize;
        ByteArrayList arrayList = toByteArrayList(container);
        for (int i = 0; i < arrayList.size(); i += chunkSize) {
            ByteArrayList chunk = HppcByte.slice(arrayList, i, i + chunkSize);
            consumer.accept(chunk);
        }
    }

    public static @NotNull ByteArrayList toByteArrayList(@NotNull ByteContainer container) {
        return container instanceof ByteArrayList arrayList ? arrayList : new ByteArrayList(container);
    }

    /* Java `List` <-> `ByteList` conversions */

    public static @NotNull ByteArrayList toByteList(@NotNull Iterable<Byte> list) {
        ByteArrayList arrayList = new ByteArrayList();
        list.forEach(arrayList::add);
        return arrayList;
    }

    public static @NotNull Stream<ByteCursor> toJavaStream(@NotNull ByteContainer container) {
        return StreamSupport.stream(container.spliterator(), false);
    }

    public static @NotNull ArrayList<Byte> toJavaList(@NotNull ByteContainer container) {
        ArrayList<Byte> list = new ArrayList<>(container.size());
        for (ByteCursor cursor : container) {
            list.add(cursor.value);
        }
        return list;
    }
}
