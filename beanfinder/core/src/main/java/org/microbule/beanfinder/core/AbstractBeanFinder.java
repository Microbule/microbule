package org.microbule.beanfinder.core;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import com.google.common.collect.MapMaker;
import org.microbule.beanfinder.api.BeanFinder;
import org.microbule.beanfinder.core.listener.CollectionBeanFinderListener;
import org.microbule.beanfinder.core.listener.MapBeanFinderListener;
import org.microbule.beanfinder.core.listener.RefBeanFinderListener;

public abstract class AbstractBeanFinder implements BeanFinder {
//----------------------------------------------------------------------------------------------------------------------
// BeanFinder Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public <B> List<B> beanList(Class<B> beanType) {
        final List<B> list = new CopyOnWriteArrayList<>();
        findBeans(beanType, new CollectionBeanFinderListener<>(list));
        return list;
    }

    @Override
    public <K, B> Map<K, B> beanMap(Class<B> beanType, Function<B, K> keyFunction) {
        final Map<K, B> map = new MapMaker().makeMap();
        findBeans(beanType, new MapBeanFinderListener<>(map, keyFunction));
        return map;
    }

    @Override
    public <B> AtomicReference<B> beanReference(Class<B> beanType, B defaultValue) {
        final AtomicReference<B> ref = new AtomicReference<>(defaultValue);
        findBeans(beanType, new RefBeanFinderListener<>(ref, defaultValue));
        return ref;
    }

    @Override
    public <B> Set<B> sortedBeanSet(Class<B> beanType, Comparator<? super B> comparator) {
        final Set<B> beans = new ConcurrentSkipListSet<>(comparator);
        findBeans(beanType, new CollectionBeanFinderListener<>(beans));
        return beans;
    }

    @Override
    public <B extends Comparable> Set<B> sortedBeanSet(Class<B> beanType) {
        final Set<B> beans = new ConcurrentSkipListSet<>();
        findBeans(beanType, new CollectionBeanFinderListener<>(beans));
        return beans;
    }
}
