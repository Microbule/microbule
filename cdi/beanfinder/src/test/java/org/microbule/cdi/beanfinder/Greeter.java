package org.microbule.cdi.beanfinder;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.microbule.beanfinder.api.BeanFinder;

@Singleton
@Named("greeter")
public class Greeter {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final List<HelloBean> list;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    @Inject
    public Greeter(BeanFinder finder) {
        list = finder.beanList(HelloBean.class);
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public List<HelloBean> getList() {
        return list;
    }
}
