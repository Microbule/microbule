package org.microbule.requestlog.decorator;

import org.microbule.core.DefaultJaxrsServerFactory;
import org.microbule.test.hello.HelloTestCase;

public class RequestLogDecoratorTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addDecorators(DefaultJaxrsServerFactory factory) {
        factory.addDecorator("requestlog", new RequestLogDecorator());
    }
}