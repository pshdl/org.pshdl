package javax.annotation;

import java.lang.annotation.*;

import javax.annotation.meta.*;

/**
 * This annotation can be applied to a package, class or method to indicate that
 * the method parameters in that element are nonnull by default unless there is:
 * <ul>
 * <li>An explicit nullness annotation
 * <li>The method overrides a method in a superclass (in which case the
 * annotation of the corresponding parameter in the superclass applies)
 * <li>there is a default parameter annotation applied to a more tightly nested
 * element.
 * </ul>
 * 
 */
@Documented
@Nonnull
@TypeQualifierDefault(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParametersAreNonnullByDefault {
}