package org.microbule.util.reflect;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DelegatingClassLoader extends ClassLoader {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final ClassLoader delegate;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public DelegatingClassLoader(ClassLoader parent, ClassLoader delegate) {
        super(parent);
        this.delegate = delegate;
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public ClassLoader getDelegate() {
        return delegate;
    }

//----------------------------------------------------------------------------------------------------------------------
// Canonical Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DelegatingClassLoader that = (DelegatingClassLoader) o;

        return new EqualsBuilder()
                .append(getParent(), that.getParent())
                .append(delegate, that.delegate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getParent())
                .append(delegate)
                .toHashCode();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return delegate.loadClass(name);
    }
}
