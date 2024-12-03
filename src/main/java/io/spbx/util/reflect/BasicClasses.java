package io.spbx.util.reflect;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;

@Stateless
@Pure
@CheckReturnValue
public class BasicClasses {
    public static boolean isPublic(@NotNull Class<?> klass) {
        return Modifier.isPublic(klass.getModifiers());
    }

    public static boolean isStatic(@NotNull Class<?> klass) {
        return Modifier.isStatic(klass.getModifiers());
    }

    public static boolean isPrivate(@NotNull Class<?> klass) {
        return Modifier.isPrivate(klass.getModifiers());
    }

    public static boolean isNested(@NotNull Class<?> klass) {
        return klass.getNestHost() != klass;
    }

    // https://stackoverflow.com/questions/17468198/java-how-to-check-if-an-object-is-an-instance-of-a-non-static-inner-class-rega
    public static boolean isInnerNested(Class<?> klass) {
        return (isNested(klass) || klass.isMemberClass()) && !isStatic(klass);
    }

    public static @NotNull Class<?> getNestHost(@NotNull Class<?> klass) {
        Class<?> nestHost = klass.getNestHost();
        return nestHost == klass ? nestHost : getNestHost(nestHost);
    }
}
