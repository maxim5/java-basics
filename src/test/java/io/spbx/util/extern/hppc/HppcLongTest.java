package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.LongArrayList;
import com.carrotsearch.hppc.LongHashSet;
import com.carrotsearch.hppc.LongLongHashMap;
import com.carrotsearch.hppc.LongObjectHashMap;
import com.carrotsearch.hppc.cursors.LongCursor;
import com.google.common.truth.IterableSubject;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.testing.func.MockConsumer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.arrayOf;
import static io.spbx.util.testing.TestingBasics.listOf;
import static io.spbx.util.testing.TestingPrimitives.longs;
import static io.spbx.util.testing.extern.hppc.AssertHppc.assertArray;

@Tag("fast")
public class HppcLongTest {
    @Test
    public void list_slice() {
        LongArrayList list = LongArrayList.from(1, 2, 3);

        assertArray(HppcLong.slice(list, 0, 2)).containsExactlyInOrder(1, 2);
        assertArray(HppcLong.slice(list, 0, 3)).containsExactlyInOrder(1, 2, 3);
        assertArray(HppcLong.slice(list, 0, 4)).containsExactlyInOrder(1, 2, 3);
        assertArray(HppcLong.slice(list, 1, 2)).containsExactlyInOrder(2);
        assertArray(HppcLong.slice(list, 1, 1)).containsExactlyInOrder();
    }

    @Test
    public void iterate_chunks() {
        try (MockConsumer.Tracker ignored = MockConsumer.trackAllConsumersDone()) {
            HppcLong.iterateChunks(LongArrayList.from(), 2, MockConsumer.expecting());
            HppcLong.iterateChunks(LongArrayList.from(1), 2, MockConsumer.expecting(LongArrayList.from(1)));
            HppcLong.iterateChunks(LongArrayList.from(1, 2), 2, MockConsumer.expecting(LongArrayList.from(1, 2)));
            HppcLong.iterateChunks(LongArrayList.from(1, 2, 3), 2,
                                   MockConsumer.expecting(LongArrayList.from(1, 2), LongArrayList.from(3)));
            HppcLong.iterateChunks(LongArrayList.from(1, 2, 3, 4), 2,
                                   MockConsumer.expecting(LongArrayList.from(1, 2), LongArrayList.from(3, 4)));
        }
    }

    @Test
    public void to_long_array_list() {
        assertArray(HppcLong.toLongArrayList(LongArrayList.from(1, 2, 3))).containsExactlyInOrder(1, 2, 3);
        assertArray(HppcLong.toLongArrayList(LongHashSet.from(1, 2, 3))).containsExactlyNoOrder(1, 2, 3);
    }

    @Test
    public void to_long_list() {
        assertArray(HppcLong.toLongList(listOf())).containsExactlyInOrder();
        assertArray(HppcLong.toLongList(listOf(3L, 2L, 1L))).containsExactlyInOrder(3, 2, 1);
    }

    @Test
    public void long_list_to_java_stream() {
        assertLongStreamThat(HppcLong.toJavaStream(LongArrayList.from())).isEmpty();
        assertLongStreamThat(HppcLong.toJavaStream(LongArrayList.from(1L))).containsExactly(1L).inOrder();
        assertLongStreamThat(HppcLong.toJavaStream(LongArrayList.from(1L, 2L, 3L))).containsExactly(1L, 2L, 3L).inOrder();
    }

    @Test
    public void to_java_list() {
        assertThat(HppcLong.toJavaList(LongArrayList.from())).isEmpty();
        assertThat(HppcLong.toJavaList(LongArrayList.from(1L))).containsExactly(1L).inOrder();
        assertThat(HppcLong.toJavaList(LongArrayList.from(1L, 2L, 3L))).containsExactly(1L, 2L, 3L).inOrder();
    }

    @Test
    public void long_long_map_to_java_map() {
        assertThat(HppcLong.toJavaMap(new LongLongHashMap())).isEmpty();
        assertThat(HppcLong.toJavaMap(LongLongHashMap.from(longs(1), longs(7)))).containsExactly(1L, 7L);
        assertThat(HppcLong.toJavaMap(LongLongHashMap.from(longs(1, 2), longs(7, 8)))).containsExactly(1L, 7L, 2L, 8L);
    }

    @Test
    public void long_obj_map_to_java_map() {
        assertThat(HppcLong.toJavaMap(new LongObjectHashMap<>())).isEmpty();
        assertThat(HppcLong.toJavaMap(LongObjectHashMap.from(longs(1), arrayOf("foo")))).containsExactly(1L, "foo");
        assertThat(HppcLong.toJavaMap(LongObjectHashMap.from(longs(1, 2), arrayOf("a", "b"))))
            .containsExactly(1L, "a", 2L, "b");
    }

    @CheckReturnValue
    private static @NotNull IterableSubject assertLongStreamThat(@NotNull Stream<LongCursor> stream) {
        return assertThat(stream.map(cursor -> cursor.value).toList());
    }
}
