package io.spbx.util.base.cmd;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.cmd.TestingCommandLine.spec;
import static io.spbx.util.testing.AssertFailure.assertFailure;

@Tag("fast")
public class CommandLineSpecTest {
    @Test
    public void arg_spec_invalid_format() {
        assertFailure(() -> CommandLineSpec.of(spec(""))).throwsAssertion();
        assertFailure(() -> CommandLineSpec.of(spec("foo"))).throwsAssertion();
        assertFailure(() -> CommandLineSpec.of(spec("--foo", "bar"))).throwsAssertion();
        assertFailure(() -> CommandLineSpec.of(spec("--foo", ""))).throwsAssertion();
    }

    @Test
    public void arg_spec_invalid_duplicates() {
        assertFailure(() -> CommandLineSpec.of(spec("--foo", "--foo"))).throwsAssertion();
        assertFailure(() -> CommandLineSpec.of(spec("--foo", "-foo"))).throwsAssertion();
        assertFailure(() -> CommandLineSpec.of(spec("--foo"), spec("--foo"))).throwsAssertion();
        assertFailure(() -> CommandLineSpec.of(spec("--foo"), spec("-foo"))).throwsAssertion();
        assertFailure(() -> CommandLineSpec.of(spec("--foo"), spec("--bar", "-foo"))).throwsAssertion();
        assertFailure(() -> CommandLineSpec.of(spec("--foo", "--bar"), spec("--baz", "-bar"))).throwsAssertion();
    }

    @Test
    public void arg_spec_toHumanDescription() {
        assertThat(spec("--foo").toHumanDescription()).isEqualTo("`--foo`");
        assertThat(spec("--foo", "-f").toHumanDescription()).isEqualTo("`--foo` [-f]");
        assertThat(spec("--foo", "-f", "-x").toHumanDescription()).isEqualTo("`--foo` [-f, -x]");
    }
}
