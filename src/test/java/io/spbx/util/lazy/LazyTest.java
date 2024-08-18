package io.spbx.util.lazy;

import io.spbx.util.testing.MockFunction;
import io.spbx.util.testing.MockSupplier;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class LazyTest {
    @Test
    public void lazy_simple() {
        MockSupplier<String> mockSupplier = MockSupplier.mock("foo");

        Lazy<String> lazy = Lazy.of(mockSupplier);
        assertThat(mockSupplier.timesCalled()).isEqualTo(0);

        assertThat(lazy.get()).isEqualTo("foo");
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);

        assertThat(lazy.get()).isEqualTo("foo");
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
    }

    @Test
    public void lazy_transform_and_derived_called_first() {
        MockSupplier<String> mockSupplier = MockSupplier.mock("foo");
        MockFunction.Applying<String, Integer> mockFunction = MockFunction.applying(String::length);

        Lazy<String> lazy = Lazy.of(mockSupplier);
        Lazy<Integer> derived = lazy.transform(mockFunction);
        assertThat(mockSupplier.timesCalled()).isEqualTo(0);
        assertThat(mockFunction.timesCalled()).isEqualTo(0);

        assertThat(derived.get()).isEqualTo(3);
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(1);

        assertThat(derived.get()).isEqualTo(3);
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(1);

        assertThat(lazy.get()).isEqualTo("foo");
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(1);
    }

    @Test
    public void lazy_transform_and_base_called_before_transform() {
        MockSupplier<String> mockSupplier = MockSupplier.mock("foo");
        MockFunction.Applying<String, Integer> mockFunction = MockFunction.applying(String::length);

        Lazy<String> lazy = Lazy.of(mockSupplier);
        assertThat(lazy.get()).isEqualTo("foo");
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);

        Lazy<Integer> derived = lazy.transform(mockFunction);
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(0);

        assertThat(derived.get()).isEqualTo(3);
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(1);

        assertThat(derived.get()).isEqualTo(3);
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(1);

        assertThat(lazy.get()).isEqualTo("foo");
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(1);
    }

    @Test
    public void lazy_transform_bool_and_derived_called_first() {
        MockSupplier<String> mockSupplier = MockSupplier.mock("foo");
        MockFunction.Applying<String, Boolean> mockFunction = MockFunction.applying(String::isEmpty);

        Lazy<String> lazy = Lazy.of(mockSupplier);
        LazyBoolean derived = lazy.transformBool(mockFunction);
        assertThat(mockSupplier.timesCalled()).isEqualTo(0);
        assertThat(mockFunction.timesCalled()).isEqualTo(0);

        assertThat(derived.get()).isFalse();
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(1);

        assertThat(derived.get()).isFalse();
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(1);

        assertThat(lazy.get()).isEqualTo("foo");
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(1);
    }

    @Test
    public void lazy_transform_bool_and_base_called_before_transform() {
        MockSupplier<String> mockSupplier = MockSupplier.mock("foo");
        MockFunction.Applying<String, Boolean> mockFunction = MockFunction.applying(String::isEmpty);

        Lazy<String> lazy = Lazy.of(mockSupplier);
        assertThat(lazy.get()).isEqualTo("foo");
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);

        LazyBoolean derived = lazy.transformBool(mockFunction);
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(0);

        assertThat(derived.get()).isFalse();
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(1);

        assertThat(derived.get()).isFalse();
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(1);

        assertThat(lazy.get()).isEqualTo("foo");
        assertThat(mockSupplier.timesCalled()).isEqualTo(1);
        assertThat(mockFunction.timesCalled()).isEqualTo(1);
    }
}
