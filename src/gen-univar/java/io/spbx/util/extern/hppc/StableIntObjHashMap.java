package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.AbstractIterator;
import com.carrotsearch.hppc.IntObjectAssociativeContainer;
import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.cursors.IntObjectCursor;
import io.spbx.util.collect.container.IntSize;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.processing.Generated;
import java.util.Iterator;

/**
 * A version of Hppc {@link IntObjectHashMap} which provides a stable {@link Iterator}.
 *
 * @link <a href='https://github.com/carrotsearch/hppc'>Hppc GitHub</a>
 */
@NotThreadSafe
@Generated(value = "Stable$Type$ObjHashMap.java", date = "2024-12-02T15:53:08.725573800Z")
public class StableIntObjHashMap<VType> extends IntObjectHashMap<VType> implements IntSize {
    public StableIntObjHashMap() {
        super();
    }

    public StableIntObjHashMap(int expectedElements) {
        super(expectedElements);
    }

    public StableIntObjHashMap(int expectedElements, double loadFactor) {
        super(expectedElements, loadFactor);
    }

    public StableIntObjHashMap(IntObjectAssociativeContainer container) {
        super(container);
    }

    /** Creates a hash map from two index-aligned arrays of key-value pairs. */
    public static <VType> StableIntObjHashMap<VType> from(int[] keys, VType[] values) {
        if (keys.length != values.length) {
            throw new IllegalArgumentException("Arrays of keys and values must have an identical length");
        }
        StableIntObjHashMap map = new StableIntObjHashMap(keys.length);
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    @Override
    protected int nextIterationSeed() {
        return 0;
    }

    public static <VType> Iterator<IntObjectCursor<VType>> stableIterator(IntObjectHashMap<VType> map) {
        return new StableEntryIterator<VType>(map);
    }

    static final class StableEntryIterator<VType> extends AbstractIterator<IntObjectCursor<VType>> {
        private static final int increment = 29;    // == HashContainers.iterationIncrement(0)
        private final IntObjectCursor<VType> cursor = new IntObjectCursor<>();
        private final IntObjectHashMap<VType> map;
        private int slot = 0;                       // == seed & mask, which is 0 for `seed == 0` and any mask
        private int index;

        public StableEntryIterator(IntObjectHashMap<VType> map) {
            this.map = map;
        }

        @Override
        protected IntObjectCursor fetch() {
            int mask = mask(map);
            while (index <= mask) {
                int existing;
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

        private static int mask(IntObjectHashMap<?> map) {
            return map.keys.length - 2;     // `map.mask` always equals to this, see IntObjectHashMap#allocateBuffers
        }

        private static boolean hasEmptyKey(IntObjectHashMap<?> map) {
            return map.containsKey(0);      // Quick O(1) check, see IntObjectHashMap#containsKey
        }
    }
}
