package org.microbule.cdi.container;

import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Path;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.microbule.container.api.ServerDefinition;
import org.microbule.container.core.DefaultServerDefinition;
import org.microbule.container.core.StaticContainer;

@Singleton
@Named("cdiContainer")
public class CdiContainer extends StaticContainer {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    private BeanManager beanManager;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public void onInitialized(@Observes @Initialized(ApplicationScoped.class) Object init) {
        initialize();
    }

    @Override
    protected <B> Iterable<B> plugins(Class<B> pluginType) {
        final Instance<B> plugins = CDI.current().select(pluginType);
        return Lists.newArrayList(plugins.iterator());
    }

    @Override
    protected Iterable<ServerDefinition> servers() {
        return beanManager.getBeans(Object.class).stream()
                .flatMap(bean -> ClassUtils.getAllInterfaces(bean.getBeanClass()).stream().map(serviceInterface -> new ImmutablePair<>(bean, serviceInterface)))
                .filter(pair -> pair.getRight().isAnnotationPresent(Path.class))
                .map(pair -> {
                    final Bean<?> bean = pair.getLeft();
                    return new DefaultServerDefinition(bean.getName(), pair.getRight(), beanManager.getReference(bean, Object.class, beanManager.createCreationalContext(bean)));
                }).collect(Collectors.toList());
    }
}
