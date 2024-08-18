package io.spbx.util.code.gen;

import com.google.common.flogger.FluentLogger;
import com.google.errorprone.annotations.Immutable;
import io.spbx.util.text.BasicJoin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;

import static io.spbx.util.base.BasicNulls.firstNonNullIfExist;

@Immutable
class JavaMarking implements Marking {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    public static final JavaMarking JAVA_MARK = new JavaMarking();

    @Override
    public @NotNull String compose(@NotNull Directive directive) {
        return BasicJoin.of(
            opening(directive.type()),
            directive.predefined(), directive.modifier().makeKey(directive.name()),
            directive.attrs(),
            closing(directive.type())
        ).onlyNonEmpty().join(' ');
    }

    private static @NotNull String opening(@NotNull Directive.Type type) {
        return switch (type) {
            case BLOCK -> "/*=";
            case INLINE -> "//=";
            case COMMENT_BLOCK -> "/*~";
            case COMMENT_INLINE -> "//~";
        };
    }

    private static @NotNull String closing(@NotNull Directive.Type type) {
        return switch (type) {
            case BLOCK -> "=*/";
            case INLINE -> "=//";
            case COMMENT_BLOCK -> "~*/";
            case COMMENT_INLINE -> "~//";
        };
    }

    @Override
    public @Nullable DirectivePosition extract(@NotNull String input) {
        return firstNonNullIfExist(List.of(
            () -> tryExtractCommentBlock(input),
            () -> tryExtractCommentInline(input),
            () -> tryExtractBlock(input),
            () -> tryExtractInline(input)
        ));
    }

    private static @Nullable DirectivePosition tryExtractCommentBlock(@NotNull String input) {
        int i = input.indexOf("/*~");
        int j = input.lastIndexOf("~*/");
        if (i == -1 || j == -1) {
            if (i >= 0 || j >= 0 || input.contains("*~/")) {
                log.at(Level.WARNING).log("Possible typo. Did you mean /*~ ... ~*/: `%s`", input);
            }
            return null;
        }
        String content = input.substring(i + 3, j).trim();
        Directive directive = Directive.commentOf(content, Directive.Type.COMMENT_BLOCK);
        return new DirectivePosition(directive, i, j + 3);
    }

    private static @Nullable DirectivePosition tryExtractCommentInline(@NotNull String input) {
        int i = input.indexOf("//~");
        int j = input.lastIndexOf("~//");
        if (i == -1) {
            return null;
        }
        int k = j >= 0 ? j : input.length();
        String content = input.substring(i + 3, k).trim();
        Directive directive = Directive.commentOf(content, Directive.Type.COMMENT_INLINE);
        return new DirectivePosition(directive, i, input.length());
    }

    private static @Nullable DirectivePosition tryExtractBlock(@NotNull String input) {
        int i = input.indexOf("/*=");
        int j = input.lastIndexOf("=*/");
        if (i == -1 || j == -1) {
            if (i >= 0 || j >= 0 || input.contains("*=/")) {
                log.at(Level.WARNING).log("Possible typo. Did you mean /*= ... =*/: `%s`", input);
            }
            return null;
        }
        String content = input.substring(i + 3, j).trim();
        Directive directive = Directive.parseDirective(content, Directive.Type.BLOCK);
        return new DirectivePosition(directive, i, j + 3);
    }

    private static @Nullable DirectivePosition tryExtractInline(@NotNull String input) {
        int i = input.indexOf("//=");
        int j = input.lastIndexOf("=//");
        if (i == -1) {
            return null;
        }
        int k = j >= 0 ? j : input.length();
        String content = input.substring(i + 3, k).trim();
        Directive directive = Directive.parseDirective(content, Directive.Type.INLINE);
        return new DirectivePosition(directive, i, input.length());
    }
}
