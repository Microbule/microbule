package org.microbule.test.osgi;

import java.util.Dictionary;
import java.util.Hashtable;

public class ServicePropsBuilder {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Dictionary<String, Object> props = new Hashtable<>();

//----------------------------------------------------------------------------------------------------------------------
// Static Methods
//----------------------------------------------------------------------------------------------------------------------

    public static ServicePropsBuilder props() {
        return new ServicePropsBuilder();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public Dictionary<String, Object> build() {
        return props;
    }

    public ServicePropsBuilder with(String name, Object value) {
        if (value == null) {
            props.remove(name);
        } else {
            props.put(name, value);
        }
        return this;
    }
}
