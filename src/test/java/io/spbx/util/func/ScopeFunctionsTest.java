package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import io.spbx.util.testing.MockFunction;
import io.spbx.util.testing.MockFunction.Applying;
import io.spbx.util.testing.MockRunnable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class ScopeFunctionsTest {
    @Test
    public void also_apply_simple() {
        MockConsumer<String, RuntimeException> mockConsumer = MockConsumer.mock();
        String result = ScopeFunctions.alsoApply("foo", mockConsumer);
        assertThat(result).isEqualTo("foo");
        assertThat(mockConsumer.argsCalled()).containsExactly("foo");
    }

    @Test
    public void also_run_simple() {
        MockRunnable mockRunnable = new MockRunnable();
        String result = ScopeFunctions.alsoRun("foo", mockRunnable);
        assertThat(result).isEqualTo("foo");
        assertThat(mockRunnable.timesCalled()).isEqualTo(1);
    }

    @Test
    public void with_simple() {
        Applying<String, Integer> mockFunction = MockFunction.applying(String::length);
        Integer result = ScopeFunctions.with("foo", mockFunction);
        assertThat(result).isEqualTo(3);
        assertThat(mockFunction.argsCalled()).containsExactly("foo");
    }
}
