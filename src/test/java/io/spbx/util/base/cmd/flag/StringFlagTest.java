package io.spbx.util.base.cmd.flag;

import io.spbx.util.base.str.BasicStrings;
import io.spbx.util.func.Predicates;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.func.ScopeFunctions.also;
import static io.spbx.util.testing.AssertBasics.assertPredicate;

@Tag("fast")
public class StringFlagTest {
    @RegisterExtension private static final FlagRegistryCleanup FLAGS_CLEANUP = new FlagRegistryCleanup();

    @Test
    public void string_flag_to_arg_spec_simple() {
        also(StringFlag.of().key("-key").build().toArgSpec(), spec -> {
            assertThat(spec.key()).isEqualTo("-key");
            assertThat(spec.aliases()).isEmpty();
            assertThat(spec.isMandatory()).isTrue();
            assertThat(spec.validator()).isSameInstanceAs(Predicates.isNonNull());
        });
        also(StringFlag.of().key("--key").alias("-alias").mandatory().build().toArgSpec(), spec -> {
            assertThat(spec.key()).isEqualTo("--key");
            assertThat(spec.aliases()).containsExactly("-alias");
            assertThat(spec.isMandatory()).isTrue();
            assertThat(spec.validator()).isSameInstanceAs(Predicates.isNonNull());
        });
        also(StringFlag.of().key("-key").defaultValue("foo").build().toArgSpec(), spec -> {
            assertThat(spec.key()).isEqualTo("-key");
            assertThat(spec.aliases()).isEmpty();
            assertThat(spec.isMandatory()).isFalse();
            assertThat(spec.validator()).isSameInstanceAs(Predicates.isNonNull());
        });
        also(StringFlag.of().key("-key").defaultValue("foo").optional().build().toArgSpec(), spec -> {
            assertThat(spec.key()).isEqualTo("-key");
            assertThat(spec.aliases()).isEmpty();
            assertThat(spec.isMandatory()).isFalse();
            assertThat(spec.validator()).isSameInstanceAs(Predicates.isNonNull());
        });
        also(StringFlag.of().key("-key").notEmpty().defaultValue("foo").build().toArgSpec(), spec -> {
            assertThat(spec.key()).isEqualTo("-key");
            assertThat(spec.aliases()).isEmpty();
            assertThat(spec.isMandatory()).isFalse();
            assertPredicate(spec.validator()).isEquivalentTo(BasicStrings::isNotEmpty, null, "", "foo");
        });
    }

    @Test
    public void string_flag_to_arg_spec_validation() {
        also(StringFlag.of().key("-key").lengthBetween(2, 3).defaultValue("foo").build().toArgSpec(), spec -> {
            assertThat(spec.key()).isEqualTo("-key");
            assertThat(spec.aliases()).isEmpty();
            assertThat(spec.isMandatory()).isFalse();
            assertPredicate(spec.validator()).isEquivalentTo(s -> s != null && s.length() >= 2 && s.length() <= 3,
                                                             null, "", "1", "12", "123", "1234");
        });
    }
}
