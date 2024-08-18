package io.spbx.util.base;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class BasicStringsTest {
    @Test
    public void removePrefix_simple() {
        assertThat(BasicStrings.removePrefix("", "")).isEqualTo("");
        assertThat(BasicStrings.removePrefix("", "foo")).isEqualTo("");

        assertThat(BasicStrings.removePrefix("foobar", "foo")).isEqualTo("bar");
        assertThat(BasicStrings.removePrefix("foobar", "")).isEqualTo("foobar");

        assertThat(BasicStrings.removePrefix("foobar", "fox")).isEqualTo("foobar");
        assertThat(BasicStrings.removePrefix("foobar", "Foo")).isEqualTo("foobar");
    }

    @Test
    public void removeSuffix_simple() {
        assertThat(BasicStrings.removeSuffix("", "")).isEqualTo("");
        assertThat(BasicStrings.removeSuffix("", "foo")).isEqualTo("");

        assertThat(BasicStrings.removeSuffix("foobar", "bar")).isEqualTo("foo");
        assertThat(BasicStrings.removeSuffix("foobar", "")).isEqualTo("foobar");

        assertThat(BasicStrings.removeSuffix("foobar", "baz")).isEqualTo("foobar");
        assertThat(BasicStrings.removeSuffix("foobar", "Bar")).isEqualTo("foobar");
    }

    @Test
    public void ensurePrefix_simple() {
        assertThat(BasicStrings.ensurePrefix("", "")).isEqualTo("");
        assertThat(BasicStrings.ensurePrefix("", "foo")).isEqualTo("foo");

        assertThat(BasicStrings.ensurePrefix("foobar", "foo")).isEqualTo("foobar");
        assertThat(BasicStrings.ensurePrefix("foobar", "")).isEqualTo("foobar");

        assertThat(BasicStrings.ensurePrefix("foobar", "fox")).isEqualTo("foxfoobar");
        assertThat(BasicStrings.ensurePrefix("foobar", "Foo")).isEqualTo("Foofoobar");
    }
    
    @Test
    public void ensureSuffix_simple() {
        assertThat(BasicStrings.ensureSuffix("", "")).isEqualTo("");
        assertThat(BasicStrings.ensureSuffix("", "foo")).isEqualTo("foo");

        assertThat(BasicStrings.ensureSuffix("foobar", "foo")).isEqualTo("foobarfoo");
        assertThat(BasicStrings.ensureSuffix("foobar", "bar")).isEqualTo("foobar");
        assertThat(BasicStrings.ensureSuffix("foobar", "")).isEqualTo("foobar");

        assertThat(BasicStrings.ensureSuffix("foobar", "fox")).isEqualTo("foobarfox");
        assertThat(BasicStrings.ensureSuffix("foobar", "Foo")).isEqualTo("foobarFoo");
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
