package io.spbx.util.code.gen;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.spbx.util.code.gen.CompiledTemplate.Block;
import io.spbx.util.code.gen.CompiledTemplate.CompiledDirective;
import io.spbx.util.code.gen.CompiledTemplate.DirectiveBlock;
import io.spbx.util.code.gen.CompiledTemplate.LiteralBlock;
import io.spbx.util.code.gen.Directive.Predefined;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.code.gen.DirectiveBuilder.commentBlock;
import static io.spbx.util.testing.TestingBasics.listOf;

public class TemplateCompilerTest {
    @Test
    public void simple_empty() {
        String template = "";
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly();
    }

    @Test
    public void simple_only_literals() {
        String template = """
            foo
            bar
        """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(literal("    foo", "    bar"));
    }

    @Test
    public void simple_only_literals_with_empty_line() {
        String template = """
            foo

            bar
        """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(literal("    foo", "", "    bar"));
    }

    @Test
    public void simple_block_comment() {
        String template = """
            foo
            /*~ The text! ~*/
            bar
            """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(literal("foo", ""), comment("The text!"), literal("bar"));
    }

    @Test
    public void simple_inline_comment() {
        String template = """
            foo
            //~ The text!
            bar
            """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(literal("foo", ""), comment("The text!"), literal("bar"));
    }

    @Test
    public void simple_one_block() {
        String template = """
            /*= foo-start =*/
            foo
            /*= foo-end =*/
        """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(block(directiveOf("foo"), "    foo"));
    }

    @Test
    public void simple_one_block_without_start_suffix() {
        String template = """
            /*= foo =*/
            foo
            /*= foo-end =*/
        """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(block(directiveOf("foo"), "    foo"));
    }

    @Test
    public void simple_one_block_extra_literals_on_the_same_line() {
        String template = """
            foo /*= foo-start =*/ bar
            foo
            bar /*= foo-end =*/ foo
        """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            literal("    foo "),
            block(directiveOf("foo"), " bar", "    foo", "    bar "),
            literal(" foo")
        );
    }

    @Test
    public void simple_inline_block() {
        String template = """
            foo
            //= foo =//
            bar
            """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            literal("foo", ""),
            block(directiveOf("foo")),
            literal("bar")
        );
    }

    @Test
    public void simple_inline_block_with_literal() {
        String template = """
            foo //= baz
            bar
        """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            literal("    foo "),
            block(directiveOf("baz")),
            literal("    bar")
        );
    }

    @Test
    public void simple_nested_block() {
        String template = """
            /*= foo-start =*/
                /*= bar-start =*/
                    foobar
                /*= bar-end =*/
            /*= foo-end =*/
        """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            block(directiveOf("foo"), listOf(
                block(directiveOf("bar"), "            foobar")
            ))
        );
    }

    @Test
    public void simple_literals_and_nested_block() {
        String template = """
            baz
            
            /*= foo-start =*/
            foo
            /*= bar-start =*/
            foobar
            /*= bar-end =*/
            bar
            /*= foo-end =*/
            """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            literal("baz", ""),
            block(directiveOf("foo"), listOf(
                literal("foo"),
                block(directiveOf("bar"), "foobar"),
                literal("bar")
            ))
        );
    }

    @Test
    public void simple_if_block() {
        String template = """
            /*= if =*/
            foo
            /*= end =*/
        """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(block(directiveOf(Predefined.IF), "    foo"));
    }

    @Test
    public void simple_if_condition_block() {
        String template = """
            /*= if $foo$ == bar =*/
            foo
            /*= end =*/
        """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(block(directiveOf(Predefined.IF).attrs("$foo$ == bar"), "    foo"));
    }

    @Test
    public void simple_if_else_block() {
        String template = """
            /*= if =*/
            foo
            /*= else =*/
            bar
            /*= end =*/
        """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            block(directiveOf(Predefined.IF), "    foo"),
            block(directiveOf(Predefined.ELSE), "    bar")
        );
    }

    @Test
    public void simple_if_else_condition_block() {
        String template = """
            /*= if $foo$ == bar =*/
            foo
            /*= else =*/
            bar
            /*= end =*/
        """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            block(directiveOf(Predefined.IF).attrs("$foo$ == bar"), "    foo"),
            block(directiveOf(Predefined.ELSE).attrs("$foo$ == bar"), "    bar")
        );
    }

    @Test
    public void simple_remove() {
        String template = """
            foo
            foo /*= remove =*/
            bar //= remove
            bar
            """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            literal("foo", "foo "),
            block(directiveOf(Predefined.REMOVE)),
            literal("bar "),
            block(directiveOf(Predefined.REMOVE)),
            literal("bar")
        );
    }

    @Test
    public void simple_placeholder() {
        String template = """
            foo
            /*= placeholder foo =*/
            bar
            """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            literal("foo", ""),
            block(directiveOf(Predefined.PLACEHOLDER).attrs("foo")),
            literal("bar")
        );
    }

    @Test
    public void simple_import() {
        String template = """
            foo
            /*= import baz =*/
            bar
            """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            literal("foo", ""),
            block(directiveOf(Predefined.IMPORT).attrs("baz")),
            literal("bar")
        );
    }

    @Test
    public void simple_end_of_template() {
        String template = """
            foo
            bar
            /*= EOT =*/
            baz
            """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            literal("foo", "bar"),
            block(directiveOf(Predefined.EOD_OF_TEMPLATE), listOf(literal("baz")))
        );
    }

    @Test
    public void simple_end_of_template_inline() {
        String template = """
            foo //= EOT
            bar
            baz
            """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            literal("foo "),
            block(directiveOf(Predefined.EOD_OF_TEMPLATE), listOf(literal("bar", "baz")))
        );
    }

    @Test
    public void nested_if_import_blocks() {
        String template = """
            /*= if $foo$ =*/
                /*= import file block =*/
            /*= end =*/
        """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            block(directiveOf(Predefined.IF).attrs("$foo$"), listOf(
                literal("        "),
                block(directiveOf(Predefined.IMPORT).attrs("file block"))
            ))
        );
    }

    @Test
    public void nested_with_if_blocks() {
        String template = """
            /*= with $foo$ =*/
                /*= if $bar$ =*/
                /*= end =*/
            /*= end =*/
        """;
        CompiledTemplate compiled = TemplateCompiler.compile(template.lines(), JavaMarking.JAVA_MARK);
        assertTemplate(compiled).containsExactly(
            block(directiveOf(Predefined.WITH).attrs("$foo$"), listOf(
                block(directiveOf(Predefined.IF).attrs("$bar$"))
            ))
        );
    }

    private static @NotNull DirectiveBuilder directiveOf(@NotNull String name) {
        return DirectiveBuilder.block().name(name);
    }

    private static @NotNull DirectiveBuilder directiveOf(@NotNull Predefined predefined) {
        return DirectiveBuilder.block().predef(predefined);
    }

    private static @NotNull LiteralBlock literal(@NotNull String @NotNull ... lines) {
        return LiteralBlock.of(listOf(lines));
    }

    private static @NotNull Block comment(@NotNull String text) {
        return DirectiveBlock.of(CompiledDirective.from(commentBlock(text).build()), listOf());
    }

    private static @NotNull Block block(@NotNull DirectiveBuilder builder, @NotNull String @NotNull ... lines) {
        return DirectiveBlock.of(CompiledDirective.from(builder.build()), lines.length > 0 ? listOf(literal(lines)) : listOf());
    }

    private static @NotNull Block block(@NotNull DirectiveBuilder builder, @NotNull List<Block> blocks) {
        return DirectiveBlock.of(CompiledDirective.from(builder.build()), blocks);
    }

    @CheckReturnValue
    private static @NotNull CompiledTemplateSubject assertTemplate(@NotNull CompiledTemplate template) {
        return new CompiledTemplateSubject(template);
    }

    @CanIgnoreReturnValue
    private record CompiledTemplateSubject(@NotNull CompiledTemplate template) {
        public @NotNull CompiledTemplateSubject containsExactly(@NotNull Block @NotNull ... expected) {
            assertThat(template.blocks()).containsExactlyElementsIn(expected);
            return this;
        }
    }
}
