package io.spbx.util.base.security;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.Unchecked;
import io.spbx.util.base.ops.ByteOps;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Stateless
@Pure
@CheckReturnValue
public class BasicHash {
    public static @NotNull MessageDigest md5() {
        return newMessageDigest("MD5");
    }

    public static @NotNull MessageDigest newMessageDigest(@NotNull String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            return Unchecked.rethrow("Algorithm not found: " + algorithm, e);
        }
    }

    public static byte @NotNull[] xor(byte @NotNull[] input, byte @NotNull[] key) {
        return ByteOps.fill(input.length, i -> (byte) (input[i] ^ key[i % key.length]));
    }
}
