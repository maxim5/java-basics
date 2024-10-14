package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;
import java.util.function.ToLongFunction;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.TestingFunctions.isPositive;
import static io.spbx.util.func.TestingFunctions.negate;

@Tag("fast")
public class LongConsumersTest {
    /** {@link LongConsumers#conditional(LongPredicate, LongConsumer)} **/

    @Test
    public void conditional_predicate_and_consumer_reference() {
        MockConsumer<Long, RuntimeException> mock = MockConsumer.mock();
        LongConsumer chain = LongConsumers.conditional(TestingFunctions::isPositive, mock);
        chain.accept(0);
        assertThat(mock.argsCalled()).isEmpty();
        chain.accept(3);
        assertThat(mock.argsCalled()).containsExactly(3L);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void conditional_predicate_and_consumer_lambda() {
        MockConsumer<Long, RuntimeException> mock = MockConsumer.mock();
        LongConsumer chain = LongConsumers.conditional(i -> isPositive(i), mock);
        chain.accept(0);
        assertThat(mock.argsCalled()).isEmpty();
        chain.accept(3);
        assertThat(mock.argsCalled()).containsExactly(3L);
    }

    /** {@link LongConsumers#chainObj(ToLongFunction, LongConsumer)}  **/

    @Test
    public void chain_obj_function_and_consumer_reference() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        Consumer<Long> chain = LongConsumers.chainObj(TestingFunctions::negate, mock);
        chain.accept(123L);
        assertThat(mock.argsCalled()).containsExactly(-123L);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_obj_function_and_consumer_lambda() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        Consumer<Long> chain = LongConsumers.chainObj(i -> negate(i), mock);
        chain.accept(123L);
        assertThat(mock.argsCalled()).containsExactly(-123L);
    }

    /** {@link LongConsumers#chainLong(LongFunction, Consumer)} **/

    @Test
    public void chain_long_function_and_consumer_reference() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        LongConsumer chain = LongConsumers.chainLong(String::valueOf, mock);
        chain.accept(123L);
        assertThat(mock.argsCalled()).containsExactly("123");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_long_function_and_consumer_lambda() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        LongConsumer chain = LongConsumers.chainLong(i -> String.valueOf(i), mock);
        chain.accept(123L);
        assertThat(mock.argsCalled()).containsExactly("123");
    }

    /** {@link LongConsumers#chainLongs(LongUnaryOperator, LongConsumer)} **/

    @Test
    public void chain_longs_operator_and_consumer_reference() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        LongConsumer chain = LongConsumers.chainLongs(TestingFunctions::negate, mock);
        chain.accept(123L);
        assertThat(mock.argsCalled()).containsExactly(-123L);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_longs_operator_and_consumer_lambda() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        LongConsumer chain = LongConsumers.chainLongs(i -> negate(i), mock);
        chain.accept(123L);
        assertThat(mock.argsCalled()).containsExactly(-123L);
    }
}
