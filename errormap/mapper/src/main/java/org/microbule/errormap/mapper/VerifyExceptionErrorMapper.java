package org.microbule.errormap.mapper;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import com.google.common.base.VerifyException;
import org.microbule.errormap.spi.ConstantErrorMapper;

@Named("verifyExceptionMapper")
@Singleton
public class VerifyExceptionErrorMapper extends ConstantErrorMapper {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public VerifyExceptionErrorMapper() {
        super(VerifyException.class, Response.Status.BAD_REQUEST);
    }
}
