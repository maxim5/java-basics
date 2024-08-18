package io.spbx.util.code.gen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.Immutable;
import io.spbx.util.base.BasicExceptions.IllegalStateExceptions;
import io.spbx.util.base.BasicExceptions.InternalErrors;
import io.spbx.util.base.BasicParsing;
import io.spbx.util.base.Pair;
import io.spbx.util.code.dsl.expr.Brackets;
import io.spbx.util.code.dsl.expr.Expr;
import io.spbx.util.code.dsl.expr.ExprEvaluator;
import io.spbx.util.code.dsl.expr.Identifier;
import io.spbx.util.code.dsl.expr.InfixOp;
import io.spbx.util.code.dsl.expr.Literal;
import io.spbx.util.code.dsl.expr.Numeric;
import io.spbx.util.code.dsl.expr.PostfixOp;
import io.spbx.util.code.dsl.expr.PrefixOp;
import io.spbx.util.code.dsl.expr.Sequence;
import io.spbx.util.code.dsl.expr.SyntaxOptions;
import io.spbx.util.code.dsl.expr.SyntaxParser;
import io.spbx.util.collect.BasicMaps;
import io.spbx.util.collect.StringContentMap;
import io.spbx.util.func.Allowed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Immutable
record Attrs(@NotNull List<Expr> exprs) {
    public static final Attrs EMPTY = new Attrs(ImmutableList.of());

    private static final SyntaxOptions SYNTAX_OPTIONS = SyntaxOptions.of()
        .prefixes(Allowed.whitelistOf(PrefixOp.NOT))
        .infixes(Allowed.whitelistOf(InfixOp.EQ, InfixOp.EQ2, InfixOp.NEQ, InfixOp.AND, InfixOp.AND2, InfixOp.OR, InfixOp.OR2))
        .postfixes(Allowed.allDisallowed())
        .brackets(Allowed.whitelistOf(Brackets.PARENTHESIS, Brackets.SQUARE))
        .separators(Allowed.allDisallowed());
    private static final SyntaxParser PARSER = new SyntaxParser(SYNTAX_OPTIONS);

    public static @NotNull Attrs parse(@NotNull CharSequence content) {
        List<Expr> exprs = PARSER.parseExpressionsList(content);
        return new Attrs(ImmutableList.copyOf(exprs));
    }

    public @NotNull Map<String, String> toNamedMap(@NotNull String @NotNull ... names) {
        Map<String, String> result = BasicMaps.newMutableMap();

        ExprEvaluator evaluator = new ExprEvaluator() {
            @Override public @Nullable String eval(@Nullable Object left, @NotNull InfixOp op, @Nullable Object right) {
                switch (op) {
                    case EQ -> {
                        if (left != null && right != null) {
                            result.put(left.toString(), right.toString());
                        }
                    }
                    case EQ2, NEQ, AND, AND2, OR, OR2 -> {}
                    default -> InternalErrors.fail("Unexpected infix op: " + op);
                }
                return null;
            }

            @Override public @NotNull String eval(@NotNull Identifier identifier) {
                return identifier.name().toString();
            }

            @Override public @NotNull String eval(@NotNull Numeric numeric) {
                return numeric.value().toString();
            }

            @Override public @NotNull String eval(@NotNull Literal literal) {
                return literal.value().toString();
            }

            @Override public @Nullable String eval(@Nullable Object start, @NotNull List<Pair<InfixOp, @Nullable Object>> rest) {
                return null;
            }

            @Override public @Nullable String eval(@NotNull PrefixOp op, @Nullable Object val) {
                return null;
            }

            @Override public @Nullable String eval(@Nullable Object val, @NotNull PostfixOp op) {
                return null;
            }

            @Override public @Nullable String eval(@NotNull List<@Nullable Object> terms, @NotNull Sequence.Separator sep) {
                return null;
            }
        };

        int i = 0;
        for (Expr expr : exprs) {
            String eval = (String) expr.eval(evaluator);
            if (eval != null && i < names.length) {
                result.putIfAbsent(names[i++], eval);
            }
        }

        return ImmutableMap.copyOf(result);
    }

    public boolean eval(@NotNull Variables vars) {
        ExprEvaluator evaluator = new ExprEvaluator() {
            @Override public @NotNull CharSequence eval(@NotNull Identifier identifier) {
                return vars.getOrDefault(identifier.name(), identifier.name());
            }

            @Override public @NotNull Boolean eval(@Nullable Object left, @NotNull InfixOp op, @Nullable Object right) {
                return switch (op) {
                    case EQ, EQ2 -> StringContentMap.contentEqual(left, right);
                    case NEQ -> !StringContentMap.contentEqual(left, right);
                    case AND -> toBoolean(left) & toBoolean(right);
                    case AND2 -> toBoolean(left) && toBoolean(right);
                    case OR -> toBoolean(left) | toBoolean(right);
                    case OR2 -> toBoolean(left) || toBoolean(right);
                    default -> InternalErrors.fail("Unexpected infix op: " + op);
                };
            }

            @Override public @NotNull Boolean eval(@Nullable Object start, @NotNull List<Pair<InfixOp, @Nullable Object>> rest) {
                List<InfixOp> ops = rest.stream().map(Pair::first).distinct().toList();
                IllegalStateExceptions.assure(ops.size() == 1, "Unsupported syntax. Multi-expression must use the same op: %s", ops);
                Stream<Object> values = Stream.concat(Stream.of(start), rest.stream().map(Pair::second));
                return switch (ops.getFirst()) {
                    case EQ, EQ2 -> values.map(StringContentMap::toCharSequence).distinct().count() == 1;
                    case AND, AND2 -> values.map(Attrs::toBoolean).reduce((a, b) -> a && b).orElseThrow();
                    case OR, OR2 -> values.map(Attrs::toBoolean).reduce((a, b) -> a || b).orElseThrow();
                    default -> IllegalStateExceptions.fail("Unsupported syntax. This op is not allowed: " + ops.getFirst());
                };
            }

            @Override public @NotNull Boolean eval(@NotNull PrefixOp op, @Nullable Object val) {
                return switch (op) {
                    case NOT -> !toBoolean(val);
                    default -> InternalErrors.fail("Unexpected prefix op: " + op);
                };
            }

            @Override public @NotNull CharSequence eval(@Nullable Object val, @NotNull PostfixOp op) {
                return InternalErrors.fail("Unexpected postfix op: " + op);
            }

            @Override public @NotNull CharSequence eval(@NotNull List<@Nullable Object> terms, @NotNull Sequence.Separator sep) {
                return InternalErrors.fail("Unexpected separator: " + sep);
            }

            @Override public @NotNull CharSequence eval(@NotNull Numeric numeric) {
                return numeric.value();
            }

            @Override public @NotNull CharSequence eval(@NotNull Literal literal) {
                return literal.value();
            }
        };

        return exprs.stream().allMatch(expr -> toBoolean(expr.eval(evaluator)));
    }

    private static boolean toBoolean(@Nullable Object object) {
        return object != null && (object instanceof CharSequence s ? BasicParsing.parseBoolSafe(s) : forceBoolean(object));
    }

    private static @NotNull Boolean forceBoolean(@NotNull Object object) {
        InternalErrors.assure(object instanceof Boolean, "Must be a boolean: %s", object);
        return (Boolean) object;
    }
}
