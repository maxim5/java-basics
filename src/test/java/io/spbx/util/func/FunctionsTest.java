package io.spbx.util.func;

import io.spbx.util.base.EasyCast;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class FunctionsTest {
    @Test
    public void chain_three_types_functions() {
        Function<Integer, String> chain = Functions.chain(Integer::bitCount, Integer::toHexString);
        assertThat(chain.apply(0xff)).isEqualTo("8");
        assertThat(chain.apply(0xffff)).isEqualTo("10");
    }

    @Test
    public void chain_bool_predicate_and_function() {
        Function<String, Integer> chain = Functions.chain("foo"::endsWith, EasyCast::castToInt);
        assertThat(chain.apply("foo")).isEqualTo(1);
        assertThat(chain.apply("bar")).isEqualTo(0);
    }

    @Test
    public void nonNullify() {
        Function<Object, String> chain = Functions.nonNullify(Object::toString, "NULL");
        assertThat(chain.apply(1)).isEqualTo("1");
        assertThat(chain.apply(null)).isEqualTo("NULL");
    }

    @Test
    public void bypassNull() {
        Function<Object, String> chain = Functions.bypassNull(Object::toString);
        assertThat(chain.apply(1)).isEqualTo("1");
        assertThat(chain.apply(null)).isEqualTo(null);
    }
}
