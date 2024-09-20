package io.spbx.util.code.gen;

import io.spbx.util.code.gen.CompiledTemplate.CompiledDirective;
import io.spbx.util.code.gen.Directive.Predefined;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.code.gen.DirectiveBuilder.block;
import static io.spbx.util.testing.TestingBasics.mapOf;

@Tag("fast")
public class CompiledTemplateTest {
    @Test
    public void compiled_directive_eval_condition() {
        assertThat(if_("").evalCondition(vars())).isTrue();
        assertThat(if_("1=1").evalCondition(vars())).isTrue();
        assertThat(if_("1=2").evalCondition(vars())).isFalse();
        assertThat(if_("1=1 2=2").evalCondition(vars())).isTrue();
        assertThat(if_("1=1 2=3").evalCondition(vars())).isFalse();

        assertThat(if_("$foo").evalCondition(vars("$foo", "true"))).isTrue();
        assertThat(if_("$foo").evalCondition(vars("$foo", "false"))).isFalse();
        assertThat(if_("$foo").evalCondition(vars("$bar", "true"))).isFalse();
        assertThat(if_("$foo").evalCondition(vars("$foo", "true", "$bar", "false"))).isTrue();
        assertThat(if_("$foo").evalCondition(vars())).isFalse();

        assertThat(if_("$foo=1 $bar=2").evalCondition(vars("$foo", "1", "$bar", "2"))).isTrue();
        assertThat(if_("$foo=1 $bar=2").evalCondition(vars("$bar", "2", "$foo", "1"))).isTrue();
        assertThat(if_("$foo=1 $bar=2").evalCondition(vars("$foo", "1"))).isFalse();
        assertThat(if_("$foo=1 $bar=2").evalCondition(vars("$bar", "2"))).isFalse();
        assertThat(if_("$foo=1 $bar=2").evalCondition(vars("$foo", "2", "$bar", "1"))).isFalse();
    }

    private static @NotNull CompiledDirective if_(@NotNull String attrs) {
        return CompiledDirective.from(block().predef(Predefined.IF).attrs(attrs).build());
    }

    private static @NotNull Variables vars(@NotNull String @NotNull ... attrs) {
        return Variables.of(mapOf((Object[]) attrs));
    }
}
