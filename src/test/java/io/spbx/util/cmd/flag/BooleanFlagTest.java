package io.spbx.util.cmd.flag;

import io.spbx.util.base.str.BasicParsing;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.ScopeFunctions.also;
import static io.spbx.util.testing.AssertBasics.assertPredicate;

@Tag("fast")
public class BooleanFlagTest {
    @RegisterExtension private static final FlagRegistryCleanup FLAGS_CLEANUP = new FlagRegistryCleanup();

    @Test
    public void boolean_flag_to_arg_spec_simple() {
        also(BooleanFlag.of().key("-key").build().toArgSpec(), spec -> {
            assertThat(spec.key()).isEqualTo("-key");
            assertThat(spec.aliases()).isEmpty();
            assertThat(spec.isMandatory()).isFalse();
            assertPredicate(spec.validator()).isEquivalentTo(s -> s == null || BasicParsing.isValidBoolean(s),
                                                             null, "", "true", "false", "TRUE", "FALSE", "foo");
        });
        also(BooleanFlag.of().key("--key").alias("-alias").mandatory().build().toArgSpec(), spec -> {
            assertThat(spec.key()).isEqualTo("--key");
            assertThat(spec.aliases()).containsExactly("-alias");
            assertThat(spec.isMandatory()).isTrue();
            assertPredicate(spec.validator()).isEquivalentTo(s -> s == null || BasicParsing.isValidBoolean(s),
                                                             null, "", "true", "false", "TRUE", "FALSE", "foo");
        });
    }
}
