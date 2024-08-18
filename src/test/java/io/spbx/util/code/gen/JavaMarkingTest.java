package io.spbx.util.code.gen;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.spbx.util.code.gen.Directive.Modifier;
import io.spbx.util.code.gen.Directive.Predefined;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.code.gen.DirectiveBuilder.*;
import static io.spbx.util.code.gen.JavaMarking.JAVA_MARK;

public class JavaMarkingTest {
    @Test
    public void compose_simple() {
        assertThat(JAVA_MARK.compose(block().predef(Predefined.REMOVE).build())).isEqualTo("/*= remove =*/");
        assertThat(JAVA_MARK.compose(inline().predef(Predefined.REMOVE).build())).isEqualTo("//= remove =//");

        assertThat(JAVA_MARK.compose(block().predef(Predefined.EOD_OF_TEMPLATE).build())).isEqualTo("/*= EOT =*/");
        assertThat(JAVA_MARK.compose(inline().predef(Predefined.EOD_OF_TEMPLATE).build())).isEqualTo("//= EOT =//");

        assertThat(JAVA_MARK.compose(block().predef(Predefined.IF).build())).isEqualTo("/*= if =*/");
        assertThat(JAVA_MARK.compose(inline().predef(Predefined.IF).build())).isEqualTo("//= if =//");

        assertThat(JAVA_MARK.compose(block().predef(Predefined.ELSE).build())).isEqualTo("/*= else =*/");
        assertThat(JAVA_MARK.compose(inline().predef(Predefined.ELSE).build())).isEqualTo("//= else =//");

        assertThat(JAVA_MARK.compose(block().name("foo").attrs("a=b").build())).isEqualTo("/*= foo a=b =*/");
        assertThat(JAVA_MARK.compose(inline().name("foo").attrs("a=b").build())).isEqualTo("//= foo a=b =//");
    }

    @Test
    public void compose_comments_simple() {
        assertThat(JAVA_MARK.compose(commentBlock("foo bar").build())).isEqualTo("/*~ foo bar ~*/");
        assertThat(JAVA_MARK.compose(commentInline("foo bar").build())).isEqualTo("//~ foo bar ~//");
    }

    @Test
    public void extract_comments_simple() {
        assertDir(JAVA_MARK.extract("/*~ Foo ~*/")).isAt(0, 11).contains(commentBlock("Foo"));
        assertDir(JAVA_MARK.extract("/*~ foo bar ~*/")).isAt(0, 15).contains(commentBlock("foo bar"));
        assertDir(JAVA_MARK.extract("/*~ foo   bar baz ~*/")).isAt(0, 21).contains(commentBlock("foo   bar baz"));

        assertDir(JAVA_MARK.extract("//~ Foo")).isAt(0, 7).contains(commentInline("Foo"));
        assertDir(JAVA_MARK.extract("//~ foo bar")).isAt(0, 11).contains(commentInline("foo bar"));
        assertDir(JAVA_MARK.extract("//~ foo bar ~//")).isAt(0, 15).contains(commentInline("foo bar"));
        assertDir(JAVA_MARK.extract("//~ foo bar ~//  baz")).isAt(0, 20).contains(commentInline("foo bar"));
    }

    @Test
    public void extract_remove_block_simple() {
        assertDir(JAVA_MARK.extract("/*= remove =*/")).isAt(0, 14).contains(block().predef(Predefined.REMOVE));
        assertDir(JAVA_MARK.extract(" /*= remove =*/ ")).isAt(1, 15).contains(block().predef(Predefined.REMOVE));
        assertDir(JAVA_MARK.extract(" /*=   remove \t =*/ ")).isAt(1, 19).contains(block().predef(Predefined.REMOVE));

        assertDir(JAVA_MARK.extract("/*= Remove =*/")).isAt(0, 14).contains(block().name("Remove"));
        assertDir(JAVA_MARK.extract("/*= REMOVE =*/")).isAt(0, 14).contains(block().name("REMOVE"));

        assertDir(JAVA_MARK.extract(" /* remove */ ")).isNull();
        assertDir(JAVA_MARK.extract(" /* remove */ ")).isNull();
        assertDir(JAVA_MARK.extract(" /*= remove */ ")).isNull();
        assertDir(JAVA_MARK.extract(" /* remove =*/ ")).isNull();
        assertDir(JAVA_MARK.extract(" /* = remove = */ ")).isNull();
    }

    @Test
    public void extract_remove_inline_simple() {
        assertDir(JAVA_MARK.extract("//= remove")).isAt(0, 10).contains(inline().predef(Predefined.REMOVE));
        assertDir(JAVA_MARK.extract("  //= remove ")).isAt(2, 13).contains(inline().predef(Predefined.REMOVE));
        assertDir(JAVA_MARK.extract("  //= remove =// ")).isAt(2, 17).contains(inline().predef(Predefined.REMOVE));
        assertDir(JAVA_MARK.extract("//= remove =//  foo")).isAt(0, 19).contains(inline().predef(Predefined.REMOVE));

        assertDir(JAVA_MARK.extract("// remove")).isNull();
        assertDir(JAVA_MARK.extract("// remove =//")).isNull();
        assertDir(JAVA_MARK.extract("// = remove")).isNull();
        assertDir(JAVA_MARK.extract("// = remove =//")).isNull();
    }

    @Test
    public void extract_remove_block_non_directive_parts() {
        assertDir(JAVA_MARK.extract("class /*= remove =*/")).isAt(6, 20).contains(block().predef(Predefined.REMOVE));
        assertDir(JAVA_MARK.extract("/*= remove =*/ {")).isAt(0, 14).contains(block().predef(Predefined.REMOVE));
        assertDir(JAVA_MARK.extract(" foo bar /*= remove =*/ baz")).isAt(9, 23).contains(block().predef(Predefined.REMOVE));
    }

    @Test
    public void extract_name_simple() {
        assertDir(JAVA_MARK.extract("/*=foo=*/")).isAt(0, 9).contains(block().name("foo"));
        assertDir(JAVA_MARK.extract("/*= foo =*/")).isAt(0, 11).contains(block().name("foo"));
        assertDir(JAVA_MARK.extract("/*= foo-bar =*/")).isAt(0, 15).contains(block().name("foo-bar"));
    }

    @Test
    public void extract_modifiers_simple() {
        assertDir(JAVA_MARK.extract("/*= start =*/")).isAt(0, 13).contains(block().with(Modifier.START).name(""));
        assertDir(JAVA_MARK.extract("/*= foo-start =*/")).isAt(0, 17).contains(block().name("foo").with(Modifier.START));

        assertDir(JAVA_MARK.extract("/*= end =*/")).isAt(0, 11).contains(block().with(Modifier.END).name(""));
        assertDir(JAVA_MARK.extract("/*= foo-end =*/")).isAt(0, 15).contains(block().name("foo").with(Modifier.END));

        assertDir(JAVA_MARK.extract("/*= placeholder =*/")).isAt(0, 19).contains(block().predef(Predefined.PLACEHOLDER));
        assertDir(JAVA_MARK.extract("/*= if =*/")).isAt(0, 10).contains(block().predef(Predefined.IF));
        assertDir(JAVA_MARK.extract("/*= else =*/")).isAt(0, 12).contains(block().predef(Predefined.ELSE));
    }

    @Test
    public void extract_attrs_simple() {
        assertDir(JAVA_MARK.extract("/*= foo foo=bar =*/")).isAt(0, 19).contains(block().name("foo").attrs("foo=bar"));

        assertDir(JAVA_MARK.extract("/*= foo bar =*/")).isAt(0, 15).contains(block().name("foo").attrs("bar"));
        assertDir(JAVA_MARK.extract("/*= foo !bar =*/")).isAt(0, 16).contains(block().name("foo").attrs("!bar"));

        assertDir(JAVA_MARK.extract("/*= foo bar='' =*/")).isAt(0, 18).contains(block().name("foo").attrs("bar=''"));
        assertDir(JAVA_MARK.extract("/*= foo bar='=' =*/")).isAt(0, 19).contains(block().name("foo").attrs("bar='='"));

        assertDir(JAVA_MARK.extract("/*= foo a=b c=d =*/")).isAt(0, 19).contains(block().name("foo").attrs("a=b c=d"));
    }

    @Test
    public void extract_eon_of_template_simple() {
        assertDir(JAVA_MARK.extract("/*= EOT =*/")).isAt(0, 11).contains(block().predef(Predefined.EOD_OF_TEMPLATE));
        assertDir(JAVA_MARK.extract("//= EOT")).isAt(0, 7).contains(inline().predef(Predefined.EOD_OF_TEMPLATE));
    }

    @Test
    public void extract_import_simple() {
        assertDir(JAVA_MARK.extract("/*= import foo =*/")).isAt(0, 18).contains(block().predef(Predefined.IMPORT).attrs("foo"));
        assertDir(JAVA_MARK.extract("/*= import file=foo =*/")).isAt(0, 23).contains(block().predef(Predefined.IMPORT).attrs("file=foo"));
    }

    @CheckReturnValue
    private static @NotNull DirectivePositionSubject assertDir(@Nullable DirectivePosition position) {
        return new DirectivePositionSubject(position);
    }

    @CanIgnoreReturnValue
    private record DirectivePositionSubject(@Nullable DirectivePosition position) {
        public void isNull() {
            assertThat(position).isNull();
        }

        public @NotNull DirectivePositionSubject isAt(int start, int end) {
            assertThat(position).isNotNull();
            assertThat(position.start()).isEqualTo(start);
            assertThat(position.end()).isEqualTo(end);
            return this;
        }

        public @NotNull DirectivePositionSubject contains(@NotNull Directive directive) {
            assertThat(position).isNotNull();
            assertThat(position.directive()).isEqualTo(directive);
            return this;
        }

        public @NotNull DirectivePositionSubject contains(@NotNull DirectiveBuilder builder) {
            return contains(builder.build());
        }
    }
}
