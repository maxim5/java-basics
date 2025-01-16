package io.spbx.util.func;

import com.carrotsearch.hppc.IntHashSet;
import com.carrotsearch.hppc.IntSet;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

@Tag("fast")
public class IntMatcherTest {
    @Test
    public void matcher_test_simple() {
        assertThat(allMatching(IntMatcher.ASCII_LETTERS).size()).isEqualTo(52);
        assertThat(allMatching(IntMatcher.ASCII_LOWERCASE).size()).isEqualTo(26);
        assertThat(allMatching(IntMatcher.ASCII_UPPERCASE).size()).isEqualTo(26);
        assertThat(allMatching(IntMatcher.DIGITS).size()).isEqualTo(10);
        assertThat(allMatching(IntMatcher.HEX).size()).isEqualTo(16 + 6);
        assertThat(allMatching(IntMatcher.ALPHA_NUM).size()).isEqualTo(63);
        assertThat(allMatching(IntMatcher.BASE64_URL).size()).isEqualTo(64);
    }

    private static @NotNull IntSet allMatching(@NotNull IntMatcher matcher) {
        IntHashSet set = new IntHashSet();
        for (int i = 0; i < 128; i++) {
            if (matcher.test(i)) {
                set.add(i);
            }
        }
        return set;
    }
}
