package org.microbule.test;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.junit.After;
import org.junit.Before;
import org.microbule.api.JaxrsServer;
import org.microbule.core.JaxrsProxyFactoryImpl;
import org.microbule.core.JaxrsServerFactoryImpl;

public abstract class JaxrsTestCase<T> extends MockObjectTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String BASE_ADDRESS_PATTERN = "http://localhost:%d/%s";
    public static final int DEFAULT_PORT = 8383;
    private JaxrsServer server;
    private String baseAddress;

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

    protected void addDecorators(JaxrsServerFactoryImpl factory) {
        // Do nothing!
    }

    protected void addDecorators(JaxrsProxyFactoryImpl proxyFactory) {
        // Do nothing!
    }

    protected String createBaseAddress() {
        return String.format(BASE_ADDRESS_PATTERN, getPort(), getClass().getSimpleName());
    }

    protected Map<String, Object> createProperties() {
        return new HashMap<>();
    }

    protected T createProxy() {
        final JaxrsProxyFactoryImpl proxyFactory = new JaxrsProxyFactoryImpl();
        addDecorators(proxyFactory);
        return proxyFactory.createProxy(getServiceInterface(), baseAddress, createProperties());
    }

    @SuppressWarnings("unchecked")
    private Class<T> getServiceInterface() {
        final Map<TypeVariable<?>, Type> arguments = TypeUtils.getTypeArguments(getClass(), JaxrsTestCase.class);
        final TypeVariable<Class<JaxrsTestCase>> variable = JaxrsTestCase.class.getTypeParameters()[0];
        return (Class<T>) arguments.get(variable);
    }

    protected WebTarget createWebTarget(){
        return ClientBuilder.newClient().target(getBaseAddress());
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
        final JaxrsServerFactoryImpl factory = new JaxrsServerFactoryImpl();
        addDecorators(factory);
        baseAddress = createBaseAddress();
        server = factory.createJaxrsServer(getServiceInterface(), createImplementation(), baseAddress, createProperties());
    }
}
