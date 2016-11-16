package org.microbule.core;

import org.apache.cxf.feature.Feature;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.apache.cxf.transport.common.gzip.GZIPFeature;
import org.microbule.spi.JaxrsObjectConfig;

public class Features {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String GZIP_THRESHOLD_PROP = "microbule.feature.gzip.threshold";
    public static final String GZIP_FORCE_PROP = "microbule.feature.gzip.force";
    public static final int GZIP_DEFAULT_THRESHOLD = 1024;
    public static final String LOGGING_FEATURE_NAME = "logging";
    public static final String SWAGGER_FEATURE_NAME = "swagger";
    public static final String GZIP_FEATURE_NAME = "gzip";

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static Feature createGzipFeature(JaxrsObjectConfig cfg) {
        GZIPFeature gzip = new GZIPFeature();
        gzip.setThreshold(cfg.getProperty(GZIP_THRESHOLD_PROP, Integer::parseInt, GZIP_DEFAULT_THRESHOLD));
        gzip.setForce(cfg.isTrue(GZIP_FORCE_PROP));
        return gzip;
    }

    public static Feature createLoggingFeature() {
        return new LoggingFeature();
    }

    public static Feature createSwaggerFeature() {
        final Swagger2Feature feature = new Swagger2Feature();
        feature.setPrettyPrint(true);
        return feature;
    }
}
