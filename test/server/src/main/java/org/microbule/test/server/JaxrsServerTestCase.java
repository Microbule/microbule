package org.microbule.test.server;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.After;
import org.junit.Before;
import org.microbule.api.JaxrsProxyFactory;
import org.microbule.api.JaxrsServer;
import org.microbule.api.JaxrsServerFactory;
import org.microbule.beanfinder.core.SimpleBeanFinder;
import org.microbule.config.core.MapConfig;
import org.microbule.core.DefaultJaxrsProxyFactory;
import org.microbule.core.DefaultJaxrsServerFactory;
import org.microbule.test.core.MockObjectTestCase;

public abstract class JaxrsServerTestCase<T> extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String BASE_ADDRESS_PATTERN = "http://localhost:%d/%s";
    public static final int DEFAULT_PORT = 8383;
    public static final String USE_ASYNC_HTTP_CONDUIT_PROP = "use.async.http.conduit";

    private JaxrsServer server;
    private String baseAddress;
    private final SimpleBeanFinder finder = new SimpleBeanFinder();
    private DefaultJaxrsProxyFactory proxyFactory;

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract T createImplementation();

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public String getBaseAddress() {
        return baseAddress;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected void addBeans(SimpleBeanFinder finder) {
        // Do nothing!
    }

    protected String createBaseAddress() {
        return String.format(BASE_ADDRESS_PATTERN, getPort(), getClass().getSimpleName());
    }

    protected MapConfig createConfig() {
        return new MapConfig();
    }

    protected T createProxy() {
        MapConfig config = createConfig();
        config.addValue(JaxrsProxyFactory.ADDRESS_PROP, baseAddress);
        final T proxy = proxyFactory.createProxy(getServiceInterface(), config);
        WebClient.getConfig(proxy).getRequestContext().put(USE_ASYNC_HTTP_CONDUIT_PROP, Boolean.TRUE);
        return proxy;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getServiceInterface() {
        final Map<TypeVariable<?>, Type> arguments = TypeUtils.getTypeArguments(getClass(), JaxrsServerTestCase.class);
        final TypeVariable<Class<JaxrsServerTestCase>> variable = JaxrsServerTestCase.class.getTypeParameters()[0];
        return (Class<T>) arguments.get(variable);
    }

    protected WebTarget createWebTarget() {
        return ClientBuilder.newClient().target(getBaseAddress()).property(USE_ASYNC_HTTP_CONDUIT_PROP, Boolean.TRUE);
    }

    protected int getPort() {
        return DEFAULT_PORT;
    }

    @After
    public void shutdownServer() {
        if (server != null) {
            server.shutdown();
        }
    }

    @Before
    public void startServer() {
        addBeans(finder);

        finder.initialize();

        final DefaultJaxrsServerFactory factory = new DefaultJaxrsServerFactory(finder);
        factory.start();
        proxyFactory = new DefaultJaxrsProxyFactory(finder);
        proxyFactory.start();
        baseAddress = createBaseAddress();

        final MapConfig config = createConfig();
        config.addValue(JaxrsServerFactory.ADDRESS_PROP, baseAddress);
        server = factory.createJaxrsServer(getServiceInterface(), createImplementation(), config);
    }
}
