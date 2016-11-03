package org.microbule.decorator.requestlog;

import org.microbule.core.JaxrsServerFactoryImpl;
import org.microbule.test.hello.HelloTestCase;

public class RequestLogDecoratorTest extends HelloTestCase {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected void addDecorators(JaxrsServerFactoryImpl factory) {
        factory.addDecorator("requestlog", new RequestLogDecorator());
    }
}