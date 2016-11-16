package org.microbule.core;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.apache.cxf.feature.Feature;
import org.microbule.spi.JaxrsObjectConfig;

public class FeaturesBuilder<T extends JaxrsObjectConfig> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String ENABLED_FEATURE_PROP_PATTERN = "microbule.feature.%s.enabled";
    private final List<Feature> features = new LinkedList<>();
    private final T config;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public FeaturesBuilder(T config) {
        this.config = config;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public FeaturesBuilder<T> addFeature(String name, Function<? super T, Feature> provider) {
        if (config.getProperty(String.format(ENABLED_FEATURE_PROP_PATTERN, name), Boolean::parseBoolean, Boolean.TRUE)) {
            features.add(provider.apply(config));
        }
        return this;
    }

    public List<Feature> build() {
        return features;
    }
}
