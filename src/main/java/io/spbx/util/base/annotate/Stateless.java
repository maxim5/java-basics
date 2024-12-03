package io.spbx.util.base.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The class to which this annotation is applied does not have any state.
 * <p>
 * Often this is used to mark a static method container which is not intended to be instantiated.
 * But in some cases the annotated class can implement convenient interfaces
 * (e.g. a {@code Converter} or a {@code Codec}) and still doesn't have any state.
 * <p>
 * Stateless objects are inherently immutable and thread-safe.
 *
 * @see javax.annotation.concurrent.Immutable
 * @see javax.annotation.concurrent.ThreadSafe
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Stateless {
}
