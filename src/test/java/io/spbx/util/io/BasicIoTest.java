package io.spbx.util.io;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Base64;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class BasicIoTest {
    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 3, 4, 10, 15, 16, 20 })
    public void base64Length_simple(int sourceLength) {
        assertThat(BasicIo.base64Length(sourceLength)).isEqualTo(trueBase64LengthWithPadding(sourceLength));
        assertThat(BasicIo.base64LengthNoPadding(sourceLength)).isEqualTo(trueBase64LengthNoPadding(sourceLength));
    }

    private static int trueBase64LengthWithPadding(int sourceLength) {
        return Base64.getUrlEncoder().encodeToString(new byte[sourceLength]).length();
    }

    private static int trueBase64LengthNoPadding(int sourceLength) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(new byte[sourceLength]).length();
    }
}
