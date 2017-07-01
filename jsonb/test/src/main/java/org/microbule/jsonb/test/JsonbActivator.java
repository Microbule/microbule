package org.microbule.jsonb.test;

import javax.json.bind.JsonbBuilder;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonbActivator implements BundleActivator {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonbActivator.class);

//----------------------------------------------------------------------------------------------------------------------
// BundleActivator Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public void start(BundleContext context) throws Exception {
        LOGGER.info(JsonbBuilder.create().toJson(new Person("Apache", "Johnzon")));
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    public static class Person {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

        private final String firstName;
        private final String lastName;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

        public Person(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }
    }
}
