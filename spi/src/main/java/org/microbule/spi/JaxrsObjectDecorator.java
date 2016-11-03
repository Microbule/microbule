package org.microbule.spi;

public interface JaxrsObjectDecorator<T extends JaxrsObjectConfig> {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    void decorate(T object);
}
