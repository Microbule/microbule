package org.microbule.api;

import java.util.Map;

public interface JaxrsServerFactory {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    JaxrsServer createJaxrsServer(Class<?> serviceInterface, Object serviceImplementation, String baseAddress, Map<String,Object> properties);
}
