package org.microbule.jsonb.decorator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.jsonb.api.JsonbFactory;
import org.microbule.jsonb.provider.JsonResponseParsingException;
import org.microbule.jsonb.provider.JsonbProvider;
import org.microbule.spi.JaxrsProxyDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Singleton
@Named("jsonbProxyDecorator")
public class JsonbProxyDecorator implements JaxrsProxyDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final JsonbFactory jsonbFactory;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public JsonbProxyDecorator(JsonbFactory jsonbFactory) {
        this.jsonbFactory = jsonbFactory;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        descriptor.addProvider(new JsonbProvider(jsonbFactory::createJsonb, JsonResponseParsingException::new));
    }

    @Override
    public String name() {
        return "jsonb";
    }
}
