package io.spbx.util.code.dsl.expr;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.code.dsl.expr.SyntaxOptions.ALL_ALLOWED;
import static io.spbx.util.code.dsl.expr.TestingExpr.toLexem;

public class LexemTest {
    @Test
    public void is_identifier() {
        assertThat(toLexem("foo").isIdentifier()).isTrue();
        assertThat(toLexem("FOO").isIdentifier()).isTrue();
        assertThat(toLexem("foo_bar").isIdentifier()).isTrue();
        assertThat(toLexem("foo_123").isIdentifier()).isTrue();
        assertThat(toLexem("_foo").isIdentifier()).isTrue();
        assertThat(toLexem("$foo").isIdentifier()).isTrue();
        assertThat(toLexem("_").isIdentifier()).isTrue();
        assertThat(toLexem("___").isIdentifier()).isTrue();
        assertThat(toLexem("$").isIdentifier()).isTrue();
        assertThat(toLexem("$$$").isIdentifier()).isTrue();

        assertThat(toLexem("-").isIdentifier()).isFalse();
        assertThat(toLexem("0").isIdentifier()).isFalse();
        assertThat(toLexem(" ").isIdentifier()).isFalse();
        assertThat(toLexem("&").isIdentifier()).isFalse();
    }

    @Test
    public void is_numeric() {
        assertThat(toLexem("0").isNumeric()).isTrue();
        assertThat(toLexem("1234567890").isNumeric()).isTrue();
        assertThat(toLexem("0123").isNumeric()).isTrue();
        assertThat(toLexem("0xff").isNumeric()).isTrue();
        assertThat(toLexem("0xabcd_efff_ABCD_EFFF").isNumeric()).isTrue();
        assertThat(toLexem("1e100").isNumeric()).isTrue();
        assertThat(toLexem("123_456").isNumeric()).isTrue();
        assertThat(toLexem("123_456l").isNumeric()).isTrue();
        assertThat(toLexem("123_456L").isNumeric()).isTrue();

        assertThat(toLexem("-").isNumeric()).isFalse();
        assertThat(toLexem("xff").isNumeric()).isFalse();
        assertThat(toLexem("foo").isNumeric()).isFalse();
        assertThat(toLexem("_").isNumeric()).isFalse();
        assertThat(toLexem(" ").isNumeric()).isFalse();
        assertThat(toLexem("&").isNumeric()).isFalse();
    }

    @Test
    public void to_infix() {
        assertThat(toLexem("+").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.PLUS);
        assertThat(toLexem("-").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.MINUS);
        assertThat(toLexem("*").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.MULT);
        assertThat(toLexem("**").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.POW);
        assertThat(toLexem("/").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.DIV);
        assertThat(toLexem("//").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.DIV2);
        assertThat(toLexem("%").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.MOD);
        assertThat(toLexem("%%").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.MOD2);

        assertThat(toLexem("&").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.AND);
        assertThat(toLexem("&&").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.AND2);
        assertThat(toLexem("|").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.OR);
        assertThat(toLexem("||").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.OR2);

        assertThat(toLexem("<").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.LT);
        assertThat(toLexem("<<").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.LSH);
        assertThat(toLexem("<<<").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.LSH3);
        assertThat(toLexem(">").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.GT);
        assertThat(toLexem(">>").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.RSH);
        assertThat(toLexem(">>>").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.RSH3);

        assertThat(toLexem("=").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.EQ);
        assertThat(toLexem("==").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.EQ2);
        assertThat(toLexem("===").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.EQ3);
        assertThat(toLexem("!=").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.NEQ);
        assertThat(toLexem("!==").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.NEQ3);
        assertThat(toLexem(":=").toInfixOp(ALL_ALLOWED)).isEqualTo(InfixOp.ASSIGN);

        assertThat(toLexem("foo").toInfixOp(ALL_ALLOWED)).isNull();
        assertThat(toLexem(" ").toInfixOp(ALL_ALLOWED)).isNull();
        assertThat(toLexem("0").toInfixOp(ALL_ALLOWED)).isNull();
    }

    @Test
    public void to_prefix() {
        assertThat(toLexem("!").toPrefixOp(ALL_ALLOWED)).isEqualTo(PrefixOp.NOT);
        assertThat(toLexem("+").toPrefixOp(ALL_ALLOWED)).isEqualTo(PrefixOp.PLUS);
        assertThat(toLexem("++").toPrefixOp(ALL_ALLOWED)).isEqualTo(PrefixOp.PLUS2);
        assertThat(toLexem("&").toPrefixOp(ALL_ALLOWED)).isEqualTo(PrefixOp.AMP);
        assertThat(toLexem("*").toPrefixOp(ALL_ALLOWED)).isEqualTo(PrefixOp.STAR);

        assertThat(toLexem("foo").toPrefixOp(ALL_ALLOWED)).isNull();
        assertThat(toLexem(" ").toPrefixOp(ALL_ALLOWED)).isNull();
        assertThat(toLexem("0").toPrefixOp(ALL_ALLOWED)).isNull();
    }

    @Test
    public void to_postfix() {
        assertThat(toLexem("++").toPostfixOp(ALL_ALLOWED)).isEqualTo(PostfixOp.PLUS2);
        assertThat(toLexem("--").toPostfixOp(ALL_ALLOWED)).isEqualTo(PostfixOp.MINUS2);
        assertThat(toLexem("?").toPostfixOp(ALL_ALLOWED)).isEqualTo(PostfixOp.QUESTION);

        assertThat(toLexem("foo").toPostfixOp(ALL_ALLOWED)).isNull();
        assertThat(toLexem(" ").toPostfixOp(ALL_ALLOWED)).isNull();
        assertThat(toLexem("0").toPostfixOp(ALL_ALLOWED)).isNull();
    }
}
