package org.microbule.requestlog.decorator;

import org.microbule.beanfinder.core.SimpleBeanFinder;
import org.microbule.test.server.hello.HelloTestCase;

public class RequestLogDecoratorTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleBeanFinder finder) {
        finder.addBean(new RequestLogDecorator());
    }
}