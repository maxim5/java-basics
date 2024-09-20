package io.spbx.util.code.gen;

import com.google.common.truth.Truth;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.spbx.util.testing.TestingBasics.*;
import static org.junit.Assert.assertThrows;

@Tag("fast")
public class CodeBuilderTest {
    @Test
    public void build_via_appendLine() {
        CodeBuilder builder = new CodeBuilder();
        assertThat(builder).containsLinesExactly();
        assertThat(builder.appendLine("foo")).containsLinesExactly("foo");
        assertThat(builder.appendLine("bar")).containsLinesExactly("foo", "bar");
    }

    @Test
    public void build_via_appendLine_parts() {
        assertThat(new CodeBuilder().appendLine("a", "b")).containsLinesExactly("ab");
        assertThat(new CodeBuilder().appendLine("a", "b", "c")).containsLinesExactly("abc");
        assertThat(new CodeBuilder().appendLine("a", "b", "c", "d")).containsLinesExactly("abcd");
    }

    @Test
    public void build_via_appendFormattedLine() {
        assertThat(new CodeBuilder().appendFormattedLine("%s", "foo")).containsLinesExactly("foo");
        assertThat(new CodeBuilder().appendFormattedLine("%s%s", "foo", "bar")).containsLinesExactly("foobar");
        assertThat(new CodeBuilder().appendFormattedLine("%d-%d-%d", 1, 2, 3)).containsLinesExactly("1-2-3");
    }

    @Test
    public void build_via_appendLines() {
        CodeBuilder builder = new CodeBuilder();
        assertThat(builder).containsLinesExactly();
        assertThat(builder.appendLines(listOf("foo"))).containsLinesExactly("foo");
        assertThat(builder.appendLines(streamOf("bar"))).containsLinesExactly("foo", "bar");
        assertThat(builder.appendLines(arrayOf("baz"))).containsLinesExactly("foo", "bar", "baz");
    }

    @Test
    public void build_via_appendMultiline() {
        CodeBuilder builder = new CodeBuilder();
        assertThat(builder).containsLinesExactly();
        assertThat(builder.appendMultiline("foo")).containsLinesExactly("foo");
        assertThat(builder.appendMultiline("\n")).containsLinesExactly("foo", "");
        assertThat(builder.appendMultiline("bar\n")).containsLinesExactly("foo", "", "bar");
    }

    @Test
    public void build_via_appendMultiline_block() {
        assertThat(new CodeBuilder().appendMultiline("foo\n")).containsLinesExactly("foo");
        assertThat(new CodeBuilder().appendMultiline("foo\nbar")).containsLinesExactly("foo", "bar");
        assertThat(new CodeBuilder().appendMultiline("foo\rbar")).containsLinesExactly("foo", "bar");
        assertThat(new CodeBuilder().appendMultiline("foo\r\nbar")).containsLinesExactly("foo", "bar");
    }

    @Test
    public void build_via_appendFormattedMultiline() {
        assertThat(new CodeBuilder().appendFormattedMultiline("%s", "foo")).containsLinesExactly("foo");
        assertThat(new CodeBuilder().appendFormattedMultiline("%s\n", "foo")).containsLinesExactly("foo");
        assertThat(new CodeBuilder().appendFormattedMultiline("%d\r\n%d", 1, 2)).containsLinesExactly("1", "2");
        assertThat(new CodeBuilder().appendFormattedMultiline("\n%d-%d", 1, 2)).containsLinesExactly("", "1-2");
    }

    @Test
    public void build_via_appendMultilines_block() {
        assertThat(new CodeBuilder().appendMultilines(listOf("foo\nbar"))).containsLinesExactly("foo", "bar");
        assertThat(new CodeBuilder().appendMultilines(streamOf("foo\nbar"))).containsLinesExactly("foo", "bar");
        assertThat(new CodeBuilder().appendMultilines(arrayOf("foo\nbar"))).containsLinesExactly("foo", "bar");
    }

    @Test
    public void transform_builder() {
        CodeBuilder builder = new CodeBuilder()
            .appendLine("  foo  ")
            .appendLine("  bar \t  ")
            .transform(String::stripTrailing);
        assertThat(builder).containsLinesExactly("  foo", "  bar");
    }

    @Test
    public void isolation() {
        CodeBuilder builder1 = new CodeBuilder();
        assertThat(builder1).containsLinesExactly();
        assertThat(builder1.appendLine("foo")).containsLinesExactly("foo");

        CodeBuilder builder2 = new CodeBuilder().appendLines(builder1);
        assertThat(builder2).containsLinesExactly("foo");

        assertThat(builder1.appendLine("bar")).containsLinesExactly("foo", "bar");
        assertThat(builder2).containsLinesExactly("foo");
    }

    @Test
    public void build_incorrectly() {
        assertThrows(AssertionError.class, () -> new CodeBuilder().appendLine("\n"));
        assertThrows(AssertionError.class, () -> new CodeBuilder().appendLine("\r"));
        assertThrows(AssertionError.class, () -> new CodeBuilder().appendLine("foo\n\rbar"));
        assertThrows(AssertionError.class, () -> new CodeBuilder().appendFormattedLine("foo\n\rbar"));
        assertThrows(AssertionError.class, () -> new CodeBuilder().appendLines(listOf("\n")));
        assertThrows(AssertionError.class, () -> new CodeBuilder().appendLines(streamOf("\n")));
        assertThrows(AssertionError.class, () -> new CodeBuilder().appendLines(arrayOf("\n")));
    }

    @CheckReturnValue
    private static @NotNull CodeBuilderSubject assertThat(@NotNull CodeBuilder snippet) {
        return new CodeBuilderSubject(snippet);
    }

    @CanIgnoreReturnValue
    private record CodeBuilderSubject(@NotNull CodeBuilder builder) {
        public @NotNull CodeBuilderSubject containsLinesExactly(@NotNull String @NotNull ... lines) {
            Truth.assertThat(builder).containsExactlyElementsIn(lines);
            return this;
        }
    }
}
