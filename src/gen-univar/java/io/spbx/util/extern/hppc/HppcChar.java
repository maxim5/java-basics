package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.CharArrayList;
import com.carrotsearch.hppc.CharContainer;
import com.carrotsearch.hppc.CharLookupContainer;
import com.carrotsearch.hppc.cursors.CharCursor;
import com.carrotsearch.hppc.predicates.CharPredicate;
import com.carrotsearch.hppc.procedures.CharCharProcedure;
import com.carrotsearch.hppc.procedures.CharObjectProcedure;

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
 * Helper utils for Hppc char classes:
 * {@link CharContainer}, {@link CharArrayList}, {@link CharHashSet}, etc.
 *
 * @link <a href='https://github.com/carrotsearch/hppc'>Hppc GitHub</a>
 */
@Stateless
@Generated(value = "Hppc$Type$.java", date = "2025-01-14T10:07:33.441113700Z")
public class HppcChar {
    /*
     * In case Hppc library is not in a classpath, try to fail early and produce a friendly error message.
     */
    static {
        RuntimeRequirement.verify("com.carrotsearch.hppc.CharArrayList");
    }

    /* `CharList` manipulations */

    public static @NotNull CharArrayList slice(@NotNull CharArrayList list, int fromIndex, int toIndex) {
        assert fromIndex >= 0 && toIndex >= 0 && fromIndex <= toIndex :
            "Invalid range: from=%d to=%d".formatted(fromIndex, toIndex);
        fromIndex = Math.min(fromIndex, list.elementsCount);
        toIndex = Math.min(toIndex, list.elementsCount);
        CharArrayList slice = new CharArrayList();
        slice.buffer = Arrays.copyOfRange(list.buffer, fromIndex, toIndex);
        slice.elementsCount = toIndex - fromIndex;
        return slice;
    }

    public static void iterateChunks(@NotNull CharContainer container,
                                     int chunkSize,
                                     @NotNull Consumer<CharArrayList> consumer) {
        assert chunkSize > 0 : "Invalid chunk size: " + chunkSize;
        CharArrayList arrayList = toCharArrayList(container);
        for (int i = 0; i < arrayList.size(); i += chunkSize) {
            CharArrayList chunk = HppcChar.slice(arrayList, i, i + chunkSize);
            consumer.accept(chunk);
        }
    }

    public static @NotNull CharArrayList toCharArrayList(@NotNull CharContainer container) {
        return container instanceof CharArrayList arrayList ? arrayList : new CharArrayList(container);
    }

    /* Java `List` <-> `CharList` conversions */

    public static @NotNull CharArrayList toCharList(@NotNull Iterable<Character> list) {
        CharArrayList arrayList = new CharArrayList();
        list.forEach(arrayList::add);
        return arrayList;
    }

    public static @NotNull Stream<CharCursor> toJavaStream(@NotNull CharContainer container) {
        return StreamSupport.stream(container.spliterator(), false);
    }

    public static @NotNull ArrayList<Character> toJavaList(@NotNull CharContainer container) {
        ArrayList<Character> list = new ArrayList<>(container.size());
        for (CharCursor cursor : container) {
            list.add(cursor.value);
        }
        return list;
    }
}
