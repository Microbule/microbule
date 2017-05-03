package org.microbule.beanfinder.core;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleBeanFinder extends StaticBeanFinder {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Set<Object> beans = new HashSet<>();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    public <B> SimpleBeanFinder addBean(B bean) {
        beans.add(bean);
        return this;
    }

    @Override
    protected <B> Iterable<B> beansOfType(Class<B> beanType) {
        return beans.stream()
                .filter(beanType::isInstance)
                .map(beanType::cast)
                .collect(Collectors.toList());
    }

    public <B> SimpleBeanFinder removeBean(B bean) {
        beans.remove(bean);
        beanRemoved(bean);
        return this;
    }
}
