package io.spbx.util.lazy;

import io.spbx.util.testing.func.MockSupplier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class AtomicCacheComputeTest {
    @Test
    public void simple() {
        MockSupplier<String> mock = MockSupplier.mock("foo");

        AtomicCacheCompute<String> cache = new AtomicCacheCompute<>();
        assertThat(mock.timesCalled()).isEqualTo(0);

        assertThat(cache.getOrCompute(mock)).isEqualTo("foo");
        assertThat(mock.timesCalled()).isEqualTo(1);

        assertThat(cache.getOrCompute(mock)).isEqualTo("foo");
        assertThat(mock.timesCalled()).isEqualTo(1);
    }
}
