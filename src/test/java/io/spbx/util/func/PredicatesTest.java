package io.spbx.util.func;

import io.spbx.util.testing.func.MockConsumer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.TestingFunctions.isPositive;
import static io.spbx.util.testing.AssertBasics.assertPredicate;

@Tag("fast")
public class PredicatesTest {
    /** {@link Predicates#and}, {@link Predicates#or} **/

    @Test
    public void and_simple() {
        assertPredicate(Predicates.<Integer>and(i -> i > 0, i -> i < 5)).isTrueFor(1, 2, 3, 4).isFalseFor(0, 5, 6);
        assertPredicate(Predicates.<Integer>and(i -> i > 0, true)).isTrueFor(1, 2, 3, 4, 5).isFalseFor(0, -1, -2);
        assertPredicate(Predicates.<Integer>and(false, i -> i > 0)).isFalseFor(-2, -1, 0, 1, 2);
        assertPredicate(Predicates.<Integer>and(i -> i > 0, i -> i % 3 == 0, i -> i < 10))
            .isTrueFor(3, 6)
            .isFalseFor(-3, -1, 0, 1, 2, 4, 5, 7, 8, 10, 12);

        assertPredicate(Predicates.<Integer>and(null, null)).isTrueFor(0, 1, 2, 3);
        assertPredicate(Predicates.<Integer>and(null, i -> i < 5)).isTrueFor(0, 1, 2, 3, 4).isFalseFor(5, 6);
        assertPredicate(Predicates.<Integer>and(i -> i < 5, null)).isTrueFor(0, 1, 2, 3, 4).isFalseFor(5, 6);
        assertPredicate(Predicates.<Integer>and(i -> i > 0, i -> i % 3 == 0, null))
            .isTrueFor(3, 6, 9, 12)
            .isFalseFor(-3, -1, 0, 1, 2, 4, 5, 7, 8, 10);
    }

    @Test
    public void or_simple() {
        assertPredicate(Predicates.<Integer>or(i -> i < 2, i -> i > 5)).isTrueFor(0, 1, 6, 7).isFalseFor(2, 3, 4, 5);
        assertPredicate(Predicates.<Integer>or(i -> i < 2, true)).isTrueFor(-2, -1, 0, 1, 2);
        assertPredicate(Predicates.<Integer>or(false, i -> i > 0)).isTrueFor(1, 2).isFalseFor(-1, 0);
        assertPredicate(Predicates.<Integer>or(i -> i % 2 == 0, i -> i % 3 == 0, i -> i % 5 == 0))
            .isTrueFor(0, 2, 3, 5, 6, 8, 9, 10)
            .isFalseFor(1, 11, 13, 17);

        assertPredicate(Predicates.<Integer>or(null, null)).isTrueFor(0, 1, 2, 3);
        assertPredicate(Predicates.<Integer>or(null, i -> i < 5)).isTrueFor(0, 1, 2, 3, 4).isFalseFor(5, 6);
        assertPredicate(Predicates.<Integer>or(i -> i < 5, null)).isTrueFor(0, 1, 2, 3, 4).isFalseFor(5, 6);
        assertPredicate(Predicates.<Integer>or(i -> i > 0, i -> i % 3 == 0, null))
            .isTrueFor(-3, 0, 3, 6, 9, 12)
            .isFalseFor(-5, -2, -1);
    }

    /** {@link Predicates#peek(Consumer, Predicate)} **/

    @Test
    public void peek_consumer_and_predicate() {
        MockConsumer<Integer, RuntimeException> mock = MockConsumer.mock();
        Predicate<Integer> chain = Predicates.peek(mock, TestingFunctions::isPositive);
        assertThat(chain.test(0)).isFalse();
        assertThat(mock.argsCalled()).containsExactly(0);
        assertThat(chain.test(6)).isTrue();
        assertThat(mock.argsCalled()).containsExactly(0, 6);
    }

    @Test
    @SuppressWarnings({ "resource", "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void peek_consumer_and_predicate_lambda() {
        MockConsumer<Integer, RuntimeException> mock = MockConsumer.mock();
        Predicate<Integer> chain = Predicates.peek(i -> mock.accept(i), n -> isPositive(n));
        assertThat(chain.test(0)).isFalse();
        assertThat(mock.argsCalled()).containsExactly(0);
        assertThat(chain.test(6)).isTrue();
        assertThat(mock.argsCalled()).containsExactly(0, 6);
    }

    /** {@link Predicates#chain(Function, Predicate)} **/

    @Test
    public void chain_function_and_predicate_reference() {
        Predicate<Object> chain = Predicates.chain(String::valueOf, String::isBlank);
        assertThat(chain.test(" ")).isTrue();
        assertThat(chain.test("_")).isFalse();
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_function_and_predicate_lambda() {
        Predicate<Object> chain = Predicates.chain(o -> String.valueOf(o), s -> s.isBlank());
        assertThat(chain.test(" ")).isTrue();
        assertThat(chain.test("_")).isFalse();
    }
}
