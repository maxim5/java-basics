package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.IntConsumer;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class IntConsumersTest {
    @Test
    public void int_consumers_chain_predicate_and_consumer() {
        MockConsumer<Integer, RuntimeException> mock = MockConsumer.mock();
        IntConsumer chain = IntConsumers.chain(n -> n > 5, mock);
        chain.accept(3);
        assertThat(mock.argsCalled()).isEmpty();
        chain.accept(6);
        assertThat(mock.argsCalled()).containsExactly(6);
    }
}
