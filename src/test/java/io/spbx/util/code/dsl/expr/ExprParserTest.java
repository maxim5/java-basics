package io.spbx.util.code.dsl.expr;

import io.spbx.util.base.annotate.CanIgnoreReturnValue;
import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.code.dsl.expr.Sequence.Separator;
import io.spbx.util.func.Allowed;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.code.dsl.expr.TestingExpr.sequence;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("fast")
public class ExprParserTest {
    private static final SyntaxOptions OPTIONS = SyntaxOptions.of()
        .infixes(Allowed.blacklistOf(InfixOp.QUESTION, InfixOp.COLON))
        .postfixes(Allowed.blacklistOf(PostfixOp.PLUS, PostfixOp.MINUS))
        .brackets(Allowed.blacklistOf(Brackets.ANGLE));

    private static final Identifier $a = TestingExpr.identifier("a");
    private static final Identifier $b = TestingExpr.identifier("b");
    private static final Identifier $c = TestingExpr.identifier("c");
    private static final Numeric _123 = TestingExpr.numeric(123);
    private static final Numeric _xff = TestingExpr.numeric("0xFF_");

    @Test
    public void parse_term_simple() {
        assertParser(parserOf($a, "#").parseTerm()).matches($a).isAt("#");
        assertParser(parserOf($a, " ", "#").parseTerm()).matches($a).isAt("#");
        assertParser(parserOf(" ", $a, " ", "#").parseTerm()).matches($a).isAt("#");

        assertParser(parserOf(123, "#").parseTerm()).matches(_123).isAt("#");
        assertParser(parserOf(123, " ", "#").parseTerm()).matches(_123).isAt("#");
        assertParser(parserOf(" ", 123, " ", "#").parseTerm()).matches(_123).isAt("#");

        assertParser(parserOf(_xff, "#").parseTerm()).matches(_xff).isAt("#");
        assertParser(parserOf(_xff, " ", "#").parseTerm()).matches(_xff).isAt("#");
        assertParser(parserOf(" ", _xff, " ", "#").parseTerm()).matches(_xff).isAt("#");

        assertParser(parserOf("&", $a, "#").parseTerm()).matches(TestingExpr.prefix(PrefixOp.AMP, $a)).isAt("#");
        assertParser(parserOf("&", " ", $a, " ", "#").parseTerm()).matches(TestingExpr.prefix(PrefixOp.AMP, $a)).isAt("#");

        assertParser(parserOf("(", $a, ")", "#").parseTerm()).matches(TestingExpr.inBrackets(Brackets.PARENTHESIS, $a)).isAt("#");
        assertParser(parserOf("(", $a, ")", " ", "#").parseTerm()).matches(TestingExpr.inBrackets(Brackets.PARENTHESIS, $a)).isAt("#");
        assertParser(parserOf(" ", "(", $a, ")", " ", "#").parseTerm()).matches(TestingExpr.inBrackets(Brackets.PARENTHESIS, $a)).isAt("#");

        assertParser(parserOf("`", "a", "`", "#").parseTerm()).matches(TestingExpr.literal(Quotes.BACKTICK, "a")).isAt("#");
        assertParser(parserOf("`", " ", "a", " ", "`", "#").parseTerm()).matches(TestingExpr.literal(Quotes.BACKTICK, " a ")).isAt("#");
        assertParser(parserOf(" ", "`", "a", "`", "#").parseTerm()).matches(TestingExpr.literal(Quotes.BACKTICK, "a")).isAt("#");
        assertParser(parserOf(" ", "`", "a", "`", " ", "#").parseTerm()).matches(TestingExpr.literal(Quotes.BACKTICK, "a")).isAt("#");
    }

    @Test
    public void parse_term_in_the_middle() {
        assertParser(parserOf($a, "++", ".").parseTerm()).matches($a).isAt("++");
        assertParser(parserOf($a, " ", "++", ".").parseTerm()).matches($a).isAt("++");
        assertParser(parserOf($a, " ", "++", " ", ".").parseTerm()).matches($a).isAt("++");

        assertParser(parserOf($a, "+", $b).parseTerm()).matches($a).isAt("+");
        assertParser(parserOf($a, " ", "+", " ", $b).parseTerm()).matches($a).isAt("+");
    }

    @Test
    public void parse_operand_simple() {
        assertParser(parserOf($a).parseOperand()).matches($a).isAtTheEnd();
        assertParser(parserOf(123).parseOperand()).matches(_123).isAtTheEnd();
        assertParser(parserOf(_xff).parseOperand()).matches(_xff).isAtTheEnd();

        assertParser(parserOf("[", $a, "]").parseOperand()).matches(TestingExpr.inBrackets(Brackets.SQUARE, $a)).isAtTheEnd();
        assertParser(parserOf("`", "a", "`").parseOperand()).matches(TestingExpr.literal(Quotes.BACKTICK, "a")).isAtTheEnd();
    }

    @Test
    public void parse_operand_prefix_and_postfix() {
        assertParser(parserOf("+", 123).parseOperand()).matches(TestingExpr.prefix(PrefixOp.PLUS, _123)).isAtTheEnd();
        assertParser(parserOf("++", 123).parseOperand()).matches(TestingExpr.prefix(PrefixOp.PLUS2, _123)).isAtTheEnd();
        assertParser(parserOf(123, "--").parseOperand()).matches(TestingExpr.postfix(_123, PostfixOp.MINUS2)).isAtTheEnd();
        assertParser(parserOf(123, "++").parseOperand()).matches(TestingExpr.postfix(_123, PostfixOp.PLUS2)).isAtTheEnd();
        assertParser(parserOf("&", $a, "++").parseOperand()).matches(TestingExpr.postfix(TestingExpr.prefix(PrefixOp.AMP, $a), PostfixOp.PLUS2)).isAtTheEnd();

        assertParser(parserOf("*", "*", $a).parseOperand()).matches(TestingExpr.prefix(PrefixOp.STAR, TestingExpr.prefix(PrefixOp.STAR, $a))).isAtTheEnd();
        assertParser(parserOf("++", "*", $a).parseOperand()).matches(TestingExpr.prefix(PrefixOp.PLUS2, TestingExpr.prefix(PrefixOp.STAR, $a))).isAtTheEnd();

        assertParser(parserOf($a, "++", "+").parseOperand()).matches(TestingExpr.postfix($a, PostfixOp.PLUS2)).isAt("+");
        assertParser(parserOf($a, "++", "++").parseOperand()).matches(TestingExpr.postfix($a, PostfixOp.PLUS2)).isAt("++");
    }

    @Test
    public void parse_operand_spaces() {
        assertParser(parserOf($a, ".").parseOperand()).matches($a).isAt(".");
        assertParser(parserOf(" ", $a, ".").parseOperand()).matches($a).isAt(".");
        assertParser(parserOf(" ", $a, " ", ".").parseOperand()).matches($a).isAt(".");

        assertParser(parserOf("-", $a, ".").parseOperand()).matches(TestingExpr.prefix(PrefixOp.MINUS, $a)).isAt(".");
        assertParser(parserOf("-", $a, " ", ".").parseOperand()).matches(TestingExpr.prefix(PrefixOp.MINUS, $a)).isAt(".");
        assertParser(parserOf(" ", "-", $a, ".").parseOperand()).matches(TestingExpr.prefix(PrefixOp.MINUS, $a)).isAt(".");
        assertParser(parserOf(" ", "-", " ", $a, ".").parseOperand()).matches(TestingExpr.prefix(PrefixOp.MINUS, $a)).isAt(".");
        assertParser(parserOf(" ", "-", " ", $a, " ", ".").parseOperand()).matches(TestingExpr.prefix(PrefixOp.MINUS, $a)).isAt(".");

        assertParser(parserOf($a, "++", ".").parseOperand()).matches(TestingExpr.postfix($a, PostfixOp.PLUS2)).isAt(".");
        assertParser(parserOf($a, " ", "++", ".").parseOperand()).matches(TestingExpr.postfix($a, PostfixOp.PLUS2)).isAt(".");
        assertParser(parserOf($a, " ", "++", " ", ".").parseOperand()).matches(TestingExpr.postfix($a, PostfixOp.PLUS2)).isAt(".");
        assertParser(parserOf(" ", $a, "++", ".").parseOperand()).matches(TestingExpr.postfix($a, PostfixOp.PLUS2)).isAt(".");
        assertParser(parserOf(" ", $a, " ", "++", ".").parseOperand()).matches(TestingExpr.postfix($a, PostfixOp.PLUS2)).isAt(".");

        assertParser(parserOf("--", $a, "++", ".").parseOperand())
            .matches(TestingExpr.postfix(TestingExpr.prefix(PrefixOp.MINUS2, $a), PostfixOp.PLUS2)).isAt(".");
        assertParser(parserOf("--", " ", $a, " ", "++", " ", ".").parseOperand())
            .matches(TestingExpr.postfix(TestingExpr.prefix(PrefixOp.MINUS2, $a), PostfixOp.PLUS2)).isAt(".");
    }

    @Test
    public void parse_operation_binary_simple() {
        assertParser(parserOf($a, "#").parseOperation()).matches($a).isAt("#");
        assertParser(parserOf(" ", $a, "#").parseOperation()).matches($a).isAt("#");
        assertParser(parserOf(" ", $a, " ", "#").parseOperation()).matches($a).isAt("#");

        assertParser(parserOf($a, "*", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.MULT, $b)).isAt(".");
        assertParser(parserOf($a, " ", "*", " ", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.MULT, $b)).isAt(".");
        assertParser(parserOf($a, " ", "*", " ", $b, " ", ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.MULT, $b)).isAt(".");
        assertParser(parserOf(" ", $a, "*", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.MULT, $b)).isAt(".");
        assertParser(parserOf(" ", $a, " ", "*", " ", $b, " ", ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.MULT, $b)).isAt(".");
    }

    @Test
    public void parse_operation_binary_different_ops() {
        assertParser(parserOf($a, "=", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.EQ, $b)).isAt(".");
        assertParser(parserOf($a, "==", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.EQ2, $b)).isAt(".");
        assertParser(parserOf($a, "===", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.EQ3, $b)).isAt(".");
        assertParser(parserOf($a, "!=", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.NEQ, $b)).isAt(".");
        assertParser(parserOf($a, "!==", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.NEQ3, $b)).isAt(".");

        assertParser(parserOf($a, "+", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.PLUS, $b)).isAt(".");
        assertParser(parserOf($a, "-", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.MINUS, $b)).isAt(".");
        assertParser(parserOf($a, "*", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.MULT, $b)).isAt(".");
        assertParser(parserOf($a, "**", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.POW, $b)).isAt(".");
        assertParser(parserOf($a, "/", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.DIV, $b)).isAt(".");
        assertParser(parserOf($a, "//", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.DIV2, $b)).isAt(".");
        assertParser(parserOf($a, "%", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.MOD, $b)).isAt(".");
        assertParser(parserOf($a, "%%", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.MOD2, $b)).isAt(".");

        assertParser(parserOf($a, "&", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.AND, $b)).isAt(".");
        assertParser(parserOf($a, "&&", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.AND2, $b)).isAt(".");
        assertParser(parserOf($a, "|", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.OR, $b)).isAt(".");
        assertParser(parserOf($a, "||", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.OR2, $b)).isAt(".");
        assertParser(parserOf($a, "^", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.XOR, $b)).isAt(".");
        assertParser(parserOf($a, "^^", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.XOR2, $b)).isAt(".");
    }

    @Test
    public void parse_operation_binary_with_prefix_and_postfix() {
        assertParser(parserOf($a, "+", $b, ".").parseOperation()).matches(TestingExpr.binary($a, InfixOp.PLUS, $b)).isAt(".");
        assertParser(parserOf($a, "++", "+", $b, ".").parseOperation())
            .matches(TestingExpr.binary(TestingExpr.postfix($a, PostfixOp.PLUS2), InfixOp.PLUS, $b)).isAt(".");
        assertParser(parserOf($a, "+", "++", $b, ".").parseOperation())
            .matches(TestingExpr.binary($a, InfixOp.PLUS, TestingExpr.prefix(PrefixOp.PLUS2, $b))).isAt(".");
        assertParser(parserOf($a, "++", "+", "++", $b, ".").parseOperation())
            .matches(TestingExpr.binary(TestingExpr.postfix($a, PostfixOp.PLUS2), InfixOp.PLUS, TestingExpr.prefix(PrefixOp.PLUS2, $b))).isAt(".");

        assertParser(parserOf($a, "++", "**", "*", $b, ".").parseOperation())
            .matches(TestingExpr.binary(TestingExpr.postfix($a, PostfixOp.PLUS2), InfixOp.POW, TestingExpr.prefix(PrefixOp.STAR, $b))).isAt(".");
    }

    @Test
    public void parse_operation_binary_with_brackets_operand() {
        assertParser(parserOf($a, "*", "(", $b, ")", ".").parseOperation())
            .matches(TestingExpr.binary($a, InfixOp.MULT, TestingExpr.inBrackets(Brackets.PARENTHESIS, $b))).isAt(".");
        assertParser(parserOf(" ", $a, " ", "*", " ", "(", " ", $b, " ", ")", " ", ".").parseOperation())
            .matches(TestingExpr.binary($a, InfixOp.MULT, TestingExpr.inBrackets(Brackets.PARENTHESIS, $b))).isAt(".");

        assertParser(parserOf($a, "*", "(", $b, "+", $a, ")", ".").parseOperation())
            .matches(TestingExpr.binary($a, InfixOp.MULT, TestingExpr.inBrackets(Brackets.PARENTHESIS, TestingExpr.binary($b, InfixOp.PLUS, $a)))).isAt(".");
        assertParser(parserOf(" ", $a, " ", "*", " ", "(", " ", $b, " ", "+", " ", $a, " ", ")", " ", ".").parseOperation())
            .matches(TestingExpr.binary($a, InfixOp.MULT, TestingExpr.inBrackets(Brackets.PARENTHESIS, TestingExpr.binary($b, InfixOp.PLUS, $a)))).isAt(".");

        assertParser(parserOf("(", $b, ")", "&&", $a, ".").parseOperation())
            .matches(TestingExpr.binary(TestingExpr.inBrackets(Brackets.PARENTHESIS, $b), InfixOp.AND2, $a)).isAt(".");
        assertParser(parserOf(" ", "(", " ", $b, " ", ")", " ", "&&", " ", $a, " ", ".").parseOperation())
            .matches(TestingExpr.binary(TestingExpr.inBrackets(Brackets.PARENTHESIS, $b), InfixOp.AND2, $a)).isAt(".");

        assertParser(parserOf("(", $b, "+", $a, ")", "&&", $a, ".").parseOperation())
            .matches(TestingExpr.binary(TestingExpr.inBrackets(Brackets.PARENTHESIS, TestingExpr.binary($b, InfixOp.PLUS, $a)), InfixOp.AND2, $a)).isAt(".");
        assertParser(parserOf(" ", "(", " ", $b, " ", "+", " ", $a, " ", ")", " ", "&&", " ", $a, " ", ".").parseOperation())
            .matches(TestingExpr.binary(TestingExpr.inBrackets(Brackets.PARENTHESIS, TestingExpr.binary($b, InfixOp.PLUS, $a)), InfixOp.AND2, $a)).isAt(".");
    }

    @Test
    public void parse_operation_multi_simple() {
        assertParser(parserOf($a, "+", $b, "*", $a, ".").parseOperation())
            .matches(TestingExpr.multi($a, InfixOp.PLUS, $b, InfixOp.MULT, $a)).isAt(".");
        assertParser(parserOf(" ", $a, " ", "+", " ", $b, " ", "*", " ", $a, " ", ".").parseOperation())
            .matches(TestingExpr.multi($a, InfixOp.PLUS, $b, InfixOp.MULT, $a)).isAt(".");

        assertParser(parserOf($a, "+", $b, "*", $a, "-", $b, ".").parseOperation())
            .matches(TestingExpr.multi($a, InfixOp.PLUS, $b, InfixOp.MULT, $a, InfixOp.MINUS, $b)).isAt(".");
        assertParser(parserOf(" ", $a, " ", "+", " ", $b, " ", "*", " ", $a, " ", "-", " ", $b, " ", ".").parseOperation())
            .matches(TestingExpr.multi($a, InfixOp.PLUS, $b, InfixOp.MULT, $a, InfixOp.MINUS, $b)).isAt(".");
    }

    @Test
    public void parse_operation_multi_with_brackets_operand() {
        assertParser(parserOf("(", $b, ")", "*", $a, "+", $a, ".").parseOperation())
            .matches(TestingExpr.multi(TestingExpr.inBrackets(Brackets.PARENTHESIS, $b), InfixOp.MULT, $a, InfixOp.PLUS, $a)).isAt(".");
        assertParser(parserOf(" ", "(", " ", $b, " ", ")", " ", "*", " ", $a, " ", "+", " ", $a, " ", ".").parseOperation())
            .matches(TestingExpr.multi(TestingExpr.inBrackets(Brackets.PARENTHESIS, $b), InfixOp.MULT, $a, InfixOp.PLUS, $a)).isAt(".");

        assertParser(parserOf($a, "*", "(", $b, ")", "+", $a, ".").parseOperation())
            .matches(TestingExpr.multi($a, InfixOp.MULT, TestingExpr.inBrackets(Brackets.PARENTHESIS, $b), InfixOp.PLUS, $a)).isAt(".");
        assertParser(parserOf(" ", $a, " ", "*", " ", "(", " ", $b, " ", ")", " ", "+", " ", $a, " ", ".").parseOperation())
            .matches(TestingExpr.multi($a, InfixOp.MULT, TestingExpr.inBrackets(Brackets.PARENTHESIS, $b), InfixOp.PLUS, $a)).isAt(".");

        assertParser(parserOf($a, "+", $a, "*", "(", $b, ")", ".").parseOperation())
            .matches(TestingExpr.multi($a, InfixOp.PLUS, $a, InfixOp.MULT, TestingExpr.inBrackets(Brackets.PARENTHESIS, $b))).isAt(".");
        assertParser(parserOf(" ", $a, " ", "+", " ", $a, " ", "*", " ", "(", " ", $b, " ", ")", " ", ".").parseOperation())
            .matches(TestingExpr.multi($a, InfixOp.PLUS, $a, InfixOp.MULT, TestingExpr.inBrackets(Brackets.PARENTHESIS, $b))).isAt(".");
    }

    @Test
    public void parse_operation_sequence_simple() {
        assertParser(parserOf($a, ".", $b, "#").parseOperation()).matches(sequence($a, Separator.DOT, $b)).isAt("#");
        assertParser(parserOf($a, ",", $b, "#").parseOperation()).matches(sequence($a, Separator.COMMA, $b)).isAt("#");
        assertParser(parserOf($a, ":", $b, "#").parseOperation()).matches(sequence($a, Separator.COLON, $b)).isAt("#");

        assertParser(parserOf(" ", $a, " ", ".", " ", $b, " ", "#").parseOperation()).matches(sequence($a, Separator.DOT, $b)).isAt("#");
        assertParser(parserOf(" ", $a, " ", ",", " ", $b, " ", "#").parseOperation()).matches(sequence($a, Separator.COMMA, $b)).isAt("#");
        assertParser(parserOf(" ", $a, " ", ":", " ", $b, " ", "#").parseOperation()).matches(sequence($a, Separator.COLON, $b)).isAt("#");

        assertParser(parserOf($a, ".", $b, ".", $c, "#").parseOperation()).matches(TestingExpr.sequence(Separator.DOT, $a, $b, $c)).isAt("#");
        assertParser(parserOf($a, ",", $b, ",", $c, "#").parseOperation()).matches(TestingExpr.sequence(Separator.COMMA, $a, $b, $c)).isAt("#");
        assertParser(parserOf($a, ":", $b, ":", $c, "#").parseOperation()).matches(TestingExpr.sequence(Separator.COLON, $a, $b, $c)).isAt("#");

        assertParser(parserOf(" ", $a, " ", ".", " ", $b, " ", ".", " ", $c, " ", "#").parseOperation())
            .matches(TestingExpr.sequence(Separator.DOT, $a, $b, $c)).isAt("#");
        assertParser(parserOf(" ", $a, " ", ",", " ", $b, " ", ",", " ", $c, " ", "#").parseOperation())
            .matches(TestingExpr.sequence(Separator.COMMA, $a, $b, $c)).isAt("#");
        assertParser(parserOf(" ", $a, " ", ":", " ", $b, " ", ":", " ", $c, " ", "#").parseOperation())
            .matches(TestingExpr.sequence(Separator.COLON, $a, $b, $c)).isAt("#");

        assertParser(parserOf($a, " ", $b, "#").parseOperation()).matches($a).isAt("b");
    }

    @Test
    public void parse_operation_sequence_invalid() {
        assertThrows(IllegalStateException.class, () -> parserOf($a, ".", $b, ":").parseOperation());
        assertThrows(IllegalStateException.class, () -> parserOf($a, ".", $b, ",").parseOperation());
        assertThrows(IllegalStateException.class, () -> parserOf($a, ",", $b, ".").parseOperation());
    }

    @Test
    public void parse_in_brackets_simple() {
        assertParser(parserOf("(", $a, ")", ".").parseExprInBrackets()).matches(TestingExpr.inBrackets(Brackets.PARENTHESIS, $a)).isAt(".");
        assertParser(parserOf("[", $a, "]", ".").parseExprInBrackets()).matches(TestingExpr.inBrackets(Brackets.SQUARE, $a)).isAt(".");
        assertParser(parserOf("{", $a, "}", ".").parseExprInBrackets()).matches(TestingExpr.inBrackets(Brackets.CURLY, $a)).isAt(".");

        assertParser(parserOf("(", "(", $a, ")", ")", ".").parseExprInBrackets())
            .matches(TestingExpr.inBrackets(Brackets.PARENTHESIS, TestingExpr.inBrackets(Brackets.PARENTHESIS, $a))).isAt(".");
        assertParser(parserOf("[", "{", $a, "}", "]", ".").parseExprInBrackets())
            .matches(TestingExpr.inBrackets(Brackets.SQUARE, TestingExpr.inBrackets(Brackets.CURLY, $a))).isAt(".");

        assertParser(parserOf("(", $a, "*", $b, ")", ".").parseExprInBrackets())
            .matches(TestingExpr.inBrackets(Brackets.PARENTHESIS, TestingExpr.binary($a, InfixOp.MULT, $b))).isAt(".");
        assertParser(parserOf("[", $a, "*", $b, "]", ".").parseExprInBrackets())
            .matches(TestingExpr.inBrackets(Brackets.SQUARE, TestingExpr.binary($a, InfixOp.MULT, $b))).isAt(".");
        assertParser(parserOf("{", $a, "*", $b, "}", ".").parseExprInBrackets())
            .matches(TestingExpr.inBrackets(Brackets.CURLY, TestingExpr.binary($a, InfixOp.MULT, $b))).isAt(".");
    }

    @Test
    public void parse_in_brackets_spaces() {
        assertParser(parserOf(" ", "(", $a, ")", ".").parseExprInBrackets())
            .matches(TestingExpr.inBrackets(Brackets.PARENTHESIS, $a)).isAt(".");
        assertParser(parserOf(" ", "(", " ", $a, ")", ".").parseExprInBrackets())
            .matches(TestingExpr.inBrackets(Brackets.PARENTHESIS, $a)).isAt(".");
        assertParser(parserOf(" ", "(", " ", $a, " ", ")", ".").parseExprInBrackets())
            .matches(TestingExpr.inBrackets(Brackets.PARENTHESIS, $a)).isAt(".");
        assertParser(parserOf(" ", "(", " ", $a, " ", ")", " ", ".").parseExprInBrackets())
            .matches(TestingExpr.inBrackets(Brackets.PARENTHESIS, $a)).isAt(".");

        assertParser(parserOf(" ", "[", " ", $a, " ", "*", " ", $b, " ", "]", " ", ".").parseExprInBrackets())
            .matches(TestingExpr.inBrackets(Brackets.SQUARE, TestingExpr.binary($a, InfixOp.MULT, $b))).isAt(".");
    }

    @Test
    public void parse_in_brackets_sequence() {
        assertParser(parserOf("(", $a, ",", $b, ")", ".").parseExprInBrackets())
            .matches(TestingExpr.inBrackets(Brackets.PARENTHESIS, sequence($a, Separator.COMMA, $b))).isAt(".");
        assertParser(parserOf(" ", "(", " ", $a, " ", ",", " ", $b, " ", ")", " ", ".").parseExprInBrackets())
            .matches(TestingExpr.inBrackets(Brackets.PARENTHESIS, sequence($a, Separator.COMMA, $b))).isAt(".");
    }

    @Test
    public void parse_in_brackets_invalid() {
        assertThrows(IllegalStateException.class, () -> parserOf("(").parseExprInBrackets());
        assertThrows(IllegalStateException.class, () -> parserOf(")").parseExprInBrackets());
        assertThrows(IllegalStateException.class, () -> parserOf("[").parseExprInBrackets());
        assertThrows(IllegalStateException.class, () -> parserOf("]").parseExprInBrackets());
        assertThrows(IllegalStateException.class, () -> parserOf("{").parseExprInBrackets());
        assertThrows(IllegalStateException.class, () -> parserOf("}").parseExprInBrackets());

        assertThrows(IllegalStateException.class, () -> parserOf("[", ")").parseExprInBrackets());
        assertThrows(IllegalStateException.class, () -> parserOf("[", "a").parseExprInBrackets());
        assertThrows(IllegalStateException.class, () -> parserOf("(", "(", "a", ")").parseExprInBrackets());
    }

    @Test
    public void parse_literal_simple() {
        assertParser(parserOf("'", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, "")).isAt(".");
        assertParser(parserOf("\"", "\"", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.DOUBLE, "")).isAt(".");
        assertParser(parserOf("`", "`", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.BACKTICK, "")).isAt(".");

        assertParser(parserOf(" ", "'", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, "")).isAt(".");
        assertParser(parserOf(" ", "\"", "\"", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.DOUBLE, "")).isAt(".");
        assertParser(parserOf(" ", "`", "`", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.BACKTICK, "")).isAt(".");
        assertParser(parserOf(" ", "'", "'", " ", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, "")).isAt(".");
        assertParser(parserOf(" ", "\"", "\"", " ", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.DOUBLE, "")).isAt(".");
        assertParser(parserOf(" ", "`", "`", " ", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.BACKTICK, "")).isAt(".");

        assertParser(parserOf("'", " ", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, " ")).isAt(".");
        assertParser(parserOf("'", "123", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, "123")).isAt(".");
        assertParser(parserOf("'", "foo", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, "foo")).isAt(".");
        assertParser(parserOf("'", "!!", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, "!!")).isAt(".");
        assertParser(parserOf("'", "(", ")", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, "()")).isAt(".");
        assertParser(parserOf("'", "[", "}", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, "[}")).isAt(".");

        assertParser(parserOf("'", " ", "foo", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, " foo")).isAt(".");
        assertParser(parserOf("'", " ", "foo", " ", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, " foo ")).isAt(".");

        assertParser(parserOf("'", " ", "123", "%", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, " 123%")).isAt(".");
        assertParser(parserOf("'", "%", "123", " ", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, "%123 ")).isAt(".");

        assertParser(parserOf("'", "`", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, "`")).isAt(".");
        assertParser(parserOf("'", "`", "\"", "'", ".").parseLiteral()).matches(TestingExpr.literal(Quotes.SINGLE, "`\"")).isAt(".");
    }

    @Test
    public void parse_literal_invalid() {
        assertThrows(IllegalStateException.class, () -> parserOf("'").parseLiteral());
        assertThrows(IllegalStateException.class, () -> parserOf("`").parseLiteral());
        assertThrows(IllegalStateException.class, () -> parserOf("\"").parseLiteral());
        assertThrows(IllegalStateException.class, () -> parserOf("'", "a").parseLiteral());
        assertThrows(IllegalStateException.class, () -> parserOf("'", "a", "`").parseLiteral());
    }

    @Test
    public void parse_sequence_non_space_simple() {
        assertParser(parserOf($a, ".", $b, "#").parseSequence()).matches(sequence($a, Separator.DOT, $b)).isAt("#");
        assertParser(parserOf($a, ",", $b, "#").parseSequence()).matches(sequence($a, Separator.COMMA, $b)).isAt("#");
        assertParser(parserOf($a, ":", $b, "#").parseSequence()).matches(sequence($a, Separator.COLON, $b)).isAt("#");

        assertParser(parserOf(" ", $a, " ", ".", " ", $b, " ", "#").parseSequence()).matches(sequence($a, Separator.DOT, $b)).isAt("#");
        assertParser(parserOf(" ", $a, " ", ",", " ", $b, " ", "#").parseSequence()).matches(sequence($a, Separator.COMMA, $b)).isAt("#");
        assertParser(parserOf(" ", $a, " ", ":", " ", $b, " ", "#").parseSequence()).matches(sequence($a, Separator.COLON, $b)).isAt("#");

        assertParser(parserOf($a, ".", $b, ".", $c, "#").parseSequence()).matches(TestingExpr.sequence(Separator.DOT, $a, $b, $c)).isAt("#");
        assertParser(parserOf($a, ",", $b, ",", $c, "#").parseSequence()).matches(TestingExpr.sequence(Separator.COMMA, $a, $b, $c)).isAt("#");
        assertParser(parserOf($a, ":", $b, ":", $c, "#").parseSequence()).matches(TestingExpr.sequence(Separator.COLON, $a, $b, $c)).isAt("#");

        assertParser(parserOf(" ", $a, " ", ".", " ", $b, " ", ".", " ", $c, " ", "#").parseSequence())
            .matches(TestingExpr.sequence(Separator.DOT, $a, $b, $c)).isAt("#");
        assertParser(parserOf(" ", $a, " ", ",", " ", $b, " ", ",", " ", $c, " ", "#").parseSequence())
            .matches(TestingExpr.sequence(Separator.COMMA, $a, $b, $c)).isAt("#");
        assertParser(parserOf(" ", $a, " ", ":", " ", $b, " ", ":", " ", $c, " ", "#").parseSequence())
            .matches(TestingExpr.sequence(Separator.COLON, $a, $b, $c)).isAt("#");
    }

    @Test
    public void parse_sequence_non_space_invalid() {
        assertThrows(IllegalStateException.class, () -> parserOf($a, ".", $b, ",", $c).parseLiteral());
        assertThrows(IllegalStateException.class, () -> parserOf($a, ":", $b, ".", $c).parseLiteral());
    }

    @Test
    public void parse_sequence_space_simple() {
        assertParser(parserOf($a, " ", $b, "#").parseSequence()).matches(sequence($a, Separator.SPACE, $b)).isAt("#");
        assertParser(parserOf(" ", $a, " ", $b, "#").parseSequence()).matches(sequence($a, Separator.SPACE, $b)).isAt("#");
        assertParser(parserOf(" ", $a, " ", $b, " ", $c, "#").parseSequence()).matches(TestingExpr.sequence(Separator.SPACE, $a, $b, $c)).isAt("#");

        assertParser(parserOf($a).parseSequence()).matches($a).isAtTheEnd();
        assertParser(parserOf(" ", $a).parseSequence()).matches($a).isAtTheEnd();

        assertParser(parserOf($a, " ").parseSequence()).matches($a).isAtTheEnd();
        assertParser(parserOf(" ", $a, " ").parseSequence()).matches($a).isAtTheEnd();
        assertParser(parserOf(" ", $a, " ", $b, " ").parseSequence()).matches(sequence($a, Separator.SPACE, $b)).isAtTheEnd();
        assertParser(parserOf(" ", $a, " ", $b, " ", $c, " ").parseSequence()).matches(TestingExpr.sequence(Separator.SPACE, $a, $b, $c)).isAtTheEnd();
    }

    @Test
    public void parse_sequence_space_invalid() {
        assertThrows(IllegalStateException.class, () -> parserOf($a, " ", "#").parseLiteral());
        assertThrows(IllegalStateException.class, () -> parserOf($a, " ", $b, " ", "#").parseLiteral());
    }

    @Test
    public void integration_parse_expr_simple() {
        assertParser(parserFrom("a").parseOperation()).matches($a).isAtTheEnd();
        assertParser(parserFrom("a #").parseOperation()).matches($a).isAt("#");

        assertParser(parserFrom("a=b .").parseOperation()).matches(TestingExpr.binary($a, InfixOp.EQ, $b)).isAt(".");
        assertParser(parserFrom("a = b .").parseOperation()).matches(TestingExpr.binary($a, InfixOp.EQ, $b)).isAt(".");
        assertParser(parserFrom(" a   =   b .").parseOperation()).matches(TestingExpr.binary($a, InfixOp.EQ, $b)).isAt(".");
    }

    @Test
    public void integration_parse_expr_complex() {
        assertParser(parserFrom("(a + b) * (!a >> 123) .").parseOperation())
            .matches(TestingExpr.binary(TestingExpr.inBrackets(Brackets.PARENTHESIS, TestingExpr.binary($a, InfixOp.PLUS, $b)),
                                        InfixOp.MULT,
                                        TestingExpr.inBrackets(Brackets.PARENTHESIS, TestingExpr.binary(TestingExpr.prefix(PrefixOp.NOT, $a), InfixOp.RSH, _123))))
            .isAt(".");
    }

    private ExprParser current;

    private @NotNull ExprParser parserOf(@NotNull Object @NotNull ... values) {
        return current = TestingExpr.newParser(OPTIONS, values);
    }

    private @NotNull ExprParser parserFrom(@NotNull String input) {
        ExprLexer lexer = ExprLexer.from(input);
        return current = new ExprParser(OPTIONS, lexer);
    }

    @CheckReturnValue
    private @NotNull ParsedExprSubject assertParser(@NotNull Expr expr) {
        return new ParsedExprSubject(expr, new ParserSubject(current));
    }

    @CheckReturnValue
    private static @NotNull ParserSubject assertParser(@NotNull ExprParser parser) {
        return new ParserSubject(parser);
    }

    @CanIgnoreReturnValue
    private record ParserSubject(@NotNull ExprParser parser) {
        public @NotNull ParsedExprSubject when(@NotNull Function<ExprParser, Expr> func) {
            return new ParsedExprSubject(func.apply(parser), this);
        }

        public @NotNull ParserSubject isAtTheEnd() {
            return isAt(Lexem.TERMINAL);
        }

        public @NotNull ParserSubject isAt(@NotNull String lexem) {
            return isAt(TestingExpr.toLexem(lexem));
        }

        public @NotNull ParserSubject isAt(@NotNull Lexem lexem) {
            assertThat(parser.lexer().peekNext()).isEqualTo(lexem);
            return this;
        }
    }

    @CanIgnoreReturnValue
    private record ParsedExprSubject(@NotNull Expr expr, @NotNull ParserSubject parserSubject) {
        public @NotNull ParserSubject matches(@NotNull Expr expected) {
            assertThat(expr).isEqualTo(expected);
            return parserSubject;
        }
    }
}
