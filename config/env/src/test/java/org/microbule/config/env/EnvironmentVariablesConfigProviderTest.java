package org.microbule.config.env;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.microbule.config.api.Config;

public class EnvironmentVariablesConfigProviderTest extends Assert {
    @Test
    public void testGetProxyConfig() {
        EnvironmentVariablesConfigProvider provider = new EnvironmentVariablesConfigProvider();
        final Config config = provider.getProxyConfig(MyService.class);
        assertFalse(config.value("foo").isPresent());
    }

    @Test
    public void testGetServerConfig() {
        Map<String, String> env = new HashMap<>();
        env.put("microbule_MyService_server_foo", "bar");
        EnvironmentVariablesConfigProvider provider = new EnvironmentVariablesConfigProvider(() -> env);
        final Config config = provider.getServerConfig(MyService.class);
        assertTrue(config.value("foo").isPresent());
    }

    public interface MyService {

    }
}