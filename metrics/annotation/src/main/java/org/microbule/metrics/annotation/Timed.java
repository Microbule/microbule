package org.microbule.metrics.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Timed {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    String name() default EMPTY;

    String strategy() default EMPTY;
}
