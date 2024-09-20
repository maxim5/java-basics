package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.LongConsumer;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class LongConsumersTest {
    @Test
    public void long_consumers_chain_predicate_and_consumer() {
        MockConsumer<Long, RuntimeException> mock = MockConsumer.mock();
        LongConsumer chain = LongConsumers.chain(n -> n > 5L, mock);
        chain.accept(3L);
        assertThat(mock.argsCalled()).isEmpty();
        chain.accept(6L);
        assertThat(mock.argsCalled()).containsExactly(6L);
    }
}
