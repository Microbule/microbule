package org.microbule.spring.beanfinder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Bean
    public HelloService enHello() {
        return new HelloServiceImpl("en", "Hello, %s!");
    }

    @Bean
    public HelloService esHello() {
        return new HelloServiceImpl("es", "Hola, %s!");
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private static final class HelloServiceImpl implements HelloService {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final String lang;
        private final String pattern;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

        public HelloServiceImpl(String lang, String pattern) {
            this.lang = lang;
            this.pattern = pattern;
        }

//----------------------------------------------------------------------------------------------------------------------
// HelloService Implementation
//----------------------------------------------------------------------------------------------------------------------


        @Override
        public String lang() {
            return lang;
        }

        @Override
        public String sayHello(String name) {
            return String.format(pattern, name);
        }
    }
}
