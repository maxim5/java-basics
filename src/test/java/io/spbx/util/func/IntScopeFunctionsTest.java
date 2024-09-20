package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import io.spbx.util.testing.MockFunction;
import io.spbx.util.testing.MockFunction.Applying;
import io.spbx.util.testing.MockRunnable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class IntScopeFunctionsTest {
    @Test
    public void also_apply_simple() {
        MockConsumer<Integer, RuntimeException> mockConsumer = MockConsumer.mock();
        int result = IntScopeFunctions.alsoApply(123, mockConsumer);
        assertThat(result).isEqualTo(123);
        assertThat(mockConsumer.argsCalled()).containsExactly(123);
    }

    @Test
    public void also_run_simple() {
        MockRunnable mockRunnable = new MockRunnable();
        int result = IntScopeFunctions.alsoRun(123, mockRunnable);
        assertThat(result).isEqualTo(123);
        assertThat(mockRunnable.timesCalled()).isEqualTo(1);
    }

    @Test
    public void with_simple() {
        Applying<Integer, Integer> mockFunction = MockFunction.applying(Integer::bitCount);
        int result = IntScopeFunctions.with(123, mockFunction);
        assertThat(result).isEqualTo(6);
        assertThat(mockFunction.argsCalled()).containsExactly(123);
    }

    @Test
    public void with_string_simple() {
        Applying<String, Integer> mockFunction = MockFunction.applying(String::length);
        int result = IntScopeFunctions.with("foo", mockFunction);
        assertThat(result).isEqualTo(3);
        assertThat(mockFunction.argsCalled()).containsExactly("foo");
    }
}
