package org.microbule.core;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.savoirtech.eos.pattern.whiteboard.KeyedWhiteboard;
import org.microbule.spi.JaxrsObject;
import org.osgi.framework.BundleContext;

public abstract class JaxrsDecoratorWhiteboard<T> extends KeyedWhiteboard<String, T> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String NAME_PROP = "name";
    private static final String ENABLED_PROP_PATTERN = "microbule.%s.enabled";

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    protected JaxrsDecoratorWhiteboard(BundleContext bundleContext, Class<T> serviceType) {
        super(bundleContext, serviceType, (svc, props) -> props.getProperty(NAME_PROP));
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected List<T> decoratorsFor(JaxrsObject object) {
        return asMap().entrySet().stream()
                .filter(entry -> object.getProperty(enabledProperty(entry), Boolean::parseBoolean, Boolean.TRUE))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private String enabledProperty(Map.Entry<String, T> entry) {
        return String.format(ENABLED_PROP_PATTERN, entry.getKey());
    }
}
