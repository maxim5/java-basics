package io.spbx.util.security;

import io.spbx.util.base.ops.ByteOps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

@Immutable
public class Secret {
    public static final Secret EMPTY = new Secret(ByteOps.EMPTY_ARRAY);

    private final byte[] key;

    private Secret(byte @NotNull[] key) {
        this.key = key;
    }

    public static @NotNull Secret of(byte @Nullable[] key) {
        return key != null ? new Secret(Arrays.copyOf(key, key.length)) : EMPTY;
    }

    public static @NotNull Secret of(@Nullable String key, @NotNull Charset charset) {
        return key != null ? new Secret(key.getBytes(charset)) : EMPTY;
    }

    public static @NotNull Secret ofAscii(@Nullable String key) {
        return Secret.of(key, StandardCharsets.US_ASCII);
    }

    public boolean isEmpty() {
        return key.length == 0;
    }

    public boolean isNotEmpty() {
        return key.length > 0;
    }

    public @NotNull String toHumanReadableString(@NotNull Charset charset) {
        return new String(key, charset);
    }

    public @NotNull String toHumanReadableAsciiString() {
        return new String(key, StandardCharsets.US_ASCII);
    }

    public @Nullable String toNonEmptyHumanReadableStringOrNull(@NotNull Charset charset) {
        return isNotEmpty() ? new String(key, charset) : null;
    }

    public @Nullable String toNonEmptyHumanReadableAsciiStringOrNull() {
        return isNotEmpty() ? new String(key, StandardCharsets.US_ASCII) : null;
    }

    public byte @NotNull[] toByteArray() {
        return Arrays.copyOf(key, key.length);
    }

    public byte @Nullable[] toNonEmptyByteArray() {
        return isNotEmpty() ? Arrays.copyOf(key, key.length) : null;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof Secret that && Objects.deepEquals(this.key, that.key);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(key);
    }

    @Override
    public String toString() {
        return "[%s%s***%s]".formatted(charAt(0, '?'), charAt(1, '?'), charAt(key.length - 1, '?'));
    }

    private char charAt(int i, char def) {
        return i < 0 || i >= key.length || key[i] < 32 ? def : (char) key[i];
    }
}
