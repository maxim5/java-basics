package io.spbx.util.code.jvm;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.code.jvm.JavaNumberValidator.isValidJavaUnsignedIntegerLiteral;
import static io.spbx.util.code.jvm.JavaNumberValidator.isValidJavaUnsignedLongLiteral;

public class JavaNumberValidatorTest {
    @Test
    public void unsigned_integer_simple() {
        assertThat(isValidJavaUnsignedIntegerLiteral("0")).isTrue();
        assertThat(isValidJavaUnsignedIntegerLiteral("123")).isTrue();
        assertThat(isValidJavaUnsignedIntegerLiteral("123_456")).isTrue();

        assertThat(isValidJavaUnsignedIntegerLiteral("00")).isTrue();
        assertThat(isValidJavaUnsignedIntegerLiteral("0123")).isTrue();
        assertThat(isValidJavaUnsignedIntegerLiteral("0123_456")).isTrue();

        assertThat(isValidJavaUnsignedIntegerLiteral("0x00")).isTrue();
        assertThat(isValidJavaUnsignedIntegerLiteral("0xff")).isTrue();
        assertThat(isValidJavaUnsignedIntegerLiteral("0xabcdef")).isTrue();
        assertThat(isValidJavaUnsignedIntegerLiteral("0xFFFF_FFFF")).isTrue();

        assertThat(isValidJavaUnsignedIntegerLiteral("0L")).isFalse();
        assertThat(isValidJavaUnsignedIntegerLiteral("_123")).isFalse();
    }

    @Test
    public void unsigned_long_simple() {
        assertThat(isValidJavaUnsignedLongLiteral("0")).isTrue();
        assertThat(isValidJavaUnsignedLongLiteral("0l")).isTrue();
        assertThat(isValidJavaUnsignedLongLiteral("0L")).isTrue();
        assertThat(isValidJavaUnsignedLongLiteral("123")).isTrue();
        assertThat(isValidJavaUnsignedLongLiteral("123L")).isTrue();
        assertThat(isValidJavaUnsignedLongLiteral("123_456")).isTrue();
        assertThat(isValidJavaUnsignedLongLiteral("123_456L")).isTrue();

        assertThat(isValidJavaUnsignedLongLiteral("00")).isTrue();
        assertThat(isValidJavaUnsignedLongLiteral("0123")).isTrue();
        assertThat(isValidJavaUnsignedLongLiteral("0123_456")).isTrue();
        assertThat(isValidJavaUnsignedLongLiteral("0123_456L")).isTrue();

        assertThat(isValidJavaUnsignedLongLiteral("0x00")).isTrue();
        assertThat(isValidJavaUnsignedLongLiteral("0xff")).isTrue();
        assertThat(isValidJavaUnsignedLongLiteral("0xabcdef")).isTrue();
        assertThat(isValidJavaUnsignedLongLiteral("0xFFFF_FFFF")).isTrue();
        assertThat(isValidJavaUnsignedLongLiteral("0xFFFF_FFFFl")).isTrue();

        assertThat(isValidJavaUnsignedLongLiteral("_123")).isFalse();
        assertThat(isValidJavaUnsignedLongLiteral("_123L")).isFalse();
        assertThat(isValidJavaUnsignedLongLiteral("0x_00")).isFalse();
    }
}
