package org.microbule.core;

import java.util.LinkedList;
import java.util.List;

import org.apache.cxf.feature.Feature;
import org.microbule.spi.JaxrsServiceDescriptor;

public class JaxrsServiceDescriptorImpl implements JaxrsServiceDescriptor {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Class<?> serviceInterface;
    private final List<Object> providers = new LinkedList<>();
    private final List<Feature> features = new LinkedList<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServiceDescriptorImpl(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDescriptor Implementation
//----------------------------------------------------------------------------------------------------------------------

    public JaxrsServiceDescriptor addFeature(Feature feature) {
        features.add(feature);
        return this;
    }

    public JaxrsServiceDescriptor addProvider(Object provider) {
        providers.add(provider);
        return this;
    }

    public Class<?> serviceInterface() {
        return serviceInterface;
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    List<Feature> getFeatures() {
        return features;
    }

    List<Object> getProviders() {
        return providers;
    }
}
