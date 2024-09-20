package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class PredicatesTest {
    @Test
    public void chain_consumer_and_predicate() {
        MockConsumer<Integer, RuntimeException> mock = MockConsumer.mock();
        Predicate<Integer> chain = Predicates.chain(mock, n -> n > 5);
        assertThat(chain.test(3)).isFalse();
        assertThat(mock.argsCalled()).containsExactly(3);
        assertThat(chain.test(6)).isTrue();
        assertThat(mock.argsCalled()).containsExactly(3, 6);
    }
}
