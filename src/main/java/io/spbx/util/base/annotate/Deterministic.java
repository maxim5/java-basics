package io.spbx.util.base.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method is called <em>deterministic</em> if it returns the same value (according to {@code ==})
 * every time it is called with the same arguments and in the same environment. The arguments
 * include the receiver, and the environment includes all of the Java heap (that is, all fields of
 * all objects and all static variables).
 * <p>
 * Determinism refers to the return value during a non-exceptional execution. If a method throws
 * an exception, the Throwable does not have to be exactly the same object on each invocation (and
 * generally should not be, to capture the correct stack trace).
 * <p>
 * This annotation is important to pluggable type-checking because, after a call to a
 * {@code @Deterministic} method, flow-sensitive type refinement can assume that anything learned
 * about the first invocation is true about subsequent invocations (so long as no
 * non-{@code @}{@link SideEffectFree} method call intervenes). For example, the following code
 * never suffers a null pointer exception, so the Nullness Checker need not issue a warning:
 *
 * {@snippet lang = "java":
 * if (x.myDeterministicMethod() != null) {
 *   x.myDeterministicMethod().hashCode();
 * }
 * }
 *
 * <p>
 * Note that {@code @Deterministic} guarantees that the result is identical according to {@code
 * ==}, <b>not</b> just equal according to {@code equals()}. This means that writing
 * {@code @Deterministic} on a method that returns a reference (including a String) is often
 * erroneous unless the returned value is cached or interned.
 * <p>
 * Also see {@link Pure}, which means both deterministic and {@link SideEffectFree}.
 * <p>
 * <b>Analysis:</b> The Checker Framework performs a conservative analysis to verify a
 * {@code @Deterministic} annotation. The Checker Framework issues a warning if the method uses any
 * of the following Java constructs:
 *
 * <ol>
 *   <li>Assignment to any expression, except for local variables and method parameters.<br>
 *       (Note that storing into an array element, such a {@code a[i] = x}, is not an assignment to
 *       a variable and is therefore forbidden.)
 *   <li>A method invocation of a method that is not {@link Deterministic}.
 *   <li>Construction of a new object.
 *   <li>Catching any exceptions. This restriction prevents a method from obtaining a reference to a
 *       newly-created exception object and using these objects (or some property thereof) to change
 *       the method's return value. For instance, the following method must be forbidden.
 *
 * {@snippet lang = "java":
 * @Deterministic
 * int f() {
 *   try {
 *     int b = 0;
 *     int a = 1 / b;
 *   } catch (Throwable t) {
 *     return t.hashCode();
 *   }
 *   return 0;
 * }
 * }
 * </ol>
 *
 * When a constructor is annotated as {@code Deterministic} (or {@code @Pure}), that means that all
 * the fields are deterministic (the same values, if the arguments are the same). The constructed
 * object itself is different. That is, a constructor <em>invocation</em> is never deterministic
 * since it returns a different new object each time.
 * <p>
 * Note that the rules for checking currently imply that every {@code Deterministic} method is
 * also {@link SideEffectFree}. This might change in the future; in general, a deterministic method
 * does not need to be side-effect-free.
 * <p>
 * These rules are conservative: any code that passes the checks is deterministic, but the
 * Checker Framework may issue false positive warnings, for code that uses one of the forbidden
 * constructs but is deterministic nonetheless.
 * <p>
 * This annotation is inherited by subtypes, just as if it were meta-annotated with {@code @InheritedAnnotation}.
 *
 * @see SideEffectFree
 * @see Pure
 */
@Documented
@Target({
    ElementType.METHOD,
    ElementType.CONSTRUCTOR,
})
@Retention(RetentionPolicy.CLASS)
public @interface Deterministic {
}
