package org.microbule.jsonb.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.ObjectUtils;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JsonbProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Supplier<Jsonb> jsonBuilderSupplier;
    private final Function<JsonbException, ? extends RuntimeException> exceptionProvider;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public JsonbProvider(Supplier<Jsonb> jsonBuilderSupplier, Function<JsonbException, ? extends RuntimeException> exceptionProvider) {
        this.jsonBuilderSupplier = jsonBuilderSupplier;
        this.exceptionProvider = exceptionProvider;
    }

//----------------------------------------------------------------------------------------------------------------------
// MessageBodyReader Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException {
        try (InputStreamReader streamReader = new InputStreamReader(entityStream, StandardCharsets.UTF_8)) {
            try {
                return jsonBuilderSupplier.get().fromJson(streamReader, resolveType(type, genericType));
            } catch (JsonbException e) {
                throw exceptionProvider.apply(e);
            }
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// MessageBodyWriter Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public long getSize(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public void writeTo(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(entityStream, StandardCharsets.UTF_8)) {
            jsonBuilderSupplier.get().toJson(object, resolveType(type, genericType), writer);
        }
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    private Type resolveType(Class<?> type, Type genericType) {
        if (ObjectUtils.notEqual(type, genericType)) {
            return genericType;
        } else {
            return type;
        }
    }
}
