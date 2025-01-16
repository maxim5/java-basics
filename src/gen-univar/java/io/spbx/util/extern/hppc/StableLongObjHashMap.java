package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.AbstractIterator;
import com.carrotsearch.hppc.LongObjectAssociativeContainer;
import com.carrotsearch.hppc.LongObjectHashMap;
import com.carrotsearch.hppc.cursors.LongObjectCursor;
import io.spbx.util.collect.container.IntSize;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.processing.Generated;
import java.util.Iterator;

/**
 * A version of Hppc {@link LongObjectHashMap} which provides a stable {@link Iterator}.
 *
 * @link <a href='https://github.com/carrotsearch/hppc'>Hppc GitHub</a>
 */
@NotThreadSafe
@Generated(value = "Stable$Type$ObjHashMap.java", date = "2025-01-14T10:07:33.450115600Z")
public class StableLongObjHashMap<VType> extends LongObjectHashMap<VType> implements IntSize {
    public StableLongObjHashMap() {
        super();
    }

    public StableLongObjHashMap(int expectedElements) {
        super(expectedElements);
    }

    public StableLongObjHashMap(int expectedElements, double loadFactor) {
        super(expectedElements, loadFactor);
    }

    public StableLongObjHashMap(LongObjectAssociativeContainer container) {
        super(container);
    }

    /** Creates a hash map from two index-aligned arrays of key-value pairs. */
    public static <VType> StableLongObjHashMap<VType> from(long[] keys, VType[] values) {
        if (keys.length != values.length) {
            throw new IllegalArgumentException("Arrays of keys and values must have an identical length");
        }
        StableLongObjHashMap map = new StableLongObjHashMap(keys.length);
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    @Override
    protected int nextIterationSeed() {
        return 0;
    }

    public static <VType> Iterator<LongObjectCursor<VType>> stableIterator(LongObjectHashMap<VType> map) {
        return new StableEntryIterator<VType>(map);
    }

    static final class StableEntryIterator<VType> extends AbstractIterator<LongObjectCursor<VType>> {
        private static final int increment = 29;    // == HashContainers.iterationIncrement(0)
        private final LongObjectCursor<VType> cursor = new LongObjectCursor<>();
        private final LongObjectHashMap<VType> map;
        private int slot = 0;                       // == seed & mask, which is 0 for `seed == 0` and any mask
        private int index;

        public StableEntryIterator(LongObjectHashMap<VType> map) {
            this.map = map;
        }

        @Override
        protected LongObjectCursor fetch() {
            int mask = mask(map);
            while (index <= mask) {
                long existing;
                index++;
                slot = (slot + increment) & mask;
                if (!((existing = map.keys[slot]) == 0)) {
                    cursor.index = slot;
                    cursor.key = existing;
                    cursor.value = (VType) map.values[slot];
                    return cursor;
                }
            }
            if (index == mask + 1 && hasEmptyKey(map)) {
                cursor.index = index;
                cursor.key = 0;
                cursor.value = (VType) map.values[index++];
                return cursor;
            }
            return done();
        }

        private static int mask(LongObjectHashMap<?> map) {
            return map.keys.length - 2;     // `map.mask` always equals to this, see LongObjectHashMap#allocateBuffers
        }

        private static boolean hasEmptyKey(LongObjectHashMap<?> map) {
            return map.containsKey(0);      // Quick O(1) check, see LongObjectHashMap#containsKey
        }
    }
}
