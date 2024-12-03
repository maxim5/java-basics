package io.spbx.util.base.cmd.flag;

import io.spbx.util.base.cmd.TestingCommandLine.ThrowingExit;
import io.spbx.util.reflect.BasicMembers;
import io.spbx.util.reflect.BasicMembers.FieldValue;
import io.spbx.util.reflect.BasicMembers.Fields;
import io.spbx.util.reflect.BasicMembers.Scope;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.arrayOf;
import static org.junit.jupiter.api.Assertions.fail;

@Tag("fast")
@SuppressWarnings("CodeBlock2Expr")
public class FlagsTest {
    @RegisterExtension private static final FlagRegistryCleanup FLAGS_CLEANUP = new FlagRegistryCleanup();

    @Test
    public void mandatory_not_empty_flag() {
        Setup setup = () -> {
            flag = Flags.defaultValue("DEFAULT").key("--foo").alias("-f").mandatory().notEmpty().build();
        };

        assertInitializeFromCommandLine(setup, arrayOf()).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Mandatory option is missing");
        });
        assertInitializeFromCommandLine(setup, arrayOf("--foo")).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Option `--foo` value is invalid");
        });
        assertInitializeFromCommandLine(setup, arrayOf("-f")).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Option `-f` value is invalid");
        });

        assertInitializeFromCommandLine(setup, arrayOf("--foo=path")).success(() -> {
            assertThat(flag.get()).isEqualTo("path");
            assertThat(flag.providedValue()).isEqualTo("path");
        });
        assertInitializeFromCommandLine(setup, arrayOf("-f=path")).success(() -> {
            assertThat(flag.get()).isEqualTo("path");
            assertThat(flag.providedValue()).isEqualTo("path");
        });
    }

    @Test
    public void optional_not_empty_flag() {
        Setup setup = () -> {
            flag = Flags.defaultValue("DEFAULT").key("--foo").alias("-f").optional().notEmpty().build();
        };

        assertInitializeFromCommandLine(setup, arrayOf("--foo")).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Option `--foo` value is invalid");
        });
        assertInitializeFromCommandLine(setup, arrayOf("-f")).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Option `-f` value is invalid");
        });

        assertInitializeFromCommandLine(setup, arrayOf()).success(() -> {
            assertThat(flag.get()).isEqualTo("DEFAULT");
            assertThat(flag.providedValue()).isNull();
        });
        assertInitializeFromCommandLine(setup, arrayOf("--foo=path")).success(() -> {
            assertThat(flag.get()).isEqualTo("path");
            assertThat(flag.providedValue()).isEqualTo("path");
        });
        assertInitializeFromCommandLine(setup, arrayOf("-f=path")).success(() -> {
            assertThat(flag.get()).isEqualTo("path");
            assertThat(flag.providedValue()).isEqualTo("path");
        });
    }

    @Test
    public void mandatory_boolean_flag() {
        Setup setup = () -> {
            booleanFlag = Flags.defaultValue(false).key("-b").mandatory().build();
        };

        assertInitializeFromCommandLine(setup, arrayOf()).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Mandatory option is missing");
        });
        assertInitializeFromCommandLine(setup, arrayOf("-b=foo")).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Option `-b` value is invalid");
        });

        assertInitializeFromCommandLine(setup, arrayOf("-b")).success(() -> {
            assertThat(booleanFlag.get()).isTrue();
            assertThat(booleanFlag.providedValue()).isEqualTo("");
        });
        assertInitializeFromCommandLine(setup, arrayOf("-b=true")).success(() -> {
            assertThat(booleanFlag.get()).isTrue();
            assertThat(booleanFlag.providedValue()).isEqualTo("true");
        });
        assertInitializeFromCommandLine(setup, arrayOf("-b=TRUE")).success(() -> {
            assertThat(booleanFlag.get()).isTrue();
            assertThat(booleanFlag.providedValue()).isEqualTo("TRUE");
        });
        assertInitializeFromCommandLine(setup, arrayOf("-b=false")).success(() -> {
            assertThat(booleanFlag.get()).isFalse();
            assertThat(booleanFlag.providedValue()).isEqualTo("false");
        });
        assertInitializeFromCommandLine(setup, arrayOf("-b=FALSE")).success(() -> {
            assertThat(booleanFlag.get()).isFalse();
            assertThat(booleanFlag.providedValue()).isEqualTo("FALSE");
        });
    }

    @Test
    public void optional_boolean_flag() {
        Setup setup = () -> {
            booleanFlag = Flags.defaultValue(false).key("-b").optional().build();
        };

        assertInitializeFromCommandLine(setup, arrayOf("-b=foo")).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Option `-b` value is invalid");
        });

        assertInitializeFromCommandLine(setup, arrayOf()).success(() -> {
            assertThat(booleanFlag.get()).isFalse();
            assertThat(booleanFlag.providedValue()).isNull();
        });
        assertInitializeFromCommandLine(setup, arrayOf("-b")).success(() -> {
            assertThat(booleanFlag.get()).isTrue();
            assertThat(booleanFlag.providedValue()).isEqualTo("");
        });
        assertInitializeFromCommandLine(setup, arrayOf("-b=true")).success(() -> {
            assertThat(booleanFlag.get()).isTrue();
            assertThat(booleanFlag.providedValue()).isEqualTo("true");
        });
        assertInitializeFromCommandLine(setup, arrayOf("-b=TRUE")).success(() -> {
            assertThat(booleanFlag.get()).isTrue();
            assertThat(booleanFlag.providedValue()).isEqualTo("TRUE");
        });
        assertInitializeFromCommandLine(setup, arrayOf("-b=false")).success(() -> {
            assertThat(booleanFlag.get()).isFalse();
            assertThat(booleanFlag.providedValue()).isEqualTo("false");
        });
        assertInitializeFromCommandLine(setup, arrayOf("-b=FALSE")).success(() -> {
            assertThat(booleanFlag.get()).isFalse();
            assertThat(booleanFlag.providedValue()).isEqualTo("FALSE");
        });
    }

    @Test
    public void string_flag_validation_length() {
        Setup setup = () -> {
            stringFlag1 = Flags.defaultValue("def").key("--foo").length(3).mandatory().build();
            stringFlag2 = Flags.defaultValue("").key("--bar").minLength(0).maxLength(5).build();
        };

        assertInitializeFromCommandLine(setup, arrayOf()).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Mandatory option is missing");
        });
        assertInitializeFromCommandLine(setup, arrayOf("--foo")).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Option `--foo` value is invalid");
        });
        assertInitializeFromCommandLine(setup, arrayOf("--bar")).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Option `--bar` value is invalid");
        });

        assertInitializeFromCommandLine(setup, arrayOf("--foo=123", "--bar=")).success(() -> {
            assertThat(stringFlag1.get()).isEqualTo("123");
            assertThat(stringFlag2.get()).isEqualTo("");
            assertThat(stringFlag1.providedValue()).isEqualTo("123");
            assertThat(stringFlag2.providedValue()).isEqualTo("");
        });
        assertInitializeFromCommandLine(setup, arrayOf("--foo=123", "--bar=12345")).success(() -> {
            assertThat(stringFlag1.get()).isEqualTo("123");
            assertThat(stringFlag2.get()).isEqualTo("12345");
            assertThat(stringFlag1.providedValue()).isEqualTo("123");
            assertThat(stringFlag2.providedValue()).isEqualTo("12345");
        });
    }

    @Test
    public void int_flag_validation_limits() {
        Setup setup = () -> {
            intFlag1 = Flags.defaultValue(777).key("--i").positive().build();
            intFlag2 = Flags.defaultValue(777_777).key("--j").nonNegative().build();
            intFlag3 = Flags.defaultValue(777_777_777).key("--k").min(1000).build();
        };

        assertInitializeFromCommandLine(setup, arrayOf("--i=0")).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Option `--i` value is invalid");
        });
        assertInitializeFromCommandLine(setup, arrayOf("--j=-1")).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Option `--j` value is invalid");
        });
        assertInitializeFromCommandLine(setup, arrayOf("--k=7")).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Option `--k` value is invalid");
        });

        assertInitializeFromCommandLine(setup, arrayOf()).success(() -> {
            assertThat(intFlag1.get()).isEqualTo(777);
            assertThat(intFlag2.get()).isEqualTo(777_777);
            assertThat(intFlag3.get()).isEqualTo(777_777_777);
            assertThat(intFlag1.providedValue()).isNull();
            assertThat(intFlag2.providedValue()).isNull();
            assertThat(intFlag3.providedValue()).isNull();
        });
        assertInitializeFromCommandLine(setup, arrayOf("--i=1", "--j=0", "--k=1000")).success(() -> {
            assertThat(intFlag1.get()).isEqualTo(1);
            assertThat(intFlag2.get()).isEqualTo(0);
            assertThat(intFlag3.get()).isEqualTo(1000);
            assertThat(intFlag1.providedValue()).isEqualTo("1");
            assertThat(intFlag2.providedValue()).isEqualTo("0");
            assertThat(intFlag3.providedValue()).isEqualTo("1000");
        });
        assertInitializeFromCommandLine(setup, arrayOf("--i=+123", "--j=0321", "--k=+01234")).success(() -> {
            assertThat(intFlag1.get()).isEqualTo(123);
            assertThat(intFlag2.get()).isEqualTo(321);
            assertThat(intFlag3.get()).isEqualTo(1234);
            assertThat(intFlag1.providedValue()).isEqualTo("+123");
            assertThat(intFlag2.providedValue()).isEqualTo("0321");
            assertThat(intFlag3.providedValue()).isEqualTo("+01234");
        });
    }

    @Test
    public void mandatory_and_optional_simple() {
        Setup setup = () -> {
            stringFlag = Flags.defaultValue("DEFAULT").key("--foo").alias("-f").notEmpty().mandatory().build();
            intFlag = Flags.defaultValue(0).key("--bar").alias("-b").optional().build();
            booleanFlag = Flags.defaultValue(false).key("--baz").alias("-bz").optional().build();
        };

        assertInitializeFromCommandLine(setup, arrayOf()).failure(throwable -> {
            assertThat(throwable).hasMessageThat().contains("Mandatory option is missing");
        });

        assertInitializeFromCommandLine(setup, arrayOf("--foo=path")).success(() -> {
            assertThat(stringFlag.get()).isEqualTo("path");
            assertThat(intFlag.get()).isEqualTo(0);
            assertThat(booleanFlag.get()).isFalse();
            assertThat(stringFlag.providedValue()).isEqualTo("path");
            assertThat(intFlag.providedValue()).isNull();
            assertThat(booleanFlag.providedValue()).isNull();
        });
        assertInitializeFromCommandLine(setup, arrayOf("--foo=_", "--bar=777", "-bz")).success(() -> {
            assertThat(stringFlag.get()).isEqualTo("_");
            assertThat(intFlag.get()).isEqualTo(777);
            assertThat(booleanFlag.get()).isTrue();
            assertThat(stringFlag.providedValue()).isEqualTo("_");
            assertThat(intFlag.providedValue()).isEqualTo("777");
            assertThat(booleanFlag.providedValue()).isEqualTo("");
        });
    }

    private StringFlag flag;

    private StringFlag stringFlag;
    private IntFlag intFlag;
    private BooleanFlag booleanFlag;

    private StringFlag stringFlag1;
    private StringFlag stringFlag2;
    private IntFlag intFlag1;
    private IntFlag intFlag2;
    private IntFlag intFlag3;

    private interface Setup extends Runnable {}
    private interface Expectation {
        void success(@NotNull Runnable check);
        void failure(@NotNull Consumer<Throwable> check);
    }

    private @NotNull Expectation assertInitializeFromCommandLine(@NotNull Setup setup, @NotNull String @NotNull[] args) {
        return new Expectation() {
            @Override public void success(@NotNull Runnable check) {
                assertInitializeFromCommandLine(setup, args, check, Assertions::fail);
            }
            @Override public void failure(@NotNull Consumer<Throwable> check) {
                assertInitializeFromCommandLine(setup, args, () -> fail("Expected to fail but succeeded"), check);
            }
        };
    }

    private void assertInitializeFromCommandLine(@NotNull Setup setup,
                                                 @NotNull String @NotNull[] args,
                                                 @NotNull Runnable success,
                                                 @NotNull Consumer<Throwable> error) {
        resetAll();
        try {
            setup.run();
            try (ThrowingExit ignore = ThrowingExit.install()) {
                Flags.initializeFromCommandLine(args);
            }
            success.run();
        } catch (Throwable throwable) {
            error.accept(throwable);
        } finally {
            resetAll();
        }
    }

    private void resetAll() {
        Flags.Registry.instance().reset();
        Fields.of(FlagsTest.class).list(Scope.DECLARED).filter(Predicate.not(BasicMembers::isStatic)).forEach(field -> {
            FieldValue.of(field).set(FlagsTest.this, null);
        });
    }
}
