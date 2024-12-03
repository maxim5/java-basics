package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.AbstractIterator;
import com.carrotsearch.hppc.IntIntAssociativeContainer;
import com.carrotsearch.hppc.IntIntHashMap;
import com.carrotsearch.hppc.cursors.IntIntCursor;
import io.spbx.util.collect.container.IntSize;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.processing.Generated;
import java.util.Iterator;

/**
 * A version of Hppc {@link IntIntHashMap} which provides a stable {@link Iterator}.
 *
 * @link <a href='https://github.com/carrotsearch/hppc'>Hppc GitHub</a>
 */
@NotThreadSafe
@Generated(value = "Stable$Type$$Type$HashMap.java", date = "2024-12-02T15:53:08.721572300Z")
public class StableIntIntHashMap extends IntIntHashMap implements IntSize {
    public StableIntIntHashMap() {
        super();
    }

    public StableIntIntHashMap(int expectedElements) {
        super(expectedElements);
    }

    public StableIntIntHashMap(int expectedElements, double loadFactor) {
        super(expectedElements, loadFactor);
    }

    public StableIntIntHashMap(IntIntAssociativeContainer container) {
        super(container);
    }

    /** Creates a hash map from two index-aligned arrays of key-value pairs. */
    public static StableIntIntHashMap from(int[] keys, int[] values) {
        if (keys.length != values.length) {
            throw new IllegalArgumentException("Arrays of keys and values must have an identical length");
        }
        StableIntIntHashMap map = new StableIntIntHashMap(keys.length);
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    @Override
    protected int nextIterationSeed() {
        return 0;
    }

    public static Iterator<IntIntCursor> stableIterator(IntIntHashMap map) {
        return new StableEntryIterator(map);
    }

    static final class StableEntryIterator extends AbstractIterator<IntIntCursor> {
        private static final int increment = 29;    // == HashContainers.iterationIncrement(0)
        private final IntIntCursor cursor = new IntIntCursor();
        private final IntIntHashMap map;
        private int slot = 0;                       // == seed & mask, which is 0 for `seed == 0` and any mask
        private int index;

        public StableEntryIterator(IntIntHashMap map) {
            this.map = map;
        }

        @Override
        protected IntIntCursor fetch() {
            int mask = mask(map);
            while (index <= mask) {
                int existing;
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

        private static int mask(IntIntHashMap map) {
            return map.keys.length - 2;     // `map.mask` always equals to this, see IntIntHashMap#allocateBuffers
        }

        private static boolean hasEmptyKey(IntIntHashMap map) {
            return map.containsKey(0);      // Quick O(1) check, see IntIntHashMap#containsKey
        }
    }
}
