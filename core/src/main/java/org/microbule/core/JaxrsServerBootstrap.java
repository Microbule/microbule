package org.microbule.core;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.google.common.collect.MapMaker;
import org.microbule.api.JaxrsConfigService;
import org.microbule.api.JaxrsServer;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.config.api.Config;
import org.microbule.container.api.MicrobuleContainer;
import org.microbule.container.api.ServerDefinition;
import org.microbule.container.api.ServerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("jaxrsServerBootstrap")
@Singleton
public class JaxrsServerBootstrap implements ServerListener {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(JaxrsServerBootstrap.class);

    private final JaxrsServerFactory factory;
    private final JaxrsConfigService configService;
    private final Map<String, JaxrsServer> servers = new MapMaker().makeMap();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public JaxrsServerBootstrap(MicrobuleContainer container, JaxrsServerFactory factory, JaxrsConfigService configService) {
        this.factory = factory;
        this.configService = configService;
        container.addServerListener(this);
    }

//----------------------------------------------------------------------------------------------------------------------
// ServerListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void registerServer(ServerDefinition serverDefinition) {
        final Config config = configService.createServerConfig(serverDefinition.serviceInterface(), serverDefinition.customConfig());
        final JaxrsServer server = factory.createJaxrsServer(serverDefinition.serviceInterface(), serverDefinition.serviceImplementation(), config);
        servers.put(serverDefinition.id(), server);
    }

    @Override
    public void unregisterServer(String id) {
        JaxrsServer server = servers.remove(id);
        if (server != null) {
            server.shutdown();
        }
    }
}
