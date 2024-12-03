package io.spbx.util.func;

import io.spbx.util.testing.func.MockConsumer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.TestingFunctions.isPositive;
import static io.spbx.util.func.TestingFunctions.negate;

@Tag("fast")
public class IntConsumersTest {
    /** {@link IntConsumers#conditional(IntPredicate, IntConsumer)} **/

    @Test
    public void conditional_predicate_and_consumer_reference() {
        MockConsumer<Integer, RuntimeException> mock = MockConsumer.mock();
        IntConsumer chain = IntConsumers.conditional(TestingFunctions::isPositive, mock);
        chain.accept(0);
        assertThat(mock.argsCalled()).isEmpty();
        chain.accept(3);
        assertThat(mock.argsCalled()).containsExactly(3);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void conditional_predicate_and_consumer_lambda() {
        MockConsumer<Integer, RuntimeException> mock = MockConsumer.mock();
        IntConsumer chain = IntConsumers.conditional(i -> isPositive(i), mock);
        chain.accept(0);
        assertThat(mock.argsCalled()).isEmpty();
        chain.accept(3);
        assertThat(mock.argsCalled()).containsExactly(3);
    }

    /** {@link IntConsumers#chainObj(ToIntFunction, IntConsumer)} **/

    @Test
    public void chain_obj_function_and_consumer_reference() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        Consumer<Integer> chain = IntConsumers.chainObj(TestingFunctions::negate, mock);
        chain.accept(123);
        assertThat(mock.argsCalled()).containsExactly(-123);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_obj_function_and_consumer_lambda() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        Consumer<Integer> chain = IntConsumers.chainObj(i -> negate(i), mock);
        chain.accept(123);
        assertThat(mock.argsCalled()).containsExactly(-123);
    }

    /** {@link IntConsumers#chainInt(IntFunction, Consumer)} **/

    @Test
    public void chain_int_function_and_consumer_reference() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        IntConsumer chain = IntConsumers.chainInt(String::valueOf, mock);
        chain.accept(123);
        assertThat(mock.argsCalled()).containsExactly("123");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_int_function_and_consumer_lambda() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        IntConsumer chain = IntConsumers.chainInt(i -> String.valueOf(i), mock);
        chain.accept(123);
        assertThat(mock.argsCalled()).containsExactly("123");
    }

    /** {@link IntConsumers#chainInts(IntUnaryOperator, IntConsumer)} **/

    @Test
    public void chain_ints_operator_and_consumer_reference() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        IntConsumer chain = IntConsumers.chainInts(TestingFunctions::negate, mock);
        chain.accept(123);
        assertThat(mock.argsCalled()).containsExactly(-123);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_ints_operator_and_consumer_lambda() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        IntConsumer chain = IntConsumers.chainInts(i -> TestingFunctions.negate(i), mock);
        chain.accept(123);
        assertThat(mock.argsCalled()).containsExactly(-123);
    }
}
