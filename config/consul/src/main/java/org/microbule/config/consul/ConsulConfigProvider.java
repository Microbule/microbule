package org.microbule.config.consul;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.google.common.base.Charsets;
import com.google.gson.reflect.TypeToken;
import org.microbule.config.api.Config;
import org.microbule.config.core.ConfigUtils;
import org.microbule.config.core.MapConfig;
import org.microbule.config.http.HttpConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Named("consulConfigProvider")
public class ConsulConfigProvider extends HttpConfigProvider<List<ConsulNode>> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String DEFAULT_BASE_ADDRESS = "http://localhost:8500";
    private static final String BASE_ADDRESS_PROP = "baseAddress";
    private static final String NAME = "consul";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsulConfigProvider.class);
    private static final TypeToken<List<ConsulNode>> TYPE_TOKEN = new TypeToken<List<ConsulNode>>() {
    };

    private final WebTarget baseTarget;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ConsulConfigProvider() {
        this(ConfigUtils.bootstrapConfig(NAME).value(BASE_ADDRESS_PROP).orElse(DEFAULT_BASE_ADDRESS));
    }

    public ConsulConfigProvider(String baseAddress) {
        super(TYPE_TOKEN);
        baseTarget = ClientBuilder.newClient().target(baseAddress)
                .path("v1")
                .path("kv")
                .path("microbule");
    }

//----------------------------------------------------------------------------------------------------------------------
// ConfigProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public String name() {
        return NAME;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected WebTarget createTarget(String... paths) {
        return extend(baseTarget, paths).queryParam("recurse", "true");
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected Config toConfig(List<ConsulNode> response, String... paths) {
        final Map<String, String> values = response.stream().collect(Collectors.toMap(ConsulNode::getKey, node -> base64Decode(node.getValue())));
        Config config = new MapConfig(values, "/").group("microbule");
        return dereferenceGroups(config, paths);
    }

    private String base64Decode(String value) {
        return new String(Base64.getDecoder().decode(value), Charsets.UTF_8);
    }
}
