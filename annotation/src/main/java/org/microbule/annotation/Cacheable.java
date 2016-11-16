package org.microbule.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    int maxAge() default -1;

    boolean mustRevalidate() default false;

    boolean noCache() default false;

    String[] noCacheFields() default {};

    boolean noStore() default false;

    boolean noTransform() default true;

    String[] privateFields() default {};

    boolean privateFlag() default false;

    boolean proxyRevalidate() default false;

    int sMaxAge() default -1;
}
