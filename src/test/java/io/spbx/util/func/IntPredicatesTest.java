package io.spbx.util.func;

import io.spbx.util.testing.func.MockConsumer;
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
import static io.spbx.util.testing.AssertBasics.assertIntPredicate;

@Tag("fast")
public class IntPredicatesTest {
    /** {@link IntPredicates#and}, {@link IntPredicates#or} **/

    @Test
    public void and_simple() {
        assertIntPredicate(IntPredicates.and(i -> i > 0, i -> i < 5)).isTrueFor(1, 2, 3, 4).isFalseFor(0, 5, 6);
        assertIntPredicate(IntPredicates.and(i -> i > 0, true)).isTrueFor(1, 2, 3, 4, 5).isFalseFor(0, -1, -2);
        assertIntPredicate(IntPredicates.and(false, i -> i > 0)).isFalseFor(-2, -1, 0, 1, 2);
        assertIntPredicate(IntPredicates.and(i -> i > 0, i -> i % 3 == 0, i -> i < 10))
            .isTrueFor(3, 6)
            .isFalseFor(-3, -1, 0, 1, 2, 4, 5, 7, 8, 10, 12);

        assertIntPredicate(IntPredicates.and(null, null)).isTrueFor(0, 1, 2, 3);
        assertIntPredicate(IntPredicates.and(null, i -> i < 5)).isTrueFor(0, 1, 2, 3, 4).isFalseFor(5, 6);
        assertIntPredicate(IntPredicates.and(i -> i < 5, null)).isTrueFor(0, 1, 2, 3, 4).isFalseFor(5, 6);
        assertIntPredicate(IntPredicates.and(i -> i > 0, i -> i % 3 == 0, null))
            .isTrueFor(3, 6, 9, 12)
            .isFalseFor(-3, -1, 0, 1, 2, 4, 5, 7, 8, 10);
    }

    @Test
    public void or_simple() {
        assertIntPredicate(IntPredicates.or(i -> i < 2, i -> i > 5)).isTrueFor(0, 1, 6, 7).isFalseFor(2, 3, 4, 5);
        assertIntPredicate(IntPredicates.or(i -> i < 2, true)).isTrueFor(-2, -1, 0, 1, 2);
        assertIntPredicate(IntPredicates.or(false, i -> i > 0)).isTrueFor(1, 2).isFalseFor(-1, 0);
        assertIntPredicate(IntPredicates.or(i -> i % 2 == 0, i -> i % 3 == 0, i -> i % 5 == 0))
            .isTrueFor(0, 2, 3, 5, 6, 8, 9, 10)
            .isFalseFor(1, 11, 13, 17);

        assertIntPredicate(IntPredicates.or(null, null)).isTrueFor(0, 1, 2, 3);
        assertIntPredicate(IntPredicates.or(null, i -> i < 5)).isTrueFor(0, 1, 2, 3, 4).isFalseFor(5, 6);
        assertIntPredicate(IntPredicates.or(i -> i < 5, null)).isTrueFor(0, 1, 2, 3, 4).isFalseFor(5, 6);
        assertIntPredicate(IntPredicates.or(i -> i > 0, i -> i % 3 == 0, null))
            .isTrueFor(-3, 0, 3, 6, 9, 12)
            .isFalseFor(-5, -2, -1);
    }


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
