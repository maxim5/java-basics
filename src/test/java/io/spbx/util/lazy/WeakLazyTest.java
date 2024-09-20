package io.spbx.util.lazy;

import io.spbx.util.testing.MockSupplier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class WeakLazyTest {
    @Test
    public void lazy_simple() {
        MockSupplier<String> mockSupplier = MockSupplier.mock("foo");

        WeakLazy<String> lazy = WeakLazy.of(mockSupplier);
        assertThat(mockSupplier.timesCalled()).isEqualTo(0);

        assertThat(lazy.get()).isEqualTo("foo");
        assertThat(mockSupplier.timesCalled()).isGreaterThan(0);

        assertThat(lazy.get()).isEqualTo("foo");
        assertThat(mockSupplier.timesCalled()).isGreaterThan(0);
    }
}
