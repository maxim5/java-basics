package io.spbx.util.code.dsl.expr;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.code.dsl.expr.TestingExpr.toLexem;
import static io.spbx.util.code.dsl.expr.TestingExpr.toLexems;

@Tag("fast")
public class ExprLexerTest {
    @Test
    public void lex_letters_and_digits() {
        assertThat(ExprLexer.lex("foo")).containsExactly(toLexem("foo"));
        assertThat(ExprLexer.lex("123")).containsExactly(toLexem("123"));
        assertThat(ExprLexer.lex("foo_bar")).containsExactly(toLexem("foo_bar"));
        assertThat(ExprLexer.lex("$$$")).containsExactly(toLexem("$$$"));
        assertThat(ExprLexer.lex("$a")).containsExactly(toLexem("$a"));
        assertThat(ExprLexer.lex("$a_b0")).containsExactly(toLexem("$a_b0"));
        assertThat(ExprLexer.lex("___")).containsExactly(toLexem("___"));
        assertThat(ExprLexer.lex("123e")).containsExactly(toLexem("123e"));
        assertThat(ExprLexer.lex("e123")).containsExactly(toLexem("e123"));
        assertThat(ExprLexer.lex("123_456")).containsExactly(toLexem("123_456"));
        assertThat(ExprLexer.lex("0xFFFF")).containsExactly(toLexem("0xFFFF"));
        assertThat(ExprLexer.lex("123L")).containsExactly(toLexem("123L"));
    }

    @Test
    public void lex_spaces() {
        assertThat(ExprLexer.lex(" ")).containsExactly(toLexem(" "));
        assertThat(ExprLexer.lex("  ")).containsExactly(toLexem("  "));
        assertThat(ExprLexer.lex("\t")).containsExactly(toLexem("\t"));
        assertThat(ExprLexer.lex("\t\t")).containsExactly(toLexem("\t\t"));
        assertThat(ExprLexer.lex("\t\r")).containsExactly(toLexem("\t\r"));
        assertThat(ExprLexer.lex("  \t\r\n")).containsExactly(toLexem("  \t\r\n"));
    }

    @Test
    public void lex_punctuation() {
        assertThat(ExprLexer.lex("#")).containsExactly(toLexem("#"));
        assertThat(ExprLexer.lex("+")).containsExactly(toLexem("+"));
        assertThat(ExprLexer.lex("++")).containsExactly(toLexem("++"));
        assertThat(ExprLexer.lex("<")).containsExactly(toLexem("<"));
        assertThat(ExprLexer.lex("<<")).containsExactly(toLexem("<<"));
        assertThat(ExprLexer.lex("<<<")).containsExactly(toLexem("<<<"));
        assertThat(ExprLexer.lex(":")).containsExactly(toLexem(":"));
        assertThat(ExprLexer.lex(":=")).containsExactly(toLexem(":="));
        assertThat(ExprLexer.lex("==")).containsExactly(toLexem("=="));
        assertThat(ExprLexer.lex("===")).containsExactly(toLexem("==="));
        assertThat(ExprLexer.lex("!==")).containsExactly(toLexem("!=="));
    }

    @Test
    public void lex_quotes() {
        assertThat(ExprLexer.lex("'")).containsExactly(toLexem("'"));
        assertThat(ExprLexer.lex("`")).containsExactly(toLexem("`"));
        assertThat(ExprLexer.lex("\"")).containsExactly(toLexem("\""));

        assertThat(ExprLexer.lex("''")).containsExactlyElementsIn(toLexems("'", "'"));
        assertThat(ExprLexer.lex("``")).containsExactlyElementsIn(toLexems("`", "`"));
        assertThat(ExprLexer.lex("\"\"")).containsExactlyElementsIn(toLexems("\"", "\""));
        assertThat(ExprLexer.lex("'``'")).containsExactlyElementsIn(toLexems("'", "`", "`", "'"));

        assertThat(ExprLexer.lex("'foo'")).containsExactlyElementsIn(toLexems("'", "foo", "'"));
        assertThat(ExprLexer.lex("`foo`")).containsExactlyElementsIn(toLexems("`", "foo", "`"));
        assertThat(ExprLexer.lex("` foo `")).containsExactlyElementsIn(toLexems("`", " ", "foo", " ", "`"));

        assertThat(ExprLexer.lex("'[]'")).containsExactlyElementsIn(toLexems("'", "[", "]", "'"));
        assertThat(ExprLexer.lex("'([])'")).containsExactlyElementsIn(toLexems("'", "(", "[", "]", ")", "'"));
    }

    @Test
    public void lex_brackets() {
        assertThat(ExprLexer.lex("(")).containsExactly(toLexem("("));
        assertThat(ExprLexer.lex(")")).containsExactly(toLexem(")"));
        assertThat(ExprLexer.lex("[")).containsExactly(toLexem("["));
        assertThat(ExprLexer.lex("]")).containsExactly(toLexem("]"));
        assertThat(ExprLexer.lex("{")).containsExactly(toLexem("{"));
        assertThat(ExprLexer.lex("}")).containsExactly(toLexem("}"));

        assertThat(ExprLexer.lex("()")).containsExactlyElementsIn(toLexems("(", ")"));
        assertThat(ExprLexer.lex("((")).containsExactlyElementsIn(toLexems("(", "("));
        assertThat(ExprLexer.lex("]]")).containsExactlyElementsIn(toLexems("]", "]"));
        assertThat(ExprLexer.lex("[}")).containsExactlyElementsIn(toLexems("[", "}"));

        assertThat(ExprLexer.lex("(a)")).containsExactlyElementsIn(toLexems("(", "a", ")"));
        assertThat(ExprLexer.lex("[a]")).containsExactlyElementsIn(toLexems("[", "a", "]"));
        assertThat(ExprLexer.lex("{a}")).containsExactlyElementsIn(toLexems("{", "a", "}"));
        assertThat(ExprLexer.lex("( a)")).containsExactlyElementsIn(toLexems("(", " ", "a", ")"));
        assertThat(ExprLexer.lex("(-a)")).containsExactlyElementsIn(toLexems("(", "-", "a", ")"));
        assertThat(ExprLexer.lex("(&a)")).containsExactlyElementsIn(toLexems("(", "&", "a", ")"));
        assertThat(ExprLexer.lex("(a++)")).containsExactlyElementsIn(toLexems("(", "a", "++", ")"));
    }

    @Test
    public void lex_expression() {
        assertThat(ExprLexer.lex(" + -")).containsExactlyElementsIn(toLexems(" ", "+", " ", "-"));
        assertThat(ExprLexer.lex(" abc")).containsExactlyElementsIn(toLexems(" ", "abc"));
        assertThat(ExprLexer.lex(":abc")).containsExactlyElementsIn(toLexems(":", "abc"));

        assertThat(ExprLexer.lex("a b")).containsExactlyElementsIn(toLexems("a", " ", "b"));
        assertThat(ExprLexer.lex("a-b")).containsExactlyElementsIn(toLexems("a", "-", "b"));
        assertThat(ExprLexer.lex("a.b")).containsExactlyElementsIn(toLexems("a", ".", "b"));
        assertThat(ExprLexer.lex("a=b")).containsExactlyElementsIn(toLexems("a", "=", "b"));
        assertThat(ExprLexer.lex("a!==b")).containsExactlyElementsIn(toLexems("a", "!==", "b"));
        assertThat(ExprLexer.lex("a << b")).containsExactlyElementsIn(toLexems("a", " ", "<<", " ", "b"));
    }
}
