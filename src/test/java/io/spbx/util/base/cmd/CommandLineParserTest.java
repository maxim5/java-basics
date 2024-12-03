package io.spbx.util.base.cmd;

import io.spbx.util.base.str.BasicParsing;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.base.cmd.TestingCommandLine.spec;
import static io.spbx.util.func.ScopeFunctions.also;
import static io.spbx.util.testing.AssertFailure.assertFailure;
import static io.spbx.util.testing.TestingBasics.arrayOf;

@Tag("fast")
public class CommandLineParserTest {
    @Test
    public void parse_option_simple() {
        CommandLineSpec spec = CommandLineSpec.of().allowArbitraryOptions();

        also(CommandLineParser.parse(arrayOf("--foo=0"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "0");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "0");
        });
        also(CommandLineParser.parse(arrayOf("--foo="), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "");
        });
        also(CommandLineParser.parse(arrayOf("--foo"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "");
        });

        also(CommandLineParser.parse(arrayOf("-foo=0"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "0");
            assertThat(result.options().allKeysMap()).containsExactly("-foo", "0");
        });
        also(CommandLineParser.parse(arrayOf("-foo="), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "");
            assertThat(result.options().allKeysMap()).containsExactly("-foo", "");
        });
        also(CommandLineParser.parse(arrayOf("-foo"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "");
            assertThat(result.options().allKeysMap()).containsExactly("-foo", "");
        });
    }

    @Test
    public void parse_option_edge_cases() {
        CommandLineSpec spec = CommandLineSpec.of().allowArbitraryOptions();

        also(CommandLineParser.parse(arrayOf("--=0"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("", "0");
            assertThat(result.options().allKeysMap()).containsExactly("--", "0");
        });
        also(CommandLineParser.parse(arrayOf("--=_"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("", "_");
            assertThat(result.options().allKeysMap()).containsExactly("--", "_");
        });

        also(CommandLineParser.parse(arrayOf("--="), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("", "");
            assertThat(result.options().allKeysMap()).containsExactly("--", "");
        });
        also(CommandLineParser.parse(arrayOf("--"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("", "");
            assertThat(result.options().allKeysMap()).containsExactly("--", "");
        });

        also(CommandLineParser.parse(arrayOf("---foo=1"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "1");
            assertThat(result.options().allKeysMap()).containsExactly("---foo", "1");
        });

        also(CommandLineParser.parse(arrayOf("--foo=="), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "=");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "=");
        });
        also(CommandLineParser.parse(arrayOf("--foo=--"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "--");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "--");
        });
    }

    @Test
    public void parse_option_alias() {
        CommandLineSpec spec = CommandLineSpec.of(spec("--foo", "-f"));

        also(CommandLineParser.parse(arrayOf("--foo=0"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "0");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "0", "-f", "0");
        });
        also(CommandLineParser.parse(arrayOf("-f=0"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "0");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "0", "-f", "0");
        });
        also(CommandLineParser.parse(arrayOf("-f="), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "", "-f", "");
        });
        also(CommandLineParser.parse(arrayOf("-f"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "", "-f", "");
        });
    }

    @Test
    public void parse_allow_others() {
        CommandLineSpec spec = CommandLineSpec.of(spec("--foo").mandatory(), spec("--bar").optional()).allowArbitraryOptions();

        also(CommandLineParser.parse(arrayOf("--foo=0", "--bar", "--baz"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "0", "bar", "", "baz", "");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "0", "--bar", "", "--baz", "");
        });
        also(CommandLineParser.parse(arrayOf("--foo", "--baz=0"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("foo", "", "baz", "0");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "", "--baz", "0");
        });
    }

    @Test
    public void parse_does_not_pass_validator() {
        CommandLineSpec spec = CommandLineSpec.of(spec("--foo").validator(BasicParsing::isValidInteger));

        assertFailure(() -> CommandLineParser.parse(arrayOf("--foo=bar"), spec))
            .throwsType(InvalidCommandLineException.class)
            .hasMessageContains("Option `--foo` value is invalid: `bar`");
        assertFailure(() -> CommandLineParser.parse(arrayOf("--foo="), spec))
            .throwsType(InvalidCommandLineException.class)
            .hasMessageContains("Option `--foo` value is invalid: ``");
        assertFailure(() -> CommandLineParser.parse(arrayOf("--foo"), spec))
            .throwsType(InvalidCommandLineException.class)
            .hasMessageContains("Option `--foo` value is invalid: `null`");
    }

    @Test
    public void parse_unrecognized_options() {
        CommandLineSpec spec = CommandLineSpec.of(spec("--foo"));

        assertFailure(() -> CommandLineParser.parse(arrayOf("--foo", "--bar"), spec))
            .throwsType(InvalidCommandLineException.class)
            .hasMessageContains("Unrecognized option: --bar");
        assertFailure(() -> CommandLineParser.parse(arrayOf("-foo"), spec))
            .throwsType(InvalidCommandLineException.class)
            .hasMessageContains("Unrecognized option: -foo");
        assertFailure(() -> CommandLineParser.parse(arrayOf("-bar"), spec))
            .throwsType(InvalidCommandLineException.class)
            .hasMessageContains("Unrecognized option: -bar");
    }

    @Test
    public void parse_mandatory_option_missing() {
        CommandLineSpec spec = CommandLineSpec.of(spec("--foo").mandatory()).allowArbitraryOptions();

        assertFailure(() -> CommandLineParser.parse(arrayOf("--bar"), spec))
            .throwsType(InvalidCommandLineException.class)
            .hasMessageContains("Mandatory option is missing: `--foo`");
        assertFailure(() -> CommandLineParser.parse(arrayOf("-foo"), spec))
            .throwsType(InvalidCommandLineException.class)
            .hasMessageContains("Mandatory option is missing: `--foo`");
    }

    @Test
    public void parse_mandatory_option_misspelt() {
        CommandLineSpec spec = CommandLineSpec.of(spec("--foo")).allowArbitraryOptions();

        assertFailure(() -> CommandLineParser.parse(arrayOf("-foo"), spec))
            .throwsType(InvalidCommandLineException.class)
            .hasMessageContains("Option misspelt: `-foo`");
        assertFailure(() -> CommandLineParser.parse(arrayOf("---foo"), spec))
            .throwsType(InvalidCommandLineException.class)
            .hasMessageContains("Option misspelt: `---foo`");
    }

    @Test
    public void parse_args_simple() {
        CommandLineSpec spec = CommandLineSpec.of();

        also(CommandLineParser.parse(arrayOf("foo"), spec), result -> {
            assertThat(result.args().asList()).containsExactly("foo");
            assertThat(result.options().asMap()).isEmpty();
            assertThat(result.options().allKeysMap()).isEmpty();
        });
        also(CommandLineParser.parse(arrayOf("foo", "bar"), spec), result -> {
            assertThat(result.args().asList()).containsExactly("foo", "bar");
            assertThat(result.options().asMap()).isEmpty();
            assertThat(result.options().allKeysMap()).isEmpty();
        });
        also(CommandLineParser.parse(arrayOf("/", "=", "_"), spec), result -> {
            assertThat(result.args().asList()).containsExactly("/", "=", "_");
            assertThat(result.options().asMap()).isEmpty();
            assertThat(result.options().allKeysMap()).isEmpty();
        });
    }

    @Test
    public void parse_help() {
        CommandLineSpec spec = CommandLineSpec.of();

        also(CommandLineParser.parse(arrayOf("--help"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("help", "");
            assertThat(result.options().allKeysMap()).containsExactly("--help", "", "-h", "");
        });
        also(CommandLineParser.parse(arrayOf("-h"), spec), result -> {
            assertThat(result.args().asList()).isEmpty();
            assertThat(result.options().asMap()).containsExactly("help", "");
            assertThat(result.options().allKeysMap()).containsExactly("--help", "", "-h", "");
        });
    }

    @Test
    public void parse_args_and_options() {
        CommandLineSpec spec = CommandLineSpec.of(spec("--foo").mandatory(), spec("--bar").optional()).allowArbitraryOptions();

        also(CommandLineParser.parse(arrayOf("--foo", "foo"), spec), result -> {
            assertThat(result.args().asList()).containsExactly("foo");
            assertThat(result.options().asMap()).containsExactly("foo", "");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "");
        });
        also(CommandLineParser.parse(arrayOf("--foo", "foo", "-qux", "baz"), spec), result -> {
            assertThat(result.args().asList()).containsExactly("foo", "baz");
            assertThat(result.options().asMap()).containsExactly("foo", "", "qux", "");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "", "-qux", "");
        });

        also(CommandLineParser.parse(arrayOf("--foo=1", "--bar=2", "3"), spec), result -> {
            assertThat(result.args().asList()).containsExactly("3");
            assertThat(result.options().asMap()).containsExactly("foo", "1", "bar", "2");
            assertThat(result.options().allKeysMap()).containsExactly("--foo", "1", "--bar", "2");
        });
    }
}
