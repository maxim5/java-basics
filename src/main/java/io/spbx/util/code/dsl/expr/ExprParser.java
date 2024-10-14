package io.spbx.util.code.dsl.expr;

import io.spbx.util.array.CharArray;
import io.spbx.util.base.BasicExceptions.IllegalStateExceptions;
import io.spbx.util.base.Pair;
import io.spbx.util.collect.ListBuilder;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import static io.spbx.util.base.BasicExceptions.IllegalStateExceptions.assure;
import static io.spbx.util.base.BasicExceptions.IllegalStateExceptions.assureNonNull;
import static io.spbx.util.base.BasicExceptions.newIllegalStateException;
import static io.spbx.util.base.BasicExceptions.newInternalError;

public class ExprParser {
    private static final Logger log = Logger.forEnclosingClass();
    private final SyntaxOptions options;
    private final ExprLexer lexer;

    public ExprParser(@NotNull SyntaxOptions options, @NotNull ExprLexer lexer) {
        this.options = options;
        this.lexer = lexer;
    }

    @VisibleForTesting
    @NotNull ExprLexer lexer() {
        return lexer;
    }

    public @NotNull Expr parseSequence() {
        log.debug().log("parseSequence: last=%s next=%s", lexer.peekLast(), lexer.peekNext());
        Sequence.Separator separator;

        Expr first = parseOperand();

        boolean isWhitespace = lexer.peekLast().isWhitespace();
        Lexem lexem = lexer.peekNextNonWhitespace();

        if ((separator = lexem.toSeparator(options)) == null) {
            if (lexem.isTerminal()) {
                return first;
            }
            if (isWhitespace) {
                return parseSequenceFromFirst(first, Sequence.Separator.SPACE);
            }
            throw newIllegalStateException("Expected a sequence separator, but got: " + lexem);
        } else {
            lexer.skipWhitespaceAndNext();
            return parseSequenceFromFirst(first, separator);
        }
    }

    public @NotNull Expr parseTerm() {
        log.debug().log("parseTerm: last=%s next=%s", lexer.peekLast(), lexer.peekNext());

        Lexem next = lexer.skipIfWhitespace().peekNext();
        return next.classify(options, new Lexem.Callback() {
            private Expr result = null;
            @Override public void onWhitespace(@NotNull CharArray value) {
                throw newInternalError("Expected a term, but got a whitespace: [%s]", value);
            }
            @Override public void onIdentifier(@NotNull Identifier identifier) {
                result = identifier;
                lexer.skipOne().skipIfWhitespace();
            }
            @Override public void onNumeric(@NotNull Numeric numeric) {
                result = numeric;
                lexer.skipOne().skipIfWhitespace();
            }
            @Override public void onPrefixOp(@NotNull PrefixOp prefixOp) {
                result = parseOperation();
            }
            @Override public void onInfixOp(@NotNull InfixOp infixOp) {
                throw newIllegalStateException("Expected a term, but got an infix op:", infixOp);
            }
            @Override public void onPostfixOp(@NotNull PostfixOp postfixOp) {
                throw newIllegalStateException("Expected a term, but got a postfix op:", postfixOp);
            }
            @Override public void onQuotes(@NotNull Quotes quotes) {
                result = parseLiteral();
            }
            @Override public void onBrackets(@NotNull Brackets brackets, boolean isOpen) {
                assure(isOpen, "Unexpected closing bracket:", brackets);
                result = parseExprInBrackets();
            }
            @Override public void onSeparator(@NotNull Sequence.Separator separator) {
                throw newIllegalStateException("Expected a term, but got:", separator);
            }
            @Override public void onTerminal() {
                throw newIllegalStateException("Expected a term, but terminated");
            }
            @Override public void onOther(@NotNull CharArray value) {
                throw newIllegalStateException("Expected a term, but got:", value);
            }
        }).result;
    }

    @NotNull Expr parseLiteral() {
        log.debug().log("parseLiteral: last=%s next=%s", lexer.peekLast(), lexer.peekNext());

        Lexem lexem = lexer.nextNonWhitespace();
        Quotes open = assureNonNull(lexem.toQuotes(options), "Expected literal, but got:", lexem);

        CharArray literal = CharArray.EMPTY;
        while (true) {
            lexem = lexer.next();
            IllegalStateExceptions.failIf(lexem.isTerminal(), "Expected literal, but terminated");
            Quotes close = lexem.toQuotes(options);
            if (close != null && open.matches(close)) {
                lexer.skipIfWhitespace();
                return new Literal(open, literal);
            }
            literal = literal.isEmpty() ? lexem.value() : CharArray.join(literal, lexem.value());  // no alloc!
        }
    }

    @NotNull Expr parseExprInBrackets() {
        log.debug().log("parseExprInBrackets: last=%s next=%s", lexer.peekLast(), lexer.peekNext());

        Lexem lexem = lexer.nextNonWhitespace();
        Brackets open = assureNonNull(lexem.toBrackets(options), "Expected brackets, but got:", lexem);
        assure(Brackets.isOpen(lexem.value()), "Expected open bracket, but got:", lexem);

        Expr operation = parseOperation();

        lexem = lexer.nextNonWhitespace();
        Brackets close = assureNonNull(lexem.toBrackets(options), "Expected brackets, but got:", lexem);
        assure(Brackets.isClose(lexem.value()), "Expected closing bracket, but got:", lexem);

        lexer.skipIfWhitespace();
        assure(open.matches(close), "Brackets mismatch: %s and %s", open, close);
        return new ExprInBrackets(open, operation);
    }

    public @NotNull Expr parseOperation() {
        log.debug().log("parseOperation: last=%s next=%s", lexer.peekLast(), lexer.peekNext());
        InfixOp infixOp;
        Sequence.Separator separator;

        Expr left = parseOperand();
        Lexem lexem = lexer.peekNextNonWhitespace();
        lexem = (infixOp = lexem.toInfixOp(options)) != null ? lexer.nextNonWhitespace() : lexem;
        if (infixOp == null) {
            if ((separator = lexem.toSeparator(options)) != null && separator.isNotWhitespace()) {
                lexer.nextNonWhitespace();
                return parseSequenceFromFirst(left, separator);
            }
            return left;
        }

        Expr right = parseOperand();

        lexem = lexer.skipIfWhitespace().peekNext();
        if (lexem.toInfixOp(options) == null) {
            lexer.skipIfWhitespace();
            return new BinaryExpr(left, infixOp, right);
        }

        ListBuilder<Pair<InfixOp, Expr>> builder = ListBuilder.of(Pair.of(infixOp, right));
        while (true) {
            lexem = (infixOp = lexem.toInfixOp(options)) != null ? lexer.nextNonWhitespace() : lexem;
            if (infixOp == null) {
                return MultiAriExpr.of(left, builder.toGuavaImmutableList());
            }

            Expr operand = parseOperand();

            builder.add(Pair.of(infixOp, operand));
            lexem = lexer.skipIfWhitespace().peekNext();
        }
    }

    @NotNull Expr parseOperand() {
        log.debug().log("parseOperand: last=%s next=%s", lexer.peekLast(), lexer.peekNext());
        PrefixOp prefixOp;
        PostfixOp postfixOp;

        Lexem lexem = lexer.skipIfWhitespace().peekNext();
        lexem = (prefixOp = lexem.toPrefixOp(options)) != null ? lexer.skipWhitespaceAndNext().peekNextNonWhitespace() : lexem;

        Expr operand = parseTerm();

        lexem = lexer.skipIfWhitespace().peekNextNonWhitespace();
        lexem = (postfixOp = lexem.toPostfixOp(options)) != null ? lexer.skipWhitespaceAndNext().peekNextNonWhitespace() : lexem;

        lexer.skipIfWhitespace();
        return UnaryPostfixExpr.wrapIfNonNull(UnaryPrefixExpr.wrapIfNonNull(prefixOp, operand), postfixOp);
    }

    @NotNull Sequence parseSequenceFromFirst(@NotNull Expr first, @NotNull Sequence.Separator separator) {
        log.debug().log("parseSequenceFromFirst: last=%s next=%s", lexer.peekLast(), lexer.peekNext());
        boolean isWhitespace = separator.isWhitespace();
        Sequence.Separator sep;

        ListBuilder<Expr> builder = ListBuilder.of(first);
        while (true) {
            Expr operand = parseOperand();

            builder.add(operand);
            Lexem lexem = isWhitespace ? lexer.skipBackIfWhitespace().peekNext() : lexer.peekNextNonWhitespace();

            if ((sep = lexem.toSeparator(options)) == null) {
                return Sequence.of(builder.toGuavaImmutableList(), separator);
            }

            assure(separator.equals(sep), "Expected %s separator, but got: %s", separator, sep);
            lexem = isWhitespace ? lexer.skipIfWhitespace().peekNextNonWhitespace() : lexer.nextNonWhitespace();

            if (isWhitespace && lexem.isTerminal()) {
                return Sequence.of(builder.toGuavaImmutableList(), separator);
            }
        }
    }
}
