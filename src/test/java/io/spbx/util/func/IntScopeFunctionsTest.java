package io.spbx.util.func;

import io.spbx.util.testing.MockConsumer;
import io.spbx.util.testing.MockFunction;
import io.spbx.util.testing.MockFunction.Applying;
import io.spbx.util.testing.MockRunnable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class IntScopeFunctionsTest {
    private static final String NULL = null;

    /** {@link IntScopeFunctions#also(int, IntConsumer)} **/

    @Test
    @SuppressWarnings({ "resource", "FunctionalExpressionCanBeFolded" })
    public void also_consumer_reference() {
        MockConsumer<Integer, RuntimeException> mockConsumer = MockConsumer.mock();
        int result = IntScopeFunctions.also(123, mockConsumer::accept);
        assertThat(result).isEqualTo(123);
        assertThat(mockConsumer.argsCalled()).containsExactly(123);
    }

    @Test
    @SuppressWarnings({ "resource", "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void also_consumer_lambda() {
        MockConsumer<Integer, RuntimeException> mockConsumer = MockConsumer.mock();
        int result = IntScopeFunctions.also(123, value -> mockConsumer.accept(value));
        assertThat(result).isEqualTo(123);
        assertThat(mockConsumer.argsCalled()).containsExactly(123);
    }

    /** {@link IntScopeFunctions#also(int, Runnable)} **/

    @Test
    @SuppressWarnings("FunctionalExpressionCanBeFolded")
    public void also_runnable_reference() {
        MockRunnable mockRunnable = new MockRunnable();
        int result = IntScopeFunctions.also(123, mockRunnable::run);
        assertThat(result).isEqualTo(123);
        assertThat(mockRunnable.timesCalled()).isEqualTo(1);
    }

    @Test
    @SuppressWarnings({ "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void also_runnable_lambda() {
        MockRunnable mockRunnable = new MockRunnable();
        int result = IntScopeFunctions.also(123, () -> mockRunnable.run());
        assertThat(result).isEqualTo(123);
        assertThat(mockRunnable.timesCalled()).isEqualTo(1);
    }

    /** {@link IntScopeFunctions#with(int, IntFunction)} **/

    @Test
    @SuppressWarnings("FunctionalExpressionCanBeFolded")
    public void with_int_reference() {
        Applying<Integer, Integer> mockFunction = MockFunction.applying(Integer::bitCount);
        int result = IntScopeFunctions.with(123, mockFunction::apply);
        assertThat(result).isEqualTo(6);
        assertThat(mockFunction.argsCalled()).containsExactly(123);
    }

    @Test
    @SuppressWarnings({ "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void with_int_lambda() {
        Applying<Integer, Integer> mockFunction = MockFunction.applying(Integer::bitCount);
        int result = IntScopeFunctions.with(123, value -> mockFunction.apply(value));
        assertThat(result).isEqualTo(6);
        assertThat(mockFunction.argsCalled()).containsExactly(123);
    }

    /** {@link IntScopeFunctions#with(Object, ToIntFunction)} **/

    @Test
    @SuppressWarnings("MethodRefCanBeReplacedWithLambda")
    public void with_string_reference() {
        Applying<String, Integer> mockFunction = MockFunction.applying(String::length);
        int result = IntScopeFunctions.with("foo", mockFunction::apply);
        assertThat(result).isEqualTo(3);
        assertThat(mockFunction.argsCalled()).containsExactly("foo");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void with_string_lambda() {
        Applying<String, Integer> mockFunction = MockFunction.applying(String::length);
        int result = IntScopeFunctions.with("foo", value -> mockFunction.apply(value));
        assertThat(result).isEqualTo(3);
        assertThat(mockFunction.argsCalled()).containsExactly("foo");
    }

    /** {@link IntScopeFunctions#map(Object, ToIntFunction)} **/

    @Test
    @SuppressWarnings("MethodRefCanBeReplacedWithLambda")
    public void map_string_reference() {
        Applying<String, Integer> mockFunction = MockFunction.applying(String::length);
        assertThat(IntScopeFunctions.map(NULL, mockFunction::apply)).isEqualTo(0);
        assertThat(IntScopeFunctions.map("foo", mockFunction::apply)).isEqualTo(3);
        assertThat(mockFunction.argsCalled()).containsExactly("foo");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void map_string_lambda() {
        Applying<String, Integer> mockFunction = MockFunction.applying(String::length);
        assertThat(IntScopeFunctions.map(NULL, value -> mockFunction.apply(value))).isEqualTo(0);
        assertThat(IntScopeFunctions.map("foo", value -> mockFunction.apply(value))).isEqualTo(3);
        assertThat(mockFunction.argsCalled()).containsExactly("foo");
    }
}
