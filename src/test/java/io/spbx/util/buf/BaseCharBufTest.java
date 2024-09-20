package io.spbx.util.buf;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.buf.BaseCharBuf.*;

@Tag("fast")
public class BaseCharBufTest {
    @Test
    public void hashCode_simple() {
        int expected = 1219042;  // BaseCharBuf.hashCode(chars("foo"));

        assertThat(BaseCharBuf.hashCode(chars("foo"))).isEqualTo(expected);
        assertThat(BaseCharBuf.hashCode(chars("-foo-"), 1, 4)).isEqualTo(expected);
        assertThat(BaseCharBuf.hashCode(chars("-foo-"), 1, 4, ch -> ch)).isEqualTo(expected);
        assertThat(BaseCharBuf.hashCode(chars("-FOO-"), 1, 4, Character::toLowerCase)).isEqualTo(expected);
        assertThat(BaseCharBuf.hashCode("foo", 3, String::charAt)).isEqualTo(expected);
        assertThat(BaseCharBuf.hashCode("foobar", 3, String::charAt)).isEqualTo(expected);
        assertThat(BaseCharBuf.hashCode("FOO", 3, (s, index) -> Character.toLowerCase(s.charAt(index)))).isEqualTo(expected);

        assertThat(BaseCharBuf.hashCode(chars("foo"), HASH_SEED, HASH_LEFT, HASH_RIGHT)).isEqualTo(expected);
        assertThat(BaseCharBuf.hashCode(chars("-foo-"), 1, 4, HASH_SEED, HASH_LEFT, HASH_RIGHT)).isEqualTo(expected);
        assertThat(BaseCharBuf.hashCode(chars("-foo-"), 1, 4, ch -> ch, HASH_SEED, HASH_LEFT, HASH_RIGHT)).isEqualTo(expected);
        assertThat(BaseCharBuf.hashCode(chars("-FOO-"), 1, 4, Character::toLowerCase, HASH_SEED, HASH_LEFT, HASH_RIGHT)).isEqualTo(expected);
        assertThat(BaseCharBuf.hashCode("foo", 3, String::charAt, HASH_SEED, HASH_LEFT, HASH_RIGHT)).isEqualTo(expected);
        assertThat(BaseCharBuf.hashCode("foobar", 3, String::charAt, HASH_SEED, HASH_LEFT, HASH_RIGHT)).isEqualTo(expected);
        assertThat(BaseCharBuf.hashCode("FOO", 3, (s, index) -> Character.toLowerCase(s.charAt(index)), HASH_SEED, HASH_LEFT, HASH_RIGHT)).isEqualTo(expected);
    }

    private static char @NotNull[] chars(@NotNull String s) {
        return s.toCharArray();
    }

    class Str extends BaseCharBuf<Str> {
        protected Str(char @NotNull[] chars, int start, int end) {
            super(chars, start, end);
        }
        @Override protected @NotNull Str _this() {
            return this;
        }
        @Override protected @NotNull Str _wrap(char @NotNull[] chars, int start, int end) {
            return new Str(chars, start, end);
        }
    }
}
