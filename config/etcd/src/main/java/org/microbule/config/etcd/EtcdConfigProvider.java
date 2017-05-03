package org.microbule.config.etcd;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.microbule.config.api.Config;
import org.microbule.config.core.MapConfig;
import org.microbule.config.http.HttpConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class EtcdConfigProvider extends HttpConfigProvider<EtcdResponse> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(EtcdConfigProvider.class);
    private final WebTarget baseTarget;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public EtcdConfigProvider(String baseAddress) {
        super(EtcdResponse.class);
        baseTarget = ClientBuilder.newClient().target(baseAddress)
                .path("v2")
                .path("keys")
                .path("microbule");
    }

//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public String name() {
        return "etcd";
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected WebTarget createTarget(String... paths) {
        return extend(baseTarget, paths).queryParam("recursive", "true");
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected Config toConfig(EtcdResponse response, String... paths) {
        Map<String, String> values = new HashMap<>();
        fillMap(values, response.getNode());
        Config config = new MapConfig(values, "/").group("microbule");
        return dereferenceGroups(config, paths);
    }

    private void fillMap(Map<String, String> values, EtcdNode node) {
        if (node != null) {
            if (node.isDir()) {
                node.getNodes().forEach(child -> fillMap(values, child));
            } else {
                values.put(node.getKey().substring(1), node.getValue());
            }
        }
    }
}
