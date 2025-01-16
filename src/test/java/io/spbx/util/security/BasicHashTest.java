package io.spbx.util.security;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.spbx.util.testing.TestingBytes.assertBytes;
import static io.spbx.util.testing.TestingPrimitives.bytes;

@Tag("fast")
public class BasicHashTest {
    @Test
    public void xor_simple() {
        assertBytes(BasicHash.xor(bytes(1, 2, 3), bytes(1))).containsExactlyInOrder(0, 3, 2);
        assertBytes(BasicHash.xor(bytes(1, 2, 3), bytes(1, 2))).containsExactlyInOrder(0, 0, 2);
        assertBytes(BasicHash.xor(bytes(1, 2, 3), bytes(1, 2, 3))).containsExactlyInOrder(0, 0, 0);
        assertBytes(BasicHash.xor(bytes(1, 2, 3), bytes(1, 2, 3, 4))).containsExactlyInOrder(0, 0, 0);

        assertBytes(BasicHash.xor(bytes(1, 2, 3), bytes(0))).containsExactlyInOrder(1, 2, 3);
        assertBytes(BasicHash.xor(bytes(1, 2, 3), bytes(0, 0))).containsExactlyInOrder(1, 2, 3);
        assertBytes(BasicHash.xor(bytes(1, 2, 3), bytes(0, 0, 0))).containsExactlyInOrder(1, 2, 3);
        assertBytes(BasicHash.xor(bytes(1, 2, 3), bytes(0, 0, 0, 0))).containsExactlyInOrder(1, 2, 3);
    }
}
