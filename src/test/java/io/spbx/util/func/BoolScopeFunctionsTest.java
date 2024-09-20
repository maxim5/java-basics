package io.spbx.util.func;

import io.spbx.util.testing.MockFunction;
import io.spbx.util.testing.MockFunction.Applying;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class BoolScopeFunctionsTest {
    @Test
    public void with_simple() {
        Applying<String, Boolean> mockFunction = MockFunction.applying(String::isBlank);
        boolean result = BoolScopeFunctions.with("   ", mockFunction);
        assertThat(result).isEqualTo(true);
        assertThat(mockFunction.argsCalled()).containsExactly("   ");
    }
}
