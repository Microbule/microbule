package org.microbule.requestlog.decorator;

import org.microbule.container.core.SimpleContainer;
import org.microbule.test.server.hello.HelloTestCase;

public class RequestLogDecoratorTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addBeans(SimpleContainer container) {
        container.addBean(new RequestLogDecorator());
    }
}