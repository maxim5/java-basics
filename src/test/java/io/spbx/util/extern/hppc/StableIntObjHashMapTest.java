package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.HashContainers;
import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.cursors.IntObjectCursor;
import io.spbx.util.base.tuple.IntPair;
import io.spbx.util.collect.stream.BasicStreams;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.ScopeFunctions.also;
import static io.spbx.util.testing.TestingBasics.arrayOf;
import static io.spbx.util.testing.TestingBasics.toStr;
import static io.spbx.util.testing.TestingPrimitives.ints;

@Tag("fast")
public class StableIntObjHashMapTest {
    private static final Logger log = Logger.forEnclosingClass();

    @Test
    public void stable_iterator_trivial_maps() {
        assertIterators(new IntObjectHashMap<Integer>());
        assertIterators(IntObjectHashMap.from(ints(0), arrayOf(0)));
        assertIterators(IntObjectHashMap.from(ints(1), arrayOf(1)));
        assertIterators(IntObjectHashMap.from(ints(1), arrayOf(2)));
        assertIterators(IntObjectHashMap.from(ints(-1, 0, 1), arrayOf(0, 1, 2)));
    }

    @Test
    public void stable_iterator_corner_case_size_3() {
        IntObjectHashMap<Integer> map = new IntObjectHashMap<>();
        also(map.put(-1, 0), () -> assertIterators(map));
        also(map.put(0, 1), () -> assertIterators(map));
        also(map.put(1, 2), () -> assertIterators(map));
    }

    @Test
    public void stable_iterator_corner_case_size_4() {
        IntObjectHashMap<Integer> map = new IntObjectHashMap<>();
        also(map.put(0, 0), () -> assertIterators(map));
        also(map.put(1, 1), () -> assertIterators(map));
        also(map.put(16, 16), () -> assertIterators(map));
        also(map.put(17, 17), () -> assertIterators(map));
    }

    @Test
    public void stable_iterator_single_entry_maps_ultimate() {
        int max = 16;
        IntObjectHashMap<Integer> map = new IntObjectHashMap<>();
        for (int i = 0; i < max; i++) {
            also(map.remove(i - 1), () -> assertIterators(map));
            also(map.put(i, i), () -> assertIterators(map));
            also(map.put(i, i + 1), () -> assertIterators(map));
        }
    }

    @Test
    public void stable_iterator_small_maps_ultimate() {
        int max = 16;
        IntObjectHashMap<Integer> map = new IntObjectHashMap<>();
        for (int i = 0; i < max; i++) {
            also(map.put(i, i), () -> assertIterators(map));
        }
    }

    @Test
    public void stable_iterator_more_small_maps_ultimate() {
        int max = 16;
        IntObjectHashMap<Integer> map = new IntObjectHashMap<>();
        for (int i = 0; i < max; i++) {
            also(map.put(0, i), () -> assertIterators(map));
            also(map.put(i, i * 2), () -> assertIterators(map));
            also(map.put(i - 1, i), () -> assertIterators(map));
            also(map.remove(i / 3), () -> assertIterators(map));
        }
    }

    @Test
    public void stable_iterator_medium_maps_ultimate() {
        int max = 16;
        IntObjectHashMap<Integer> map = new IntObjectHashMap<>();
        for (int i = 0; i < max; i++) {
            also(map.put(i, i), () -> assertIterators(map));
            also(map.put(i + max, i + max), () -> assertIterators(map));
        }
    }

    @Test
    public void stable_iterator_big_maps_ultimate() {
        int max = 16;
        IntObjectHashMap<Integer> map = new IntObjectHashMap<>();
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                also(map.put(i * max + j, i), () -> assertIterators(map));
            }
        }
    }

    private static <T> void assertIterators(@NotNull IntObjectHashMap<T> map) {
        List<IntPair> original = toList(map.iterator());
        List<IntPair> stable1 = toList(StableIntObjHashMap.stableIterator(map));
        List<IntPair> stable2 = toList(StableIntObjHashMap.stableIterator(map));
        assertThat(stable1).containsAtLeastElementsIn(original);
        assertThat(stable2).containsAtLeastElementsIn(stable1).inOrder();

        StableIntObjHashMap<T> stableMap = stableCloneOf(map);
        assertThat(toList(stableMap.iterator())).containsAtLeastElementsIn(stable1).inOrder();
        assertThat(toList(stableMap.iterator())).containsAtLeastElementsIn(stable1).inOrder();
    }

    private static <T> @NotNull List<IntPair> toList(@NotNull Iterator<IntObjectCursor<T>> iterator) {
        return BasicStreams.streamOf(iterator).map(cursor -> new IntPair(cursor.key, (Integer) cursor.value)).toList();
    }

    private static <T> @NotNull StableIntObjHashMap<T> stableCloneOf(@NotNull IntObjectHashMap<T> map) {
        int size = (int) ((map.keys.length - 2) * HashContainers.DEFAULT_LOAD_FACTOR);
        StableIntObjHashMap<T> clone = new StableIntObjHashMap<>(size);
        assert clone.keys.length == map.keys.length && clone.values.length == map.values.length;

        clone.putAll(map);
        assert clone.keys.length == map.keys.length && clone.values.length == map.values.length;
        clone.keys = map.keys;
        clone.values = map.values;

        log.trace().log("map:   len=%d keys=%s values=%s", map.keys.length, toStr(map.keys), toStr(map.values));
        log.trace().log("clone: len=%d keys=%s values=%s", clone.keys.length, toStr(clone.keys), toStr(clone.values));

        return clone;
    }
}
