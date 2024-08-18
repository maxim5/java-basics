package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import io.spbx.util.testing.MockFunction;
import io.spbx.util.testing.MockFunction.Applying;
import io.spbx.util.testing.MockRunnable;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

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

    @Test
    public void ints_also_apply_simple() {
        MockConsumer<Integer, RuntimeException> mockConsumer = MockConsumer.mock();
        int result = ScopeFunctions.Ints.alsoApply(123, mockConsumer);
        assertThat(result).isEqualTo(123);
        assertThat(mockConsumer.argsCalled()).containsExactly(123);
    }

    @Test
    public void ints_also_run_simple() {
        MockRunnable mockRunnable = new MockRunnable();
        int result = ScopeFunctions.Ints.alsoRun(123, mockRunnable);
        assertThat(result).isEqualTo(123);
        assertThat(mockRunnable.timesCalled()).isEqualTo(1);
    }

    @Test
    public void ints_with_simple() {
        Applying<Integer, Integer> mockFunction = MockFunction.applying(Integer::bitCount);
        int result = ScopeFunctions.Ints.with(123, mockFunction);
        assertThat(result).isEqualTo(6);
        assertThat(mockFunction.argsCalled()).containsExactly(123);
    }

    @Test
    public void ints_with_string_simple() {
        Applying<String, Integer> mockFunction = MockFunction.applying(String::length);
        int result = ScopeFunctions.Ints.with("foo", mockFunction);
        assertThat(result).isEqualTo(3);
        assertThat(mockFunction.argsCalled()).containsExactly("foo");
    }

    @Test
    public void longs_also_apply_simple() {
        MockConsumer<Integer, RuntimeException> mockConsumer = MockConsumer.mock();
        long result = ScopeFunctions.Longs.alsoApply(123L, mockConsumer);
        assertThat(result).isEqualTo(123L);
        assertThat(mockConsumer.argsCalled()).containsExactly(123L);
    }

    @Test
    public void longs_also_run_simple() {
        MockRunnable mockRunnable = new MockRunnable();
        long result = ScopeFunctions.Longs.alsoRun(123L, mockRunnable);
        assertThat(result).isEqualTo(123L);
        assertThat(mockRunnable.timesCalled()).isEqualTo(1);
    }

    @Test
    public void longs_with_simple() {
        Applying<Long, Integer> mockFunction = MockFunction.applying(Long::bitCount);
        int result = ScopeFunctions.Longs.with(123L, mockFunction);
        assertThat(result).isEqualTo(6);
        assertThat(mockFunction.argsCalled()).containsExactly(123L);
    }

    @Test
    public void longs_with_string_simple() {
        Applying<String, Long> mockFunction = MockFunction.applying(Long::parseLong);
        long result = ScopeFunctions.Longs.with("123", mockFunction);
        assertThat(result).isEqualTo(123);
        assertThat(mockFunction.argsCalled()).containsExactly("123");
    }

    @Test
    public void bools_with_simple() {
        Applying<String, Boolean> mockFunction = MockFunction.applying(String::isBlank);
        boolean result = ScopeFunctions.Bools.with("   ", mockFunction);
        assertThat(result).isEqualTo(true);
        assertThat(mockFunction.argsCalled()).containsExactly("   ");
    }
}
