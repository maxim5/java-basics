package io.spbx.util.code.dsl.expr;

import io.spbx.util.base.error.BasicExceptions.IllegalStateExceptions;
import io.spbx.util.collect.list.ListBuilder;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.util.List;

import static io.spbx.util.func.ScopeFunctions.also;

@Immutable
public record SyntaxParser(@NotNull SyntaxOptions options) {
    public @NotNull Expr parseSequence(@NotNull CharSequence input) {
        ExprLexer lexer = ExprLexer.from(input);
        ExprParser parser = new ExprParser(options, lexer);
        return also(parser.parseSequence(), () -> assertLexerAtTheEnd(lexer));
    }

    public @NotNull Expr parseTerm(@NotNull CharSequence input) {
        ExprLexer lexer = ExprLexer.from(input);
        ExprParser parser = new ExprParser(options, lexer);
        return also(parser.parseTerm(), () -> assertLexerAtTheEnd(lexer));
    }

    public @NotNull Expr parseExpression(@NotNull CharSequence input) {
        ExprLexer lexer = ExprLexer.from(input);
        ExprParser parser = new ExprParser(options, lexer);
        return also(parser.parseOperation(), () -> assertLexerAtTheEnd(lexer));
    }

    public @NotNull List<Expr> parseTermsList(@NotNull CharSequence input) {
        ExprLexer lexer = ExprLexer.from(input);
        ExprParser parser = new ExprParser(options, lexer);
        ListBuilder<Expr> terms = ListBuilder.builder();
        while (!lexer.isAtTheEnd()) {
            terms.add(parser.parseTerm());
        }
        return terms.toList();
    }

    public @NotNull List<Expr> parseExpressionsList(@NotNull CharSequence input) {
        ExprLexer lexer = ExprLexer.from(input);
        ExprParser parser = new ExprParser(options, lexer);
        ListBuilder<Expr> operations = ListBuilder.builder();
        while (!lexer.isAtTheEnd()) {
            Expr item = parser.parseOperation();
            operations.add(item);
        }
        return operations.toList();
    }

    private static void assertLexerAtTheEnd(@NotNull ExprLexer lexer) {
        Lexem lexem = lexer.peekNextNonWhitespace();
        IllegalStateExceptions.assure(lexem.isTerminal(), "Unresolved expression:", lexem);
    }
}
