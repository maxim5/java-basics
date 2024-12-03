package io.spbx.util.base.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code NonPure} is a method annotation that means the method might have side effects and/or might
 * be nondeterministic. Conceptually, it completes the "lattice" of purity annotations by serving as
 * a top element. That is, any {@code @}{@link Pure} method can be treated as {@code @}{@link
 * SideEffectFree} or {@code @}{@link Deterministic}, and any {@code @}{@link SideEffectFree} or
 * {@code @}{@link Deterministic} method can be treated as {@code @NonPure}.
 * <p>
 * This annotation should not be written by a programmer, because leaving a method unannotated is
 * equivalent to writing this annotation.
 * <p>
 * The purpose of this annotation is for use by tools. A tool may distinguish between unannotated
 * methods (that the tool has not yet examined) and {@code @NonPure} methods (that the tool has
 * determined to be neither {@code @SideEffectFree} nor {@code @Deterministic}).
 * <p>
 * When used on a type, the annotation applies to all methods that not explicitly annotated as {@link NonPure}.
 *
 * @see Pure
 * @see Deterministic
 * @see SideEffectFree
 */
@Documented
@Target({
    ElementType.METHOD,
    ElementType.CONSTRUCTOR,
    ElementType.TYPE,
})
@Retention(RetentionPolicy.CLASS)
public @interface NonPure {
}
