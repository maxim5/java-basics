package io.spbx.util.func;

import io.spbx.util.testing.func.MockConsumer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class ConsumersTest {
    /** {@link Consumers#fanOut(Consumer, Consumer)} **/

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
    @SuppressWarnings({ "resource", "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void fan_out_two_consumers_lambda() {
        MockConsumer<Integer, RuntimeException> mock1 = MockConsumer.mock();
        MockConsumer<Integer, RuntimeException> mock2 = MockConsumer.mock();
        Consumer<Integer> consumer = Consumers.fanOut(i -> mock1.accept(i), j -> mock2.accept(j));
        consumer.accept(7);
        assertThat(mock1.argsCalled()).containsExactly(7);
        assertThat(mock2.argsCalled()).containsExactly(7);
    }

    /** {@link Consumers#fanOut(Consumer, Consumer, Consumer)} **/

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
    @SuppressWarnings({ "resource", "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void fan_out_three_consumers_lambda() {
        MockConsumer<Integer, RuntimeException> mock1 = MockConsumer.mock();
        MockConsumer<Integer, RuntimeException> mock2 = MockConsumer.mock();
        MockConsumer<Integer, RuntimeException> mock3 = MockConsumer.mock();
        Consumer<Integer> consumer = Consumers.fanOut(i -> mock1.accept(i), j -> mock2.accept(j), k -> mock3.accept(k));
        consumer.accept(7);
        assertThat(mock1.argsCalled()).containsExactly(7);
        assertThat(mock2.argsCalled()).containsExactly(7);
        assertThat(mock3.argsCalled()).containsExactly(7);
    }

    /** {@link Consumers#conditional(Predicate, Consumer)}  **/

    @Test
    public void conditional_predicate_and_consumer_reference() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        Consumer<String> chain = Consumers.conditional("foo"::endsWith, mock);
        chain.accept("bar");
        assertThat(mock.argsCalled()).isEmpty();
        chain.accept("foo");
        assertThat(mock.argsCalled()).containsExactly("foo");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void conditional_predicate_and_consumer_lambda() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        Consumer<String> chain = Consumers.conditional(s -> "foo".endsWith(s), mock);
        chain.accept("bar");
        assertThat(mock.argsCalled()).isEmpty();
        chain.accept("foo");
        assertThat(mock.argsCalled()).containsExactly("foo");
    }

    /** {@link Consumers#chain(Function, Consumer)} **/

    @Test
    public void chain_function_and_consumer_reference() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        Consumer<String> chain = Consumers.chain(String::toUpperCase, mock);
        chain.accept("foo");
        assertThat(mock.argsCalled()).containsExactly("FOO");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_function_and_consumer_lambda() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        Consumer<String> chain = Consumers.chain(s -> s.toUpperCase(), mock);
        chain.accept("foo");
        assertThat(mock.argsCalled()).containsExactly("FOO");
    }
}
