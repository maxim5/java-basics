package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.AbstractIterator;
import com.carrotsearch.hppc.ShortObjectAssociativeContainer;
import com.carrotsearch.hppc.ShortObjectHashMap;
import com.carrotsearch.hppc.cursors.ShortObjectCursor;
import io.spbx.util.collect.container.IntSize;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.processing.Generated;
import java.util.Iterator;

/**
 * A version of Hppc {@link ShortObjectHashMap} which provides a stable {@link Iterator}.
 *
 * @link <a href='https://github.com/carrotsearch/hppc'>Hppc GitHub</a>
 */
@NotThreadSafe
@Generated(value = "Stable$Type$ObjHashMap.java", date = "2024-12-02T15:53:08.725573800Z")
public class StableShortObjHashMap<VType> extends ShortObjectHashMap<VType> implements IntSize {
    public StableShortObjHashMap() {
        super();
    }

    public StableShortObjHashMap(int expectedElements) {
        super(expectedElements);
    }

    public StableShortObjHashMap(int expectedElements, double loadFactor) {
        super(expectedElements, loadFactor);
    }

    public StableShortObjHashMap(ShortObjectAssociativeContainer container) {
        super(container);
    }

    /** Creates a hash map from two index-aligned arrays of key-value pairs. */
    public static <VType> StableShortObjHashMap<VType> from(short[] keys, VType[] values) {
        if (keys.length != values.length) {
            throw new IllegalArgumentException("Arrays of keys and values must have an identical length");
        }
        StableShortObjHashMap map = new StableShortObjHashMap(keys.length);
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    @Override
    protected int nextIterationSeed() {
        return 0;
    }

    public static <VType> Iterator<ShortObjectCursor<VType>> stableIterator(ShortObjectHashMap<VType> map) {
        return new StableEntryIterator<VType>(map);
    }

    static final class StableEntryIterator<VType> extends AbstractIterator<ShortObjectCursor<VType>> {
        private static final int increment = 29;    // == HashContainers.iterationIncrement(0)
        private final ShortObjectCursor<VType> cursor = new ShortObjectCursor<>();
        private final ShortObjectHashMap<VType> map;
        private int slot = 0;                       // == seed & mask, which is 0 for `seed == 0` and any mask
        private int index;

        public StableEntryIterator(ShortObjectHashMap<VType> map) {
            this.map = map;
        }

        @Override
        protected ShortObjectCursor fetch() {
            int mask = mask(map);
            while (index <= mask) {
                short existing;
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

        private static int mask(ShortObjectHashMap<?> map) {
            return map.keys.length - 2;     // `map.mask` always equals to this, see ShortObjectHashMap#allocateBuffers
        }

        private static boolean hasEmptyKey(ShortObjectHashMap<?> map) {
            return map.containsKey((short) 0);  // Quick O(1) check, see ShortObjectHashMap#containsKey
        }
    }
}
