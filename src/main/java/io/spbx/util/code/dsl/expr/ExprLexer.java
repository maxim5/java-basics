package io.spbx.util.code.dsl.expr;

import com.google.common.collect.ImmutableList;
import io.spbx.util.array.CharArray;
import io.spbx.util.collect.ListBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ExprLexer {
    private final List<Lexem> lexems;
    private final int size;
    private int next = 0;

    ExprLexer(@NotNull Iterable<Lexem> lexems) {
        this.lexems = ImmutableList.copyOf(lexems);
        this.size = this.lexems.size();
    }

    public static @NotNull ExprLexer from(@NotNull CharSequence input) {
        return new ExprLexer(ExprLexer.lex(input));
    }

    public static @NotNull ExprLexer from(@NotNull CharArray input) {
        return new ExprLexer(ExprLexer.lex(input));
    }

    public boolean hasNext() {
        return next < size;
    }

    public boolean hasPrev() {
        return next > 0;
    }

    public @NotNull Lexem peekNext() {
        return hasNext() ? lexems.get(next) : Lexem.TERMINAL;
    }

    public @NotNull Lexem next() {
        return hasNext() ? lexems.get(next++) : Lexem.TERMINAL;
    }

    public @NotNull Lexem peekNextNonWhitespace() {
        Lexem peek = peekNext();
        if (peek.isWhitespace()) {
            return next + 1 < size ? lexems.get(next + 1) : Lexem.TERMINAL;
        }
        return peek;
    }

    public @NotNull Lexem nextNonWhitespace() {
        Lexem nxt = next();
        if (nxt.isWhitespace()) {
            return next();
        }
        return nxt;
    }

    public @NotNull Lexem peekLast() {
        return hasPrev() ? lexems.get(next - 1) : Lexem.TERMINAL;
    }

    public @NotNull Lexem prev() {
        return hasPrev() ? lexems.get(next--) : Lexem.TERMINAL;
    }

    public @NotNull Lexem peekLastNonWhitespace() {
        Lexem peek = peekLast();
        if (peek.isWhitespace()) {
            return next - 1 > 0 ? lexems.get(next - 2) : Lexem.TERMINAL;
        }
        return peek;
    }

    public @NotNull Lexem prevNonWhitespace() {
        Lexem prev = prev();
        if (prev.isWhitespace()) {
            return prev();
        }
        return prev;
    }

    public @NotNull ExprLexer skipOne() {
        if (hasNext())
            next++;
        return this;
    }

    public @NotNull ExprLexer skipIfWhitespace() {
        if (peekNext().isWhitespace())
            next++;
        return this;
    }

    public @NotNull ExprLexer skipWhitespaceAndNext() {
        nextNonWhitespace();
        return this;
    }

    public @NotNull ExprLexer skipBackOne() {
        if (hasPrev())
            next--;
        return this;
    }

    public @NotNull ExprLexer skipBackIfWhitespace() {
        if (peekLast().isWhitespace())
            next--;
        return this;
    }

    public boolean isAtTheEnd() {
        return peekNextNonWhitespace().isTerminal();
    }

    public static @NotNull List<Lexem> lex(@NotNull CharSequence input) {
        return ExprLexer.lex(CharArray.of(input));
    }

    public static @NotNull List<Lexem> lex(@NotNull CharArray input) {
        ListBuilder<Lexem> builder = ListBuilder.builder(input.length());

        class LexerContext {
            int startIndex = 0;
            CharKind prevKind = null;

            boolean isMatch(@NotNull CharKind first) {
                return prevKind == first;
            }
            boolean isMatch(@NotNull CharKind first, @NotNull CharKind second) {
                return prevKind == first || prevKind == second;
            }
            boolean isMatch(@NotNull CharKind @NotNull ... kinds) {
                return Arrays.asList(kinds).contains(prevKind);
            }

            void pushLexem(int index) {
                if (prevKind != null) {
                    builder.add(Lexem.of(input.substring(startIndex, index)));
                }
                startIndex = index;
            }
        }

        LexerContext ctx = new LexerContext();
        input.forEachIndexed((index, ch) -> {
            CharKind kind = CharKind.isAscii(ch) ? CharKind.fromAscii(ch) : CharKind.fromChar(ch);

            switch (kind) {
                case WHITESPACE, PUNCTUATION -> {
                    if (!ctx.isMatch(kind))
                        ctx.pushLexem(index);
                }
                case DIGIT, LETTER, ID -> {
                    if (ctx.isMatch(CharKind.WHITESPACE, CharKind.PUNCTUATION, CharKind.BRACKETS, CharKind.QUOTE, CharKind.OTHER))
                        ctx.pushLexem(index);
                }
                case BRACKETS, QUOTE, OTHER -> ctx.pushLexem(index);
            }

            ctx.prevKind = kind;
        });

        ctx.pushLexem(input.length());
        return builder.toGuavaImmutableList();
    }

    private enum CharKind {
        WHITESPACE,
        DIGIT,
        LETTER,
        ID,
        PUNCTUATION,
        BRACKETS,
        QUOTE,
        OTHER;

        private int toIndex() {
            CharKind[] values = values();
            for (int i = 0; i < values.length; i++) {
                if (values[i] == this)
                    return i;
            }
            return -1;
        }

        private static @NotNull CharKind fromIndex(int idx) {
            return values()[idx];
        }

        static @NotNull CharKind fromChar(char ch) {
            return Character.isWhitespace(ch) ? WHITESPACE :
                Character.isDigit(ch) ? DIGIT :
                Character.isLetter(ch) ? LETTER :
                ID_CHARS.contains(ch) ? ID :
                BRACKETS_CHARS.contains(ch) ? BRACKETS :
                QUOTE_CHARS.contains(ch) ? QUOTE :
                PUNCTUATION_CHARS.contains(ch) ? PUNCTUATION : OTHER;
        }

        private static final CharArray ID_CHARS = CharArray.of("$_");
        private static final CharArray BRACKETS_CHARS = CharArray.of("()[]{}");
        private static final CharArray QUOTE_CHARS = CharArray.of("`'\"");
        private static final CharArray PUNCTUATION_CHARS = CharArray.of("~!@#%^&*-=+:;\\/|<>.,?");

        private static final int[] ASCII_CHARS_TO_KIND = new int[128];
        static {
            for (int i = 0; i < ASCII_CHARS_TO_KIND.length; i++) {
                ASCII_CHARS_TO_KIND[i] = CharKind.fromChar((char) i).toIndex();
            }
        }

        static @NotNull CharKind fromAscii(byte ch) {
            return ch >= 0 ? CharKind.fromIndex(ASCII_CHARS_TO_KIND[ch]) : OTHER;
        }

        static @NotNull CharKind fromAscii(char ch) {
            return isAscii(ch) ? CharKind.fromIndex(ASCII_CHARS_TO_KIND[ch]) : OTHER;
        }

        static boolean isAscii(char ch) {
            return ch < 128;
        }
    }
}
