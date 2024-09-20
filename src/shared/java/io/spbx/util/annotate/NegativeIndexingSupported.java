package io.spbx.util.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated class or method allows negative indexing.
 * <p>
 * Negative indexes are applied from the right end, i.e. "Python style":
 * {@code -1} is treated as {@code length() - 1},
 * {@code -2} is treated as {@code length() - 2},
 * and so on.
 * Positive indexes are treated the usual way from the left end, access beyond {@code length()} results in exception.
 * <p>
 * So, for example, if an {@code obj} contains a String value {@code "bar"}, negative indexing would work this way:
 * <ul>
 *     <li>{@code obj.valueAt(0) == 'b'}</li>
 *     <li>{@code obj.valueAt(1) == 'a'}</li>
 *     <li>{@code obj.valueAt(2) == 'r'}</li>
 *     <li>{@code obj.valueAt(3)} throws</li>
 *     <li>{@code obj.valueAt(-1) == 'r'}</li>
 *     <li>{@code obj.valueAt(-2) == 'a'}</li>
 *     <li>{@code obj.valueAt(-3) == 'b'}</li>
 *     <li>{@code obj.valueAt(-4)} throws</li>
 * </ul>
 * <p>
 * The annotation assumes that the annotated class provides the {@code length()} or {@code size()} property.
 * Otherwise, the method must also specify the exact way the indexes are applied from.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface NegativeIndexingSupported {
}
