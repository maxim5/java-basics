package io.spbx.util.collect.stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.TestingBasics.*;

@Tag("fast")
public class BasicStreamsTest {
    private static final Integer NULL = null;

    @Test
    public void stream_factory_empty_or_single() {
        assertThat(BasicStreams.empty()).isEmpty();
        assertThat(BasicStreams.single("1")).containsExactly("1");
        assertThat(BasicStreams.single(NULL)).containsExactly(NULL);
        assertThat(BasicStreams.streamOf()).isEmpty();
        assertThat(BasicStreams.streamOf("1")).containsExactly("1");
        assertThat(BasicStreams.streamOf(NULL)).containsExactly(NULL);
    }

    @Test
    public void streamOf_arrays_or_iterables() {
        assertThat(BasicStreams.streamOf(arrayOf())).isEmpty();
        assertThat(BasicStreams.streamOf(arrayOf(1))).containsExactly(1);
        assertThat(BasicStreams.streamOf(1, 2)).containsExactly(1, 2);
        assertThat(BasicStreams.streamOf(NULL, NULL)).containsExactly(null, null);

        assertThat(BasicStreams.streamOf(iterableOf())).isEmpty();
        assertThat(BasicStreams.streamOf(iterableOf(1))).containsExactly(1);
        assertThat(BasicStreams.streamOf(iterableOf(1, 2))).containsExactly(1, 2);
        assertThat(BasicStreams.streamOf(iterableOf(NULL, NULL))).containsExactly(null, null);
    }

    @Test
    public void streamOf_subarrays() {
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 0)).containsExactly(1, 2, 3);
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 1)).containsExactly(2, 3);
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 2)).containsExactly(3);
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 3)).isEmpty();
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 4)).isEmpty();
        assertFailure(() -> BasicStreams.streamOf(arrayOf(1, 2, 3), -1).toArray()).throwsIllegalArgument();

        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 0, 0)).isEmpty();
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 0, 1)).containsExactly(1);
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 0, 2)).containsExactly(1, 2);
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 0, 3)).containsExactly(1, 2, 3);
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 0, 4)).containsExactly(1, 2, 3);
        assertFailure(() -> BasicStreams.streamOf(arrayOf(1, 2, 3), 0, -1).toArray()).throwsIllegalArgument();

        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 1, 0)).isEmpty();
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 1, 1)).containsExactly(2);
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 1, 2)).containsExactly(2, 3);
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 1, 3)).containsExactly(2, 3);

        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 2, 0)).isEmpty();
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 2, 1)).containsExactly(3);
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 2, 2)).containsExactly(3);

        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 3, 0)).isEmpty();
        assertThat(BasicStreams.streamOf(arrayOf(1, 2, 3), 3, 1)).isEmpty();
    }

    @Test
    public void streamOf_iterators() {
        assertThat(BasicStreams.streamOf(iteratorOf())).isEmpty();
        assertThat(BasicStreams.streamOf(iteratorOf(1))).containsExactly(1);
        assertThat(BasicStreams.streamOf(iteratorOf(1, 2))).containsExactly(1, 2);
        assertThat(BasicStreams.streamOf(iteratorOf(NULL, NULL))).containsExactly(null, null);

        assertThat(BasicStreams.streamOf(spliteratorOf())).isEmpty();
        assertThat(BasicStreams.streamOf(spliteratorOf(1))).containsExactly(1);
        assertThat(BasicStreams.streamOf(spliteratorOf(1, 2))).containsExactly(1, 2);
        assertThat(BasicStreams.streamOf(spliteratorOf(NULL, NULL))).containsExactly(null, null);
    }

    @Test
    public void repeat_simple() {
        assertThat(BasicStreams.repeat("1", 0)).isEmpty();
        assertThat(BasicStreams.repeat("1", 1)).containsExactly("1");
        assertThat(BasicStreams.repeat("1", 2)).containsExactly("1", "1");
        assertThat(BasicStreams.repeat("1", 3)).containsExactly("1", "1", "1");
        assertThat(BasicStreams.repeat(NULL, 0)).isEmpty();
        assertThat(BasicStreams.repeat(NULL, 1)).containsExactly(NULL);
        assertThat(BasicStreams.repeat(NULL, 2)).containsExactly(NULL, NULL);
        assertThat(BasicStreams.repeat(NULL, 3)).containsExactly(NULL, NULL, NULL);
    }

    @Test
    public void emptyIfNull_simple() {
        assertThat(BasicStreams.emptyIfNull(NULL_STREAM)).isEmpty();
        assertThat(BasicStreams.emptyIfNull(Stream.of())).isEmpty();
        assertThat(BasicStreams.emptyIfNull(Stream.of(1))).containsExactly(1);
    }

    @Test
    public void emptyIfNull_ints() {
        assertThat(BasicStreams.emptyIfNull(NULL_INT_STREAM)).isEmpty();
        assertThat(BasicStreams.emptyIfNull(IntStream.of())).isEmpty();
        assertThat(BasicStreams.emptyIfNull(IntStream.of(1))).containsExactly(1);
    }

    @Test
    public void emptyIfNull_longs() {
        assertThat(BasicStreams.emptyIfNull(NULL_LONG_STREAM)).isEmpty();
        assertThat(BasicStreams.emptyIfNull(LongStream.of())).isEmpty();
        assertThat(BasicStreams.emptyIfNull(LongStream.of(1L))).containsExactly(1L);
    }

    @Test
    public void concat_two_streams() {
        assertThat(BasicStreams.concat(streamOf(), streamOf())).isEmpty();
        assertThat(BasicStreams.concat(streamOf(), NULL_STREAM)).isEmpty();
        assertThat(BasicStreams.concat(NULL_STREAM, streamOf())).isEmpty();
        assertThat(BasicStreams.concat(NULL_STREAM, NULL_STREAM)).isEmpty();

        assertThat(BasicStreams.concat(streamOf(1), streamOf(2))).containsExactly(1, 2);
        assertThat(BasicStreams.concat(streamOf(1), NULL_STREAM)).containsExactly(1);
        assertThat(BasicStreams.concat(NULL_STREAM, streamOf(1))).containsExactly(1);
    }

    @Test
    public void concat_two_iterables() {
        assertThat(BasicStreams.concat(iterableOf(), iterableOf())).isEmpty();
        assertThat(BasicStreams.concat(iterableOf(), NULL_ITERABLE)).isEmpty();
        assertThat(BasicStreams.concat(NULL_ITERABLE, iterableOf())).isEmpty();
        assertThat(BasicStreams.concat(NULL_ITERABLE, NULL_ITERABLE)).isEmpty();

        assertThat(BasicStreams.concat(iterableOf(1), iterableOf(2))).containsExactly(1, 2);
        assertThat(BasicStreams.concat(iterableOf(1), NULL_ITERABLE)).containsExactly(1);
        assertThat(BasicStreams.concat(NULL_ITERABLE, iterableOf(1))).containsExactly(1);
    }

    @Test
    public void concat_two_arrays() {
        assertThat(BasicStreams.concat(arrayOf(), arrayOf())).isEmpty();
        assertThat(BasicStreams.concat(arrayOf(), NULL_ARRAY)).isEmpty();
        assertThat(BasicStreams.concat(NULL_ARRAY, arrayOf())).isEmpty();
        assertThat(BasicStreams.concat(NULL_ARRAY, NULL_ARRAY)).isEmpty();

        assertThat(BasicStreams.concat(arrayOf(1), arrayOf(2))).containsExactly(1, 2);
        assertThat(BasicStreams.concat(arrayOf(1), NULL_ARRAY)).containsExactly(1);
        assertThat(BasicStreams.concat(NULL_ARRAY, arrayOf(1))).containsExactly(1);
    }

    @Test
    public void concat_three_streams() {
        assertThat(BasicStreams.concat(streamOf(), streamOf(), streamOf())).isEmpty();
        assertThat(BasicStreams.concat(streamOf(), NULL_STREAM, NULL_STREAM)).isEmpty();
        assertThat(BasicStreams.concat(NULL_STREAM, streamOf(), NULL_STREAM)).isEmpty();
        assertThat(BasicStreams.concat(NULL_STREAM, NULL_STREAM, streamOf())).isEmpty();
        assertThat(BasicStreams.concat(NULL_STREAM, NULL_STREAM, NULL_STREAM)).isEmpty();

        assertThat(BasicStreams.concat(streamOf(1), streamOf(2), streamOf(3))).containsExactly(1, 2, 3);
        assertThat(BasicStreams.concat(streamOf(1), NULL_STREAM, NULL_STREAM)).containsExactly(1);
        assertThat(BasicStreams.concat(NULL_STREAM, streamOf(1), NULL_STREAM)).containsExactly(1);
        assertThat(BasicStreams.concat(NULL_STREAM, NULL_STREAM, streamOf(1))).containsExactly(1);
    }

    @Test
    public void concat_three_iterables() {
        assertThat(BasicStreams.concat(iterableOf(), iterableOf(), iterableOf())).isEmpty();
        assertThat(BasicStreams.concat(iterableOf(), NULL_ITERABLE, NULL_ITERABLE)).isEmpty();
        assertThat(BasicStreams.concat(NULL_ITERABLE, iterableOf(), NULL_ITERABLE)).isEmpty();
        assertThat(BasicStreams.concat(NULL_ITERABLE, NULL_ITERABLE, iterableOf())).isEmpty();
        assertThat(BasicStreams.concat(NULL_ITERABLE, NULL_ITERABLE, NULL_ITERABLE)).isEmpty();

        assertThat(BasicStreams.concat(iterableOf(1), iterableOf(2), iterableOf(3))).containsExactly(1, 2, 3);
        assertThat(BasicStreams.concat(iterableOf(1), NULL_ITERABLE, NULL_ITERABLE)).containsExactly(1);
        assertThat(BasicStreams.concat(NULL_ITERABLE, iterableOf(1), NULL_ITERABLE)).containsExactly(1);
        assertThat(BasicStreams.concat(NULL_ITERABLE, NULL_ITERABLE, iterableOf(1))).containsExactly(1);
    }

    @Test
    public void concat_three_arrays() {
        assertThat(BasicStreams.concat(arrayOf(), arrayOf(), arrayOf())).isEmpty();
        assertThat(BasicStreams.concat(arrayOf(), NULL_ARRAY, NULL_ARRAY)).isEmpty();
        assertThat(BasicStreams.concat(NULL_ARRAY, arrayOf(), NULL_ARRAY)).isEmpty();
        assertThat(BasicStreams.concat(NULL_ARRAY, NULL_ARRAY, arrayOf())).isEmpty();
        assertThat(BasicStreams.concat(NULL_ARRAY, NULL_ARRAY, NULL_ARRAY)).isEmpty();

        assertThat(BasicStreams.concat(arrayOf(1), arrayOf(2), arrayOf(3))).containsExactly(1, 2, 3);
        assertThat(BasicStreams.concat(arrayOf(1), NULL_ARRAY, NULL_ARRAY)).containsExactly(1);
        assertThat(BasicStreams.concat(NULL_ARRAY, arrayOf(1), NULL_ARRAY)).containsExactly(1);
        assertThat(BasicStreams.concat(NULL_ARRAY, NULL_ARRAY, arrayOf(1))).containsExactly(1);
    }

    @Test
    public void prependToStream_stream() {
        assertThat(BasicStreams.prependToStream(1, streamOf())).containsExactly(1);
        assertThat(BasicStreams.prependToStream(1, streamOf(2))).containsExactly(1, 2);
        assertThat(BasicStreams.prependToStream(1, streamOf(2, 3))).containsExactly(1, 2, 3);
    }

    @Test
    public void prependToStream_iterable() {
        assertThat(BasicStreams.prependToStream(1, iterableOf())).containsExactly(1);
        assertThat(BasicStreams.prependToStream(1, iterableOf(2))).containsExactly(1, 2);
        assertThat(BasicStreams.prependToStream(1, iterableOf(2, 3))).containsExactly(1, 2, 3);
    }

    @Test
    public void appendToStream_stream() {
        assertThat(BasicStreams.appendToStream(streamOf(), 1)).containsExactly(1);
        assertThat(BasicStreams.appendToStream(streamOf(1), 2)).containsExactly(1, 2);
        assertThat(BasicStreams.appendToStream(streamOf(1, 2), 3)).containsExactly(1, 2, 3);
    }

    @Test
    public void appendToStream_iterable() {
        assertThat(BasicStreams.appendToStream(iterableOf(), 1)).containsExactly(1);
        assertThat(BasicStreams.appendToStream(iterableOf(1), 2)).containsExactly(1, 2);
        assertThat(BasicStreams.appendToStream(iterableOf(1, 2), 3)).containsExactly(1, 2, 3);
    }

    @Test
    public void zip_two_streams_with_function() {
        assertThat(BasicStreams.zip(streamOf(), streamOf(), Integer::sum)).isEmpty();
        assertThat(BasicStreams.zip(streamOf(1), streamOf(), Integer::sum)).isEmpty();
        assertThat(BasicStreams.zip(streamOf(), streamOf(1), Integer::sum)).isEmpty();

        assertThat(BasicStreams.zip(NULL_STREAM, NULL_STREAM, Integer::sum)).isEmpty();
        assertThat(BasicStreams.zip(streamOf(1), NULL_STREAM, Integer::sum)).isEmpty();
        assertThat(BasicStreams.zip(NULL_STREAM, streamOf(1), Integer::sum)).isEmpty();

        assertThat(BasicStreams.zip(streamOf(1, 777), streamOf(2), Integer::sum)).containsExactly(3);
        assertThat(BasicStreams.zip(streamOf(1), streamOf(2, 777), Integer::sum)).containsExactly(3);

        assertThat(BasicStreams.zip(streamOf(1), streamOf("a"), (i, s) -> i + s)).containsExactly("1a");
        assertThat(BasicStreams.zip(streamOf(1, 2), streamOf("a", "b"), (i, s) -> i + s)).containsExactly("1a", "2b");

        assertThat(BasicStreams.zip(streamOf(1), streamOf(2), (i, j) -> NULL)).containsExactly(NULL);
        assertThat(BasicStreams.zip(streamOf(NULL), streamOf(NULL), (i, j) -> NULL)).containsExactly(NULL);
    }

    @Test
    public void zip_two_streams_into_pairs() {
        assertThat(BasicStreams.zip(streamOf(), streamOf())).isEmpty();
        assertThat(BasicStreams.zip(streamOf(1), streamOf())).isEmpty();
        assertThat(BasicStreams.zip(streamOf(), streamOf(1))).isEmpty();

        assertThat(BasicStreams.zip(NULL_STREAM, NULL_STREAM)).isEmpty();
        assertThat(BasicStreams.zip(streamOf(1), NULL_STREAM)).isEmpty();
        assertThat(BasicStreams.zip(NULL_STREAM, streamOf(1))).isEmpty();

        assertThat(BasicStreams.zip(streamOf(1, 777), streamOf(2))).containsExactly(pairOf(1, 2));
        assertThat(BasicStreams.zip(streamOf(1), streamOf(2, 777))).containsExactly(pairOf(1, 2));

        assertThat(BasicStreams.zip(streamOf(1), streamOf("a"))).containsExactly(pairOf(1, "a"));
        assertThat(BasicStreams.zip(streamOf(1, 2), streamOf("a", "b"))).containsExactly(pairOf(1, "a"), pairOf(2, "b"));
    }
}
