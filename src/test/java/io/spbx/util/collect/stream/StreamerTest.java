package io.spbx.util.collect.stream;

import io.spbx.util.base.tuple.Pair;
import io.spbx.util.collect.stream.Streamer.IndexedFunction;
import io.spbx.util.testing.annotate.TestFor;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.AssertStreamer.assertStream;
import static io.spbx.util.testing.TestingBasics.*;

@Tag("fast")
@TestFor({ Streamer.class, BiStreamer.class })
public class StreamerTest {
    private static final Integer NULL = null;

    @Test
    public void streamer_of_simple() {
        assertStream(Streamer.of()).isEmpty();
        assertStream(Streamer.of(1)).containsExactlyInOrder(1);
        assertStream(Streamer.of(NULL)).containsExactlyInOrder(NULL);
        assertStream(Streamer.of(1, 2, 3)).containsExactlyInOrder(1, 2, 3);
        assertStream(Streamer.of(NULL, NULL)).containsExactlyInOrder(NULL, NULL);

        assertStream(Streamer.of(NULL_STREAM)).isEmpty();
        assertStream(Streamer.of(streamOf())).isEmpty();
        assertStream(Streamer.of(streamOf(1))).containsExactlyInOrder(1);
        assertStream(Streamer.of(streamOf(NULL, NULL))).containsExactlyInOrder(NULL, NULL);

        assertStream(Streamer.of(NULL_LIST)).isEmpty();
        assertStream(Streamer.of(listOf())).isEmpty();
        assertStream(Streamer.of(listOf(1))).containsExactlyInOrder(1);
        assertStream(Streamer.of(listOf(NULL, NULL))).containsExactlyInOrder(NULL, NULL);

        assertStream(Streamer.of(NULL_SET)).isEmpty();
        assertStream(Streamer.of(setOf())).isEmpty();
        assertStream(Streamer.of(setOf(1))).containsExactlyInOrder(1);
        assertStream(Streamer.of(setOf(1, NULL))).containsExactlyInOrder(1, NULL);

        assertStream(Streamer.of(NULL_ITERABLE)).isEmpty();
        assertStream(Streamer.of(iterableOf())).isEmpty();
        assertStream(Streamer.of(iterableOf(1))).containsExactlyInOrder(1);
        assertStream(Streamer.of(iterableOf(NULL, NULL))).containsExactlyInOrder(NULL, NULL);

        assertStream(Streamer.of(NULL_ITERATOR)).isEmpty();
        assertStream(Streamer.of(iteratorOf())).isEmpty();
        assertStream(Streamer.of(iteratorOf(1))).containsExactlyInOrder(1);
        assertStream(Streamer.of(iteratorOf(NULL, NULL))).containsExactlyInOrder(NULL, NULL);
    }

    @Test
    public void streamer_concat_simple() {
        assertStream(Streamer.concat(streamOf(), streamOf())).isEmpty();
        assertStream(Streamer.concat(streamOf(), NULL_STREAM)).isEmpty();
        assertStream(Streamer.concat(NULL_STREAM, streamOf())).isEmpty();
        assertStream(Streamer.concat(NULL_STREAM, NULL_STREAM)).isEmpty();
        assertStream(Streamer.concat(streamOf(1), streamOf())).containsExactlyInOrder(1);
        assertStream(Streamer.concat(streamOf(), streamOf(2))).containsExactlyInOrder(2);
        assertStream(Streamer.concat(streamOf(1), streamOf(2))).containsExactlyInOrder(1, 2);

        assertStream(Streamer.concat(iterableOf(), iterableOf())).isEmpty();
        assertStream(Streamer.concat(iterableOf(), NULL_ITERABLE)).isEmpty();
        assertStream(Streamer.concat(NULL_ITERABLE, iterableOf())).isEmpty();
        assertStream(Streamer.concat(NULL_ITERABLE, NULL_ITERABLE)).isEmpty();
        assertStream(Streamer.concat(iterableOf(1), iterableOf())).containsExactlyInOrder(1);
        assertStream(Streamer.concat(iterableOf(), iterableOf(2))).containsExactlyInOrder(2);
        assertStream(Streamer.concat(iterableOf(1), iterableOf(2))).containsExactlyInOrder(1, 2);

        assertStream(Streamer.concat(arrayOf(), arrayOf())).isEmpty();
        assertStream(Streamer.concat(arrayOf(), NULL_ARRAY)).isEmpty();
        assertStream(Streamer.concat(NULL_ARRAY, arrayOf())).isEmpty();
        assertStream(Streamer.concat(NULL_ARRAY, NULL_ARRAY)).isEmpty();
        assertStream(Streamer.concat(arrayOf(1), arrayOf())).containsExactlyInOrder(1);
        assertStream(Streamer.concat(arrayOf(), arrayOf(2))).containsExactlyInOrder(2);
        assertStream(Streamer.concat(arrayOf(1), arrayOf(2))).containsExactlyInOrder(1, 2);

        assertStream(Streamer.concat(Streamer.of(), Streamer.of())).isEmpty();
        assertStream(Streamer.concat(Streamer.of(), null)).isEmpty();
        assertStream(Streamer.concat(null, Streamer.of())).isEmpty();
        assertStream(Streamer.concat((Streamer<?>) null, null)).isEmpty();
        assertStream(Streamer.concat(Streamer.of(1), Streamer.of())).containsExactlyInOrder(1);
        assertStream(Streamer.concat(Streamer.of(), Streamer.of(2))).containsExactlyInOrder(2);
        assertStream(Streamer.concat(Streamer.of(1), Streamer.of(2))).containsExactlyInOrder(1, 2);
    }

    @Test
    public void streamer_repeat_simple() {
        assertStream(Streamer.repeat(1, 0)).isEmpty();
        assertStream(Streamer.repeat(1, 1)).containsExactlyInOrder(1);
        assertStream(Streamer.repeat(1, 3)).containsExactlyInOrder(1, 1, 1);
        assertStream(Streamer.repeat(NULL, 0)).isEmpty();
        assertStream(Streamer.repeat(NULL, 1)).containsExactlyInOrder(NULL);
        assertStream(Streamer.repeat(NULL, 3)).containsExactlyInOrder(NULL, NULL, NULL);
    }

    @Test
    public void streamer_bi_of_simple() {
        assertStream(Streamer.Bi.of(streamOf())).isEmpty();
        assertStream(Streamer.Bi.of(streamOf(pairOf(1, 2)))).containsExactlyInOrder(pairOf(1, 2));
        assertStream(Streamer.Bi.of(streamOf(Map.entry(1, 2)))).containsExactlyInOrder(pairOf(1, 2));
        assertStream(Streamer.Bi.of(streamOf((Pair<?, ?>) null))).containsExactlyInOrder(Pair.empty());
        assertStream(Streamer.Bi.of(streamOf((Map.Entry<?, ?>) null))).containsExactlyInOrder(Pair.empty());

        assertStream(Streamer.Bi.of(iterableOf())).isEmpty();
        assertStream(Streamer.Bi.of(iterableOf(pairOf(1, 2)))).containsExactlyInOrder(pairOf(1, 2));
        assertStream(Streamer.Bi.of(iterableOf(Map.entry(1, 2)))).containsExactlyInOrder(pairOf(1, 2));
        assertStream(Streamer.Bi.of(iterableOf((Pair<?, ?>) null))).containsExactlyInOrder(Pair.empty());
        assertStream(Streamer.Bi.of(iterableOf((Map.Entry<?, ?>) null))).containsExactlyInOrder(Pair.empty());

        assertStream(Streamer.Bi.of(arrayOf())).isEmpty();
        assertStream(Streamer.Bi.of(arrayOf(pairOf(1, 2)))).containsExactlyInOrder(pairOf(1, 2));
        assertStream(Streamer.Bi.of(arrayOf(Map.entry(1, 2)))).containsExactlyInOrder(pairOf(1, 2));
        assertStream(Streamer.Bi.of(arrayOf((Pair<?, ?>) null))).containsExactlyInOrder(Pair.empty());
        assertStream(Streamer.Bi.of(arrayOf((Map.Entry<?, ?>) null))).containsExactlyInOrder(Pair.empty());
    }

    @Test
    public void streamer_of_map_simple() {
        assertStream(Streamer.of(NULL_MAP)).isEmpty();
        assertStream(Streamer.of(mapOf())).isEmpty();
        assertStream(Streamer.of(mapOf(NULL, NULL))).containsExactlyInOrder(Pair.empty());
        assertStream(Streamer.of(mapOf(1, NULL))).containsExactlyInOrder(pairOf(1, NULL));
        assertStream(Streamer.of(mapOf(NULL, 1))).containsExactlyInOrder(pairOf(NULL, 1));
        assertStream(Streamer.of(mapOf(1, 1))).containsExactlyInOrder(pairOf(1, 1));

        assertStream(Streamer.of(mapOf(1, 2, 3, 4))).containsExactlyInOrder(pairOf(1, 2), pairOf(3, 4));
        assertStream(Streamer.of(mapOf(1, 2, 3, 2))).containsExactlyInOrder(pairOf(1, 2), pairOf(3, 2));
    }

    @Test
    public void streamer_zip_simple() {
        assertStream(Streamer.zip(streamOf(), streamOf())).isEmpty();
        assertStream(Streamer.zip(streamOf(), NULL_STREAM)).isEmpty();
        assertStream(Streamer.zip(NULL_STREAM, streamOf())).isEmpty();
        assertStream(Streamer.zip(NULL_STREAM, NULL_STREAM)).isEmpty();
        assertStream(Streamer.zip(streamOf(1), streamOf())).isEmpty();
        assertStream(Streamer.zip(streamOf(), streamOf(2))).isEmpty();
        assertStream(Streamer.zip(streamOf(1), streamOf(2))).containsExactlyInOrder(pairOf(1, 2));
        assertStream(Streamer.zip(streamOf(1, 2), streamOf(3, 4))).containsExactlyInOrder(pairOf(1, 3), pairOf(2, 4));

        assertStream(Streamer.zip(iterableOf(), iterableOf())).isEmpty();
        assertStream(Streamer.zip(iterableOf(), NULL_ITERABLE)).isEmpty();
        assertStream(Streamer.zip(NULL_ITERABLE, iterableOf())).isEmpty();
        assertStream(Streamer.zip(NULL_ITERABLE, NULL_ITERABLE)).isEmpty();
        assertStream(Streamer.zip(iterableOf(1), iterableOf())).isEmpty();
        assertStream(Streamer.zip(iterableOf(), iterableOf(2))).isEmpty();
        assertStream(Streamer.zip(iterableOf(1), iterableOf(2))).containsExactlyInOrder(pairOf(1, 2));
        assertStream(Streamer.zip(iterableOf(1, 2), iterableOf(3, 4))).containsExactlyInOrder(pairOf(1, 3), pairOf(2, 4));

        assertStream(Streamer.zip(arrayOf(), arrayOf())).isEmpty();
        assertStream(Streamer.zip(arrayOf(), NULL_ARRAY)).isEmpty();
        assertStream(Streamer.zip(NULL_ARRAY, arrayOf())).isEmpty();
        assertStream(Streamer.zip(NULL_ARRAY, NULL_ARRAY)).isEmpty();
        assertStream(Streamer.zip(arrayOf(1), arrayOf())).isEmpty();
        assertStream(Streamer.zip(arrayOf(), arrayOf(2))).isEmpty();
        assertStream(Streamer.zip(arrayOf(1), arrayOf(2))).containsExactlyInOrder(pairOf(1, 2));
        assertStream(Streamer.zip(arrayOf(1, 2), arrayOf(3, 4))).containsExactlyInOrder(pairOf(1, 3), pairOf(2, 4));
    }

    @Test
    public void map_simple() {
        assertStream(Streamer.<Long>of().map(Long::intValue)).isEmpty();
        assertStream(Streamer.of(1L).map(Long::intValue)).containsExactlyInOrder(1);
        assertStream(Streamer.of(1L, 2L).map(Long::intValue)).containsExactlyInOrder(1, 2);
        assertStream(Streamer.of(1, 2, 3).map(String::valueOf)).containsExactlyInOrder("1", "2", "3");
        assertFailure(() -> Streamer.of(1L, 2L, null).map(Long::intValue).toArrayList()).throwsNPE();
    }

    @Test
    public void mapIfNonNull_simple() {
        Function<Integer, Integer> mapper = i -> i % 2 == 0 ? null : i;
        assertStream(Streamer.<Long>of().mapIfNonNull(Long::intValue)).isEmpty();
        assertStream(Streamer.of(1).mapIfNonNull(mapper)).containsExactlyInOrder(1);
        assertStream(Streamer.of(2).mapIfNonNull(mapper)).isEmpty();
        assertStream(Streamer.of(1, 2).mapIfNonNull(mapper)).containsExactlyInOrder(1);
        assertStream(Streamer.of(1, 2, 3, 4).mapIfNonNull(mapper)).containsExactlyInOrder(1, 3);
    }

    @Test
    public void mapIfPresent_simple() {
        Function<Integer, Optional<Integer>> mapper = i -> i % 2 == 0 ? Optional.empty() : Optional.of(i);
        assertStream(Streamer.<Integer>of().mapIfPresent(mapper)).isEmpty();
        assertStream(Streamer.of(1).mapIfPresent(mapper)).containsExactlyInOrder(1);
        assertStream(Streamer.of(2).mapIfPresent(mapper)).isEmpty();
        assertStream(Streamer.of(1, 2).mapIfPresent(mapper)).containsExactlyInOrder(1);
        assertStream(Streamer.of(1, 2, 3, 4).mapIfPresent(mapper)).containsExactlyInOrder(1, 3);
    }

    @Test
    public void mapIndex_simple() {
        assertStream(Streamer.<Long>of().mapIndex(Long::sum)).isEmpty();
        assertStream(Streamer.of(1L).mapIndex(Long::sum)).containsExactlyInOrder(1L);
        assertStream(Streamer.of(1L, 2L).mapIndex(Long::sum)).containsExactlyInOrder(1L, 3L);
        assertStream(Streamer.of(1L, 2L, 3L).mapIndex(Long::sum)).containsExactlyInOrder(1L, 3L, 5L);
    }

    @Test
    public void mapIndex_with_skip() {
        assertStream(Streamer.of(1L, 2L, 3L).skip(0).mapIndex(Long::sum)).containsExactlyInOrder(1L, 3L, 5L);
        assertStream(Streamer.of(1L, 2L, 3L).skip(1).mapIndex(Long::sum)).containsExactlyInOrder(2L, 4L);
        assertStream(Streamer.of(1L, 2L, 3L).skip(2).mapIndex(Long::sum)).containsExactlyInOrder(3L);
    }

    @Test
    public void flatMap_simple() {
        Function<Integer, Stream<Integer>> mapper = i -> BasicStreams.repeat(i, 3);
        assertStream(Streamer.<Integer>of().flatMap(mapper)).isEmpty();
        assertStream(Streamer.of(1).flatMap(mapper)).containsExactlyInOrder(1, 1, 1);
        assertStream(Streamer.of(NULL).flatMap(mapper)).containsExactlyInOrder(NULL, NULL, NULL);
        assertStream(Streamer.of(1, 2).flatMap(mapper)).containsExactlyInOrder(1, 1, 1, 2, 2, 2);
    }

    @Test
    public void flatMapIndex_simple() {
        IndexedFunction<Integer, Stream<Integer>> mapper = (val, idx) -> streamOf(-idx, val);
        assertStream(Streamer.<Integer>of().flatMapIndex(mapper)).isEmpty();
        assertStream(Streamer.of(1).flatMapIndex(mapper)).containsExactlyInOrder(0, 1);
        assertStream(Streamer.of(NULL).flatMapIndex(mapper)).containsExactlyInOrder(0, NULL);
        assertStream(Streamer.of(1, 2).flatMapIndex(mapper)).containsExactlyInOrder(0, 1, -1, 2);
    }

    @Test
    public void mapMulti_simple() {
        BiConsumer<Integer, Consumer<Integer>> mapper = (val, consumer) -> {
            consumer.accept(val);
            consumer.accept(2 * val);
        };
        assertStream(Streamer.<Integer>of().mapMulti(mapper)).isEmpty();
        assertStream(Streamer.of(1).mapMulti(mapper)).containsExactlyInOrder(1, 2);
        assertStream(Streamer.of(1, 5).mapMulti(mapper)).containsExactlyInOrder(1, 2, 5, 10);
    }

    @Test
    public void split_simple() {
        assertStream(Streamer.<Integer>of().split()).isEmpty();
        assertStream(Streamer.of(1).split()).containsExactlyInOrder(pairOf(1, 1));
        assertStream(Streamer.of(1, null).split()).containsExactlyInOrder(pairOf(1, 1), pairOf(null, null));
    }

    @Test
    public void split_joint_mapper() {
        Function<Integer, Entry<Integer, String>> mapper = i -> pairOf(i, String.valueOf(i));
        assertStream(Streamer.<Integer>of().split(mapper)).isEmpty();
        assertStream(Streamer.of(1).split(mapper)).containsExactlyInOrder(pairOf(1, "1"));
        assertStream(Streamer.of(1, 2).split(mapper)).containsExactlyInOrder(pairOf(1, "1"), pairOf(2, "2"));
    }

    @Test
    public void split_joint_mapper_null() {
        Function<Integer, Entry<Integer, String>> mapper = i -> pairOf(i, null);
        assertStream(Streamer.<Integer>of().split(mapper)).isEmpty();
        assertStream(Streamer.of(1).split(mapper)).containsExactlyInOrder(pairOf(1, null));
        assertStream(Streamer.of(1, 2).split(mapper)).containsExactlyInOrder(pairOf(1, null), pairOf(2, null));
    }

    @Test
    public void split_key_value_mappers() {
        assertStream(Streamer.<Integer>of().split(i -> i, String::valueOf)).isEmpty();
        assertStream(Streamer.of(1).split(i -> i, String::valueOf)).containsExactlyInOrder(pairOf(1, "1"));
        assertStream(Streamer.of(1, 2).split(i -> i, String::valueOf)).containsExactlyInOrder(pairOf(1, "1"), pairOf(2, "2"));
    }

    @Test
    public void flatSplit_joint_mapper() {
        Function<Long, Stream<Pair<Long, String>>> mapper = i -> streamOf(pairOf(i, String.valueOf(i)), pairOf(i + 1, null));
        assertStream(Streamer.<Long>of().flatSplit(mapper)).isEmpty();
        assertStream(Streamer.of(1L).flatSplit(mapper)).containsExactlyInOrder(pairOf(1L, "1"), pairOf(2L, null));
        assertStream(Streamer.of(1L, 2L).flatSplit(mapper)).containsExactlyInOrder(pairOf(1L, "1"), pairOf(2L, null),
                                                                                   pairOf(2L, "2"), pairOf(3L, null));
    }

    @Test
    public void flatSplit_key_value_mappers() {
        Function<Integer, Stream<Integer>> keyMap = i -> streamOf(i, i + 1);
        Function<Integer, Stream<String>> valMap = i -> streamOf(String.valueOf(i), null);
        assertStream(Streamer.<Integer>of().flatSplit(keyMap, valMap)).isEmpty();
        assertStream(Streamer.of(1).flatSplit(keyMap, valMap)).containsExactlyInOrder(pairOf(1, "1"), pairOf(2, null));
        assertStream(Streamer.of(1, 2).flatSplit(keyMap, valMap)).containsExactlyInOrder(pairOf(1, "1"), pairOf(2, null),
                                                                                         pairOf(2, "2"), pairOf(3, null));
    }

    @Test
    public void zipLeft_supplier() {
        assertStream(Streamer.<Integer>of().zipLeft(() -> 0)).isEmpty();
        assertStream(Streamer.of(1).zipLeft(() -> 0)).containsExactlyInOrder(pairOf(0, 1));
        assertStream(Streamer.of(1, 2).zipLeft(() -> 0)).containsExactlyInOrder(pairOf(0, 1), pairOf(0, 2));
    }

    @Test
    public void zipLeft_mapper() {
        assertStream(Streamer.<Integer>of().zipLeft(i -> 2 * i)).isEmpty();
        assertStream(Streamer.of(1).zipLeft(i -> 2 * i)).containsExactlyInOrder(pairOf(2, 1));
        assertStream(Streamer.of(1, 2).zipLeft(i -> 2 * i)).containsExactlyInOrder(pairOf(2, 1), pairOf(4, 2));
    }

    @Test
    public void flatZipLeft_mapper() {
        Function<Integer, Stream<Integer>> mapper = i -> streamOf(i, 2 * i);
        assertStream(Streamer.<Integer>of().flatZipLeft(mapper)).isEmpty();
        assertStream(Streamer.of(1).flatZipLeft(mapper)).containsExactlyInOrder(pairOf(1, 1), pairOf(2, 1));
        assertStream(Streamer.of(1, 2).flatZipLeft(mapper)).containsExactlyInOrder(pairOf(1, 1), pairOf(2, 1),
                                                                                   pairOf(2, 2), pairOf(4, 2));
    }

    @Test
    public void zipRight_supplier() {
        assertStream(Streamer.<Integer>of().zipRight(() -> 0)).isEmpty();
        assertStream(Streamer.of(1).zipRight(() -> 0)).containsExactlyInOrder(pairOf(1, 0));
        assertStream(Streamer.of(1, 2).zipRight(() -> 0)).containsExactlyInOrder(pairOf(1, 0), pairOf(2, 0));
    }

    @Test
    public void zipRight_mapper() {
        assertStream(Streamer.<Integer>of().zipRight(i -> 2 * i)).isEmpty();
        assertStream(Streamer.of(1).zipRight(i -> 2 * i)).containsExactlyInOrder(pairOf(1, 2));
        assertStream(Streamer.of(1, 2).zipRight(i -> 2 * i)).containsExactlyInOrder(pairOf(1, 2), pairOf(2, 4));
    }

    @Test
    public void flatZipRight_mapper() {
        Function<Integer, Stream<Integer>> mapper = i -> streamOf(i, 2 * i);
        assertStream(Streamer.<Integer>of().flatZipRight(mapper)).isEmpty();
        assertStream(Streamer.of(1).flatZipRight(mapper)).containsExactlyInOrder(pairOf(1, 1), pairOf(1, 2));
        assertStream(Streamer.of(1, 2).flatZipRight(mapper)).containsExactlyInOrder(pairOf(1, 1), pairOf(1, 2),
                                                                                    pairOf(2, 2), pairOf(2, 4));
    }
}
