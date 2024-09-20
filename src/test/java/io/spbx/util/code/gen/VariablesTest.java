package io.spbx.util.code.gen;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.mapOf;

@Tag("fast")
public class VariablesTest {
    @Test
    public void fixUpKeys_simple() {
        assertThat(Variables.fixUpKeys(mapOf("foo", "bar")).toMap()).containsExactly("$foo$", "bar").inOrder();
        assertThat(Variables.fixUpKeys(mapOf("foo", null)).toMap()).containsExactly("$foo$", "").inOrder();
        assertThat(Variables.fixUpKeys(mapOf("foo", 123)).toMap()).containsExactly("$foo$", "123").inOrder();
    }

    @Test
    public void mergeAndOverwriteBy_simple() {
        Variables vars1 = Variables.of("$foo", "1", "$bar", "2");
        Variables vars2 = Variables.of("$foo", "3", "$baz", "4");
        assertThat(vars1.mergeAndOverwriteBy(vars2).toMap()).containsExactly("$foo", "3", "$bar", "2", "$baz", "4");
    }

    @Test
    public void interpolate_simple() {
        assertThat(Variables.of("$foo", "bar").interpolate("")).isEqualTo("");
        assertThat(Variables.of("$foo", "bar").interpolate("$")).isEqualTo("$");
        assertThat(Variables.of("$foo", "bar").interpolate("$foo")).isEqualTo("bar");
        assertThat(Variables.of("$foo", "bar").interpolate("$$foo")).isEqualTo("$bar");
        assertThat(Variables.of("$foo", "bar").interpolate("$foo$foo")).isEqualTo("barbar");
        assertThat(Variables.of("$foo", "bar").interpolate("$foo + foo")).isEqualTo("bar + foo");
    }

    @Test
    public void interpolate_double() {
        assertThat(Variables.of(mapOf("$foo", "[$bar]", "$bar", "123")).interpolate("$foo")).isEqualTo("[123]");
    }
}
