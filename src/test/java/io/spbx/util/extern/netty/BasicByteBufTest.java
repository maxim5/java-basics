package io.spbx.util.extern.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBytes.asByteBuf;
import static io.spbx.util.testing.TestingBytes.assertBytes;


public class BasicByteBufTest {
    public static final byte DASH = (byte) '-';
    public static final byte UNDER = (byte) '_';

    @Test
    public void copyToByteArray_simple() {
        assertBytes(BasicByteBuf.copyToByteArray(asByteBuf(""))).isEqualTo("");
        assertBytes(BasicByteBuf.copyToByteArray(asByteBuf("foo"))).isEqualTo("foo");
        assertBytes(BasicByteBuf.copyToByteArray(asByteBuf("\0"))).isEqualTo("\0");
    }

    @Test
    public void copyToByteArray_copy() {
        ByteBuf byteBuf = asByteBuf("foo");
        assertBytes(BasicByteBuf.copyToByteArray(byteBuf)).isEqualTo("foo");
        assertBytes(byteBuf).isEqualTo("foo");
    }

    @Test
    public void readUntil_simple() {
        assertBytes(BasicByteBuf.readUntil(asByteBuf("foo-bar"), DASH, 0, 100)).isEqualTo("foo");
        assertBytes(BasicByteBuf.readUntil(asByteBuf("foo--bar"), DASH, 0, 100)).isEqualTo("foo");
        assertBytes(BasicByteBuf.readUntil(asByteBuf("foo-bar"), DASH, 0, 3)).isNull();
        assertBytes(BasicByteBuf.readUntil(asByteBuf("foo-bar"), UNDER, 0, 100)).isNull();

        assertBytes(BasicByteBuf.readUntil(asByteBuf("---"), DASH, 0, 100)).isEqualTo("");
        assertBytes(BasicByteBuf.readUntil(asByteBuf("---"), DASH, 0, 1)).isEqualTo("");
        assertBytes(BasicByteBuf.readUntil(asByteBuf("---"), DASH, 0, 0)).isNull();

        assertBytes(BasicByteBuf.readUntil(asByteBuf("-"), DASH, 0, 100)).isEqualTo("");
        assertBytes(BasicByteBuf.readUntil(asByteBuf("-"), DASH, 0, 0)).isNull();

        assertBytes(BasicByteBuf.readUntil(asByteBuf(""), DASH, 0, 100)).isNull();
        assertBytes(BasicByteBuf.readUntil(asByteBuf(""), DASH, 0, 1)).isNull();
        assertBytes(BasicByteBuf.readUntil(asByteBuf(""), DASH, 0, 0)).isNull();
    }

    @Test
    public void readUntil_readIndex() {
        ByteBuf content = asByteBuf("foo-bar-baz");
        assertBytes(BasicByteBuf.readUntil(content, DASH, 0, 100)).isEqualTo("foo");
        assertBytes(content).isEqualTo("bar-baz");
        assertBytes(BasicByteBuf.readUntil(content, DASH, 0, 100)).isEqualTo("bar");
        assertBytes(content).isEqualTo("baz");
        assertBytes(BasicByteBuf.readUntil(content, DASH, 0, 100)).isNull();
    }

    @Test
    public void readUntil_readIndex_empty() {
        ByteBuf content = asByteBuf("---");
        assertBytes(BasicByteBuf.readUntil(content, DASH, 0, 100)).isEqualTo("");
        assertBytes(content).isEqualTo("--");
        assertBytes(BasicByteBuf.readUntil(content, DASH, 0, 100)).isEqualTo("");
        assertBytes(content).isEqualTo("-");
        assertBytes(BasicByteBuf.readUntil(content, DASH, 0, 100)).isEqualTo("");
        assertBytes(content).isEqualTo("");
        assertBytes(BasicByteBuf.readUntil(content, DASH, 0, 100)).isNull();
    }

    @Test
    public void parseIntSafe_simple() {
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("1"), 0)).isEqualTo(1);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("9"), 0)).isEqualTo(9);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("123"), 0)).isEqualTo(123);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("12345678"), 0)).isEqualTo(12345678);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("0"), -1)).isEqualTo(0);

        assertThat(BasicByteBuf.parseIntSafe(asByteBuf(""), -1)).isEqualTo(-1);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("foo"), -1)).isEqualTo(-1);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("1+2"), -1)).isEqualTo(-1);
    }

    @Test
    public void parseIntSafe_plus_or_minus() {
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("+0"), -1)).isEqualTo(0);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("+00"), -1)).isEqualTo(0);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("-0"), -1)).isEqualTo(0);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("-00"), -1)).isEqualTo(0);

        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("+1"), 0)).isEqualTo(1);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("+100"), 0)).isEqualTo(100);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("-1"), 0)).isEqualTo(-1);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("-100"), 0)).isEqualTo(-100);
    }

    @Test
    public void parseIntSafe_edge_cases() {
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("2147483647"), 0)).isEqualTo(Integer.MAX_VALUE);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("-2147483648"), 0)).isEqualTo(Integer.MIN_VALUE);

        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("2147483646"), 0)).isEqualTo(Integer.MAX_VALUE - 1);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("-2147483647"), 0)).isEqualTo(Integer.MIN_VALUE + 1);

        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("2147483648"), 0)).isEqualTo(0);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("-2147483649"), 0)).isEqualTo(0);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("9223372036854775807"), 0)).isEqualTo(0);
        assertThat(BasicByteBuf.parseIntSafe(asByteBuf("-9223372036854775808"), 0)).isEqualTo(0);
    }

    @Test
    public void parseLongSafe_simple() {
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("1"), 0)).isEqualTo(1);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("9"), 0)).isEqualTo(9);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("123"), 0)).isEqualTo(123);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("12345678"), 0)).isEqualTo(12345678);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("0"), -1)).isEqualTo(0);

        assertThat(BasicByteBuf.parseLongSafe(asByteBuf(""), -1)).isEqualTo(-1);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("foo"), -1)).isEqualTo(-1);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("1+2"), -1)).isEqualTo(-1);
    }

    @Test
    public void parseLongSafe_plus_or_minus() {
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("+0"), -1)).isEqualTo(0);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("+00"), -1)).isEqualTo(0);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("-0"), -1)).isEqualTo(0);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("-00"), -1)).isEqualTo(0);

        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("+1"), 0)).isEqualTo(1);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("+100"), 0)).isEqualTo(100);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("-1"), 0)).isEqualTo(-1);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("-100"), 0)).isEqualTo(-100);
    }

    @Test
    public void parseLongSafe_edge_cases() {
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("9223372036854775807"), 0)).isEqualTo(Long.MAX_VALUE);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("-9223372036854775808"), 0)).isEqualTo(Long.MIN_VALUE);

        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("9223372036854775806"), 0)).isEqualTo(Long.MAX_VALUE - 1);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("-9223372036854775807"), 0)).isEqualTo(Long.MIN_VALUE + 1);

        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("9223372036854775808"), 0)).isEqualTo(0);
        assertThat(BasicByteBuf.parseLongSafe(asByteBuf("-9223372036854775809"), 0)).isEqualTo(0);
    }

    @Test
    public void writeIntString_simple() {
        assertBytes(withNewBuffer(content -> BasicByteBuf.writeIntString(0, content))).isEqualTo("0");
        assertBytes(withNewBuffer(content -> BasicByteBuf.writeIntString(101, content))).isEqualTo("101");
        assertBytes(withNewBuffer(content -> BasicByteBuf.writeIntString(-101, content))).isEqualTo("-101");
    }

    @Test
    public void writeLongString_simple() {
        assertBytes(withNewBuffer(content -> BasicByteBuf.writeLongString(0, content))).isEqualTo("0");
        assertBytes(withNewBuffer(content -> BasicByteBuf.writeLongString(101, content))).isEqualTo("101");
        assertBytes(withNewBuffer(content -> BasicByteBuf.writeLongString(-101, content))).isEqualTo("-101");
    }

    private static @NotNull ByteBuf withNewBuffer(@NotNull Consumer<ByteBuf> consumer) {
        ByteBuf buffer = Unpooled.buffer(8);
        consumer.accept(buffer);
        return buffer;
    }
}
