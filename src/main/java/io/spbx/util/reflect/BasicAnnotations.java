package io.spbx.util.reflect;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

@Stateless
@Pure
@CheckReturnValue
public class BasicAnnotations {
    public static <A extends Annotation> @Nullable A getAnnotationOrNull(@NotNull AnnotatedElement element,
                                                                         @NotNull Class<A> annotation) {
        return element.isAnnotationPresent(annotation) ? element.getAnnotation(annotation) : null;
    }

    public static <A extends Annotation> @NotNull Optional<A> getOptionalAnnotation(@NotNull AnnotatedElement element,
                                                                                    @NotNull Class<A> annotation) {
        return Optional.ofNullable(getAnnotationOrNull(element, annotation));
    }
}
