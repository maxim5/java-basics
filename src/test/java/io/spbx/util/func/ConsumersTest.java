package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class ConsumersTest {
    @Test
    public void fan_out_two_consumers() {
        MockConsumer<Integer, RuntimeException> mock1 = MockConsumer.mock();
        MockConsumer<Integer, RuntimeException> mock2 = MockConsumer.mock();
        Consumer<Integer> consumer = Consumers.fanOut(mock1, mock2);
        consumer.accept(7);
        assertThat(mock1.argsCalled()).containsExactly(7);
        assertThat(mock2.argsCalled()).containsExactly(7);
    }

    @Test
    public void fan_out_three_consumers() {
        MockConsumer<Integer, RuntimeException> mock1 = MockConsumer.mock();
        MockConsumer<Integer, RuntimeException> mock2 = MockConsumer.mock();
        MockConsumer<Integer, RuntimeException> mock3 = MockConsumer.mock();
        Consumer<Integer> consumer = Consumers.fanOut(mock1, mock2, mock3);
        consumer.accept(7);
        assertThat(mock1.argsCalled()).containsExactly(7);
        assertThat(mock2.argsCalled()).containsExactly(7);
        assertThat(mock3.argsCalled()).containsExactly(7);
    }

    @Test
    public void chain_predicate_and_consumer() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        Consumer<String> chain = Consumers.chain("foo"::endsWith, mock);
        chain.accept("bar");
        assertThat(mock.argsCalled()).isEmpty();
        chain.accept("foo");
        assertThat(mock.argsCalled()).containsExactly("foo");
    }
}
