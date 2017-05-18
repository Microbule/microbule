package org.microbule.util.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.microbule.util.reflect.Types;

public abstract class AnnotationDrivenDynamicFeature<A extends Annotation> implements DynamicFeature {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<A> annotationType = Types.getTypeParameter(getClass().getGenericSuperclass(), AnnotationDrivenDynamicFeature.class, 0);

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract void configure(A annotation, ResourceInfo resourceInfo, FeatureContext featureContext);

//----------------------------------------------------------------------------------------------------------------------
// DynamicFeature Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        getAnnotation(resourceInfo.getResourceMethod(), annotationType)
                .ifPresent(annot -> configure(annot, resourceInfo, context));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected <T extends Annotation> Optional<T> getAnnotation(Method method, Class<T> type) {
        return MethodUtils.getOverrideHierarchy(method, ClassUtils.Interfaces.INCLUDE).stream()
                .map(m -> m.getAnnotation(type))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
