package io.spbx.util.code.dsl.expr;

import com.google.errorprone.annotations.Immutable;
import io.spbx.util.base.CharArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

import static io.spbx.util.base.BasicExceptions.newIllegalStateException;

@Immutable
public record Lexem(@NotNull CharArray value) {
    public static final Lexem TERMINAL = Lexem.of(CharArray.EMPTY);
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[_$a-zA-Z][_$a-zA-Z0-9]*");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("[0-9][_a-zA-Z0-9]*");

    public static @NotNull Lexem of(@NotNull CharArray value) {
        return new Lexem(value);
    }

    public boolean isWhitespace() {
        return value.indexOf(ch -> !Character.isWhitespace(ch)) < 0 && value.isNotEmpty();
    }

    public boolean isTerminal() {
        return value.isEmpty();
    }

    boolean isIdentifier() {
        return IDENTIFIER_PATTERN.matcher(value).matches();
    }

    public @NotNull Identifier toIdentifier(@NotNull SyntaxOptions options) {
        return options.identifiers().allowOrDie(new Identifier(value), () -> newSyntaxError("Identifier", value));
    }

    boolean isNumeric() {
        return NUMERIC_PATTERN.matcher(value).matches();
    }

    public @NotNull Numeric toNumeric(@NotNull SyntaxOptions options) {
        return options.numerics().allowOrDie(new Numeric(value), () -> newSyntaxError("Numeric value", value));
    }

    public @Nullable PrefixOp toPrefixOp(@NotNull SyntaxOptions options) {
        return options.prefixes().allowOrNull(PrefixOp.BY_CHARS.get(value));
    }

    public @Nullable InfixOp toInfixOp(@NotNull SyntaxOptions options) {
        return options.infixes().allowOrNull(InfixOp.BY_CHARS.get(value));
    }

    public @Nullable PostfixOp toPostfixOp(@NotNull SyntaxOptions options) {
        return options.postfixes().allowOrNull(PostfixOp.BY_CHARS.get(value));
    }

    public @Nullable Quotes toQuotes(@NotNull SyntaxOptions options) {
        return options.quotes().allowOrNull(Quotes.BY_CHARS.get(value));
    }

    public @Nullable Brackets toBrackets(@NotNull SyntaxOptions options) {
        return options.brackets().allowOrNull(Brackets.BY_CHARS.get(value));
    }

    public @Nullable Sequence.Separator toSeparator(@NotNull SyntaxOptions options) {
        return options.separators().allowOrNull(
            isWhitespace() ? Sequence.Separator.SPACE : Sequence.Separator.BY_CHARS.get(value)
        );
    }

    public <C extends Callback> @NotNull C classify(@NotNull SyntaxOptions options, @NotNull C callback) {
        InfixOp infixOp;
        PrefixOp prefixOp;
        PostfixOp postfixOp;
        Quotes quotes;
        Brackets brackets;
        Sequence.Separator separator;
        if (isTerminal()) {
            callback.onTerminal();
        } else if (isWhitespace()) {
            callback.onWhitespace(value);
        } else if (isIdentifier()) {
            callback.onIdentifier(toIdentifier(options));
        } else if (isNumeric()) {
            callback.onNumeric(toNumeric(options));
        } else if ((prefixOp = toPrefixOp(options)) != null) {
            callback.onPrefixOp(prefixOp);
        } else if ((infixOp = toInfixOp(options)) != null) {
            callback.onInfixOp(infixOp);
        } else if ((postfixOp = toPostfixOp(options)) != null) {
            callback.onPostfixOp(postfixOp);
        } else if ((quotes = toQuotes(options)) != null) {
            callback.onQuotes(quotes);
        } else if ((brackets = toBrackets(options)) != null) {
            callback.onBrackets(brackets, Brackets.isOpen(value));
        } else if ((separator = toSeparator(options)) != null) {
            callback.onSeparator(separator);
        } else {
            callback.onOther(value);
        }
        return callback;
    }

    public interface Callback {
        default void onWhitespace(@NotNull CharArray value) {}
        default void onIdentifier(@NotNull Identifier identifier) {}
        default void onNumeric(@NotNull Numeric numeric) {}
        default void onPrefixOp(@NotNull PrefixOp prefixOp) {}
        default void onInfixOp(@NotNull InfixOp infixOp) {}
        default void onPostfixOp(@NotNull PostfixOp postfixOp) {}
        default void onQuotes(@NotNull Quotes quotes) {}
        default void onBrackets(@NotNull Brackets brackets, boolean isOpen) {}
        default void onSeparator(@NotNull Sequence.Separator separator) {}
        default void onTerminal() {}
        default void onOther(@NotNull CharArray value) {}
    }

    @Override
    public String toString() {
        return "`%s`".formatted(value);
    }

    private static @NotNull IllegalStateException newSyntaxError(@NotNull String syntaxEntity, @NotNull Object value) {
        return newIllegalStateException("Syntax error. %s does not match syntax rules: `%s`", syntaxEntity, value);
    }
}
