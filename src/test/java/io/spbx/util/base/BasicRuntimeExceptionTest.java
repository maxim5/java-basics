package io.spbx.util.base;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.arrayOf;

@Tag("fast")
public class BasicRuntimeExceptionTest {
    private static final String NULL = null;

    @Test
    public void formatMsg_simple() {
        assertThat(BasicRuntimeException.formatMsg("", arrayOf())).isEqualTo("");
        assertThat(BasicRuntimeException.formatMsg("foo", arrayOf())).isEqualTo("foo");

        assertThat(BasicRuntimeException.formatMsg("foo: %s", arrayOf("bar"))).isEqualTo("foo: bar");
        assertThat(BasicRuntimeException.formatMsg("foo: %d", arrayOf(1))).isEqualTo("foo: 1");
        assertThat(BasicRuntimeException.formatMsg("foo: %s", arrayOf(NULL))).isEqualTo("foo: null");
        assertThat(BasicRuntimeException.formatMsg("foo: `%s`, `%d`", arrayOf(1, 2))).isEqualTo("foo: `1`, `2`");
        assertThat(BasicRuntimeException.formatMsg("foo: `%s:%s`", arrayOf(NULL, NULL))).isEqualTo("foo: `null:null`");

        assertThat(BasicRuntimeException.formatMsg("foo", arrayOf("bar"))).isEqualTo("foo bar");
        assertThat(BasicRuntimeException.formatMsg("foo", arrayOf(1))).isEqualTo("foo 1");
        assertThat(BasicRuntimeException.formatMsg("foo", arrayOf(NULL))).isEqualTo("foo null");
        assertThat(BasicRuntimeException.formatMsg("foo ", arrayOf("bar"))).isEqualTo("foo bar");
        assertThat(BasicRuntimeException.formatMsg("foo ", arrayOf(1))).isEqualTo("foo 1");
        assertThat(BasicRuntimeException.formatMsg("foo ", arrayOf(NULL))).isEqualTo("foo null");
    }
}
