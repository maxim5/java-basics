package io.spbx.util.base.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for constructors of {@code AutoCloseable}s or methods that return an {@code AutoCloseable} and require
 * that the resource is closed.
 * <p>
 * This is enforced by checking that invocations occur within the resource variable initializer
 * of a try-with-resources statement, which guarantees that the resource is always closed. The
 * analysis may be improved in the future to recognize other patterns where the resource will always
 * be closed.
 */
@Documented
@Target({
    ElementType.CONSTRUCTOR,
    ElementType.METHOD,
})
@Retention(RetentionPolicy.CLASS)
public @interface MustBeClosed {
}
