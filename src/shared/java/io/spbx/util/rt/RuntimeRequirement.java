package io.spbx.util.rt;

import org.jetbrains.annotations.NotNull;

/**
 * Asserts that a particular class is in the classpath. Useful for library utils with implicit dependencies.
 * <p>
 * Usage:
 * <pre>
 * public class Foo {
 *   static {
 *     RuntimeRequirement.verify("io.netty.buffer.ByteBuf");
 *   }
 *   // ...
 * }
 * </pre>
 */
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
