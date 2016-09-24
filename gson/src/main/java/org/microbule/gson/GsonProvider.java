package org.microbule.gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.savoirtech.eos.pattern.whiteboard.AbstractWhiteboard;
import com.savoirtech.eos.util.ServiceProperties;
import org.apache.commons.lang3.ObjectUtils;
import org.microbule.spi.JaxrsServer;
import org.microbule.spi.JaxrsServerDecorator;
import org.osgi.framework.BundleContext;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GsonProvider extends AbstractWhiteboard<GsonCustomizer, GsonCustomizer> implements MessageBodyReader<Object>, MessageBodyWriter<Object>, JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<GsonCustomizer> customizers = new CopyOnWriteArrayList<>();
    private final AtomicReference<Gson> gson = new AtomicReference<>();

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public GsonProvider(BundleContext bundleContext) {
        super(bundleContext, GsonCustomizer.class);
        rebuild();
        start();
    }

    private void rebuild() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        customizers.forEach(customizer -> customizer.customize(builder));
        gson.set(builder.create());
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServerDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServer server) {
        server.addProvider(this);
    }

//----------------------------------------------------------------------------------------------------------------------
// MessageBodyReader Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        try (InputStreamReader streamReader = new InputStreamReader(entityStream, Charsets.UTF_8)) {
            return gson.get().fromJson(streamReader, resolveType(type, genericType));
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// MessageBodyWriter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public long getSize(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public void writeTo(Object o, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        try (OutputStreamWriter writer = new OutputStreamWriter(entityStream, Charsets.UTF_8)) {
            gson.get().toJson(o, resolveType(type, genericType), writer);
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected GsonCustomizer addService(GsonCustomizer service, ServiceProperties properties) {
        customizers.add(service);
        rebuild();
        return service;
    }

    @Override
    protected void removeService(GsonCustomizer service, GsonCustomizer tracked) {
        customizers.remove(tracked);
        rebuild();
    }

    private Type resolveType(Class<?> type, Type genericType) {
        if (ObjectUtils.notEqual(type, genericType)) {
            return genericType;
        } else {
            return type;
        }
    }
}