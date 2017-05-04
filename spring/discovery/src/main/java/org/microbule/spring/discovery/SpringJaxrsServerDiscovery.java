package org.microbule.spring.discovery;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import javax.ws.rs.Path;

import org.microbule.api.JaxrsServerFactory;
import org.microbule.config.api.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(-1)
public class SpringJaxrsServerDiscovery implements BeanPostProcessor {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringJaxrsServerDiscovery.class);

    @Autowired
    private ConfigService configService;


    @Autowired
    private JaxrsServerFactory factory;


    private final List<JaxrsServerSpec> specs = new CopyOnWriteArrayList<>();

//----------------------------------------------------------------------------------------------------------------------
// BeanPostProcessor Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Stream.of(bean.getClass().getInterfaces()).forEach(serviceInterface -> {
            if(serviceInterface.isAnnotationPresent(Path.class)) {
                LOGGER.info("Discovered {} service implementation bean named \"{}\".", serviceInterface.getSimpleName(), beanName);
                specs.add(new JaxrsServerSpec(beanName, serviceInterface, bean));
            }
        });
        return bean;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @EventListener
    public void onRefreshed(ContextRefreshedEvent event) {
        specs.forEach(spec -> {
            LOGGER.info("Creating {} JAX-RS server for bean named \"{}\".", spec.getServiceInterface().getSimpleName(), spec.getBeanName());
            factory.createJaxrsServer(spec.getServiceInterface(), spec.getServiceImplementation(), configService.getServerConfig(spec.getServiceInterface()));
            LOGGER.info("{} service started.", spec.getServiceInterface().getSimpleName());
        });
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private static class JaxrsServerSpec {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final String beanName;
        private final Class<?> serviceInterface;
        private final Object serviceImplementation;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

        public JaxrsServerSpec(String beanName, Class<?> serviceInterface, Object serviceImplementation) {
            this.beanName = beanName;
            this.serviceInterface = serviceInterface;
            this.serviceImplementation = serviceImplementation;
        }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

        public String getBeanName() {
            return beanName;
        }

        public Object getServiceImplementation() {
            return serviceImplementation;
        }

        public Class<?> getServiceInterface() {
            return serviceInterface;
        }
    }
}
