package io.spbx.util.base.security;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class SecretTest {
    @Test
    public void simple_ascii() {
        Secret secret = Secret.ofAscii("foobar");
        assertThat(secret.toHumanReadableAsciiString()).isEqualTo("foobar");
        assertThat(secret.toNonEmptyHumanReadableAsciiStringOrNull()).isEqualTo("foobar");
        assertThat(secret.toHumanReadableString(StandardCharsets.UTF_8)).isEqualTo("foobar");
        assertThat(secret.toNonEmptyHumanReadableStringOrNull(StandardCharsets.UTF_8)).isEqualTo("foobar");
        assertThat(secret.toByteArray()).isEqualTo("foobar".getBytes());
        assertThat(secret.toNonEmptyByteArray()).isEqualTo("foobar".getBytes());
        assertThat(secret.toString()).isEqualTo("[fo***r]");
    }

    @Test
    public void simple_empty() {
        Secret secret = Secret.ofAscii("");
        assertThat(secret.toHumanReadableAsciiString()).isEmpty();
        assertThat(secret.toNonEmptyHumanReadableAsciiStringOrNull()).isNull();
        assertThat(secret.toHumanReadableString(StandardCharsets.UTF_8)).isEmpty();
        assertThat(secret.toNonEmptyHumanReadableStringOrNull(StandardCharsets.UTF_8)).isNull();
        assertThat(secret.toByteArray()).isEqualTo(new byte[0]);
        assertThat(secret.toNonEmptyByteArray()).isNull();
        assertThat(secret.toString()).isEqualTo("[??***?]");
    }

    @Test
    public void simple_utf8() {
        //noinspection UnnecessaryUnicodeEscape
        String key = "\u043F\u0440\u0438\u0432\u0435\u0442";
        Secret secret = Secret.of(key, StandardCharsets.UTF_8);
        assertThat(secret.toHumanReadableString(StandardCharsets.UTF_8)).isEqualTo(key);
        assertThat(secret.toNonEmptyHumanReadableStringOrNull(StandardCharsets.UTF_8)).isEqualTo(key);
        assertThat(secret.toByteArray()).isEqualTo(key.getBytes(StandardCharsets.UTF_8));
        assertThat(secret.toNonEmptyByteArray()).isEqualTo(key.getBytes(StandardCharsets.UTF_8));
        assertThat(secret.toString()).isEqualTo("[??***?]");
    }
}
