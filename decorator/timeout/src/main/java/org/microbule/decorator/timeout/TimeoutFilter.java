package org.microbule.decorator.timeout;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.microbule.spi.JaxrsProxyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class TimeoutFilter implements ClientRequestFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeoutFilter.class);

    private static final String CONNECTION_TIMEOUT_PROP_PATTERN = "microbule.timeout.%s.connectionTimeout";
    private static final String RECEIVE_TIMEOUT_PROP_PATTERN = "microbule.timeout.%s.receiveTimeout";
    private static final String DEFAULT_CONNECTION_TIMEOUT_PROP = "microbule.timeout.connectionTimeout";
    private static final String DEFAULT_RECEIVE_TIMEOUT_PROP = "microbule.timeout.receiveTimeout";
    private static final String OPERATION_NAME_PROP = "org.apache.cxf.resource.operation.name";

    private final Map<String, HTTPClientPolicy> policyMap = new HashMap<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public TimeoutFilter(JaxrsProxyConfig config, long globalConnectionTimeout, long globalReceiveTimeout) {
        final long defaultConnectionTimeout = getTimeout(config, DEFAULT_CONNECTION_TIMEOUT_PROP, globalConnectionTimeout);
        final long defaultReceiveTimeout = getTimeout(config, DEFAULT_RECEIVE_TIMEOUT_PROP, globalReceiveTimeout);
        Arrays.stream(config.getServiceInterface().getMethods())
                .forEach(method -> {
                    final String methodName = method.getName();
                    HTTPClientPolicy policy = new HTTPClientPolicy();
                    policy.setConnectionTimeout(getTimeout(config, String.format(CONNECTION_TIMEOUT_PROP_PATTERN, methodName), defaultConnectionTimeout));
                    policy.setReceiveTimeout(getTimeout(config, String.format(RECEIVE_TIMEOUT_PROP_PATTERN, methodName), defaultReceiveTimeout));
                    policyMap.put(methodName, policy);
                });
    }

    private static long getTimeout(JaxrsProxyConfig config, String name, long defaultValue) {
        return config.getProperty(name, Long::parseLong, defaultValue);
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
