/*
 * Copyright (c) 2017 The Microbule Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.microbule.itest.karaf;

import java.io.File;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.microbule.api.JaxrsProxyFactory;
import org.microbule.api.JaxrsServerFactory;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.vmOption;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class KarafFeaturesIT extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String DEFAULT_KARAF_VERSION = "4.1.1";

    @Inject
    private JaxrsProxyFactory proxyFactory;

    @Inject
    private JaxrsServerFactory serverFactory;

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Configuration
    public Option[] configure() {
        final String jvmOptions = StringUtils.defaultIfBlank(System.getProperty("jvm.options"), "-Dnoop");
        final String karafVersion = System.getProperty("karaf.version", DEFAULT_KARAF_VERSION);
        final String projectVersion = System.getProperty("project.version");
        return options(
                karafDistributionConfiguration()
                        .frameworkUrl(maven("org.apache.karaf", "apache-karaf", karafVersion).type("tar.gz"))
                        .unpackDirectory(new File("target/karaf")),
                vmOption(jvmOptions),
                configureConsole()
                        .startRemoteShell()
                        .ignoreLocalConsole(),

                features(maven("org.microbule", "microbule-features", projectVersion).type("xml").classifier("features"), "microbule"),
                junitBundles(),
                keepRuntimeFolder(),
                logLevel(LogLevelOption.LogLevel.WARN));
    }

    @Test
    public void testFeatureInstalls() throws Exception {
        assertNotNull(proxyFactory);
        assertNotNull(serverFactory);
    }
}
