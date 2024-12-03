package io.spbx.util.base.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code Pure} is a method annotation that means both {@link SideEffectFree} and {@link Deterministic}.
 * The more important of these, when performing pluggable type-checking, is usually {@link SideEffectFree}.
 * <p>
 * For a discussion of the meaning of {@code Pure} on a constructor, see the documentation of {@link Deterministic}.
 * <p>
 * This annotation is inherited by subtypes, just as if it were meta-annotated with {@code @InheritedAnnotation}.
 * <p>
 * When used on a type, the annotation applies to all methods that do not return {@code void} and not explicitly
 * annotated as {@link NonPure}.
 *
 * @see Deterministic
 * @see SideEffectFree
 * @see NonPure
 */
@Documented
@Target({
    ElementType.METHOD,
    ElementType.CONSTRUCTOR,
    ElementType.TYPE,
})
@Retention(RetentionPolicy.CLASS)
public @interface Pure {
}
