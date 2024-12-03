package io.spbx.util.code.dsl.expr;

import io.spbx.util.func.Allowed;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;

@Immutable
public record SyntaxOptions(@NotNull Allowed<Identifier> identifiers,
                            @NotNull Allowed<Numeric> numerics,
                            @NotNull Allowed<PrefixOp> prefixes,
                            @NotNull Allowed<InfixOp> infixes,
                            @NotNull Allowed<PostfixOp> postfixes,
                            @NotNull Allowed<Sequence.Separator> separators,
                            @NotNull Allowed<Quotes> quotes,
                            @NotNull Allowed<Brackets> brackets) {
    public static final SyntaxOptions ALL_ALLOWED = SyntaxOptions.of();

    public static @NotNull SyntaxOptions of() {
        return new SyntaxOptions(Allowed.allAllowed(),
                                 Allowed.allAllowed(),
                                 Allowed.allAllowed(),
                                 Allowed.allAllowed(),
                                 Allowed.allAllowed(),
                                 Allowed.allAllowed(),
                                 Allowed.allAllowed(),
                                 Allowed.allAllowed());
    }

    public @NotNull SyntaxOptions identifiers(@NotNull Allowed<Identifier> identifiers) {
        return new SyntaxOptions(identifiers, numerics, prefixes, infixes, postfixes, separators, quotes, brackets);
    }

    public @NotNull SyntaxOptions numerics(@NotNull Allowed<Numeric> numerics) {
        return new SyntaxOptions(identifiers, numerics, prefixes, infixes, postfixes, separators, quotes, brackets);
    }

    public @NotNull SyntaxOptions prefixes(@NotNull Allowed<PrefixOp> prefixes) {
        return new SyntaxOptions(identifiers, numerics, prefixes, infixes, postfixes, separators, quotes, brackets);
    }

    public @NotNull SyntaxOptions infixes(@NotNull Allowed<InfixOp> infixes) {
        return new SyntaxOptions(identifiers, numerics, prefixes, infixes, postfixes, separators, quotes, brackets);
    }

    public @NotNull SyntaxOptions postfixes(@NotNull Allowed<PostfixOp> postfixes) {
        return new SyntaxOptions(identifiers, numerics, prefixes, infixes, postfixes, separators, quotes, brackets);
    }

    public @NotNull SyntaxOptions separators(@NotNull Allowed<Sequence.Separator> separators) {
        return new SyntaxOptions(identifiers, numerics, prefixes, infixes, postfixes, separators, quotes, brackets);
    }

    public @NotNull SyntaxOptions quotes(@NotNull Allowed<Quotes> quotes) {
        return new SyntaxOptions(identifiers, numerics, prefixes, infixes, postfixes, separators, quotes, brackets);
    }

    public @NotNull SyntaxOptions brackets(@NotNull Allowed<Brackets> brackets) {
        return new SyntaxOptions(identifiers, numerics, prefixes, infixes, postfixes, separators, quotes, brackets);
    }
}
