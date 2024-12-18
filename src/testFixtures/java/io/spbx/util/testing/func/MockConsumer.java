package io.spbx.util.testing.func;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.func.ThrowConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.lang.EasyCast.castAny;
import static io.spbx.util.testing.MoreTruth.assertThat;

/**
 * A {@link Consumer} and {@link ThrowConsumer} implementation that allows to set up expectations
 * (how many times calls, what args, etc.) as well as track that all consumers are called up until the end.
 *
 * @param <T> type of items accepted by the consumer
 * @param <E> exception to throw if a mock for {@link ThrowConsumer} is necessary.
 */
public class MockConsumer<T, E extends Throwable> implements Consumer<T>, ThrowConsumer<T, E>, IntConsumer, LongConsumer, AutoCloseable {
    private final List<T> items = new ArrayList<>();
    private List<T> expected;

    protected MockConsumer() {
        if (!Tracker.trackers.isEmpty()) {
            Tracker.trackers.peek().add(this);
        }
    }

    @CheckReturnValue
    public static <T> MockConsumer<T, RuntimeException> mock() {
        return new MockConsumer<>();
    }

    @CheckReturnValue
    public static @SafeVarargs <T> MockConsumer<T, RuntimeException> expecting(@Nullable T @NotNull ... expected) {
        return new MockConsumer<T, RuntimeException>().expect(expected);
    }

    /**
     * To be used for {@code ThrowConsumer} mocks.
     */
    public static class Throw {
        @CheckReturnValue
        public static <T, E extends Throwable> MockConsumer<T, E> mock() {
            return new MockConsumer<>();
        }
        @CheckReturnValue
        public static @SafeVarargs <T, E extends Throwable> MockConsumer<T, E> expecting(@Nullable T @NotNull ... expected) {
            return new MockConsumer<T, E>().expect(expected);
        }
    }

    public final @SafeVarargs @NotNull MockConsumer<T, E> expect(@Nullable T @NotNull ... expected) {
        this.expected = Arrays.asList(expected);
        return this;
    }

    @Override
    public void accept(T item) {
        if (expected != null) {
            assertThat(expected.size() > items.size()).withMessage("Unexpected item added: %s", item).isTrue();
            assertThat(item).isEqualTo(expected.get(items.size()));
        }
        items.add(item);
    }

    @Override
    public void accept(int value) {
        accept(castAny(value));
    }

    @Override
    public void accept(long value) {
        accept(castAny(value));
    }

    public @NotNull List<T> argsCalled() {
        return items;
    }

    public int timesCalled() {
        return items.size();
    }

    public void assertAllDone() {
        if (expected != null) {
            assertThat(items).containsExactlyElementsIn(expected);
        }
    }

    @Override
    public void close() {
        assertAllDone();
    }

    public static @NotNull MockConsumer.Tracker trackAllConsumersDone() {
        return new Tracker();
    }

    public static class Tracker implements AutoCloseable {
        private static final Stack<Tracker> trackers = new Stack<>();
        private final List<MockConsumer<?, ?>> mocks = new ArrayList<>();

        protected Tracker() {
            trackers.push(this);
        }

        @Override public void close() {
            mocks.forEach(MockConsumer::close);
            trackers.pop();
        }

        public void add(@NotNull MockConsumer<?, ?> mockConsumer) {
            mocks.add(mockConsumer);
        }
    }
}
