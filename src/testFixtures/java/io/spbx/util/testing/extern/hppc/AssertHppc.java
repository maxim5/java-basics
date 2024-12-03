package io.spbx.util.testing.extern.hppc;

import com.carrotsearch.hppc.ByteArrayList;
import com.carrotsearch.hppc.ByteContainer;
import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntContainer;
import com.carrotsearch.hppc.IntHashSet;
import com.carrotsearch.hppc.IntIntHashMap;
import com.carrotsearch.hppc.IntIntMap;
import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.LongArrayList;
import com.carrotsearch.hppc.LongContainer;
import com.carrotsearch.hppc.LongHashSet;
import com.carrotsearch.hppc.LongLongHashMap;
import com.carrotsearch.hppc.LongLongMap;
import com.carrotsearch.hppc.LongObjectHashMap;
import com.carrotsearch.hppc.LongObjectMap;
import com.google.common.truth.MapSubject;
import com.google.common.truth.Truth;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.ops.IntOps;
import io.spbx.util.extern.hppc.HppcInt;
import io.spbx.util.extern.hppc.HppcLong;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static io.spbx.util.testing.extern.hppc.TestingHppc.*;

@Stateless
public class AssertHppc {
    @CheckReturnValue
    public static @NotNull IntContainerSubject assertArray(@NotNull IntContainer container) {
        return new IntContainerSubject(container);
    }

    @CheckReturnValue
    public static @NotNull IntContainerSubject assertArray(int @NotNull[] ints) {
        return assertArray(IntArrayList.from(ints));
    }

    @CheckReturnValue
    public static @NotNull LongContainerSubject assertArray(@NotNull LongContainer container) {
        return new LongContainerSubject(container);
    }

    @CheckReturnValue
    public static @NotNull LongContainerSubject assertArray(long @NotNull[] longs) {
        return assertArray(LongArrayList.from(longs));
    }

    @CheckReturnValue
    public static @NotNull ByteContainerSubject assertArray(@NotNull ByteContainer container) {
        return new ByteContainerSubject(container);
    }

    @CheckReturnValue
    public static @NotNull ByteContainerSubject assertArray(byte @NotNull[] bytes) {
        return assertArray(ByteArrayList.from(bytes));
    }

    @CheckReturnValue
    public static @NotNull IntIntMapSubject assertMap(@NotNull IntIntMap map) {
        return new IntIntMapSubject(map);
    }

    @CheckReturnValue
    public static <T> @NotNull IntObjectMapSubject<T> assertMap(@NotNull IntObjectMap<T> map) {
        return new IntObjectMapSubject<>(map);
    }

    @CheckReturnValue
    public static @NotNull LongLongMapSubject assertMap(@NotNull LongLongMap map) {
        return new LongLongMapSubject(map);
    }

    @CheckReturnValue
    public static <T> @NotNull LongObjectMapSubject<T> assertMap(@NotNull LongObjectMap<T> map) {
        return new LongObjectMapSubject<>(map);
    }

    public record IntContainerSubject(@NotNull IntContainer container) {
        public void isEmpty() {
            Truth.assertThat(container).isEmpty();
        }

        public void containsExactlyInOrder(int... expected) {
            Truth.assertThat(asIntArrayList(container)).isEqualTo(IntArrayList.from(expected));
        }

        public void containsExactlyNoOrder(int... expected) {
            Truth.assertThat(asIntHashSet(container)).isEqualTo(IntHashSet.from(expected));
        }
    }

    public record LongContainerSubject(@NotNull LongContainer container) {
        public void isEmpty() {
            Truth.assertThat(container).isEmpty();
        }

        public void containsExactlyInOrder(long... expected) {
            Truth.assertThat(asLongArrayList(container)).isEqualTo(LongArrayList.from(expected));
        }

        public void containsExactlyNoOrder(long... expected) {
            Truth.assertThat(asLongHashSet(container)).isEqualTo(LongHashSet.from(expected));
        }
    }

    public record ByteContainerSubject(@NotNull ByteContainer container) {
        public void isEmpty() {
            Truth.assertThat(container).isEmpty();
        }

        public void containsExactlyInOrder(int... expected) {
            Truth.assertThat(asByteArrayList(container)).isEqualTo(from(expected));
        }

        public void containsExactlyNoOrder(int... expected) {
            Truth.assertThat(asByteArrayList(container).sort()).isEqualTo(from(expected).sort());
        }

        // Primitive-candidate
        private static ByteArrayList from(int[] values) {
            return ByteArrayList.from(IntOps.coerceToByteArray(values));
        }
    }

    public record IntIntMapSubject(@NotNull IntIntMap map) {
        public void isEmpty() {
            Truth.assertThat(map).isEmpty();
        }

        public void isEqualTo(@NotNull IntIntMap expected) {
            Truth.assertThat(asIntIntHashMap(map)).isEqualTo(expected);
        }

        public void isEqualTo(@NotNull Map<Integer, Integer> expected) {
            this.asJavaMap().isEqualTo(expected);
        }

        public void containsExactly(int key, int value) {
            this.isEqualTo(newIntMap(key, value));
        }

        public void containsExactly(int key1, int value1, int key2, int value2) {
            this.isEqualTo(newIntMap(key1, value1, key2, value2));
        }

        public void containsExactly(int... expectedKeysValues) {
            this.isEqualTo(newIntMap(expectedKeysValues));
        }

        public void containsExactlyTrimmed(int... expectedKeysValues) {
            this.isEqualTo(trim(newIntMap(expectedKeysValues)));
        }

        @CheckReturnValue
        public @NotNull IntIntMapSubject trimmed() {
            return new IntIntMapSubject(trim(map));
        }

        @CheckReturnValue
        public @NotNull MapSubject asJavaMap() {
            return Truth.assertThat(HppcInt.toJavaMap(map));
        }
    }

    public record IntObjectMapSubject<T>(@NotNull IntObjectMap<T> map) {
        public void isEmpty() {
            Truth.assertThat(map).isEmpty();
        }

        public void isEqualTo(@NotNull IntObjectMap<T> expected) {
            Truth.assertThat(asIntObjectHashMap(map)).isEqualTo(expected);
        }

        public void isEqualTo(@NotNull Map<Integer, T> expected) {
            this.asJavaMap().isEqualTo(expected);
        }

        public void containsExactly(int key, @Nullable T expectedValue) {
            this.isEqualTo(newIntObjectMap(key, expectedValue));
        }

        public void containsExactly(int key1, @Nullable T expectedValue1, int key2, @Nullable T expectedValue2) {
            this.isEqualTo(newIntObjectMap(key1, expectedValue1, key2, expectedValue2));
        }

        public void containsExactly(@Nullable Object @NotNull ... expectedKeysValues) {
            this.isEqualTo(newIntObjectMap(expectedKeysValues));
        }

        @CheckReturnValue
        public @NotNull MapSubject asJavaMap() {
            return Truth.assertThat(HppcInt.toJavaMap(map));
        }
    }

    public record LongLongMapSubject(@NotNull LongLongMap map) {
        public void isEmpty() {
            Truth.assertThat(map).isEmpty();
        }

        public void isEqualTo(@NotNull LongLongMap expected) {
            Truth.assertThat(asLongLongHashMap(map)).isEqualTo(expected);
        }

        public void isEqualTo(@NotNull Map<Integer, Integer> expected) {
            this.asJavaMap().isEqualTo(expected);
        }

        public void containsExactly(long key, long value) {
            this.isEqualTo(newLongMap(key, value));
        }

        public void containsExactly(long key1, long value1, long key2, long value2) {
            this.isEqualTo(newLongMap(key1, value1, key2, value2));
        }

        public void containsExactly(long... expectedKeysValues) {
            this.isEqualTo(newLongMap(expectedKeysValues));
        }

        public void containsExactlyTrimmed(long... expectedKeysValues) {
            this.isEqualTo(trim(newLongMap(expectedKeysValues)));
        }

        @CheckReturnValue
        public @NotNull LongLongMapSubject trimmed() {
            return new LongLongMapSubject(trim(map));
        }

        @CheckReturnValue
        public @NotNull MapSubject asJavaMap() {
            return Truth.assertThat(HppcLong.toJavaMap(map));
        }
    }

    public record LongObjectMapSubject<T>(@NotNull LongObjectMap<T> map) {
        public void isEmpty() {
            Truth.assertThat(map).isEmpty();
        }

        public void isEqualTo(@NotNull LongObjectMap<T> expected) {
            Truth.assertThat(asLongObjectHashMap(map)).isEqualTo(expected);
        }

        public void isEqualTo(@NotNull Map<Long, T> expected) {
            this.asJavaMap().isEqualTo(expected);
        }

        public void containsExactly(long key, @Nullable T expectedValue) {
            this.isEqualTo(newLongObjectMap(key, expectedValue));
        }

        public void containsExactly(long key1, @Nullable T expectedValue1, long key2, @Nullable T expectedValue2) {
            this.isEqualTo(newLongObjectMap(key1, expectedValue1, key2, expectedValue2));
        }

        public void containsExactly(@Nullable Object @NotNull ... expectedKeysValues) {
            this.isEqualTo(newLongObjectMap(expectedKeysValues));
        }

        @CheckReturnValue
        public @NotNull MapSubject asJavaMap() {
            return Truth.assertThat(HppcLong.toJavaMap(map));
        }
    }

    private static @NotNull IntArrayList asIntArrayList(@NotNull IntContainer container) {
        return container instanceof IntArrayList list ? list : new IntArrayList(container);
    }

    private static @NotNull IntHashSet asIntHashSet(@NotNull IntContainer container) {
        return container instanceof IntHashSet set ? set : new IntHashSet(container);
    }

    private static @NotNull LongArrayList asLongArrayList(@NotNull LongContainer container) {
        return container instanceof LongArrayList list ? list : new LongArrayList(container);
    }

    private static @NotNull LongHashSet asLongHashSet(@NotNull LongContainer container) {
        return container instanceof LongHashSet set ? set : new LongHashSet(container);
    }

    private static @NotNull ByteArrayList asByteArrayList(@NotNull ByteContainer container) {
        return container instanceof ByteArrayList list ? list : new ByteArrayList(container);
    }

    private static @NotNull IntIntHashMap asIntIntHashMap(@NotNull IntIntMap map) {
        return new IntIntHashMap(map);
    }

    private static <T> @NotNull IntObjectHashMap<T> asIntObjectHashMap(@NotNull IntObjectMap<T> map) {
        return new IntObjectHashMap<>(map);
    }

    private static @NotNull LongLongHashMap asLongLongHashMap(@NotNull LongLongMap map) {
        return new LongLongHashMap(map);
    }

    private static <T> @NotNull LongObjectHashMap<T> asLongObjectHashMap(@NotNull LongObjectMap<T> map) {
        return new LongObjectHashMap<>(map);
    }
}
