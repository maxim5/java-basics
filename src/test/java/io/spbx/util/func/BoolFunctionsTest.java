package io.spbx.util.func;

import io.spbx.util.base.lang.EasyCast;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.TestingFunctions.and;

@Tag("fast")
public class BoolFunctionsTest {
    /** {@link BoolFunctions#chain(Predicate, Function)} **/

    @Test
    public void chain_predicate_and_function_reference() {
        Function<String, Integer> chain = BoolFunctions.chain("foo"::endsWith, EasyCast::castToInt);
        assertThat(chain.apply("foo")).isEqualTo(1);
        assertThat(chain.apply("bar")).isEqualTo(0);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_predicate_and_function_lambda() {
        Function<String, Integer> chain = BoolFunctions.chain(s -> "foo".endsWith(s), b -> EasyCast.castToInt(b));
        assertThat(chain.apply("foo")).isEqualTo(1);
        assertThat(chain.apply("bar")).isEqualTo(0);
    }

    /** {@link BoolFunctions#chain(BiPredicate, Function)} **/

    @Test
    public void chain_bipredicate_and_function_reference() {
        BiFunction<String, String, Integer> chain = BoolFunctions.chain(String::contains, EasyCast::castToInt);
        assertThat(chain.apply("ab", "a")).isEqualTo(1);
        assertThat(chain.apply("a", "b")).isEqualTo(0);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_bipredicate_and_function_lambda() {
        BiFunction<String, String, Integer> c = BoolFunctions.chain((s1, s2) -> s1.contains(s2), b -> EasyCast.castToInt(b));
        assertThat(c.apply("ab", "a")).isEqualTo(1);
        assertThat(c.apply("a", "b")).isEqualTo(0);
    }

    /** {@link BoolFunctions#chain(Predicate, Predicate, BiFunction)} **/

    @Test
    public void chain_three_functions_five_types_reference() {
        BiFunction<String, String, Boolean> chain =
            BoolFunctions.chain("foo"::startsWith, "bar"::endsWith, TestingFunctions::and);
        assertThat(chain.apply("foo", "bar")).isTrue();
        assertThat(chain.apply("baz", "baz")).isFalse();
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_three_functions_five_types_lambda() {
        BiFunction<String, String, Boolean> c =
            BoolFunctions.chain(s -> "foo".startsWith(s), s -> "bar".endsWith(s), (x, y) -> and(x, y));
        assertThat(c.apply("foo", "bar")).isTrue();
        assertThat(c.apply("baz", "baz")).isFalse();
    }
}
