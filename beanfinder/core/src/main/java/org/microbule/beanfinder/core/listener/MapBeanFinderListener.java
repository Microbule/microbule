package org.microbule.beanfinder.core.listener;

import java.util.Map;
import java.util.function.Function;

import org.microbule.beanfinder.api.BeanFinderListener;

public class MapBeanFinderListener<K, B> implements BeanFinderListener<B> {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Map<K, B> map;
    private final Function<B, K> keyFunction;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public MapBeanFinderListener(Map<K, B> map, Function<B, K> keyFunction) {
        this.map = map;
        this.keyFunction = keyFunction;
    }

//----------------------------------------------------------------------------------------------------------------------
// BeanFinderListener Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean beanFound(B bean) {
        return map.putIfAbsent(keyFunction.apply(bean), bean) == null;
    }

    @Override
    public void beanLost(B bean) {
        map.remove(keyFunction.apply(bean));
    }
}
