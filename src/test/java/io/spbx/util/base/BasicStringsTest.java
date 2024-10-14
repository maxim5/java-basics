package io.spbx.util.base;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class BasicStringsTest {
    @Test
    public void startsWith_char() {
        assertThat(BasicStrings.startsWith("", ' ')).isFalse();
        assertThat(BasicStrings.startsWith("_", ' ')).isFalse();
        assertThat(BasicStrings.startsWith("_ ", ' ')).isFalse();
        assertThat(BasicStrings.startsWith(" ", ' ')).isTrue();
        assertThat(BasicStrings.startsWith("  ", ' ')).isTrue();
        assertThat(BasicStrings.startsWith(" _", ' ')).isTrue();
    }

    @Test
    public void endsWith_char() {
        assertThat(BasicStrings.endsWith("", ' ')).isFalse();
        assertThat(BasicStrings.endsWith("_", ' ')).isFalse();
        assertThat(BasicStrings.endsWith(" _", ' ')).isFalse();
        assertThat(BasicStrings.endsWith(" ", ' ')).isTrue();
        assertThat(BasicStrings.endsWith("  ", ' ')).isTrue();
        assertThat(BasicStrings.endsWith("_ ", ' ')).isTrue();
    }

    @Test
    public void removePrefix_string() {
        assertThat(BasicStrings.removePrefix("", "")).isEqualTo("");
        assertThat(BasicStrings.removePrefix("", "foo")).isEqualTo("");

        assertThat(BasicStrings.removePrefix("foobar", "foo")).isEqualTo("bar");
        assertThat(BasicStrings.removePrefix("foobar", "")).isEqualTo("foobar");

        assertThat(BasicStrings.removePrefix("foobar", "fox")).isEqualTo("foobar");
        assertThat(BasicStrings.removePrefix("foobar", "Foo")).isEqualTo("foobar");
    }

    @Test
    public void removePrefix_char() {
        assertThat(BasicStrings.removePrefix("", ' ')).isEqualTo("");
        assertThat(BasicStrings.removePrefix(" ", ' ')).isEqualTo("");
        assertThat(BasicStrings.removePrefix("  ", ' ')).isEqualTo(" ");
        assertThat(BasicStrings.removePrefix("a", ' ')).isEqualTo("a");
        assertThat(BasicStrings.removePrefix("a ", ' ')).isEqualTo("a ");
        assertThat(BasicStrings.removePrefix(" a", ' ')).isEqualTo("a");
        assertThat(BasicStrings.removePrefix("", 'a')).isEqualTo("");
        assertThat(BasicStrings.removePrefix("a", 'a')).isEqualTo("");
        assertThat(BasicStrings.removePrefix("aa", 'a')).isEqualTo("a");
    }

    @Test
    public void removeSuffix_string() {
        assertThat(BasicStrings.removeSuffix("", "")).isEqualTo("");
        assertThat(BasicStrings.removeSuffix("", "foo")).isEqualTo("");

        assertThat(BasicStrings.removeSuffix("foobar", "bar")).isEqualTo("foo");
        assertThat(BasicStrings.removeSuffix("foobar", "")).isEqualTo("foobar");

        assertThat(BasicStrings.removeSuffix("foobar", "baz")).isEqualTo("foobar");
        assertThat(BasicStrings.removeSuffix("foobar", "Bar")).isEqualTo("foobar");
    }

    @Test
    public void removeSuffix_char() {
        assertThat(BasicStrings.removeSuffix("", ' ')).isEqualTo("");
        assertThat(BasicStrings.removeSuffix(" ", ' ')).isEqualTo("");
        assertThat(BasicStrings.removeSuffix("  ", ' ')).isEqualTo(" ");
        assertThat(BasicStrings.removeSuffix("a", ' ')).isEqualTo("a");
        assertThat(BasicStrings.removeSuffix("a ", ' ')).isEqualTo("a");
        assertThat(BasicStrings.removeSuffix(" a", ' ')).isEqualTo(" a");
        assertThat(BasicStrings.removeSuffix("", 'a')).isEqualTo("");
        assertThat(BasicStrings.removeSuffix("a", 'a')).isEqualTo("");
        assertThat(BasicStrings.removeSuffix("aa", 'a')).isEqualTo("a");
    }

    @Test
    public void ensurePrefix_string() {
        assertThat(BasicStrings.ensurePrefix("", "")).isEqualTo("");
        assertThat(BasicStrings.ensurePrefix("", "foo")).isEqualTo("foo");

        assertThat(BasicStrings.ensurePrefix("foobar", "foo")).isEqualTo("foobar");
        assertThat(BasicStrings.ensurePrefix("foobar", "")).isEqualTo("foobar");

        assertThat(BasicStrings.ensurePrefix("foobar", "fox")).isEqualTo("foxfoobar");
        assertThat(BasicStrings.ensurePrefix("foobar", "Foo")).isEqualTo("Foofoobar");
    }
    
    @Test
    public void ensurePrefix_char() {
        assertThat(BasicStrings.ensurePrefix("", ' ')).isEqualTo(" ");
        assertThat(BasicStrings.ensurePrefix(" ", ' ')).isEqualTo(" ");
        assertThat(BasicStrings.ensurePrefix("  ", ' ')).isEqualTo("  ");
        assertThat(BasicStrings.ensurePrefix("a", ' ')).isEqualTo(" a");
        assertThat(BasicStrings.ensurePrefix(" a", ' ')).isEqualTo(" a");
        assertThat(BasicStrings.ensurePrefix("  a", ' ')).isEqualTo("  a");
        assertThat(BasicStrings.ensurePrefix("a ", ' ')).isEqualTo(" a ");
    }

    @Test
    public void ensureSuffix_string() {
        assertThat(BasicStrings.ensureSuffix("", "")).isEqualTo("");
        assertThat(BasicStrings.ensureSuffix("", "foo")).isEqualTo("foo");

        assertThat(BasicStrings.ensureSuffix("foobar", "foo")).isEqualTo("foobarfoo");
        assertThat(BasicStrings.ensureSuffix("foobar", "bar")).isEqualTo("foobar");
        assertThat(BasicStrings.ensureSuffix("foobar", "")).isEqualTo("foobar");

        assertThat(BasicStrings.ensureSuffix("foobar", "fox")).isEqualTo("foobarfox");
        assertThat(BasicStrings.ensureSuffix("foobar", "Foo")).isEqualTo("foobarFoo");
    }

    @Test
    public void ensureSuffix_char() {
        assertThat(BasicStrings.ensureSuffix("", ' ')).isEqualTo(" ");
        assertThat(BasicStrings.ensureSuffix(" ", ' ')).isEqualTo(" ");
        assertThat(BasicStrings.ensureSuffix("  ", ' ')).isEqualTo("  ");
        assertThat(BasicStrings.ensureSuffix("a", ' ')).isEqualTo("a ");
        assertThat(BasicStrings.ensureSuffix("a ", ' ')).isEqualTo("a ");
        assertThat(BasicStrings.ensureSuffix("a  ", ' ')).isEqualTo("a  ");
        assertThat(BasicStrings.ensureSuffix(" a ", ' ')).isEqualTo(" a ");
    }

    @Test
    public void firstNotEmpty_simple() {
        assertThat(BasicStrings.firstNotEmpty("foo", "bar")).isEqualTo("foo");
        assertThat(BasicStrings.firstNotEmpty("foo", "")).isEqualTo("foo");
        assertThat(BasicStrings.firstNotEmpty("f", "")).isEqualTo("f");
        assertThat(BasicStrings.firstNotEmpty("", "bar")).isEqualTo("bar");
        assertThat(BasicStrings.firstNotEmpty("", "b")).isEqualTo("b");
    }
}
