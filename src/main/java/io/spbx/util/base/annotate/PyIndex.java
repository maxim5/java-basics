package io.spbx.util.base.annotate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated parameter can be used for negative (python) indexing.
 * Usually the parameter type is {@code int}.
 * @see AllowPythonIndexing
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.CLASS)
public @interface PyIndex {
}
