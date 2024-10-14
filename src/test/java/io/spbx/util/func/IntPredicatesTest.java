package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.TestingFunctions.isPositive;
import static io.spbx.util.func.TestingFunctions.negate;

@Tag("fast")
public class IntPredicatesTest {
    /** {@link IntPredicates#peek(IntConsumer, IntPredicate)} **/

    @Test
    public void peek_consumer_and_predicate_reference() {
        MockConsumer<Integer, RuntimeException> mock = MockConsumer.mock();
        IntPredicate peeked = IntPredicates.peek(mock, TestingFunctions::isPositive);
        assertThat(peeked.test(0)).isFalse();
        assertThat(mock.argsCalled()).containsExactly(0);
        assertThat(peeked.test(3)).isTrue();
        assertThat(mock.argsCalled()).containsExactly(0, 3);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void peek_consumer_and_predicate_lambda() {
        MockConsumer<Integer, RuntimeException> mock = MockConsumer.mock();
        IntPredicate peeked = IntPredicates.peek(mock, i -> isPositive(i));
        assertThat(peeked.test(0)).isFalse();
        assertThat(mock.argsCalled()).containsExactly(0);
        assertThat(peeked.test(3)).isTrue();
        assertThat(mock.argsCalled()).containsExactly(0, 3);
    }

    /** {@link IntPredicates#chainObj(ToIntFunction, IntPredicate)} **/

    @Test
    public void chain_obj_function_and_predicate_reference() {
        Predicate<Integer> chain = IntPredicates.chainObj(TestingFunctions::negate, TestingFunctions::isPositive);
        assertThat(chain.test(+3)).isFalse();
        assertThat(chain.test(-3)).isTrue();
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_obj_function_and_predicate_lambda() {
        Predicate<Integer> chain = IntPredicates.chainObj(i -> negate(i), i -> isPositive(i));
        assertThat(chain.test(+3)).isFalse();
        assertThat(chain.test(-3)).isTrue();
    }

    /** {@link IntPredicates#chainInt(IntFunction, Predicate)}  **/

    @Test
    public void chain_int_function_and_predicate_reference() {
        IntPredicate chain = IntPredicates.chainInt(TestingFunctions::negate, TestingFunctions::isPositive);
        assertThat(chain.test(+3)).isFalse();
        assertThat(chain.test(-3)).isTrue();
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_int_function_and_predicate_lambda() {
        IntPredicate chain = IntPredicates.chainInt(i -> negate(i), i -> isPositive(i));
        assertThat(chain.test(+3)).isFalse();
        assertThat(chain.test(-3)).isTrue();
    }

    /** {@link IntPredicates#chainInts(IntUnaryOperator, IntPredicate)} **/

    @Test
    public void chain_ints_operator_and_predicate_reference() {
        IntPredicate chain = IntPredicates.chainInts(TestingFunctions::negate, TestingFunctions::isPositive);
        assertThat(chain.test(+3)).isFalse();
        assertThat(chain.test(-3)).isTrue();
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_ints_operator_and_predicate_lambda() {
        IntPredicate chain = IntPredicates.chainInts(i -> negate(i), i -> isPositive(i));
        assertThat(chain.test(+3)).isFalse();
        assertThat(chain.test(-3)).isTrue();
    }
}
