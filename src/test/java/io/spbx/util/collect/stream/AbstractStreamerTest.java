package io.spbx.util.collect.stream;

import io.spbx.util.testing.func.MockConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.AssertStreamer.assertStream;
import static io.spbx.util.testing.TestingBasics.streamOf;
import static io.spbx.util.testing.TestingPrimitives.doubles;

@Tag("fast")
public class AbstractStreamerTest {
    private static final Integer NULL = null;

    @Test
    public void mapToInt_simple() {
        assertThat(AStreamer.<String>of().mapToInt(Integer::parseInt)).isEmpty();
        assertThat(AStreamer.of("1").mapToInt(Integer::parseInt)).containsExactly(1).inOrder();
        assertThat(AStreamer.of("1", "2", "3").mapToInt(Integer::parseInt)).containsExactly(1, 2, 3).inOrder();
    }

    @Test
    public void mapToLong_simple() {
        assertThat(AStreamer.<String>of().mapToLong(Long::parseLong)).isEmpty();
        assertThat(AStreamer.of("1").mapToLong(Long::parseLong)).containsExactly(1L).inOrder();
        assertThat(AStreamer.of("1", "2", "3").mapToLong(Long::parseLong)).containsExactly(1L, 2L, 3L).inOrder();
    }

    @Test
    public void mapToDouble_simple() {
        // No DoubleStreamSubject in Truth
        assertThat(AStreamer.<String>of().mapToDouble(Double::parseDouble).toArray()).isEmpty();
        assertThat(AStreamer.of("1").mapToDouble(Double::parseDouble).toArray()).isEqualTo(doubles(1));
        assertThat(AStreamer.of("1", "2", "3").mapToDouble(Double::parseDouble).toArray()).isEqualTo(doubles(1, 2, 3));
    }

    @Test
    public void flatMapToInt_simple() {
        Function<String, IntStream> mapper = s -> streamOf(s.split("_")).mapToInt(Integer::parseInt);
        assertThat(AStreamer.<String>of().flatMapToInt(mapper)).isEmpty();
        assertThat(AStreamer.of("1").flatMapToInt(mapper)).containsExactly(1).inOrder();
        assertThat(AStreamer.of("1_2").flatMapToInt(mapper)).containsExactly(1, 2).inOrder();
        assertThat(AStreamer.of("1_2", "3").flatMapToInt(mapper)).containsExactly(1, 2, 3).inOrder();
        assertThat(AStreamer.of("1_2_3", "4_5").flatMapToInt(mapper)).containsExactly(1, 2, 3, 4, 5).inOrder();
        assertThat(AStreamer.of("1_2_3", "4_5", "6").flatMapToInt(mapper)).containsExactly(1, 2, 3, 4, 5, 6).inOrder();
    }

    @Test
    public void flatMapToLong_simple() {
        Function<String, LongStream> mapper = s -> streamOf(s.split("_")).mapToLong(Long::parseLong);
        assertThat(AStreamer.<String>of().flatMapToLong(mapper)).isEmpty();
        assertThat(AStreamer.of("1").flatMapToLong(mapper)).containsExactly(1).inOrder();
        assertThat(AStreamer.of("1_2").flatMapToLong(mapper)).containsExactly(1, 2).inOrder();
        assertThat(AStreamer.of("1_2", "3").flatMapToLong(mapper)).containsExactly(1, 2, 3).inOrder();
        assertThat(AStreamer.of("1_2_3", "4_5").flatMapToLong(mapper)).containsExactly(1, 2, 3, 4, 5).inOrder();
        assertThat(AStreamer.of("1_2_3", "4_5", "6").flatMapToLong(mapper)).containsExactly(1, 2, 3, 4, 5, 6).inOrder();
    }

    @Test
    public void flatMapToDouble_simple() {
        // No DoubleStreamSubject in Truth
        Function<String, DoubleStream> mapper = s -> streamOf(s.split("_")).mapToDouble(Double::parseDouble);
        assertThat(AStreamer.<String>of().flatMapToDouble(mapper).toArray()).isEmpty();
        assertThat(AStreamer.of("1").flatMapToDouble(mapper).toArray()).isEqualTo(doubles(1));
        assertThat(AStreamer.of("1_2").flatMapToDouble(mapper).toArray()).isEqualTo(doubles(1, 2));
        assertThat(AStreamer.of("1_2", "3").flatMapToDouble(mapper).toArray()).isEqualTo(doubles(1, 2, 3));
        assertThat(AStreamer.of("1_2_3", "4_5").flatMapToDouble(mapper).toArray()).isEqualTo(doubles(1, 2, 3, 4, 5));
        assertThat(AStreamer.of("1_2_3", "4_5", "6").flatMapToDouble(mapper).toArray()).isEqualTo(doubles(1, 2, 3, 4, 5, 6));
    }

    @Test
    public void filter_simple() {
        assertStream(AStreamer.<Integer>of().filter(x -> x >= 0)).isEmpty();
        assertStream(AStreamer.of(0).filter(x -> x >= 0)).containsExactlyInOrder(0);
        assertStream(AStreamer.of(1).filter(x -> x >= 0)).containsExactlyInOrder(1);
        assertStream(AStreamer.of(1, 0, -1, 2).filter(x -> x >= 0)).containsExactlyInOrder(1, 0, 2);
        assertStream(AStreamer.of(-1, -2).filter(x -> x >= 0)).isEmpty();
        assertFailure(() -> Streamer.of(1, null).filter(x -> x >= 0).toArrayList()).throwsNPE();
    }

    @Test
    public void skipIf_simple() {
        assertStream(AStreamer.<Integer>of().skipIf(x -> x >= 0)).isEmpty();
        assertStream(AStreamer.of(0).skipIf(x -> x >= 0)).isEmpty();
        assertStream(AStreamer.of(-1).skipIf(x -> x >= 0)).containsExactlyInOrder(-1);
        assertStream(AStreamer.of(1, 0, -1, 2).skipIf(x -> x >= 0)).containsExactlyInOrder(-1);
        assertStream(AStreamer.of(-1, -2).skipIf(x -> x >= 0)).containsExactlyInOrder(-1, -2);
        assertFailure(() -> Streamer.of(1, null).skipIf(x -> x >= 0).toArrayList()).throwsNPE();
    }

    @Test
    public void skipNulls_simple() {
        assertStream(AStreamer.of().skipNulls()).isEmpty();
        assertStream(AStreamer.of(1).skipNulls()).containsExactlyInOrder(1);
        assertStream(AStreamer.of(1, 2).skipNulls()).containsExactlyInOrder(1, 2);
        assertStream(AStreamer.of(1, 2, 3, null).skipNulls()).containsExactlyInOrder(1, 2, 3);
        assertStream(AStreamer.of(null, null).skipNulls()).isEmpty();
    }

    @Test
    public void skip_simple() {
        assertStream(AStreamer.of().skip(1)).isEmpty();
        assertStream(AStreamer.of(1).skip(1)).isEmpty();
        assertStream(AStreamer.of(1, 2).skip(1)).containsExactlyInOrder(2);
        assertStream(AStreamer.of(1, 2, 3, null).skip(1)).containsExactlyInOrder(2, 3, null);
        assertStream(AStreamer.of(NULL).skip(1)).isEmpty();
        assertStream(AStreamer.of(null, null).skip(1)).containsExactlyInOrder(NULL);
    }

    @Test
    public void limit_simple() {
        assertStream(AStreamer.of().limit(2)).isEmpty();
        assertStream(AStreamer.of(1).limit(2)).containsExactlyInOrder(1);
        assertStream(AStreamer.of(1, 2).limit(2)).containsExactlyInOrder(1, 2);
        assertStream(AStreamer.of(1, 2, 3, null).limit(2)).containsExactlyInOrder(1, 2);
        assertStream(AStreamer.of(null, null).limit(2)).containsExactlyInOrder(null, null);
    }

    @Test
    public void distinct_simple() {
        assertStream(AStreamer.of().distinct()).isEmpty();
        assertStream(AStreamer.of(1).distinct()).containsExactlyInOrder(1);
        assertStream(AStreamer.of(1, 1, 2).distinct()).containsExactlyInOrder(1, 2);
        assertStream(AStreamer.of(1, 1, 1, null).distinct()).containsExactlyInOrder(1, null);
        assertStream(AStreamer.of(null, null, null).distinct()).containsExactlyInOrder(NULL);
    }

    @Test
    public void sorted_simple() {
        assertStream(AStreamer.of().sorted()).isEmpty();
        assertStream(AStreamer.of(1).sorted()).containsExactlyInOrder(1);
        assertStream(AStreamer.of(1, 2).sorted()).containsExactlyInOrder(1, 2);
        assertStream(AStreamer.of(2, 1).sorted()).containsExactlyInOrder(1, 2);
        assertStream(AStreamer.of(1, 2, 1).sorted()).containsExactlyInOrder(1, 1, 2);
        assertFailure(() -> AStreamer.of(1, null).sorted().close()).throwsNPE();
    }

    @Test
    public void sorted_comparator() {
        assertStream(AStreamer.<Integer>of().sorted(Integer::compare)).isEmpty();
        assertStream(AStreamer.of(1).sorted(Integer::compare)).containsExactlyInOrder(1);
        assertStream(AStreamer.of(1, 2).sorted(Integer::compare)).containsExactlyInOrder(1, 2);
        assertStream(AStreamer.of(2, 1).sorted(Integer::compare)).containsExactlyInOrder(1, 2);
        assertStream(AStreamer.of(1, 2, 1).sorted(Integer::compare)).containsExactlyInOrder(1, 1, 2);
        assertFailure(() -> AStreamer.of(1, null).sorted(Integer::compare).close()).throwsNPE();
    }

    @Test
    public void peek_simple() {
        try (MockConsumer.Tracker ignored = MockConsumer.trackAllConsumersDone()) {
            assertStream(AStreamer.of().peek(MockConsumer.expecting())).isEmpty();
            assertStream(AStreamer.of(1).peek(MockConsumer.expecting(1))).containsExactlyInOrder(1);
            assertStream(AStreamer.of(1, 2).peek(MockConsumer.expecting(1, 2))).containsExactlyInOrder(1, 2);
            assertStream(AStreamer.of(1, null).peek(MockConsumer.expecting(1, null))).containsExactlyInOrder(1, null);
            assertStream(AStreamer.of(null, null).peek(MockConsumer.expecting(null, null))).containsExactlyInOrder(null, null);
        }
    }

    private static class AStreamer<E> extends AbstractStreamer<E, AStreamer<E>> implements AutoCloseable {
        AStreamer(@NotNull Stream<E> stream) {
            super(stream);
        }

        @Override @NotNull AStreamer<E> create(@NotNull Stream<E> stream) {
            return new AStreamer<>(stream);
        }

        public static @SafeVarargs <E> @NotNull AStreamer<E> of(@Nullable E @NotNull ... items) {
            return new AStreamer<>(streamOf(items));
        }

        @Override
        public void close() {
            List<E> ignored = stream.toList();
        }
    }
}
