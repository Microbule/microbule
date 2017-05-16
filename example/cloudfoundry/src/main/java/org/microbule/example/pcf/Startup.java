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

package org.microbule.example.pcf;

import java.util.concurrent.TimeUnit;

import org.microbule.api.JaxrsProxy;
import org.microbule.example.common.HelloResource;
import org.microbule.scheduler.api.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

@Configuration
@ComponentScan(basePackages = "org.microbule")
@PropertySource("classpath:application.properties")
public class Startup {

    private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);

    @Autowired
    private JaxrsProxy<HelloResource> helloResource;

    @Autowired
    private SchedulerService schedulerService;

    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);
    }


    @EventListener
    @Order()
    public void applicationStarted(ContextRefreshedEvent event) {
        LOGGER.info("The application has started!");

        schedulerService.schedule(() -> LOGGER.info(helloResource.get().sayHello("Microbule").getGreeting()), 10, TimeUnit.SECONDS);
    }
}
