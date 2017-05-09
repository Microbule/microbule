package org.microbule.container.core.listener;

import java.util.Map;
import java.util.function.Function;

import org.microbule.container.api.PluginListener;

public class MapPluginListener<K, B> implements PluginListener<B> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Map<K, B> pluginMap;
    private final Function<B, K> keyFunction;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public MapPluginListener(Map<K, B> pluginMap, Function<B, K> keyFunction) {
        this.pluginMap = pluginMap;
        this.keyFunction = keyFunction;
    }

//----------------------------------------------------------------------------------------------------------------------
// PluginListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean registerPlugin(B plugin) {
        return pluginMap.putIfAbsent(keyFunction.apply(plugin), plugin) == null;
    }

    @Override
    public void unregisterPlugin(B bean) {
        pluginMap.remove(keyFunction.apply(bean));
    }
}
