package org.microbule.core;

import java.util.Map;

import com.google.common.collect.Lists;
import org.apache.cxf.BusFactory;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.microbule.api.JaxrsServer;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.spi.JaxrsServerConfig;

public class JaxrsServerFactoryImpl extends JaxrsObjectDecoratorRegistry<JaxrsServerConfig> implements JaxrsServerFactory {
//----------------------------------------------------------------------------------------------------------------------
// JaxrsServerFactory Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public JaxrsServer createJaxrsServer(Class<?> serviceInterface, Object serviceImplementation, String baseAddress, Map<String, Object> properties) {
        final JaxrsServerConfigImpl config = new JaxrsServerConfigImpl(serviceInterface, baseAddress, properties);
        decorate(config);
        final JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setBus(BusFactory.getDefaultBus(true));
        sf.setServiceBean(serviceImplementation);
        sf.setAddress(baseAddress);
        sf.setFeatures(Lists.newArrayList(new LoggingFeature(), createSwaggerFeature()));
        sf.setProviders(config.getProviders());
        return new JaxrsServerImpl(sf.create());
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Swagger2Feature createSwaggerFeature() {
        final Swagger2Feature feature = new Swagger2Feature();
        feature.setPrettyPrint(true);
        return feature;
    }
}
