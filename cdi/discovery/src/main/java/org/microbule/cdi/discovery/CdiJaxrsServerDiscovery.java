package org.microbule.cdi.discovery;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Path;

import org.apache.commons.lang3.ClassUtils;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.cdi.core.event.BeanFinderStarted;
import org.microbule.config.api.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
@Named("cdiJaxrsServerDiscovery")
public class CdiJaxrsServerDiscovery {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(CdiJaxrsServerDiscovery.class);

    @Inject
    private BeanManager beanManager;

    @Inject
    private JaxrsServerFactory serverFactory;

    @Inject
    private ConfigService configService;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public void discoverServers(@Observes BeanFinderStarted event) {
        final Set<Bean<?>> beans = beanManager.getBeans(Object.class);
        beans.forEach(bean -> {
            ClassUtils.getAllInterfaces(bean.getBeanClass()).forEach(serviceInterface -> {
                if (serviceInterface.isAnnotationPresent(Path.class)) {
                    final Object serviceImplementation = beanManager.getReference(bean, Object.class, beanManager.createCreationalContext(bean));
                    LOGGER.info("Creating {} JAX-RS server for bean named \"{}\".", serviceInterface.getSimpleName(), bean.getName());
                    serverFactory.createJaxrsServer(serviceInterface, serviceImplementation, configService.getServerConfig(serviceInterface));
                    LOGGER.info("{} service started.", serviceInterface.getSimpleName());
                }
            });
        });
    }
}
