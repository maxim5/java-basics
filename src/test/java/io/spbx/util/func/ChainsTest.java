package io.spbx.util.func;

import io.spbx.util.base.EasyCast;
import io.spbx.util.testing.MockConsumer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class ChainsTest {
    @Test
    public void chain_three_types_functions() {
        Function<Integer, String> chain = Chains.chain(Integer::bitCount, Integer::toHexString);
        assertThat(chain.apply(0xff)).isEqualTo("8");
        assertThat(chain.apply(0xffff)).isEqualTo("10");
    }

    @Test
    public void chain_bool_predicate_and_function() {
        Function<String, Integer> chain = Chains.chain("foo"::endsWith, EasyCast::castToInt);
        assertThat(chain.apply("foo")).isEqualTo(1);
        assertThat(chain.apply("bar")).isEqualTo(0);
    }

    @Test
    public void nonNullify() {
        Function<Object, String> chain = Chains.nonNullify(Object::toString, "NULL");
        assertThat(chain.apply(1)).isEqualTo("1");
        assertThat(chain.apply(null)).isEqualTo("NULL");
    }

    @Test
    public void bypassNull() {
        Function<Object, String> chain = Chains.bypassNull(Object::toString);
        assertThat(chain.apply(1)).isEqualTo("1");
        assertThat(chain.apply(null)).isEqualTo(null);
    }

    @Test
    public void fan_out_two_consumers() {
        MockConsumer<Integer, RuntimeException> mock1 = MockConsumer.mock();
        MockConsumer<Integer, RuntimeException> mock2 = MockConsumer.mock();
        Consumer<Integer> consumer = Chains.fanOut(mock1, mock2);
        consumer.accept(7);
        assertThat(mock1.argsCalled()).containsExactly(7);
        assertThat(mock2.argsCalled()).containsExactly(7);
    }

    @Test
    public void fan_out_three_consumers() {
        MockConsumer<Integer, RuntimeException> mock1 = MockConsumer.mock();
        MockConsumer<Integer, RuntimeException> mock2 = MockConsumer.mock();
        MockConsumer<Integer, RuntimeException> mock3 = MockConsumer.mock();
        Consumer<Integer> consumer = Chains.fanOut(mock1, mock2, mock3);
        consumer.accept(7);
        assertThat(mock1.argsCalled()).containsExactly(7);
        assertThat(mock2.argsCalled()).containsExactly(7);
        assertThat(mock3.argsCalled()).containsExactly(7);
    }

    @Test
    public void chain_predicate_and_consumer() {
        MockConsumer<String, RuntimeException> mock = MockConsumer.mock();
        Consumer<String> chain = Chains.chain("foo"::endsWith, mock);
        chain.accept("bar");
        assertThat(mock.argsCalled()).isEmpty();
        chain.accept("foo");
        assertThat(mock.argsCalled()).containsExactly("foo");
    }

    @Test
    public void predicates_chain_consumer_and_predicate() {
        MockConsumer<Integer, RuntimeException> mock = MockConsumer.mock();
        Predicate<Integer> chain = Chains.Predicates.chain(mock, n -> n > 5);
        assertThat(chain.test(3)).isFalse();
        assertThat(mock.argsCalled()).containsExactly(3);
        assertThat(chain.test(6)).isTrue();
        assertThat(mock.argsCalled()).containsExactly(3, 6);
    }

    @Test
    public void ints_chain_functions() {
        IntFunction<String> chain = Chains.Ints.chain("a"::repeat, String::toUpperCase);
        assertThat(chain.apply(3)).isEqualTo("AAA");
    }

    @Test
    public void int_consumers_chain_predicate_and_consumer() {
        MockConsumer<Integer, RuntimeException> mock = MockConsumer.mock();
        IntConsumer chain = Chains.IntConsumers.chain(n -> n > 5, mock);
        chain.accept(3);
        assertThat(mock.argsCalled()).isEmpty();
        chain.accept(6);
        assertThat(mock.argsCalled()).containsExactly(6);
    }

    @Test
    public void int_predicates_chain_consumer_and_predicate() {
        MockConsumer<Integer, RuntimeException> mock = MockConsumer.mock();
        IntPredicate chain = Chains.IntPredicates.chain(mock, n -> n > 5);
        assertThat(chain.test(3)).isFalse();
        assertThat(mock.argsCalled()).containsExactly(3);
        assertThat(chain.test(6)).isTrue();
        assertThat(mock.argsCalled()).containsExactly(3, 6);
    }

    @Test
    public void long_chain_functions() {
        LongFunction<String> chain = Chains.Longs.chain(count -> "a".repeat((int) count), String::toUpperCase);
        assertThat(chain.apply(3)).isEqualTo("AAA");
    }

    @Test
    public void long_consumers_chain_predicate_and_consumer() {
        MockConsumer<Long, RuntimeException> mock = MockConsumer.mock();
        LongConsumer chain = Chains.LongConsumers.chain(n -> n > 5L, mock);
        chain.accept(3L);
        assertThat(mock.argsCalled()).isEmpty();
        chain.accept(6L);
        assertThat(mock.argsCalled()).containsExactly(6L);
    }

    @Test
    public void long_predicates_chain_consumer_and_predicate() {
        MockConsumer<Long, RuntimeException> mock = MockConsumer.mock();
        LongPredicate chain = Chains.LongPredicates.chain(mock, n -> n > 5L);
        assertThat(chain.test(3L)).isFalse();
        assertThat(mock.argsCalled()).containsExactly(3L);
        assertThat(chain.test(6L)).isTrue();
        assertThat(mock.argsCalled()).containsExactly(3L, 6L);
    }
}
