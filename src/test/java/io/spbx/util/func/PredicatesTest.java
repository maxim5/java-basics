package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.TestingFunctions.isPositive;

@Tag("fast")
public class PredicatesTest {
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
