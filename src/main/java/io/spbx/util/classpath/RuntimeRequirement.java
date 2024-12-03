package io.spbx.util.classpath;

import io.spbx.util.base.annotate.Stateless;
import org.jetbrains.annotations.NotNull;

/**
 * Asserts that a particular class is in the classpath. Useful for library utils with implicit dependencies.
 * <p>
 * Usage:
 * {@snippet lang="java" :
 * public class Foo {
 *   static {
 *     RuntimeRequirement.verify("io.netty.buffer.ByteBuf");
 *   }
 *   // ...
 * }}
 */
@Stateless
public class RuntimeRequirement {
    public static void verify(@NotNull String className, @NotNull String requirementName) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException ignore) {
            throw new UnsupportedOperationException("Runtime requirement is missing: " + requirementName);
        }
    }

    public static void verify(@NotNull String className) {
        verify(className, className);
    }
}
