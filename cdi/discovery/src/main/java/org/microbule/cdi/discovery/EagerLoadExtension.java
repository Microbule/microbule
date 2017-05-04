package org.microbule.cdi.discovery;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EagerLoadExtension implements Extension {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(EagerLoadExtension.class);

    private final Set<Bean<?>> beans = new LinkedHashSet<>();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public void afterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager manager) {
        for (Bean<?> bean : beans) {
            LOGGER.debug("Eager-loading bean named \"{}\" of type {}...", bean.getName(), bean.getBeanClass().getCanonicalName());
            manager.getReference(bean, bean.getBeanClass(), manager.createCreationalContext(bean));
        }
    }

    public <X> void processBean(@Observes ProcessBean<X> event) {
        if (event.getAnnotated().isAnnotationPresent(ApplicationScoped.class) ||
                event.getAnnotated().isAnnotationPresent(Singleton.class)) {
            beans.add(event.getBean());
        }
    }
}
