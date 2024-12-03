package io.spbx.util.func;

import io.spbx.util.testing.func.MockConsumer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.TestingFunctions.isPositive;
import static io.spbx.util.func.TestingFunctions.negate;

@Tag("fast")
public class LongPredicatesTest {
    /** {@link LongPredicates#peek(LongConsumer, LongPredicate)} **/

    @Test
    public void peek_consumer_and_predicate_reference() {
        MockConsumer<Long, RuntimeException> mock = MockConsumer.mock();
        LongPredicate peeked = LongPredicates.peek(mock, TestingFunctions::isPositive);
        assertThat(peeked.test(0L)).isFalse();
        assertThat(mock.argsCalled()).containsExactly(0L);
        assertThat(peeked.test(3L)).isTrue();
        assertThat(mock.argsCalled()).containsExactly(0L, 3L);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void peek_consumer_and_predicate_lambda() {
        MockConsumer<Long, RuntimeException> mock = MockConsumer.mock();
        LongPredicate peeked = LongPredicates.peek(mock, i -> isPositive(i));
        assertThat(peeked.test(0L)).isFalse();
        assertThat(mock.argsCalled()).containsExactly(0L);
        assertThat(peeked.test(3L)).isTrue();
        assertThat(mock.argsCalled()).containsExactly(0L, 3L);
    }

    /** {@link LongPredicates#chainObj(ToLongFunction, LongPredicate)} **/

    @Test
    public void chain_function_and_predicate_reference() {
        Predicate<Long> chain = LongPredicates.chainObj(TestingFunctions::negate, TestingFunctions::isPositive);
        assertThat(chain.test(+3L)).isFalse();
        assertThat(chain.test(-3L)).isTrue();
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_function_and_predicate_lambda() {
        Predicate<Long> chain = LongPredicates.chainObj(i -> negate(i), i -> isPositive(i));
        assertThat(chain.test(+3L)).isFalse();
        assertThat(chain.test(-3L)).isTrue();
    }

    /** {@link LongPredicates#chainLong(LongFunction, Predicate)} **/

    @Test
    public void chain_long_function_and_predicate_reference() {
        LongPredicate chain = LongPredicates.chainLong(TestingFunctions::negate, TestingFunctions::isPositive);
        assertThat(chain.test(+3)).isFalse();
        assertThat(chain.test(-3)).isTrue();
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_long_function_and_predicate_lambda() {
        LongPredicate chain = LongPredicates.chainLong(i -> negate(i), i -> isPositive(i));
        assertThat(chain.test(+3)).isFalse();
        assertThat(chain.test(-3)).isTrue();
    }

    /** {@link LongPredicates#chainLongs(LongUnaryOperator, LongPredicate)} **/

    @Test
    public void chain_longs_operator_and_predicate_reference() {
        LongPredicate chain = LongPredicates.chainLongs(TestingFunctions::negate, TestingFunctions::isPositive);
        assertThat(chain.test(+3)).isFalse();
        assertThat(chain.test(-3)).isTrue();
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_longs_operator_and_predicate_lambda() {
        LongPredicate chain = LongPredicates.chainLongs(i -> negate(i), i -> isPositive(i));
        assertThat(chain.test(+3)).isFalse();
        assertThat(chain.test(-3)).isTrue();
    }
}
