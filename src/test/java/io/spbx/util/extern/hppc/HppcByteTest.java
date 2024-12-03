package io.spbx.util.extern.hppc;

import com.carrotsearch.hppc.ByteArrayList;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.spbx.util.testing.TestingPrimitives.bytes;
import static io.spbx.util.testing.extern.hppc.AssertHppc.assertArray;

@Tag("fast")
public class HppcByteTest {
    @Test
    public void to_array_list() {
        assertArray(HppcByte.toByteArrayList(ByteArrayList.from(bytes(1, 2, 3)))).containsExactlyInOrder(1, 2, 3);
    }
}
