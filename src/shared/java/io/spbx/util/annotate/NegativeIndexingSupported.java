package io.spbx.util.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated class or method allows negative indexing.
 * <p>
 * Negative indexes are applied from the right end, i.e. "Python style":
 * <code>-1</code> is treated as <code>length() - 1</code>,
 * <code>-2</code> is treated as <code>length() - 2</code>, etc.
 * Positive indexes are treated the usual way, i.e. access beyond <code>length()</code> results in exception.
 * <p>
 * So, for example, if an <code>obj</code> contains <code>"bar"</code> String, negative indexing would work this way:
 * <ul>
 *     <li><code>obj.valueAt(0) == 'b'</code></li>
 *     <li><code>obj.valueAt(1) == 'a'</code></li>
 *     <li><code>obj.valueAt(2) == 'r'</code></li>
 *     <li><code>obj.valueAt(3)</code> throws</li>
 *     <li><code>obj.valueAt(-1) == 'r'</code></li>
 *     <li><code>obj.valueAt(-2) == 'a'</code></li>
 *     <li><code>obj.valueAt(-3) == 'b'</code></li>
 *     <li><code>obj.valueAt(-4)</code> throws</li>
 * </ul>
 * <p>
 * The annotation assumes that the annotated class provides the <code>length()</code> or <code>size()</code> property.
 * Otherwise, the method must also specify the exact way the indexes are applied from.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface NegativeIndexingSupported {
}
