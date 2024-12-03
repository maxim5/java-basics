package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.AbstractIterator;
import com.carrotsearch.hppc.ShortShortAssociativeContainer;
import com.carrotsearch.hppc.ShortShortHashMap;
import com.carrotsearch.hppc.cursors.ShortShortCursor;
import io.spbx.util.collect.container.IntSize;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.processing.Generated;
import java.util.Iterator;

/**
 * A version of Hppc {@link ShortShortHashMap} which provides a stable {@link Iterator}.
 *
 * @link <a href='https://github.com/carrotsearch/hppc'>Hppc GitHub</a>
 */
@NotThreadSafe
@Generated(value = "Stable$Type$$Type$HashMap.java", date = "2024-12-02T15:53:08.721572300Z")
public class StableShortShortHashMap extends ShortShortHashMap implements IntSize {
    public StableShortShortHashMap() {
        super();
    }

    public StableShortShortHashMap(int expectedElements) {
        super(expectedElements);
    }

    public StableShortShortHashMap(int expectedElements, double loadFactor) {
        super(expectedElements, loadFactor);
    }

    public StableShortShortHashMap(ShortShortAssociativeContainer container) {
        super(container);
    }

    /** Creates a hash map from two index-aligned arrays of key-value pairs. */
    public static StableShortShortHashMap from(short[] keys, short[] values) {
        if (keys.length != values.length) {
            throw new IllegalArgumentException("Arrays of keys and values must have an identical length");
        }
        StableShortShortHashMap map = new StableShortShortHashMap(keys.length);
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    @Override
    protected int nextIterationSeed() {
        return 0;
    }

    public static Iterator<ShortShortCursor> stableIterator(ShortShortHashMap map) {
        return new StableEntryIterator(map);
    }

    static final class StableEntryIterator extends AbstractIterator<ShortShortCursor> {
        private static final int increment = 29;    // == HashContainers.iterationIncrement(0)
        private final ShortShortCursor cursor = new ShortShortCursor();
        private final ShortShortHashMap map;
        private int slot = 0;                       // == seed & mask, which is 0 for `seed == 0` and any mask
        private int index;

        public StableEntryIterator(ShortShortHashMap map) {
            this.map = map;
        }

        @Override
        protected ShortShortCursor fetch() {
            int mask = mask(map);
            while (index <= mask) {
                short existing;
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

        private static int mask(ShortShortHashMap map) {
            return map.keys.length - 2;     // `map.mask` always equals to this, see ShortShortHashMap#allocateBuffers
        }

        private static boolean hasEmptyKey(ShortShortHashMap map) {
            return map.containsKey((short) 0);  // Quick O(1) check, see ShortShortHashMap#containsKey
        }
    }
}
