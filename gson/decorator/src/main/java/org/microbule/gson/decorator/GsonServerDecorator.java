package org.microbule.gson.decorator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.microbule.config.api.Config;
import org.microbule.errormap.spi.ConstantErrorMapper;
import org.microbule.gson.api.GsonService;
import org.microbule.gson.provider.GsonProvider;
import org.microbule.gson.provider.JsonRequestParsingException;
import org.microbule.spi.JaxrsServerDecorator;
import org.microbule.spi.JaxrsServiceDescriptor;

@Named("gsonServerDecorator")
@Singleton
public class GsonServerDecorator implements JaxrsServerDecorator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final GsonService gsonService;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public GsonServerDecorator(GsonService gsonService) {
        this.gsonService = gsonService;
    }

//----------------------------------------------------------------------------------------------------------------------
// JaxrsServiceDecorator Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void decorate(JaxrsServiceDescriptor descriptor, Config config) {
        descriptor.addProvider(new GsonProvider(gsonService, e -> new JsonRequestParsingException(e)));
        descriptor.addProvider(new ConstantErrorMapper(JsonRequestParsingException.class, Response.Status.BAD_REQUEST));
    }

    @Override
    public String name() {
        return "gson";
    }
}
