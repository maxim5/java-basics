package io.spbx.util.collect.stream;

import io.spbx.util.base.tuple.Pair;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static io.spbx.util.testing.AssertStreamer.assertStream;
import static io.spbx.util.testing.TestingBasics.pairOf;
import static io.spbx.util.testing.TestingBasics.streamOf;

@Tag("fast")
public class BiStreamerTest {
    private static final Integer NULL = null;

    @Test
    public void map_simple() {
        assertStream(Streamer.Bi.of().map(Objects::toString, Objects::toString)).isEmpty();
        assertStream(Streamer.Bi.of(pairOf(1, NULL)).map(k -> k + 1, Objects::toString))
            .containsExactlyInOrder(pairOf(2, "null"));
        assertStream(Streamer.Bi.of(pairOf(1, 11), pairOf(2, 22)).map(k -> k + 1, Objects::toString))
            .containsExactlyInOrder(pairOf(2, "11"), pairOf(3, "22"));

        assertStream(Streamer.Bi.of().map(Pair::swap)).isEmpty();
        assertStream(Streamer.Bi.of(pairOf(1, NULL)).map(Pair::swap)).containsExactlyInOrder(pairOf(NULL, 1));
        assertStream(Streamer.Bi.of(pairOf(1, 11), pairOf(2, 22)).map(Pair::swap))
            .containsExactlyInOrder(pairOf(11, 1), pairOf(22, 2));

        assertStream(Streamer.Bi.of().map((k, v) -> Pair.of(v, k))).isEmpty();
        assertStream(Streamer.Bi.of(pairOf(1, NULL)).map((k, v) -> Pair.of(v, k))).containsExactlyInOrder(pairOf(NULL, 1));
        assertStream(Streamer.Bi.of(pairOf(1, 11), pairOf(2, 22)).map((k, v) -> Pair.of(v, k)))
            .containsExactlyInOrder(pairOf(11, 1), pairOf(22, 2));
    }

    @Test
    public void flatMap_simple() {
        assertStream(Streamer.Bi.of().flatMap(p -> BasicStreams.repeat(p, 2))).isEmpty();
        assertStream(Streamer.Bi.of(pairOf(1, 2)).flatMap(p -> BasicStreams.repeat(p, 2)))
            .containsExactlyInOrder(pairOf(1, 2), pairOf(1, 2));
        assertStream(Streamer.Bi.of(pairOf(NULL, NULL)).flatMap(p -> BasicStreams.repeat(p, 2)))
            .containsExactlyInOrder(pairOf(NULL, NULL), pairOf(NULL, NULL));

        assertStream(Streamer.Bi.of().flatMap((k, v) -> streamOf(pairOf(k, v), pairOf(v, k)))).isEmpty();
        assertStream(Streamer.Bi.of(pairOf(1, 2)).flatMap((k, v) -> streamOf(pairOf(k, v), pairOf(v, k))))
            .containsExactlyInOrder(pairOf(1, 2), pairOf(2, 1));
        assertStream(Streamer.Bi.of(pairOf(1, NULL)).flatMap((k, v) -> streamOf(pairOf(k, v), pairOf(v, k))))
            .containsExactlyInOrder(pairOf(1, NULL), pairOf(NULL, 1));

        assertStream(Streamer.Bi.of().flatMap(k -> streamOf(k, NULL), v -> streamOf(NULL, v))).isEmpty();
        assertStream(Streamer.Bi.of(pairOf(1, 2)).flatMap(k -> streamOf(k, NULL), v -> streamOf(NULL, v)))
            .containsExactlyInOrder(pairOf(1, NULL), pairOf(NULL, 2));
        assertStream(Streamer.Bi.of(pairOf(1, NULL)).flatMap(k -> streamOf(k, NULL), v -> streamOf(NULL, v)))
            .containsExactlyInOrder(pairOf(1, NULL), pairOf(NULL, NULL));
    }

    @Test
    public void mapKeys_simple() {
        assertStream(Streamer.Bi.of().mapKeys(Object::toString)).isEmpty();
        assertStream(Streamer.Bi.of(pairOf(1, NULL)).mapKeys(k -> k + 1)).containsExactlyInOrder(pairOf(2, NULL));
        assertStream(Streamer.Bi.of(pairOf(1, 11), pairOf(2, 22)).mapKeys(k -> k + 1))
            .containsExactlyInOrder(pairOf(2, 11), pairOf(3, 22));

        assertStream(Streamer.Bi.of().mapKeys(Objects::equals)).isEmpty();
        assertStream(Streamer.Bi.of(pairOf(1, 11)).mapKeys(Objects::equals)).containsExactlyInOrder(pairOf(false, 11));
        assertStream(Streamer.Bi.of(pairOf(1, 1), pairOf(2, 22)).mapKeys(Objects::equals))
            .containsExactlyInOrder(pairOf(true, 1), pairOf(false, 22));
    }

    @Test
    public void flatMapKeys_simple() {
        assertStream(Streamer.Bi.of().flatMapKeys(k -> streamOf(1, 2, 3))).isEmpty();
        assertStream(Streamer.Bi.of(pairOf(1, 11)).flatMapKeys(k -> streamOf(k, -k)))
            .containsExactlyInOrder(pairOf(1, 11), pairOf(-1, 11));
        assertStream(Streamer.Bi.of(pairOf(1, 11), pairOf(2, 22)).flatMapKeys(k -> streamOf(k, -k)))
            .containsExactlyInOrder(pairOf(1, 11), pairOf(-1, 11), pairOf(2, 22), pairOf(-2, 22));
    }

    @Test
    public void flatMapKeys_duplicates() {
        assertStream(Streamer.Bi.of(pairOf(1, 11)).flatMapKeys(k -> streamOf(k, k)))
            .containsExactlyInOrder(pairOf(1, 11), pairOf(1, 11));
    }
}
