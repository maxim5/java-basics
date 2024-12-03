package io.spbx.util.testing.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * For a testable entity, references the class or classes which are being tested.
 */
@Documented
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface TestFor {
    /**
     * Classes under the test.
     */
    Class<?>[] value() default {};

    /**
     * Classes under the test (in case they can't or shouldn't be imported).
     */
    String[] classes() default {};

    /**
     * Particular methods under the test.
     */
    String[] methods() default {};
}
