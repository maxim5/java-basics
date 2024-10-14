package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import io.spbx.util.testing.MockFunction;
import io.spbx.util.testing.MockFunction.Applying;
import io.spbx.util.testing.MockRunnable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.ToLongFunction;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class LongScopeFunctionsTest {
    private static final Long NULL = null;

    /** {@link LongScopeFunctions#also(long, LongConsumer)} **/

    @Test
    @SuppressWarnings({ "resource", "FunctionalExpressionCanBeFolded" })
    public void also_consumer_reference() {
        MockConsumer<Long, RuntimeException> mockConsumer = MockConsumer.mock();
        long result = LongScopeFunctions.also(123L, mockConsumer::accept);
        assertThat(result).isEqualTo(123L);
        assertThat(mockConsumer.argsCalled()).containsExactly(123L);
    }

    @Test
    @SuppressWarnings({ "resource", "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void also_consumer_lambda() {
        MockConsumer<Long, RuntimeException> mockConsumer = MockConsumer.mock();
        long result = LongScopeFunctions.also(123L, value -> mockConsumer.accept(value));
        assertThat(result).isEqualTo(123L);
        assertThat(mockConsumer.argsCalled()).containsExactly(123L);
    }

    /** {@link LongScopeFunctions#also(long, Runnable)} **/

    @Test
    @SuppressWarnings("FunctionalExpressionCanBeFolded")
    public void also_runnable_reference() {
        MockRunnable mockRunnable = new MockRunnable();
        long result = LongScopeFunctions.also(123L, mockRunnable::run);
        assertThat(result).isEqualTo(123L);
        assertThat(mockRunnable.timesCalled()).isEqualTo(1);
    }

    @Test
    @SuppressWarnings({ "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void also_runnable_lambda() {
        MockRunnable mockRunnable = new MockRunnable();
        long result = LongScopeFunctions.also(123L, () -> mockRunnable.run());
        assertThat(result).isEqualTo(123L);
        assertThat(mockRunnable.timesCalled()).isEqualTo(1);
    }

    /** {@link LongScopeFunctions#with(long, LongFunction)} **/

    @Test
    @SuppressWarnings("FunctionalExpressionCanBeFolded")
    public void with_long_reference() {
        Applying<Long, Integer> mockFunction = MockFunction.applying(Long::bitCount);
        int result = LongScopeFunctions.with(123L, mockFunction::apply);
        assertThat(result).isEqualTo(6);
        assertThat(mockFunction.argsCalled()).containsExactly(123L);
    }

    @Test
    @SuppressWarnings({ "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void with_long_lambda() {
        Applying<Long, Integer> mockFunction = MockFunction.applying(Long::bitCount);
        int result = LongScopeFunctions.with(123L, value -> mockFunction.apply(value));
        assertThat(result).isEqualTo(6);
        assertThat(mockFunction.argsCalled()).containsExactly(123L);
    }

    /** {@link LongScopeFunctions#with(Object, ToLongFunction)} **/

    @Test
    public void with_string_reference() {
        Applying<String, Long> mockFunction = MockFunction.applying(Long::parseLong);
        long result = LongScopeFunctions.with("123", mockFunction::apply);
        assertThat(result).isEqualTo(123L);
        assertThat(mockFunction.argsCalled()).containsExactly("123");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void with_string_lambda() {
        Applying<String, Long> mockFunction = MockFunction.applying(Long::parseLong);
        long result = LongScopeFunctions.with("123", value -> mockFunction.apply(value));
        assertThat(result).isEqualTo(123L);
        assertThat(mockFunction.argsCalled()).containsExactly("123");
    }

    /** {@link LongScopeFunctions#map(Object, ToLongFunction)} **/

    @Test
    @SuppressWarnings("MethodRefCanBeReplacedWithLambda")
    public void map_string_reference() {
        Applying<Long, Integer> mockFunction = MockFunction.applying(Long::bitCount);
        assertThat(LongScopeFunctions.map(NULL, mockFunction::apply)).isEqualTo(0);
        assertThat(LongScopeFunctions.map(123L, mockFunction::apply)).isEqualTo(6);
        assertThat(mockFunction.argsCalled()).containsExactly(123L);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void map_string_lambda() {
        Applying<Long, Integer> mockFunction = MockFunction.applying(Long::bitCount);
        assertThat(LongScopeFunctions.map(NULL, value -> mockFunction.apply(value))).isEqualTo(0);
        assertThat(LongScopeFunctions.map(123L, value -> mockFunction.apply(value))).isEqualTo(6);
        assertThat(mockFunction.argsCalled()).containsExactly(123L);
    }
}
