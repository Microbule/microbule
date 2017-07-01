package org.microbule.jsonb.decorator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.config.api.Config;
import org.microbule.jsonb.api.JsonbFactory;
import org.microbule.jsonb.provider.JsonRequestParsingException;
import org.microbule.jsonb.provider.JsonbProvider;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Singleton
@Named("jsonbServerDecorator")
public class JsonbServerDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final JsonbFactory jsonbFactory;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public JsonbServerDecorator(JsonbFactory jsonbFactory) {
        this.jsonbFactory = jsonbFactory;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        descriptor.addProvider(new JsonbProvider(jsonbFactory::createJsonb, JsonRequestParsingException::new));
    }

    @Override
    public String name() {
        return "jsonb";
    }
}
