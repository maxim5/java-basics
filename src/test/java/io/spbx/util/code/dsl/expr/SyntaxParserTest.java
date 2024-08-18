package io.spbx.util.code.dsl.expr;

import io.spbx.util.code.dsl.expr.Sequence.Separator;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.code.dsl.expr.TestingExpr.*;

public class SyntaxParserTest {
    private static final Identifier $a = identifier("a");
    private static final Identifier $b = identifier("b");
    private static final Identifier $c = identifier("c");
    private static final Identifier $d = identifier("d");

    @Test
    public void parse_sequence_simple() {
        SyntaxParser parser = new SyntaxParser(SyntaxOptions.of());

        assertThat(parser.parseSequence("a")).isEqualTo($a);
        assertThat(parser.parseSequence(" a ")).isEqualTo($a);

        assertThat(parser.parseSequence("a b")).isEqualTo(sequence(Separator.SPACE, $a, $b));
        assertThat(parser.parseSequence(" a   b ")).isEqualTo(sequence(Separator.SPACE, $a, $b));

        assertThat(parser.parseSequence("a `foo`")).isEqualTo(sequence(Separator.SPACE, $a, literal(Quotes.BACKTICK, "foo")));
        assertThat(parser.parseSequence("a (b)"))
            .isEqualTo(sequence(Separator.SPACE, $a, inBrackets(Brackets.PARENTHESIS, $b)));
        assertThat(parser.parseSequence("a (b=c)"))
            .isEqualTo(sequence(Separator.SPACE, $a, inBrackets(Brackets.PARENTHESIS, binary($b, InfixOp.EQ, $c))));
    }

    @Test
    public void parse_term_simple() {
        SyntaxParser parser = new SyntaxParser(SyntaxOptions.of());

        assertThat(parser.parseTerm("a")).isEqualTo($a);
        assertThat(parser.parseTerm(" a ")).isEqualTo($a);

        assertThat(parser.parseTerm("  '  foo bar = baz'  ")).isEqualTo(literal(Quotes.SINGLE, "  foo bar = baz"));
        assertThat(parser.parseTerm("  `  foo bar = baz`  ")).isEqualTo(literal(Quotes.BACKTICK, "  foo bar = baz"));
        assertThat(parser.parseTerm("  \"  foo bar = baz\"  ")).isEqualTo(literal(Quotes.DOUBLE, "  foo bar = baz"));
    }

    @Test
    public void parse_expression_simple() {
        SyntaxParser parser = new SyntaxParser(SyntaxOptions.of());

        assertThat(parser.parseExpression("a")).isEqualTo($a);
        assertThat(parser.parseExpression(" a ")).isEqualTo($a);

        assertThat(parser.parseExpression("a=b")).isEqualTo(binary($a, InfixOp.EQ, $b));
        assertThat(parser.parseExpression("a = b")).isEqualTo(binary($a, InfixOp.EQ, $b));
        assertThat(parser.parseExpression(" a = b ")).isEqualTo(binary($a, InfixOp.EQ, $b));
        assertThat(parser.parseExpression(" (a = b) ")).isEqualTo(inBrackets(Brackets.PARENTHESIS, binary($a, InfixOp.EQ, $b)));
    }

    @Test
    public void parse_terms_list_simple() {
        SyntaxParser parser = new SyntaxParser(SyntaxOptions.of());

        assertThat(parser.parseTermsList("a")).containsExactly($a);
        assertThat(parser.parseTermsList(" a ")).containsExactly($a);

        assertThat(parser.parseTermsList("a b")).containsExactly($a, $b);
        assertThat(parser.parseTermsList(" a b  ")).containsExactly($a, $b);

        assertThat(parser.parseTermsList(" a b  ")).containsExactly($a, $b);
        assertThat(parser.parseTermsList(" a (b)  ")).containsExactly($a, inBrackets(Brackets.PARENTHESIS, $b));
        assertThat(parser.parseTermsList("a (b=c)"))
            .containsExactly($a, inBrackets(Brackets.PARENTHESIS, binary($b, InfixOp.EQ, $c)));
    }

    @Test
    public void parse_expressions_list_simple() {
        SyntaxParser parser = new SyntaxParser(SyntaxOptions.of());

        assertThat(parser.parseExpressionsList("a")).containsExactly($a);
        assertThat(parser.parseExpressionsList(" a ")).containsExactly($a);
        assertThat(parser.parseExpressionsList("a b")).containsExactly($a, $b);
        assertThat(parser.parseExpressionsList("  a  b  ")).containsExactly($a, $b);

        assertThat(parser.parseExpressionsList("a=b c=d")).containsExactly(binary($a, InfixOp.EQ, $b), binary($c, InfixOp.EQ, $d));
        assertThat(parser.parseExpressionsList("a = b   c = d")).containsExactly(binary($a, InfixOp.EQ, $b), binary($c, InfixOp.EQ, $d));
        assertThat(parser.parseExpressionsList("a   c = d")).containsExactly($a, binary($c, InfixOp.EQ, $d));
        assertThat(parser.parseExpressionsList("a = b   c   d")).containsExactly(binary($a, InfixOp.EQ, $b), $c, $d);
    }
}
