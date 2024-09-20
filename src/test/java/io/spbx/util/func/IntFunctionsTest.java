package io.spbx.util.func;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.IntFunction;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class IntFunctionsTest {
    @Test
    public void ints_chain_functions() {
        IntFunction<String> chain = IntFunctions.chain("a"::repeat, String::toUpperCase);
        assertThat(chain.apply(3)).isEqualTo("AAA");
    }
}
