package org.microbule.errormap.mapper;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.microbule.errormap.spi.ConstantErrorMapper;

@Named("illegalArgumentExceptionMapper")
@Singleton
public class IllegalArgumentExceptionErrorMapper extends ConstantErrorMapper {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public IllegalArgumentExceptionErrorMapper() {
        super(IllegalArgumentException.class, Response.Status.BAD_REQUEST);
    }
}
