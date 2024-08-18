package io.spbx.util.code.gen;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import io.spbx.util.collect.Streamer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

import static io.spbx.util.base.EasyCast.castAny;

@CanIgnoreReturnValue
class CodeBuilder extends LinesBuilder<CodeBuilder> {
    public CodeBuilder() {
        super();
    }

    public CodeBuilder(int size) {
        super(size);
    }

    public CodeBuilder(@NotNull List<String> list) {
        super(list);
    }

    public static @NotNull CodeBuilder allocate(int size) {
        return new CodeBuilder(size);
    }

    public @NotNull CodeBuilder mapToNew(@NotNull Function<String, String> convert) {
        return new CodeBuilder(Streamer.of(lines).map(convert).toArrayList());
    }

    public @Nullable String peek() {
        return lines.isEmpty() ? null : lines.getLast();
    }

    public @NotNull CodeBuilder removeLast() {
        assert !lines.isEmpty() : "Snippet is empty";
        lines.removeLast();
        return castAny(this);
    }

    public @NotNull CodeBuilder removeLastIfBlank() {
        if (!lines.isEmpty() && lines.getLast().isBlank()) {
            lines.removeLast();
        }
        return castAny(this);
    }
}
