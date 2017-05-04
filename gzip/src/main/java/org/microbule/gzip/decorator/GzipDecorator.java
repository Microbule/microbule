package org.microbule.gzip.decorator;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Singleton
@Named("gzipDecorator")
public class GzipDecorator implements JaxrsServerDecorator, JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String THRESHOLD_PROP = "threshold";
    public static final String FORCE_PROP = "force";
    public static final int DEFAULT_TRESHOLD = 1024;

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        final GZIPFeature feature = new GZIPFeature();
        feature.setForce(config.booleanValue(FORCE_PROP).orElse(Boolean.FALSE));
        feature.setThreshold(config.integerValue(THRESHOLD_PROP).orElse(DEFAULT_TRESHOLD));
        descriptor.addFeature(feature);
    }

    @Override
    public String name() {
        return "gzip";
    }
}
