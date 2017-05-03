package org.microbule.beanfinder.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public interface BeanFinder {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    <K,B> Map<K,B> beanMap(Class<B> beanType, Function<B,K> keyFunction);

    <B> void findBeans(Class<B> beanType, BeanFinderListener<B> listener);

    <B> AtomicReference<B> beanReference(Class<B> beanType, B defaultValue);

    <B> List<B> beanList(Class<B> beanType);
}
