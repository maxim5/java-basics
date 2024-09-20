package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.LongPredicate;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class LongPredicatesTest {
    @Test
    public void long_predicates_chain_consumer_and_predicate() {
        MockConsumer<Long, RuntimeException> mock = MockConsumer.mock();
        LongPredicate chain = LongPredicates.chain(mock, n -> n > 5L);
        assertThat(chain.test(3L)).isFalse();
        assertThat(mock.argsCalled()).containsExactly(3L);
        assertThat(chain.test(6L)).isTrue();
        assertThat(mock.argsCalled()).containsExactly(3L, 6L);
    }
}
