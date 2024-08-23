package io.spbx.util.base;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.arrayOf;

public class FmtRuntimeExceptionTest {
    private static final String NULL = null;

    @Test
    public void formatMsg_simple() {
        assertThat(FmtRuntimeException.formatMsg("", arrayOf())).isEqualTo("");
        assertThat(FmtRuntimeException.formatMsg("foo", arrayOf())).isEqualTo("foo");

        assertThat(FmtRuntimeException.formatMsg("foo: %s", arrayOf("bar"))).isEqualTo("foo: bar");
        assertThat(FmtRuntimeException.formatMsg("foo: %d", arrayOf(1))).isEqualTo("foo: 1");
        assertThat(FmtRuntimeException.formatMsg("foo: %s", arrayOf(NULL))).isEqualTo("foo: null");
        assertThat(FmtRuntimeException.formatMsg("foo: `%s`, `%d`", arrayOf(1, 2))).isEqualTo("foo: `1`, `2`");
        assertThat(FmtRuntimeException.formatMsg("foo: `%s`, `%s`", arrayOf(NULL, NULL))).isEqualTo("foo: `null`, `null`");

        assertThat(FmtRuntimeException.formatMsg("foo", arrayOf("bar"))).isEqualTo("foo bar");
        assertThat(FmtRuntimeException.formatMsg("foo", arrayOf(1))).isEqualTo("foo 1");
        assertThat(FmtRuntimeException.formatMsg("foo", arrayOf(NULL))).isEqualTo("foo null");
        assertThat(FmtRuntimeException.formatMsg("foo ", arrayOf("bar"))).isEqualTo("foo bar");
        assertThat(FmtRuntimeException.formatMsg("foo ", arrayOf(1))).isEqualTo("foo 1");
        assertThat(FmtRuntimeException.formatMsg("foo ", arrayOf(NULL))).isEqualTo("foo null");
    }
}
