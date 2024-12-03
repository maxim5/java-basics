package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.HashContainers;
import com.carrotsearch.hppc.IntIntHashMap;
import com.carrotsearch.hppc.cursors.IntIntCursor;
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
import static io.spbx.util.testing.TestingBasics.toStr;
import static io.spbx.util.testing.TestingPrimitives.ints;

@Tag("fast")
public class StableIntIntHashMapTest {
    private static final Logger log = Logger.forEnclosingClass();

    @Test
    public void stable_iterator_trivial_maps() {
        assertIterators(new IntIntHashMap());
        assertIterators(IntIntHashMap.from(ints(0), ints(0)));
        assertIterators(IntIntHashMap.from(ints(1), ints(1)));
        assertIterators(IntIntHashMap.from(ints(1), ints(2)));
        assertIterators(IntIntHashMap.from(ints(-1, 0, 1), ints(0, 1, 2)));
    }

    @Test
    public void stable_iterator_corner_case_size_3() {
        IntIntHashMap map = new IntIntHashMap();
        also(map.put(-1, 0), () -> assertIterators(map));
        also(map.put(0, 1), () -> assertIterators(map));
        also(map.put(1, 2), () -> assertIterators(map));
    }

    @Test
    public void stable_iterator_corner_case_size_4() {
        IntIntHashMap map = new IntIntHashMap();
        also(map.put(0, 0), () -> assertIterators(map));
        also(map.put(1, 1), () -> assertIterators(map));
        also(map.put(16, 16), () -> assertIterators(map));
        also(map.put(17, 17), () -> assertIterators(map));
    }

    @Test
    public void stable_iterator_single_entry_maps_ultimate() {
        int max = 16;
        IntIntHashMap map = new IntIntHashMap();
        for (int i = 0; i < max; i++) {
            also(map.remove(i - 1), () -> assertIterators(map));
            also(map.put(i, i), () -> assertIterators(map));
            also(map.put(i, i + 1), () -> assertIterators(map));
        }
    }

    @Test
    public void stable_iterator_small_maps_ultimate() {
        int max = 16;
        IntIntHashMap map = new IntIntHashMap();
        for (int i = 0; i < max; i++) {
            also(map.put(i, i), () -> assertIterators(map));
        }
    }

    @Test
    public void stable_iterator_more_small_maps_ultimate() {
        int max = 16;
        IntIntHashMap map = new IntIntHashMap();
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
        IntIntHashMap map = new IntIntHashMap();
        for (int i = 0; i < max; i++) {
            also(map.put(i, i), () -> assertIterators(map));
            also(map.put(i + max, i + max), () -> assertIterators(map));
        }
    }

    @Test
    public void stable_iterator_big_maps_ultimate() {
        int max = 16;
        IntIntHashMap map = new IntIntHashMap();
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max; j++) {
                also(map.put(i * max + j, i), () -> assertIterators(map));
            }
        }
    }

    private static void assertIterators(@NotNull IntIntHashMap map) {
        List<IntPair> original = toList(map.iterator());
        List<IntPair> stable1 = toList(StableIntIntHashMap.stableIterator(map));
        List<IntPair> stable2 = toList(StableIntIntHashMap.stableIterator(map));
        assertThat(stable1).containsAtLeastElementsIn(original);
        assertThat(stable2).containsAtLeastElementsIn(stable1).inOrder();

        StableIntIntHashMap stableMap = stableCloneOf(map);
        assertThat(toList(stableMap.iterator())).containsAtLeastElementsIn(stable1).inOrder();
        assertThat(toList(stableMap.iterator())).containsAtLeastElementsIn(stable1).inOrder();
    }

    private static @NotNull List<IntPair> toList(@NotNull Iterator<IntIntCursor> iterator) {
        return BasicStreams.streamOf(iterator).map(cursor -> new IntPair(cursor.key, cursor.value)).toList();
    }

    private static @NotNull StableIntIntHashMap stableCloneOf(@NotNull IntIntHashMap map) {
        int size = (int) ((map.keys.length - 2) * HashContainers.DEFAULT_LOAD_FACTOR);
        StableIntIntHashMap clone = new StableIntIntHashMap(size);
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
