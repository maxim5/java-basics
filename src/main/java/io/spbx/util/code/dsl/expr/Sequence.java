package io.spbx.util.code.dsl.expr;

import com.google.common.collect.ImmutableList;
import io.spbx.util.base.str.CharArray;
import io.spbx.util.collect.map.BasicMaps;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.Map;

@Immutable
public record Sequence(@NotNull List<Expr> terms, @NotNull Separator sep) implements Expr {
    public static @NotNull Sequence of(@NotNull Iterable<Expr> terms, @NotNull Separator sep) {
        return new Sequence(ImmutableList.copyOf(terms), sep);
    }

    @Override
    public Object eval(@NotNull ExprEvaluator evaluator) {
        return evaluator.eval(terms.stream().map(term -> term.eval(evaluator)).toList(), sep);
    }

    public enum Separator {
        COMMA(','),
        DOT('.'),
        COLON(':'),
        SPACE(' ');

        private final char ch;

        Separator(char ch) {
            this.ch = ch;
        }

        public boolean isWhitespace() {
            return ch == ' ';
        }

        public boolean isNotWhitespace() {
            return ch != ' ';
        }

        private @NotNull CharArray array() {
            return CharArray.of(ch);
        }

        static final Map<CharArray, Separator> BY_CHARS = BasicMaps.indexBy(values(), Separator::array);
    }
}
