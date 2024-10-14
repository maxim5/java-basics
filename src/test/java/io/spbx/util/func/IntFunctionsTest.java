package io.spbx.util.func;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.TestingFunctions.isPositive;

@Tag("fast")
public class IntFunctionsTest {
    /** {@link IntFunctions#chainObj(IntFunction, Function)} **/

    @Test
    public void chain_two_functions_reference() {
        IntFunction<String> chain = IntFunctions.chainObj("a"::repeat, String::toUpperCase);
        assertThat(chain.apply(3)).isEqualTo("AAA");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_two_functions_lambda() {
        IntFunction<String> chain = IntFunctions.chainObj(count -> "a".repeat(count), s -> s.toUpperCase());
        assertThat(chain.apply(3)).isEqualTo("AAA");
    }

    @Test
    public void chain_bool_function_and_function_reference() {
        IntFunction<String> chain = IntFunctions.chainObj(TestingFunctions::isPositive, String::valueOf);
        assertThat(chain.apply(3)).isEqualTo("true");
        assertThat(chain.apply(0)).isEqualTo("false");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_bool_function_and_function_lambda() {
        IntFunction<String> chain = IntFunctions.chainObj(a -> isPositive(a), a -> String.valueOf(a));
        assertThat(chain.apply(3)).isEqualTo("true");
        assertThat(chain.apply(0)).isEqualTo("false");
    }

    /** {@link IntFunctions#chainBool(IntPredicate, Function)} **/

    @Test
    public void chain_bool_predicate_and_function_reference() {
        IntFunction<String> chain = IntFunctions.chainBool(TestingFunctions::isPositive, String::valueOf);
        assertThat(chain.apply(3)).isEqualTo("true");
        assertThat(chain.apply(0)).isEqualTo("false");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_bool_predicate_and_function_lambda() {
        IntFunction<String> chain = IntFunctions.chainBool(a -> isPositive(a), a -> String.valueOf(a));
        assertThat(chain.apply(3)).isEqualTo("true");
        assertThat(chain.apply(0)).isEqualTo("false");
    }
}
