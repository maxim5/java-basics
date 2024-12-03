package io.spbx.util.base.error;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.arrayOf;

@Tag("fast")
public class BasicExceptionsTest {
    private static final String NULL = null;

    @Test
    public void newAssertionError_simple() {
        assertThat(BasicExceptions.newAssertionError("foo")).hasMessageThat().isEqualTo("foo");
        assertThat(BasicExceptions.newAssertionError("foo:", NULL)).hasMessageThat().isEqualTo("foo: null");
        assertThat(BasicExceptions.newAssertionError("foo: ", NULL)).hasMessageThat().isEqualTo("foo: null");
        assertThat(BasicExceptions.newAssertionError("foo: %s", NULL)).hasMessageThat().isEqualTo("foo: null");
        assertThat(BasicExceptions.newAssertionError("foo: `%s`", NULL)).hasMessageThat().isEqualTo("foo: `null`");
    }

    @Test
    public void newInternalError_simple() {
        assertThat(BasicExceptions.newInternalError("foo")).hasMessageThat().isEqualTo("foo");
        assertThat(BasicExceptions.newInternalError("foo:", NULL)).hasMessageThat().isEqualTo("foo: null");
        assertThat(BasicExceptions.newInternalError("foo: ", NULL)).hasMessageThat().isEqualTo("foo: null");
        assertThat(BasicExceptions.newInternalError("foo: %s", NULL)).hasMessageThat().isEqualTo("foo: null");
        assertThat(BasicExceptions.newInternalError("foo: `%s`", NULL)).hasMessageThat().isEqualTo("foo: `null`");
    }

    @Test
    public void formatMsg_array() {
        assertThat(BasicExceptions.formatMsg("", arrayOf())).isEqualTo("");
        assertThat(BasicExceptions.formatMsg("foo", arrayOf())).isEqualTo("foo");

        assertThat(BasicExceptions.formatMsg("foo: %s", arrayOf("bar"))).isEqualTo("foo: bar");
        assertThat(BasicExceptions.formatMsg("foo: %d", arrayOf(1))).isEqualTo("foo: 1");
        assertThat(BasicExceptions.formatMsg("foo: %s", arrayOf(NULL))).isEqualTo("foo: null");
        assertThat(BasicExceptions.formatMsg("foo: `%s`, `%d`", arrayOf(1, 2))).isEqualTo("foo: `1`, `2`");
        assertThat(BasicExceptions.formatMsg("foo: `%s:%s`", arrayOf(NULL, NULL))).isEqualTo("foo: `null:null`");

        assertThat(BasicExceptions.formatMsg("foo", arrayOf("bar"))).isEqualTo("foo bar");
        assertThat(BasicExceptions.formatMsg("foo", arrayOf(1))).isEqualTo("foo 1");
        assertThat(BasicExceptions.formatMsg("foo", arrayOf(NULL))).isEqualTo("foo null");
        assertThat(BasicExceptions.formatMsg("foo ", arrayOf("bar"))).isEqualTo("foo bar");
        assertThat(BasicExceptions.formatMsg("foo ", arrayOf(1))).isEqualTo("foo 1");
        assertThat(BasicExceptions.formatMsg("foo ", arrayOf(NULL))).isEqualTo("foo null");
    }

    @Test
    public void formatMsg_single_arg() {
        assertThat(BasicExceptions.formatMsg("foo: %s", "bar")).isEqualTo("foo: bar");
        assertThat(BasicExceptions.formatMsg("foo: %d", 1)).isEqualTo("foo: 1");
        assertThat(BasicExceptions.formatMsg("foo: %s", NULL)).isEqualTo("foo: null");

        assertThat(BasicExceptions.formatMsg("foo", "bar")).isEqualTo("foo bar");
        assertThat(BasicExceptions.formatMsg("foo", 1)).isEqualTo("foo 1");
        assertThat(BasicExceptions.formatMsg("foo", NULL)).isEqualTo("foo null");
        assertThat(BasicExceptions.formatMsg("foo ", "bar")).isEqualTo("foo bar");
        assertThat(BasicExceptions.formatMsg("foo ", 1)).isEqualTo("foo 1");
        assertThat(BasicExceptions.formatMsg("foo ", NULL)).isEqualTo("foo null");
    }
}
