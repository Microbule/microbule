package org.microbule.timeout.decorator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.microbule.config.api.Config;
import org.microbule.spi.JaxrsServiceDescriptor;

@Provider
public class TimeoutFilter implements ClientRequestFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String CONNECTION_TIMEOUT_PROP_PATTERN = "%s.connectionTimeout";
    private static final String RECEIVE_TIMEOUT_PROP_PATTERN = "%s.receiveTimeout";
    private static final String DEFAULT_CONNECTION_TIMEOUT_PROP = "connectionTimeout";
    private static final String DEFAULT_RECEIVE_TIMEOUT_PROP = "receiveTimeout";
    private static final String OPERATION_NAME_PROP = "org.apache.cxf.resource.operation.name";
    private static final long DEFAULT_CONNECTION_TIMEOUT = 30000;
    private static final long DEFAULT_RECEIVE_TIMEOUT = 60000;

    private final Map<String, HTTPClientPolicy> policyMap = new HashMap<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public TimeoutFilter(JaxrsServiceDescriptor descriptor, Config config) {
        final long defaultConnectionTimeout = getTimeout(config, DEFAULT_CONNECTION_TIMEOUT_PROP, DEFAULT_CONNECTION_TIMEOUT);
        final long defaultReceiveTimeout = getTimeout(config, DEFAULT_RECEIVE_TIMEOUT_PROP, DEFAULT_RECEIVE_TIMEOUT);
        Stream.of(descriptor.serviceInterface().getMethods())
                .forEach(method -> {
                    final String methodName = method.getName();
                    HTTPClientPolicy policy = new HTTPClientPolicy();
                    policy.setConnectionTimeout(getTimeout(config, String.format(CONNECTION_TIMEOUT_PROP_PATTERN, methodName), defaultConnectionTimeout));
                    policy.setReceiveTimeout(getTimeout(config, String.format(RECEIVE_TIMEOUT_PROP_PATTERN, methodName), defaultReceiveTimeout));
                    policyMap.put(methodName, policy);
                });
    }

    private static long getTimeout(Config config, String name, long defaultValue) {
        return config.longValue(name).orElse(defaultValue);
    }

//----------------------------------------------------------------------------------------------------------------------
// ClientRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        final Message message = JAXRSUtils.getCurrentMessage();
        final Exchange exchange = message.getExchange();
        final String methodName = String.valueOf(exchange.get(OPERATION_NAME_PROP));
        message.put(HTTPClientPolicy.class, policyMap.get(methodName));
    }
}
