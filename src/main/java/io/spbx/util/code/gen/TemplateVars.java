package io.spbx.util.code.gen;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.util.Map;

@Immutable
public record TemplateVars(@NotNull Variables vars, @NotNull Map<String, Variables> contextVars) {
    public static final TemplateVars EMPTY = new TemplateVars(Variables.EMPTY, ImmutableMap.of());

    public static @NotNull TemplateVars of(@NotNull Variables vars) {
        return new TemplateVars(vars, ImmutableMap.of());
    }

    public static @NotNull TemplateVars of(@NotNull Variables vars, @NotNull Map<String, Variables> contextVars) {
        return new TemplateVars(vars, ImmutableMap.copyOf(contextVars));
    }

    @NotNull TemplateVars withVars(@NotNull Variables vars) {
        return new TemplateVars(vars, contextVars);
    }

    @NotNull TemplateVars withVarsOverwrittenBy(@NotNull Variables newVars) {
        return this.withVars(vars.mergeAndOverwriteBy(newVars));
    }

    @Override
    public String toString() {
        if (contextVars.isEmpty()) {
            return vars.toString();
        }
        return "[vars=%s, context=%s]".formatted(vars, contextVars);
    }
}
