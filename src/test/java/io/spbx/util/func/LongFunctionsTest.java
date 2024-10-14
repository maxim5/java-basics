package io.spbx.util.func;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.TestingFunctions.isPositive;

@Tag("fast")
public class LongFunctionsTest {
    /** {@link LongFunctions#chainObj(LongFunction, Function)} **/

    @Test
    public void chain_two_functions_reference() {
        LongFunction<String> chain = LongFunctions.chainObj("%x"::formatted, String::toUpperCase);
        assertThat(chain.apply(10)).isEqualTo("A");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_two_functions_lambda() {
        LongFunction<String> chain = LongFunctions.chainObj(val -> "%x".formatted(val), s -> s.toUpperCase());
        assertThat(chain.apply(10)).isEqualTo("A");
    }

    @Test
    public void chain_bool_function_and_function_reference() {
        LongFunction<String> chain = LongFunctions.chainObj(TestingFunctions::isPositive, String::valueOf);
        assertThat(chain.apply(3)).isEqualTo("true");
        assertThat(chain.apply(0)).isEqualTo("false");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_bool_function_and_function_lambda() {
        LongFunction<String> chain = LongFunctions.chainObj(a -> isPositive(a), a -> String.valueOf(a));
        assertThat(chain.apply(3)).isEqualTo("true");
        assertThat(chain.apply(0)).isEqualTo("false");
    }

    /** {@link LongFunctions#chainBool(LongPredicate, Function)} **/

    @Test
    public void chain_bool_predicate_and_function_reference() {
        LongFunction<String> chain = LongFunctions.chainBool(TestingFunctions::isPositive, String::valueOf);
        assertThat(chain.apply(3)).isEqualTo("true");
        assertThat(chain.apply(0)).isEqualTo("false");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_bool_predicate_and_function_lambda() {
        LongFunction<String> chain = LongFunctions.chainBool(a -> isPositive(a), a -> String.valueOf(a));
        assertThat(chain.apply(3)).isEqualTo("true");
        assertThat(chain.apply(0)).isEqualTo("false");
    }
}
