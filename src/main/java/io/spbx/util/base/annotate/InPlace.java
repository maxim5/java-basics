package io.spbx.util.base.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When used on a parameter, the annotation indicates that it is modified in-place by the method.
 * When used on a method, the annotation indicates one of the parameters is modified in-place. In this case,
 * the method can also be marked as {@link NonPure}.
 *
 * @see Pure
 * @see NonPure
 */
@Documented
@Target({
    ElementType.PARAMETER,
    ElementType.METHOD,
})
@Retention(RetentionPolicy.CLASS)
public @interface InPlace {
}
