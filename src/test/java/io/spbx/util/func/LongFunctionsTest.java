package io.spbx.util.func;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.LongFunction;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class LongFunctionsTest {
    @Test
    public void long_chain_functions() {
        LongFunction<String> chain = LongFunctions.chain(count -> "a".repeat((int) count), String::toUpperCase);
        assertThat(chain.apply(3)).isEqualTo("AAA");
    }
}
