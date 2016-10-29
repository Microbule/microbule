package org.microbule.decorator.cors;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.microbule.spi.JaxrsServerProperties;

@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    public static final String DEFAULT_ALLOWED_METHODS = "HEAD, GET, PUT, POST, DELETE";
    public static final String COMMA_SEPARATED = ",";
    public static final String ALLOWED_ORIGINS_PROP = "microbule.cors.allowedOrigins";
    public static final String ALLOW_CREDENTIALS_PROP = "microbule.cors.allowCredentials";
    public static final String ALLOWED_METHODS_PROP = "microbule.cors.allowedMethods";
    public static final String ALLOWED_HEADERS_PROP = "microbule.cors.allowedHeaders";
    public static final String MAX_AGE_PROP = "microbule.cors.maxAge";
    public static final String EXPOSED_HEADERS_PROP = "microbule.cors.exposedHeaders";
    public static final String EMPTY_STRING = "";
    private static final Set<String> SIMPLE_RESPONSE_HEADERS = new HashSet<>(Arrays.asList(
            "Cache-Control".toUpperCase(),
            "Content-Language".toUpperCase(),
            "Content-Type".toUpperCase(),
            "Expires".toUpperCase(),
            "Last-Modified".toUpperCase(),
            "Pragma".toUpperCase()
    ));

    private static final String HEADER_ORIGIN = "Origin";
    private static final String HEADER_AC_REQUEST_METHOD = "Access-Control-Request-Method";
    private static final String HEADER_AC_REQUEST_HEADERS = "Access-Control-Request-Headers";
    private static final String HEADER_AC_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String HEADER_AC_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private static final String HEADER_AC_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    private static final String HEADER_AC_MAX_AGE = "Access-Control-Max-Age";

    private static final String HEADER_AC_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String HEADER_VARY = "Vary";
    private static final String HEADER_AC_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    private static final String HTTP_METHOD_OPTIONS = "OPTIONS";

    private static final String ALLOW_ALL = "*";
    private static final long DEFAULT_MAX_AGE = TimeUnit.DAYS.toSeconds(1);

    private final Set<String> allowedOrigins;
    private final Set<String> allowedMethods;
    private final Set<String> allowedHeaders;
    private final Set<String> exposedHeaders;
    private final boolean allowCredentials;
    private final long maxAge;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public CorsFilter(JaxrsServerProperties serverProperties) {
        this.allowedOrigins = parseWhitelist(serverProperties.getProperty(ALLOWED_ORIGINS_PROP, ALLOW_ALL));
        this.allowedMethods = parseWhitelist(serverProperties.getProperty(ALLOWED_METHODS_PROP, DEFAULT_ALLOWED_METHODS));
        this.allowedHeaders = parseWhitelist(serverProperties.getProperty(ALLOWED_HEADERS_PROP, ALLOW_ALL).toUpperCase());
        this.exposedHeaders = parseCommaSeparatedSet(serverProperties.getProperty(EXPOSED_HEADERS_PROP, EMPTY_STRING));
        this.maxAge = serverProperties.getProperty(MAX_AGE_PROP, Long::parseLong, DEFAULT_MAX_AGE);
        this.allowCredentials = serverProperties.isTrue(ALLOW_CREDENTIALS_PROP);
    }

    private static Set<String> parseWhitelist(String whitelist) {
        if (ALLOW_ALL.equals(whitelist)) {
            return null;
        } else {
            return parseCommaSeparatedSet(whitelist);
        }
    }

    private static Set<String> parseCommaSeparatedSet(String value) {
        if(EMPTY_STRING.equals(value)) {
            return new HashSet<>();
        }
        return Arrays.stream(value.split(COMMA_SEPARATED)).map(String::trim).collect(Collectors.toSet());
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerRequestFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        if (isPreflight(request)) {
            request.abortWith(handlePreflight(request));
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// ContainerResponseFilter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        final MultivaluedMap<String, Object> headers = response.getHeaders();
        final String origin = request.getHeaderString(HEADER_ORIGIN);
        if (origin != null && isAllowedOrigin(origin)) {
            exposedHeaders.forEach(header -> headers.add(HEADER_AC_EXPOSE_HEADERS, header));
            headers.add(HEADER_AC_ALLOW_ORIGIN, origin);
            headers.add(HEADER_AC_ALLOW_CREDENTIALS, String.valueOf(allowCredentials));
        }
        headers.add(HEADER_VARY, HEADER_ORIGIN);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Response handlePreflight(ContainerRequestContext request) {
        final String origin = request.getHeaderString(HEADER_ORIGIN);
        if (!isAllowedOrigin(origin)) {
            return failedPreflight();
        }
        final String method = request.getMethod();
        if (!isWhitelisted(allowedMethods, method)) {
            return failedPreflight();
        }
        final List<String> requestHeaders = request.getHeaders().getOrDefault(HEADER_AC_REQUEST_HEADERS, new LinkedList<>()).stream().filter(header -> !SIMPLE_RESPONSE_HEADERS.contains(header)).collect(Collectors.toList());
        if (requestHeaders.stream().filter(requestHeader -> !isWhitelisted(allowedHeaders, requestHeader.toUpperCase())).findFirst().isPresent()) {
            return failedPreflight();
        }
        final Response.ResponseBuilder builder = Response.noContent();
        builder.header(HEADER_VARY, HEADER_ORIGIN);
        builder.header(HEADER_AC_ALLOW_ORIGIN, origin);
        builder.header(HEADER_AC_ALLOW_CREDENTIALS, String.valueOf(allowCredentials));
        builder.header(HEADER_AC_MAX_AGE, String.valueOf(maxAge));
        allowedMethods.forEach(allowedMethod -> builder.header(HEADER_AC_ALLOW_METHODS, allowedMethod));
        requestHeaders.forEach(requestHeader -> builder.header(HEADER_AC_ALLOW_HEADERS, requestHeader));
        return builder.build();
    }

    private boolean isAllowedOrigin(String origin) {
        return allowedOrigins == null || allowedOrigins.contains(origin);
    }

    private static boolean isWhitelisted(Set<String> whitelist, String value) {
        return whitelist == null || whitelist.contains(value);
    }

    private Response failedPreflight() {
        return Response.noContent().build();
    }

    private boolean isPreflight(ContainerRequestContext request) {
        return HTTP_METHOD_OPTIONS.equals(request.getMethod()) &&
                request.getHeaderString(HEADER_ORIGIN) != null &&
                request.getHeaderString(HEADER_AC_REQUEST_METHOD) != null;
    }
}
