package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntHashSet;
import com.carrotsearch.hppc.IntIntHashMap;
import com.carrotsearch.hppc.IntIntMap;
import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.google.common.truth.IterableSubject;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.testing.func.MockConsumer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.*;
import static io.spbx.util.testing.TestingPrimitives.ints;
import static io.spbx.util.testing.extern.hppc.AssertHppc.assertArray;
import static io.spbx.util.testing.extern.hppc.AssertHppc.assertMap;
import static io.spbx.util.testing.extern.hppc.TestingHppc.newIntMap;
import static io.spbx.util.testing.extern.hppc.TestingHppc.newIntObjectMap;
import static org.junit.jupiter.api.Assertions.fail;

@Tag("fast")
public class HppcIntTest {
    @Test
    public void list_slice() {
        IntArrayList list = IntArrayList.from(1, 2, 3);

        assertArray(HppcInt.slice(list, 0, 2)).containsExactlyInOrder(1, 2);
        assertArray(HppcInt.slice(list, 0, 3)).containsExactlyInOrder(1, 2, 3);
        assertArray(HppcInt.slice(list, 0, 4)).containsExactlyInOrder(1, 2, 3);
        assertArray(HppcInt.slice(list, 1, 2)).containsExactlyInOrder(2);
        assertArray(HppcInt.slice(list, 1, 1)).containsExactlyInOrder();
    }
    
    @Test
    public void iterate_chunks() {
        try (MockConsumer.Tracker ignored = MockConsumer.trackAllConsumersDone()) {
            HppcInt.iterateChunks(IntArrayList.from(), 2, MockConsumer.expecting());
            HppcInt.iterateChunks(IntArrayList.from(1), 2, MockConsumer.expecting(IntArrayList.from(1)));
            HppcInt.iterateChunks(IntArrayList.from(1, 2), 2, MockConsumer.expecting(IntArrayList.from(1, 2)));
            HppcInt.iterateChunks(IntArrayList.from(1, 2, 3), 2,
                                  MockConsumer.expecting(IntArrayList.from(1, 2), IntArrayList.from(3)));
            HppcInt.iterateChunks(IntArrayList.from(1, 2, 3, 4), 2,
                                   MockConsumer.expecting(IntArrayList.from(1, 2), IntArrayList.from(3, 4)));
        }
    }

    @Test
    public void map_slice() {
        IntIntHashMap map = IntIntHashMap.from(ints(1, 2), ints(7, 8));

        assertMap(HppcInt.slice(map, ints(1))).containsExactly(1, 7);
        assertMap(HppcInt.slice(map, IntArrayList.from(1))).containsExactly(1, 7);

        assertMap(HppcInt.slice(map, ints(2))).containsExactly(2, 8);
        assertMap(HppcInt.slice(map, IntArrayList.from(2))).containsExactly(2, 8);

        assertMap(HppcInt.slice(map, ints(1, 2))).containsExactly(1, 7, 2, 8);
        assertMap(HppcInt.slice(map, IntArrayList.from(1, 2))).containsExactly(1, 7, 2, 8);

        assertMap(HppcInt.slice(map, ints(1, 3))).containsExactly(1, 7, 3, 0);
        assertMap(HppcInt.slice(map, IntArrayList.from(1, 3))).containsExactly(1, 7, 3, 0);

        assertMap(HppcInt.slice(map, ints(3))).containsExactly(3, 0);
        assertMap(HppcInt.slice(map, IntArrayList.from(3))).containsExactly(3, 0);

        assertMap(HppcInt.slice(map, ints())).containsExactly();
        assertMap(HppcInt.slice(map, IntArrayList.from())).containsExactly();
    }

    @Test
    public void to_int_array_list() {
        assertArray(HppcInt.toIntArrayList(IntArrayList.from(1, 2, 3))).containsExactlyInOrder(1, 2, 3);
        assertArray(HppcInt.toIntArrayList(IntHashSet.from(1, 2, 3))).containsExactlyNoOrder(1, 2, 3);
    }

    @Test
    public void to_int_list() {
        assertArray(HppcInt.toIntList(listOf())).containsExactlyInOrder();
        assertArray(HppcInt.toIntList(listOf(3, 2, 1))).containsExactlyInOrder(3, 2, 1);
    }

    @Test
    public void to_java_stream() {
        assertIntStreamThat(HppcInt.toJavaStream(IntArrayList.from())).isEmpty();
        assertIntStreamThat(HppcInt.toJavaStream(IntArrayList.from(1))).containsExactly(1).inOrder();
        assertIntStreamThat(HppcInt.toJavaStream(IntArrayList.from(1, 2, 3))).containsExactly(1, 2, 3).inOrder();
    }

    @Test
    public void to_java_list() {
        assertThat(HppcInt.toJavaList(IntArrayList.from())).isEmpty();
        assertThat(HppcInt.toJavaList(IntArrayList.from(1))).containsExactly(1).inOrder();
        assertThat(HppcInt.toJavaList(IntArrayList.from(1, 2, 3))).containsExactly(1, 2, 3).inOrder();
    }

    @Test
    public void int_int_map_to_java_map() {
        assertThat(HppcInt.toJavaMap(new IntIntHashMap())).isEmpty();
        assertThat(HppcInt.toJavaMap(IntIntHashMap.from(ints(1), ints(7)))).containsExactly(1, 7);
        assertThat(HppcInt.toJavaMap(IntIntHashMap.from(ints(1, 2), ints(7, 8)))).containsExactly(1, 7, 2, 8);
    }

    @Test
    public void int_obj_map_to_java_map() {
        assertThat(HppcInt.toJavaMap(new IntObjectHashMap<>())).isEmpty();
        assertThat(HppcInt.toJavaMap(IntObjectHashMap.from(ints(1), arrayOf("foo")))).containsExactly(1, "foo");
        assertThat(HppcInt.toJavaMap(IntObjectHashMap.from(ints(1, 2), arrayOf("a", "b")))).containsExactly(1, "a", 2, "b");
    }

    @Test
    public void computeIfAbsent_int_int_exists() {
        IntIntMap map = newIntMap(1, 111, 2, 222);
        assertThat(HppcInt.computeIfAbsent(map, 1, () -> fail("Must not be called"))).isEqualTo(111);
        assertMap(map).containsExactly(1, 111, 2, 222);
    }

    @Test
    public void computeIfAbsent_int_int_exists_zero() {
        IntIntMap map = newIntMap(1, 0);
        assertThat(HppcInt.computeIfAbsent(map, 1, () -> fail("Must not be called"))).isEqualTo(0);
        assertMap(map).containsExactly(1, 0);
    }

    @Test
    public void computeIfAbsent_int_int_not_exists() {
        IntIntMap map = newIntMap(1, 111, 2, 222);
        assertThat(HppcInt.computeIfAbsent(map, 3, () -> 333)).isEqualTo(333);
        assertMap(map).containsExactly(1, 111, 2, 222, 3, 333);
    }

    @Test
    public void computeIfAbsent_int_obj_exists() {
        IntObjectMap<String> map = newIntObjectMap(1, "111", 2, "222");
        assertThat(HppcInt.computeIfAbsent(map, 1, () -> fail("Must not be called"))).isEqualTo("111");
        assertMap(map).containsExactly(1, "111", 2, "222");
    }

    @Test
    public void computeIfAbsent_int_obj_exists_null() {
        IntObjectMap<String> map = newIntObjectMap(1, null);
        assertThat(HppcInt.computeIfAbsent(map, 1, () -> fail("Must not be called"))).isNull();
        assertMap(map).containsExactly(1, null);
    }

    @Test
    public void computeIfAbsent_int_obj_not_exists() {
        IntObjectMap<String> map = newIntObjectMap(1, "111", 2, "222");
        assertThat(HppcInt.computeIfAbsent(map, 3, () -> "333")).isEqualTo("333");
        assertMap(map).containsExactly(1, "111", 2, "222", 3, "333");
    }

    @Test
    public void collect_to_int_list() {
        assertArray(HppcInt.collectToIntList(intStreamOf())).isEmpty();
        assertArray(HppcInt.collectToIntList(intStreamOf(1))).containsExactlyInOrder(1);
        assertArray(HppcInt.collectToIntList(intStreamOf(1, 2))).containsExactlyInOrder(1, 2);
    }

    @Test
    public void int_union() {
        assertArray(HppcInt.union(IntHashSet.from(), IntHashSet.from())).isEmpty();
        assertArray(HppcInt.union(IntHashSet.from(1, 2, 3), IntHashSet.from())).containsExactlyNoOrder(1, 2, 3);
        assertArray(HppcInt.union(IntHashSet.from(), IntHashSet.from(1, 2, 3))).containsExactlyNoOrder(1, 2, 3);
        assertArray(HppcInt.union(IntHashSet.from(1), IntHashSet.from(2))).containsExactlyNoOrder(1, 2);
        assertArray(HppcInt.union(IntHashSet.from(1, 2), IntHashSet.from(1, 2))).containsExactlyNoOrder(1, 2);
        assertArray(HppcInt.union(IntHashSet.from(1, 2, 3), IntHashSet.from(3, 4))).containsExactlyNoOrder(1, 2, 3, 4);
        assertArray(HppcInt.union(IntHashSet.from(1, 2, 3), IntHashSet.from(4, 5))).containsExactlyNoOrder(1, 2, 3, 4, 5);
    }

    @Test
    public void int_intersect() {
        assertArray(HppcInt.intersect(IntHashSet.from(), IntHashSet.from())).isEmpty();
        assertArray(HppcInt.intersect(IntHashSet.from(1, 2, 3), IntHashSet.from())).isEmpty();
        assertArray(HppcInt.intersect(IntHashSet.from(), IntHashSet.from(1, 2, 3))).isEmpty();
        assertArray(HppcInt.intersect(IntHashSet.from(1), IntHashSet.from(2))).isEmpty();
        assertArray(HppcInt.intersect(IntHashSet.from(1, 2), IntHashSet.from(1, 2))).containsExactlyNoOrder(1, 2);
        assertArray(HppcInt.intersect(IntHashSet.from(1, 2, 3), IntHashSet.from(3, 4))).containsExactlyNoOrder(3);
        assertArray(HppcInt.intersect(IntHashSet.from(1, 2, 3), IntHashSet.from(4, 5))).isEmpty();
    }

    @Test
    public void int_subtract() {
        assertArray(HppcInt.subtract(IntHashSet.from(), IntHashSet.from())).isEmpty();
        assertArray(HppcInt.subtract(IntHashSet.from(1, 2, 3), IntHashSet.from())).containsExactlyNoOrder(1, 2, 3);
        assertArray(HppcInt.subtract(IntHashSet.from(), IntHashSet.from(1, 2, 3))).containsExactlyNoOrder();
        assertArray(HppcInt.subtract(IntHashSet.from(1), IntHashSet.from(2))).containsExactlyNoOrder(1);
        assertArray(HppcInt.subtract(IntHashSet.from(1, 2), IntHashSet.from(1, 2))).containsExactlyNoOrder();
        assertArray(HppcInt.subtract(IntHashSet.from(1, 2, 3), IntHashSet.from(3, 4))).containsExactlyNoOrder(1, 2);
        assertArray(HppcInt.subtract(IntHashSet.from(1, 2, 3), IntHashSet.from(4, 5))).containsExactlyNoOrder(1, 2, 3);
    }

    @Test
    public void int_retain_all_copy() {
        assertArray(HppcInt.retainAllCopy(IntHashSet.from(), x -> x % 2 == 0)).containsExactlyNoOrder();
        assertArray(HppcInt.retainAllCopy(IntHashSet.from(1), x -> x % 2 == 0)).containsExactlyNoOrder();
        assertArray(HppcInt.retainAllCopy(IntHashSet.from(2), x -> x % 2 == 0)).containsExactlyNoOrder(2);
        assertArray(HppcInt.retainAllCopy(IntHashSet.from(1, 3), x -> x % 2 == 0)).containsExactlyNoOrder();
        assertArray(HppcInt.retainAllCopy(IntHashSet.from(1, 2, 3), x -> x % 2 == 0)).containsExactlyNoOrder(2);
    }

    @Test
    public void int_remove_all_copy() {
        assertArray(HppcInt.removeAllCopy(IntHashSet.from(), x -> x % 2 == 0)).containsExactlyNoOrder();
        assertArray(HppcInt.removeAllCopy(IntHashSet.from(1), x -> x % 2 == 0)).containsExactlyNoOrder(1);
        assertArray(HppcInt.removeAllCopy(IntHashSet.from(2), x -> x % 2 == 0)).containsExactlyNoOrder();
        assertArray(HppcInt.removeAllCopy(IntHashSet.from(1, 3), x -> x % 2 == 0)).containsExactlyNoOrder(1, 3);
        assertArray(HppcInt.removeAllCopy(IntHashSet.from(1, 2, 3), x -> x % 2 == 0)).containsExactlyNoOrder(1, 3);
    }

    @CheckReturnValue
    private static @NotNull IterableSubject assertIntStreamThat(@NotNull Stream<IntCursor> stream) {
        return assertThat(stream.map(cursor -> cursor.value).toList());
    }
}
