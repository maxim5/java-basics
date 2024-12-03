package io.spbx.util.func;

import io.spbx.util.testing.func.MockConsumer;
import io.spbx.util.testing.func.MockFunction;
import io.spbx.util.testing.func.MockFunction.Applying;
import io.spbx.util.testing.func.MockRunnable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class BoolScopeFunctionsTest {
    private static final String NULL = null;

    /** {@link BoolScopeFunctions#also(boolean, Consumer)} **/

    @Test
    @SuppressWarnings({ "resource", "FunctionalExpressionCanBeFolded" })
    public void also_consumer_reference() {
        MockConsumer<Boolean, RuntimeException> mockConsumer = MockConsumer.mock();
        assertThat(BoolScopeFunctions.also(false, mockConsumer::accept)).isFalse();
        assertThat(BoolScopeFunctions.also(true, mockConsumer::accept)).isTrue();
        assertThat(mockConsumer.argsCalled()).containsExactly(false, true);
    }

    @Test
    @SuppressWarnings({ "Convert2MethodRef", "FunctionalExpressionCanBeFolded", "resource" })
    public void also_consumer_lambda() {
        MockConsumer<Boolean, RuntimeException> mockConsumer = MockConsumer.mock();
        assertThat(BoolScopeFunctions.also(false, value -> mockConsumer.accept(value))).isFalse();
        assertThat(BoolScopeFunctions.also(true, value -> mockConsumer.accept(value))).isTrue();
        assertThat(mockConsumer.argsCalled()).containsExactly(false, true);
    }

    /** {@link BoolScopeFunctions#also(boolean, Consumer)} **/

    @Test
    @SuppressWarnings("FunctionalExpressionCanBeFolded")
    public void also_runnable_reference() {
        MockRunnable mockRunnable = new MockRunnable();
        assertThat(BoolScopeFunctions.also(false, mockRunnable::run)).isFalse();
        assertThat(BoolScopeFunctions.also(true, mockRunnable::run)).isTrue();
        assertThat(mockRunnable.timesCalled()).isEqualTo(2);
    }

    @Test
    @SuppressWarnings({ "FunctionalExpressionCanBeFolded", "Convert2MethodRef" })
    public void also_runnable_lambda() {
        MockRunnable mockRunnable = new MockRunnable();
        assertThat(BoolScopeFunctions.also(false, () -> mockRunnable.run())).isFalse();
        assertThat(BoolScopeFunctions.also(true, () -> mockRunnable.run())).isTrue();
        assertThat(mockRunnable.timesCalled()).isEqualTo(2);
    }

    /** {@link BoolScopeFunctions#with(Object, Predicate)} **/

    @Test
    public void with_object_to_boolean_reference() {
        Applying<String, Boolean> mockFunction = MockFunction.applying(String::isBlank);
        boolean result = BoolScopeFunctions.with("   ", mockFunction::apply);
        assertThat(result).isEqualTo(true);
        assertThat(mockFunction.argsCalled()).containsExactly("   ");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void with_object_to_boolean_lambda() {
        Applying<String, Boolean> mockFunction = MockFunction.applying(String::isBlank);
        boolean result = BoolScopeFunctions.with("   ", value -> mockFunction.apply(value));
        assertThat(result).isEqualTo(true);
        assertThat(mockFunction.argsCalled()).containsExactly("   ");
    }

    /** {@link BoolScopeFunctions#with(boolean, Function)} **/

    @Test
    @SuppressWarnings("FunctionalExpressionCanBeFolded")
    public void with_boolean_to_object_reference() {
        Applying<Boolean, String> mockFunction = MockFunction.applying(String::valueOf);
        String result = BoolScopeFunctions.with(true, mockFunction::apply);
        assertThat(result).isEqualTo("true");
        assertThat(mockFunction.argsCalled()).containsExactly(true);
    }

    @Test
    @SuppressWarnings({ "Convert2MethodRef", "FunctionalExpressionCanBeFolded" })
    public void with_boolean_to_object_lambda() {
        Applying<Boolean, String> mockFunction = MockFunction.applying(String::valueOf);
        String result = BoolScopeFunctions.with(true, value -> mockFunction.apply(value));
        assertThat(result).isEqualTo("true");
        assertThat(mockFunction.argsCalled()).containsExactly(true);
    }

    /** {@link BoolScopeFunctions#map(Object, Predicate)} **/

    @Test
    public void map_reference() {
        Applying<String, Boolean> mockFunction = MockFunction.applying(String::isBlank);
        assertThat(BoolScopeFunctions.map(NULL, mockFunction::apply)).isFalse();
        assertThat(BoolScopeFunctions.map("foo", mockFunction::apply)).isFalse();
        assertThat(BoolScopeFunctions.map("   ", mockFunction::apply)).isTrue();
        assertThat(mockFunction.argsCalled()).containsExactly("foo", "   ");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void map_lambda() {
        Applying<String, Boolean> mockFunction = MockFunction.applying(String::isBlank);
        assertThat(BoolScopeFunctions.map(NULL, value -> mockFunction.apply(value))).isFalse();
        assertThat(BoolScopeFunctions.map("foo", value -> mockFunction.apply(value))).isFalse();
        assertThat(BoolScopeFunctions.map("   ", value -> mockFunction.apply(value))).isTrue();
        assertThat(mockFunction.argsCalled()).containsExactly("foo", "   ");
    }
}
