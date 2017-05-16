package org.microbule.util.reflect;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import org.apache.commons.lang3.reflect.TypeUtils;

public final class Types {
//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getTypeParameter(Type subType, Class<?> superType, int index) {
        final Map<TypeVariable<?>, Type> arguments = TypeUtils.getTypeArguments(subType, superType);
        final TypeVariable<? extends Class<?>> variable = superType.getTypeParameters()[index];
        return (Class<T>) arguments.get(variable);
    }

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    private Types() {

    }
}
