package io.spbx.util.testing;

import io.spbx.util.testing.MoreTruth.MoreThrowableSubject;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AssertFailure {
    public static @NotNull ExecutableSubject assertFailure(@NotNull Executable executable) {
        return new ExecutableSubject(executable);
    }

    public record ExecutableSubject(@NotNull Executable executable) {
        public @NotNull MoreThrowableSubject throwsType(@NotNull Class<? extends Throwable> expectedType) {
            Throwable throwable = assertThrows(expectedType, executable);
            return MoreTruth.assertThat(throwable);
        }

        public @NotNull MoreThrowableSubject throwsNPE() {
            return throwsType(NullPointerException.class);
        }

        public @NotNull MoreThrowableSubject throwsAssertion() {
            return throwsType(AssertionError.class);
        }

        public @NotNull MoreThrowableSubject throwsIllegalState() {
            return throwsType(IllegalStateException.class);
        }

        public @NotNull MoreThrowableSubject throwsIllegalArgument() {
            return throwsType(IllegalArgumentException.class);
        }
    }
}
