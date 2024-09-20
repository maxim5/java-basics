package io.spbx.util.base;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

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
}
