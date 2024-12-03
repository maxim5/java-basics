package io.spbx.util.lazy;

import io.spbx.util.testing.func.MockBoolSupplier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class LazyBooleanTest {
    @Test
    public void simple() {
        MockBoolSupplier mockSupplier = MockBoolSupplier.of(true);

        LazyBoolean lazy = LazyBoolean.of(mockSupplier);
        assertThat(mockSupplier.timesCalled()).isEqualTo(0);

        assertThat(lazy.get()).isTrue();
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);

        assertThat(lazy.get()).isTrue();
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
    }
}
