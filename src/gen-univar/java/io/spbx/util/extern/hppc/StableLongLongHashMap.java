package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.AbstractIterator;
import com.carrotsearch.hppc.LongLongAssociativeContainer;
import com.carrotsearch.hppc.LongLongHashMap;
import com.carrotsearch.hppc.cursors.LongLongCursor;
import io.spbx.util.collect.container.IntSize;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.processing.Generated;
import java.util.Iterator;

/**
 * A version of Hppc {@link LongLongHashMap} which provides a stable {@link Iterator}.
 *
 * @link <a href='https://github.com/carrotsearch/hppc'>Hppc GitHub</a>
 */
@NotThreadSafe
@Generated(value = "Stable$Type$$Type$HashMap.java", date = "2024-12-02T15:53:08.721572300Z")
public class StableLongLongHashMap extends LongLongHashMap implements IntSize {
    public StableLongLongHashMap() {
        super();
    }

    public StableLongLongHashMap(int expectedElements) {
        super(expectedElements);
    }

    public StableLongLongHashMap(int expectedElements, double loadFactor) {
        super(expectedElements, loadFactor);
    }

    public StableLongLongHashMap(LongLongAssociativeContainer container) {
        super(container);
    }

    /** Creates a hash map from two index-aligned arrays of key-value pairs. */
    public static StableLongLongHashMap from(long[] keys, long[] values) {
        if (keys.length != values.length) {
            throw new IllegalArgumentException("Arrays of keys and values must have an identical length");
        }
        StableLongLongHashMap map = new StableLongLongHashMap(keys.length);
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    @Override
    protected int nextIterationSeed() {
        return 0;
    }

    public static Iterator<LongLongCursor> stableIterator(LongLongHashMap map) {
        return new StableEntryIterator(map);
    }

    static final class StableEntryIterator extends AbstractIterator<LongLongCursor> {
        private static final int increment = 29;    // == HashContainers.iterationIncrement(0)
        private final LongLongCursor cursor = new LongLongCursor();
        private final LongLongHashMap map;
        private int slot = 0;                       // == seed & mask, which is 0 for `seed == 0` and any mask
        private int index;

        public StableEntryIterator(LongLongHashMap map) {
            this.map = map;
        }

        @Override
        protected LongLongCursor fetch() {
            int mask = mask(map);
            while (index <= mask) {
                long existing;
                index++;
                slot = (slot + increment) & mask;
                if (!((existing = map.keys[slot]) == 0)) {
                    cursor.index = slot;
                    cursor.key = existing;
                    cursor.value = map.values[slot];
                    return cursor;
                }
            }
            if (index == mask + 1 && hasEmptyKey(map)) {
                cursor.index = index;
                cursor.key = 0;
                cursor.value = map.values[index++];
                return cursor;
            }
            return done();
        }

        private static int mask(LongLongHashMap map) {
            return map.keys.length - 2;     // `map.mask` always equals to this, see LongLongHashMap#allocateBuffers
        }

        private static boolean hasEmptyKey(LongLongHashMap map) {
            return map.containsKey(0);      // Quick O(1) check, see LongLongHashMap#containsKey
        }
    }
}
