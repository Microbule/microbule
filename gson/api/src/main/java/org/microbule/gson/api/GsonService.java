package org.microbule.gson.api;

import java.io.Reader;
import java.lang.reflect.Type;

public interface GsonService {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    <T> T fromJson(Reader json, Type type);

    void toJson(Object src, Type type, Appendable writer);
}
