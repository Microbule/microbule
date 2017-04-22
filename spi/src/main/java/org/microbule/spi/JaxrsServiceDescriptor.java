package org.microbule.spi;


import org.apache.cxf.feature.Feature;

public interface JaxrsServiceDescriptor {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    JaxrsServiceDescriptor addFeature(Feature feature);

    JaxrsServiceDescriptor addProvider(Object provider);

    Class<?> serviceInterface();
}
