package io.spbx.util.func;

import io.spbx.util.base.lang.EasyCast;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class FunctionsTest {
    /** {@link Functions#nonNullify(Function, Object)} **/

    @Test
    public void nonNullify_reference() {
        Function<Object, String> chain = Functions.nonNullify(Object::toString, "NULL");
        assertThat(chain.apply(1)).isEqualTo("1");
        assertThat(chain.apply(null)).isEqualTo("NULL");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void nonNullify_lambda() {
        Function<Object, String> chain = Functions.nonNullify(o -> o.toString(), "NULL");
        assertThat(chain.apply(1)).isEqualTo("1");
        assertThat(chain.apply(null)).isEqualTo("NULL");
    }

    /** {@link Functions#bypassNull(Function)} **/

    @Test
    public void bypassNull_reference() {
        Function<Object, String> chain = Functions.bypassNull(Object::toString);
        assertThat(chain.apply(1)).isEqualTo("1");
        assertThat(chain.apply(null)).isEqualTo(null);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void bypassNull_lambda() {
        Function<Object, String> chain = Functions.bypassNull(o -> o.toString());
        assertThat(chain.apply(1)).isEqualTo("1");
        assertThat(chain.apply(null)).isEqualTo(null);
    }

    /** {@link Functions#chain(Function, Function)} **/

    @Test
    public void chain_two_functions_three_types_reference() {
        Function<Integer, String> chain = Functions.chain(Integer::bitCount, Integer::toHexString);
        assertThat(chain.apply(0xff)).isEqualTo("8");
        assertThat(chain.apply(0xffff)).isEqualTo("10");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_two_functions_three_types_lambda() {
        Function<Integer, String> chain = Functions.chain(i -> Integer.bitCount(i), j -> Integer.toHexString(j));
        assertThat(chain.apply(0xff)).isEqualTo("8");
        assertThat(chain.apply(0xffff)).isEqualTo("10");
    }

    @Test
    public void chain_bool_function_and_function_reference() {
        Function<String, Integer> chain = Functions.chain("foo"::endsWith, EasyCast::castToInt);
        assertThat(chain.apply("foo")).isEqualTo(1);
        assertThat(chain.apply("bar")).isEqualTo(0);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_bool_function_and_function_lambda() {
        Function<String, Integer> chain = Functions.chain(s -> "foo".endsWith(s), b -> EasyCast.castToInt(b));
        assertThat(chain.apply("foo")).isEqualTo(1);
        assertThat(chain.apply("bar")).isEqualTo(0);
    }

    /** {@link Functions#chain(Function, Function, Function)} **/

    @Test
    public void chain_three_functions_four_types_reference() {
        Function<Integer, Integer> chain = Functions.chain(Integer::bitCount, Integer::toHexString, String::length);
        assertThat(chain.apply(0xff)).isEqualTo(1);
        assertThat(chain.apply(0xffff)).isEqualTo(2);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_three_functions_four_types_lambda() {
        Function<Integer, Integer> chain = Functions.chain(i -> Integer.bitCount(i), j -> Integer.toHexString(j), s -> s.length());
        assertThat(chain.apply(0xff)).isEqualTo(1);
        assertThat(chain.apply(0xffff)).isEqualTo(2);
    }

    /** {@link Functions#chain(BiFunction, Function)} **/

    @Test
    public void chain_bifunction_and_function_reference() {
        BiFunction<Integer, Integer, String> chain = Functions.chain(Integer::sum, Integer::toHexString);
        assertThat(chain.apply(1, 2)).isEqualTo("3");
        assertThat(chain.apply(7, 8)).isEqualTo("f");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_bifunction_and_function_lambda() {
        BiFunction<Integer, Integer, String> chain = Functions.chain((a, b) -> a + b, i -> Integer.toHexString(i));
        assertThat(chain.apply(1, 2)).isEqualTo("3");
        assertThat(chain.apply(7, 8)).isEqualTo("f");
    }

    @Test
    public void chain_bool_bool_bifunction_and_function_reference() {
        BiFunction<String, String, Integer> chain = Functions.chain(String::contains, EasyCast::castToInt);
        assertThat(chain.apply("ab", "a")).isEqualTo(1);
        assertThat(chain.apply("a", "b")).isEqualTo(0);
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_bool_bool_bifunction_and_function_lambda() {
        BiFunction<String, String, Integer> c = Functions.chain((s1, s2) -> s1.contains(s2), b -> EasyCast.castToInt(b));
        assertThat(c.apply("ab", "a")).isEqualTo(1);
        assertThat(c.apply("a", "b")).isEqualTo(0);
    }

    /** {@link Functions#chain(Function, Function, BiFunction)} **/

    @Test
    public void chain_three_functions_five_types_reference() {
        BiFunction<Integer, Integer, String> c = Functions.chain(String::valueOf, Integer::toHexString, (x, y) -> x + y);
        assertThat(c.apply(0, 15)).isEqualTo("0f");
        assertThat(c.apply(16, 16)).isEqualTo("1610");
    }

    @Test
    @SuppressWarnings("Convert2MethodRef")
    public void chain_three_functions_five_types_lambda() {
        BiFunction<Integer, Integer, String> chain =
            Functions.chain(i -> String.valueOf(i), j -> Integer.toHexString(j), (x, y) -> x + y);
        assertThat(chain.apply(0, 15)).isEqualTo("0f");
        assertThat(chain.apply(16, 16)).isEqualTo("1610");
    }
}
