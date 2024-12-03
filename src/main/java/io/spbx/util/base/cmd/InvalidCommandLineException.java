package io.spbx.util.base.cmd;

import com.google.common.collect.ImmutableList;
import io.spbx.util.base.error.BasicRuntimeException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import static io.spbx.util.collect.stream.BasicStreams.streamOf;

public class InvalidCommandLineException extends BasicRuntimeException {
    private final ImmutableList<Throwable> errors;

    public InvalidCommandLineException(@NotNull Iterable<Throwable> errors) {
        super(streamOf(errors).map(Throwable::getMessage).collect(Collectors.joining(", ")));
        this.errors = ImmutableList.copyOf(errors);
        assert !this.errors.isEmpty();
    }

    public @NotNull ImmutableList<Throwable> errors() {
        return errors;
    }

    public static void assureNoErrors(@NotNull List<Throwable> errors) {
        if (!errors.isEmpty()) {
            throw new InvalidCommandLineException(errors);
        }
    }
}
