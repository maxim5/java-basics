package io.spbx.util.extern.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.str.BasicParsing.DECIMAL;
import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.TestingBytes.asByteBuf;
import static io.spbx.util.testing.TestingBytes.assertBytes;

@Tag("fast")
public class NettyByteBufsTest {
    public static final byte DASH = (byte) '-';
    public static final byte UNDER = (byte) '_';

    @Test
    public void copyToByteArray_simple() {
        assertBytes(NettyByteBufs.copyToByteArray(asByteBuf(""))).isEqualTo("");
        assertBytes(NettyByteBufs.copyToByteArray(asByteBuf("foo"))).isEqualTo("foo");
        assertBytes(NettyByteBufs.copyToByteArray(asByteBuf("\0"))).isEqualTo("\0");
    }

    @Test
    public void copyToByteArray_copy() {
        ByteBuf byteBuf = asByteBuf("foo");
        assertBytes(NettyByteBufs.copyToByteArray(byteBuf)).isEqualTo("foo");
        assertBytes(byteBuf).isEqualTo("foo");
    }

    @Test
    public void readUntil_simple() {
        assertBytes(NettyByteBufs.readUntil(asByteBuf("foo-bar"), DASH, 0, 100)).isEqualTo("foo");
        assertBytes(NettyByteBufs.readUntil(asByteBuf("foo--bar"), DASH, 0, 100)).isEqualTo("foo");
        assertBytes(NettyByteBufs.readUntil(asByteBuf("foo-bar"), DASH, 0, 3)).isNull();
        assertBytes(NettyByteBufs.readUntil(asByteBuf("foo-bar"), UNDER, 0, 100)).isNull();

        assertBytes(NettyByteBufs.readUntil(asByteBuf("---"), DASH, 0, 100)).isEqualTo("");
        assertBytes(NettyByteBufs.readUntil(asByteBuf("---"), DASH, 0, 1)).isEqualTo("");
        assertBytes(NettyByteBufs.readUntil(asByteBuf("---"), DASH, 0, 0)).isNull();

        assertBytes(NettyByteBufs.readUntil(asByteBuf("-"), DASH, 0, 100)).isEqualTo("");
        assertBytes(NettyByteBufs.readUntil(asByteBuf("-"), DASH, 0, 0)).isNull();

        assertBytes(NettyByteBufs.readUntil(asByteBuf(""), DASH, 0, 100)).isNull();
        assertBytes(NettyByteBufs.readUntil(asByteBuf(""), DASH, 0, 1)).isNull();
        assertBytes(NettyByteBufs.readUntil(asByteBuf(""), DASH, 0, 0)).isNull();
    }

    @Test
    public void readUntil_readIndex() {
        ByteBuf content = asByteBuf("foo-bar-baz");
        assertBytes(NettyByteBufs.readUntil(content, DASH, 0, 100)).isEqualTo("foo");
        assertBytes(content).isEqualTo("bar-baz");
        assertBytes(NettyByteBufs.readUntil(content, DASH, 0, 100)).isEqualTo("bar");
        assertBytes(content).isEqualTo("baz");
        assertBytes(NettyByteBufs.readUntil(content, DASH, 0, 100)).isNull();
    }

    @Test
    public void readUntil_readIndex_empty() {
        ByteBuf content = asByteBuf("---");
        assertBytes(NettyByteBufs.readUntil(content, DASH, 0, 100)).isEqualTo("");
        assertBytes(content).isEqualTo("--");
        assertBytes(NettyByteBufs.readUntil(content, DASH, 0, 100)).isEqualTo("");
        assertBytes(content).isEqualTo("-");
        assertBytes(NettyByteBufs.readUntil(content, DASH, 0, 100)).isEqualTo("");
        assertBytes(content).isEqualTo("");
        assertBytes(NettyByteBufs.readUntil(content, DASH, 0, 100)).isNull();
    }

    @Test
    public void parseInt_simple() {
        assertThat(NettyByteBufs.parseInt(asByteBuf("0"))).isEqualTo(0);
        assertThat(NettyByteBufs.parseInt(asByteBuf("123"))).isEqualTo(123);

        assertThat(NettyByteBufs.parseInt(asByteBuf("1"), DECIMAL)).isEqualTo(1);
        assertThat(NettyByteBufs.parseInt(asByteBuf("9"), DECIMAL)).isEqualTo(9);
        assertThat(NettyByteBufs.parseInt(asByteBuf("123"), DECIMAL)).isEqualTo(123);
        assertThat(NettyByteBufs.parseInt(asByteBuf("12345678"), DECIMAL)).isEqualTo(12345678);
        assertThat(NettyByteBufs.parseInt(asByteBuf("0"), DECIMAL)).isEqualTo(0);

        assertFailure(() -> NettyByteBufs.parseInt(asByteBuf(""), DECIMAL)).throwsNumberFormatException();
        assertFailure(() -> NettyByteBufs.parseInt(asByteBuf("foo"), DECIMAL)).throwsNumberFormatException();
        assertFailure(() -> NettyByteBufs.parseInt(asByteBuf("1+2"), DECIMAL)).throwsNumberFormatException();
    }

    @Test
    public void parseInt_plus_or_minus() {
        assertThat(NettyByteBufs.parseInt(asByteBuf("+0"), DECIMAL)).isEqualTo(0);
        assertThat(NettyByteBufs.parseInt(asByteBuf("+00"), DECIMAL)).isEqualTo(0);
        assertThat(NettyByteBufs.parseInt(asByteBuf("-0"), DECIMAL)).isEqualTo(0);
        assertThat(NettyByteBufs.parseInt(asByteBuf("-00"), DECIMAL)).isEqualTo(0);

        assertThat(NettyByteBufs.parseInt(asByteBuf("+1"), DECIMAL)).isEqualTo(1);
        assertThat(NettyByteBufs.parseInt(asByteBuf("+100"), DECIMAL)).isEqualTo(100);
        assertThat(NettyByteBufs.parseInt(asByteBuf("-1"), DECIMAL)).isEqualTo(-1);
        assertThat(NettyByteBufs.parseInt(asByteBuf("-100"), DECIMAL)).isEqualTo(-100);
    }

    @Test
    public void parseInt_edge_cases() {
        assertThat(NettyByteBufs.parseInt(asByteBuf("2147483647"), DECIMAL)).isEqualTo(Integer.MAX_VALUE);
        assertThat(NettyByteBufs.parseInt(asByteBuf("-2147483648"), DECIMAL)).isEqualTo(Integer.MIN_VALUE);

        assertThat(NettyByteBufs.parseInt(asByteBuf("2147483646"), DECIMAL)).isEqualTo(Integer.MAX_VALUE - 1);
        assertThat(NettyByteBufs.parseInt(asByteBuf("-2147483647"), DECIMAL)).isEqualTo(Integer.MIN_VALUE + 1);

        assertFailure(() -> NettyByteBufs.parseInt(asByteBuf("2147483648"), DECIMAL)).throwsNumberFormatException();
        assertFailure(() -> NettyByteBufs.parseInt(asByteBuf("-2147483649"), DECIMAL)).throwsNumberFormatException();
        assertFailure(() -> NettyByteBufs.parseInt(asByteBuf("9223372036854775807"), DECIMAL)).throwsNumberFormatException();
        assertFailure(() -> NettyByteBufs.parseInt(asByteBuf("-9223372036854775808"), DECIMAL)).throwsNumberFormatException();
    }

    @Test
    public void parseIntSafe_simple() {
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("1"), 0)).isEqualTo(1);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("9"), 0)).isEqualTo(9);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("123"), 0)).isEqualTo(123);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("12345678"), 0)).isEqualTo(12345678);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("0"), -1)).isEqualTo(0);

        assertThat(NettyByteBufs.parseIntSafe(asByteBuf(""), -1)).isEqualTo(-1);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("foo"), -1)).isEqualTo(-1);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("1+2"), -1)).isEqualTo(-1);
    }

    @Test
    public void parseIntSafe_plus_or_minus() {
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("+0"), -1)).isEqualTo(0);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("+00"), -1)).isEqualTo(0);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("-0"), -1)).isEqualTo(0);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("-00"), -1)).isEqualTo(0);

        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("+1"), 0)).isEqualTo(1);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("+100"), 0)).isEqualTo(100);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("-1"), 0)).isEqualTo(-1);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("-100"), 0)).isEqualTo(-100);
    }

    @Test
    public void parseIntSafe_edge_cases() {
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("2147483647"), 0)).isEqualTo(Integer.MAX_VALUE);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("-2147483648"), 0)).isEqualTo(Integer.MIN_VALUE);

        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("2147483646"), 0)).isEqualTo(Integer.MAX_VALUE - 1);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("-2147483647"), 0)).isEqualTo(Integer.MIN_VALUE + 1);

        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("2147483648"), 0)).isEqualTo(0);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("-2147483649"), 0)).isEqualTo(0);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("9223372036854775807"), 0)).isEqualTo(0);
        assertThat(NettyByteBufs.parseIntSafe(asByteBuf("-9223372036854775808"), 0)).isEqualTo(0);
    }

    @Test
    public void parseLong_simple() {
        assertThat(NettyByteBufs.parseLong(asByteBuf("0"))).isEqualTo(0);
        assertThat(NettyByteBufs.parseLong(asByteBuf("123"))).isEqualTo(123);

        assertThat(NettyByteBufs.parseLong(asByteBuf("1"), DECIMAL)).isEqualTo(1);
        assertThat(NettyByteBufs.parseLong(asByteBuf("9"), DECIMAL)).isEqualTo(9);
        assertThat(NettyByteBufs.parseLong(asByteBuf("123"), DECIMAL)).isEqualTo(123);
        assertThat(NettyByteBufs.parseLong(asByteBuf("12345678"), DECIMAL)).isEqualTo(12345678);
        assertThat(NettyByteBufs.parseLong(asByteBuf("0"), DECIMAL)).isEqualTo(0);

        assertFailure(() -> NettyByteBufs.parseLong(asByteBuf(""), DECIMAL)).throwsNumberFormatException();
        assertFailure(() -> NettyByteBufs.parseLong(asByteBuf("foo"), DECIMAL)).throwsNumberFormatException();
        assertFailure(() -> NettyByteBufs.parseLong(asByteBuf("1+2"), DECIMAL)).throwsNumberFormatException();
    }

    @Test
    public void parseLong_plus_or_minus() {
        assertThat(NettyByteBufs.parseLong(asByteBuf("+0"), DECIMAL)).isEqualTo(0);
        assertThat(NettyByteBufs.parseLong(asByteBuf("+00"), DECIMAL)).isEqualTo(0);
        assertThat(NettyByteBufs.parseLong(asByteBuf("-0"), DECIMAL)).isEqualTo(0);
        assertThat(NettyByteBufs.parseLong(asByteBuf("-00"), DECIMAL)).isEqualTo(0);

        assertThat(NettyByteBufs.parseLong(asByteBuf("+1"), DECIMAL)).isEqualTo(1);
        assertThat(NettyByteBufs.parseLong(asByteBuf("+100"), DECIMAL)).isEqualTo(100);
        assertThat(NettyByteBufs.parseLong(asByteBuf("-1"), DECIMAL)).isEqualTo(-1);
        assertThat(NettyByteBufs.parseLong(asByteBuf("-100"), DECIMAL)).isEqualTo(-100);
    }

    @Test
    public void parseLong_edge_cases() {
        assertThat(NettyByteBufs.parseLong(asByteBuf("9223372036854775807"), DECIMAL)).isEqualTo(Long.MAX_VALUE);
        assertThat(NettyByteBufs.parseLong(asByteBuf("-9223372036854775808"), DECIMAL)).isEqualTo(Long.MIN_VALUE);

        assertThat(NettyByteBufs.parseLong(asByteBuf("9223372036854775806"), DECIMAL)).isEqualTo(Long.MAX_VALUE - 1);
        assertThat(NettyByteBufs.parseLong(asByteBuf("-9223372036854775807"), DECIMAL)).isEqualTo(Long.MIN_VALUE + 1);

        assertFailure(() -> NettyByteBufs.parseLong(asByteBuf("9223372036854775808"), DECIMAL)).throwsNumberFormatException();
        assertFailure(() -> NettyByteBufs.parseLong(asByteBuf("-9223372036854775809"), DECIMAL)).throwsNumberFormatException();
    }

    @Test
    public void parseLongSafe_simple() {
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("1"), 0)).isEqualTo(1);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("9"), 0)).isEqualTo(9);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("123"), 0)).isEqualTo(123);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("12345678"), 0)).isEqualTo(12345678);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("0"), -1)).isEqualTo(0);

        assertThat(NettyByteBufs.parseLongSafe(asByteBuf(""), -1)).isEqualTo(-1);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("foo"), -1)).isEqualTo(-1);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("1+2"), -1)).isEqualTo(-1);
    }

    @Test
    public void parseLongSafe_plus_or_minus() {
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("+0"), -1)).isEqualTo(0);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("+00"), -1)).isEqualTo(0);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("-0"), -1)).isEqualTo(0);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("-00"), -1)).isEqualTo(0);

        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("+1"), 0)).isEqualTo(1);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("+100"), 0)).isEqualTo(100);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("-1"), 0)).isEqualTo(-1);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("-100"), 0)).isEqualTo(-100);
    }

    @Test
    public void parseLongSafe_edge_cases() {
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("9223372036854775807"), 0)).isEqualTo(Long.MAX_VALUE);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("-9223372036854775808"), 0)).isEqualTo(Long.MIN_VALUE);

        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("9223372036854775806"), 0)).isEqualTo(Long.MAX_VALUE - 1);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("-9223372036854775807"), 0)).isEqualTo(Long.MIN_VALUE + 1);

        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("9223372036854775808"), 0)).isEqualTo(0);
        assertThat(NettyByteBufs.parseLongSafe(asByteBuf("-9223372036854775809"), 0)).isEqualTo(0);
    }

    @Test
    public void writeIntString_simple() {
        assertBytes(withNewBuffer(content -> NettyByteBufs.writeIntString(0, content))).isEqualTo("0");
        assertBytes(withNewBuffer(content -> NettyByteBufs.writeIntString(101, content))).isEqualTo("101");
        assertBytes(withNewBuffer(content -> NettyByteBufs.writeIntString(-101, content))).isEqualTo("-101");
    }

    @Test
    public void writeLongString_simple() {
        assertBytes(withNewBuffer(content -> NettyByteBufs.writeLongString(0, content))).isEqualTo("0");
        assertBytes(withNewBuffer(content -> NettyByteBufs.writeLongString(101, content))).isEqualTo("101");
        assertBytes(withNewBuffer(content -> NettyByteBufs.writeLongString(-101, content))).isEqualTo("-101");
    }

    private static @NotNull ByteBuf withNewBuffer(@NotNull Consumer<ByteBuf> consumer) {
        ByteBuf buffer = Unpooled.buffer(8);
        consumer.accept(buffer);
        return buffer;
    }
}
