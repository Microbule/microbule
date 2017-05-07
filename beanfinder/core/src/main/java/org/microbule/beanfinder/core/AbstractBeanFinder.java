package org.microbule.beanfinder.core;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import com.google.common.collect.MapMaker;
import com.google.common.util.concurrent.Uninterruptibles;
import org.microbule.beanfinder.api.BeanFinder;
import org.microbule.beanfinder.core.listener.CollectionBeanFinderListener;
import org.microbule.beanfinder.core.listener.MapBeanFinderListener;
import org.microbule.beanfinder.core.listener.RefBeanFinderListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBeanFinder implements BeanFinder {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBeanFinder.class);

    private final CountDownLatch completionLatch = new CountDownLatch(1);

//----------------------------------------------------------------------------------------------------------------------
// BeanFinder Implementation
//----------------------------------------------------------------------------------------------------------------------

    @Override
    public void awaitCompletion() {
        LOGGER.debug("Awaiting completion latch...");
        Uninterruptibles.awaitUninterruptibly(completionLatch);
    }

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
    public <B extends Comparable<? super B>> SortedSet<B> beanSortedSet(Class<B> beanType) {
        return beanSortedSet(beanType, Comparator.naturalOrder());
    }

    @Override
    public <B> SortedSet<B> beanSortedSet(Class<B> beanType, Comparator<? super B> comparator) {
        final SortedSet<B> beans = new ConcurrentSkipListSet<>(comparator);
        findBeans(beanType, new CollectionBeanFinderListener<>(beans));
        return beans;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected void complete() {
        LOGGER.debug("Releasing completion latch...");
        completionLatch.countDown();
    }
}
