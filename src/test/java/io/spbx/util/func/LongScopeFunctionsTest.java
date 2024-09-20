package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import io.spbx.util.testing.MockFunction;
import io.spbx.util.testing.MockFunction.Applying;
import io.spbx.util.testing.MockRunnable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class LongScopeFunctionsTest {
    @Test
    public void also_apply_simple() {
        MockConsumer<Integer, RuntimeException> mockConsumer = MockConsumer.mock();
        long result = LongScopeFunctions.alsoApply(123L, mockConsumer);
        assertThat(result).isEqualTo(123L);
        assertThat(mockConsumer.argsCalled()).containsExactly(123L);
    }

    @Test
    public void also_run_simple() {
        MockRunnable mockRunnable = new MockRunnable();
        long result = LongScopeFunctions.alsoRun(123L, mockRunnable);
        assertThat(result).isEqualTo(123L);
        assertThat(mockRunnable.timesCalled()).isEqualTo(1);
    }

    @Test
    public void with_simple() {
        Applying<Long, Integer> mockFunction = MockFunction.applying(Long::bitCount);
        int result = LongScopeFunctions.with(123L, mockFunction);
        assertThat(result).isEqualTo(6);
        assertThat(mockFunction.argsCalled()).containsExactly(123L);
    }

    @Test
    public void with_string_simple() {
        Applying<String, Long> mockFunction = MockFunction.applying(Long::parseLong);
        long result = LongScopeFunctions.with("123", mockFunction);
        assertThat(result).isEqualTo(123);
        assertThat(mockFunction.argsCalled()).containsExactly("123");
    }
}
