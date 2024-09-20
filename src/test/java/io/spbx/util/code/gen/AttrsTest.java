package io.spbx.util.code.gen;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.mapOf;

@Tag("fast")
public class AttrsTest {
    @Test
    public void toNamedMap_without_defaults() {
        assertThat(Attrs.parse("").toNamedMap()).isEmpty();
        assertThat(Attrs.parse("foo").toNamedMap()).isEmpty();
        assertThat(Attrs.parse("foo bar").toNamedMap()).isEmpty();
        assertThat(Attrs.parse("a=foo bar").toNamedMap()).containsExactly("a", "foo");
        assertThat(Attrs.parse("foo a=bar").toNamedMap()).containsExactly("a", "bar");
    }

    @Test
    public void toNamedMap_one_default() {
        assertThat(Attrs.parse("").toNamedMap("a")).containsExactly();
        assertThat(Attrs.parse("foo").toNamedMap("a")).containsExactly("a", "foo");
        assertThat(Attrs.parse("foo bar").toNamedMap("a")).containsExactly("a", "foo");
        assertThat(Attrs.parse("foo a=bar").toNamedMap("a")).containsExactly("a", "bar");
        assertThat(Attrs.parse("a=foo").toNamedMap("a")).containsExactly("a", "foo");
        assertThat(Attrs.parse("b=foo").toNamedMap("a")).containsExactly("b", "foo");
        assertThat(Attrs.parse("a=foo bar").toNamedMap("a")).containsExactly("a", "foo");
        assertThat(Attrs.parse("b=foo bar").toNamedMap("a")).containsExactly("a", "bar", "b", "foo");
        assertThat(Attrs.parse("b=foo c=bar").toNamedMap("a")).containsExactly("b", "foo", "c", "bar");
        assertThat(Attrs.parse("b=foo a=bar").toNamedMap("a")).containsExactly("a", "bar", "b", "foo");
    }

    @Test
    public void toNamedMap_two_defaults() {
        assertThat(Attrs.parse("").toNamedMap("a", "b")).containsExactly();
        assertThat(Attrs.parse("foo").toNamedMap("a", "b")).containsExactly("a", "foo");
        assertThat(Attrs.parse("foo bar").toNamedMap("a", "b")).containsExactly("a", "foo", "b", "bar");
        assertThat(Attrs.parse("foo a=bar").toNamedMap("a", "b")).containsExactly("a", "bar");
        assertThat(Attrs.parse("a=foo").toNamedMap("a", "b")).containsExactly("a", "foo");
        assertThat(Attrs.parse("b=foo").toNamedMap("a", "b")).containsExactly("b", "foo");
        assertThat(Attrs.parse("a=foo bar").toNamedMap("a", "b")).containsExactly("a", "foo");
        assertThat(Attrs.parse("b=foo bar").toNamedMap("a", "b")).containsExactly("a", "bar", "b", "foo");
        assertThat(Attrs.parse("b=foo c=bar").toNamedMap("a", "b")).containsExactly("b", "foo", "c", "bar");
        assertThat(Attrs.parse("b=foo a=bar").toNamedMap("a", "b")).containsExactly("a", "bar", "b", "foo");
    }

    @Test
    public void toNamedMap_expr() {
        assertThat(Attrs.parse("(a == b) || (b == c)").toNamedMap()).isEmpty();
        assertThat(Attrs.parse("(a == b) || (b == c) || (a == c)").toNamedMap()).isEmpty();
    }

    @Test
    public void toNamedMap_filename() {
        assertThat(Attrs.parse("`path/to/file.java`").toNamedMap("a")).containsExactly("a", "path/to/file.java");
        assertThat(Attrs.parse("`path/to/file.java` foo").toNamedMap("a")).containsExactly("a", "path/to/file.java");
        assertThat(Attrs.parse("`path/to/file.java` foo").toNamedMap("a", "b")).containsExactly("a", "path/to/file.java",
                                                                                                "b", "foo");
    }

    @Test
    public void eval_simple() {
        assertThat(Attrs.parse("$foo").eval(vars("$foo", "true"))).isTrue();
        assertThat(Attrs.parse("$foo").eval(vars("$foo", "TRUE"))).isTrue();
        assertThat(Attrs.parse("$foo").eval(vars("$foo", "false"))).isFalse();
        assertThat(Attrs.parse("$foo").eval(vars("$foo", "bar"))).isFalse();
        assertThat(Attrs.parse("$foo").eval(vars("$bar", "bar"))).isFalse();

        assertThat(Attrs.parse("! $foo").eval(vars("$foo", "true"))).isFalse();
        assertThat(Attrs.parse("! $foo").eval(vars("$foo", "false"))).isTrue();
        assertThat(Attrs.parse("! $foo").eval(vars("$bar", "true"))).isTrue();

        assertThat(Attrs.parse("$foo=$bar").eval(vars("$foo", "1", "$bar", "1"))).isTrue();
        assertThat(Attrs.parse("$foo=$bar").eval(vars("$foo", "1", "$bar", "2"))).isFalse();

        assertThat(Attrs.parse("$foo=1 $bar=2").eval(vars("$foo", "1", "$bar", "2"))).isTrue();
        assertThat(Attrs.parse("$foo=1 $bar=2").eval(vars("$foo", "1", "$bar", "1"))).isFalse();
        assertThat(Attrs.parse("$foo=1 $bar=2").eval(vars("$foo", "2", "$bar", "2"))).isFalse();
        assertThat(Attrs.parse("$foo=1 $bar=2").eval(vars("$foo", "2", "$bar", "1"))).isFalse();
        assertThat(Attrs.parse("$foo=1 $bar=2").eval(vars("$foo", "1"))).isFalse();
        assertThat(Attrs.parse("$foo=1 $bar=2").eval(vars("$bar", "2"))).isFalse();
    }

    @Test
    public void eval_expr() {
        assertThat(Attrs.parse("($foo = 1) || ($bar = 2)").eval(vars("$foo", "1", "$bar", "2"))).isTrue();
        assertThat(Attrs.parse("($foo = 1) || ($bar = 2)").eval(vars("$foo", "1", "$bar", "3"))).isTrue();
        assertThat(Attrs.parse("($foo = 1) || ($bar = 2)").eval(vars("$foo", "2", "$bar", "2"))).isTrue();
        assertThat(Attrs.parse("($foo = 1) || ($bar = 2)").eval(vars("$foo", "3", "$bar", "3"))).isFalse();

        assertThat(Attrs.parse("($a == $b) || ($b == $c)").eval(vars("$a", "1", "$b", "2", "$c", "3"))).isFalse();
        assertThat(Attrs.parse("($a == $b) || ($b == $c)").eval(vars("$a", "1", "$b", "2", "$c", "1"))).isFalse();
        assertThat(Attrs.parse("($a == $b) || ($b == $c)").eval(vars("$a", "1", "$b", "2", "$c", "2"))).isTrue();
        assertThat(Attrs.parse("($a == $b) || ($b == $c)").eval(vars("$a", "1", "$b", "1", "$c", "3"))).isTrue();
        assertThat(Attrs.parse("($a == $b) || ($b == $c) || ($a == $c)").eval(vars("$a", "1", "$b", "2", "$c", "3"))).isFalse();
        assertThat(Attrs.parse("($a == $b) || ($b == $c) || ($a == $c)").eval(vars("$a", "1", "$b", "2", "$c", "1"))).isTrue();
        assertThat(Attrs.parse("($a == $b) || ($b == $c) || ($a == $c)").eval(vars("$a", "1", "$b", "2", "$c", "2"))).isTrue();
    }

    private static @NotNull Variables vars(@NotNull String @NotNull ... attrs) {
        return Variables.of(mapOf((Object[]) attrs));
    }
}
