package io.spbx.util.base.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the return value of the annotated API is ignorable.
 * <p>
 * This is the opposite of {@link CheckReturnValue}. It can be used inside classes or packages
 * annotated with {@code @CheckReturnValue} to exempt specific APIs from the default.
 *
 * @see CheckReturnValue
 */
@Documented
@Target({
    ElementType.METHOD,
    ElementType.CONSTRUCTOR,
    ElementType.TYPE,
    ElementType.PACKAGE,
})
@Retention(RetentionPolicy.CLASS)
public @interface CanIgnoreReturnValue {
}
