package io.spbx.util.base;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        byte[] output = new byte[input.length];
        for (int i = 0; i < output.length; i++) {
            output[i] = (byte) (input[i] ^ key[i % key.length]);
        }
        return output;
    }
}
