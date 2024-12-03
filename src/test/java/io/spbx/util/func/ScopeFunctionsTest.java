package io.spbx.util.func;

import io.spbx.util.testing.func.MockConsumer;
import io.spbx.util.testing.func.MockFunction;
import io.spbx.util.testing.func.MockFunction.Applying;
import io.spbx.util.testing.func.MockRunnable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class ScopeFunctionsTest {
    private static final String NULL = null;

    /** {@link ScopeFunctions#also(Object, ThrowConsumer)} **/

    @Test
    @SuppressWarnings({ "resource", "FunctionalExpressionCanBeFolded" })
    public void also_consumer_reference() {
        MockConsumer<String, RuntimeException> mockConsumer = MockConsumer.mock();
        String result = ScopeFunctions.also("foo", mockConsumer::accept);
        assertThat(result).isEqualTo("foo");
        assertThat(mockConsumer.argsCalled()).containsExactly("foo");
    }

    @Test
    @SuppressWarnings({ "resource", "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void also_consumer_lambda() {
        MockConsumer<String, RuntimeException> mockConsumer = MockConsumer.mock();
        String result = ScopeFunctions.also("foo", s -> mockConsumer.accept(s));
        assertThat(result).isEqualTo("foo");
        assertThat(mockConsumer.argsCalled()).containsExactly("foo");
    }

    /** {@link ScopeFunctions#also(Object, ThrowRunnable)} **/

    @Test
    @SuppressWarnings("FunctionalExpressionCanBeFolded")
    public void also_runnable_reference() {
        MockRunnable mockRunnable = new MockRunnable();
        String result = ScopeFunctions.also("foo", mockRunnable::run);
        assertThat(result).isEqualTo("foo");
        assertThat(mockRunnable.timesCalled()).isEqualTo(1);
    }

    @Test
    @SuppressWarnings({ "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void also_runnable_lambda() {
        MockRunnable mockRunnable = new MockRunnable();
        String result = ScopeFunctions.also("foo", () -> mockRunnable.run());
        assertThat(result).isEqualTo("foo");
        assertThat(mockRunnable.timesCalled()).isEqualTo(1);
    }

    /** {@link ScopeFunctions#let(Object, ThrowConsumer)} **/

    @Test
    @SuppressWarnings({ "resource", "FunctionalExpressionCanBeFolded" })
    public void let_consumer_reference() {
        MockConsumer<String, RuntimeException> mockConsumer = MockConsumer.mock();
        assertThat(ScopeFunctions.let(NULL, mockConsumer::accept)).isNull();
        assertThat(mockConsumer.argsCalled()).isEmpty();
        assertThat(ScopeFunctions.let("foo", mockConsumer::accept)).isEqualTo("foo");
        assertThat(mockConsumer.argsCalled()).containsExactly("foo");
    }

    @Test
    @SuppressWarnings({ "resource", "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void let_consumer_lambda() {
        MockConsumer<String, RuntimeException> mockConsumer = MockConsumer.mock();
        assertThat(ScopeFunctions.let(NULL, s -> mockConsumer.accept(s))).isNull();
        assertThat(mockConsumer.argsCalled()).isEmpty();
        assertThat(ScopeFunctions.let("foo", s -> mockConsumer.accept(s))).isEqualTo("foo");
        assertThat(mockConsumer.argsCalled()).containsExactly("foo");
    }

    /** {@link ScopeFunctions#let(Object, ThrowRunnable)} **/

    @Test
    @SuppressWarnings("FunctionalExpressionCanBeFolded")
    public void let_runnable_reference() {
        MockRunnable mockRunnable = new MockRunnable();
        assertThat(ScopeFunctions.let(NULL, mockRunnable::run)).isNull();
        assertThat(mockRunnable.timesCalled()).isEqualTo(0);
        assertThat(ScopeFunctions.let("foo", mockRunnable::run)).isEqualTo("foo");
        assertThat(mockRunnable.timesCalled()).isEqualTo(1);
    }

    @Test
    @SuppressWarnings({ "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void let_runnable_lambda() {
        MockRunnable mockRunnable = new MockRunnable();
        assertThat(ScopeFunctions.let(NULL, () -> mockRunnable.run())).isNull();
        assertThat(mockRunnable.timesCalled()).isEqualTo(0);
        assertThat(ScopeFunctions.let("foo", () -> mockRunnable.run())).isEqualTo("foo");
        assertThat(mockRunnable.timesCalled()).isEqualTo(1);
    }

    /** {@link ScopeFunctions#with(Object, ThrowFunction)} **/

    @Test
    public void with_reference() {
        Applying<String, Integer> mockFunction = MockFunction.applying(String::length);
        Integer result = ScopeFunctions.with("foo", mockFunction::apply);
        assertThat(result).isEqualTo(3);
        assertThat(mockFunction.argsCalled()).containsExactly("foo");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void with_lambda() {
        Applying<String, Integer> mockFunction = MockFunction.applying(String::length);
        Integer result = ScopeFunctions.with("foo", s -> mockFunction.apply(s));
        assertThat(result).isEqualTo(3);
        assertThat(mockFunction.argsCalled()).containsExactly("foo");
    }

    /** {@link ScopeFunctions#map(Object, ThrowFunction)} **/

    @Test
    public void map_reference() {
        Applying<String, Integer> mockFunction = MockFunction.applying(String::length);
        assertThat(ScopeFunctions.<String, Integer, RuntimeException>map(NULL, mockFunction::apply)).isNull();
        assertThat(ScopeFunctions.<String, Integer, RuntimeException>map("foo", mockFunction::apply)).isEqualTo(3);
        assertThat(mockFunction.argsCalled()).containsExactly("foo");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void map_lambda() {
        Applying<String, Integer> mockFunction = MockFunction.applying(String::length);
        assertThat(ScopeFunctions.<String, Integer, RuntimeException>map(NULL, s -> mockFunction.apply(s))).isNull();
        assertThat(ScopeFunctions.<String, Integer, RuntimeException>map("foo", s -> mockFunction.apply(s))).isEqualTo(3);
        assertThat(mockFunction.argsCalled()).containsExactly("foo");
    }
}
