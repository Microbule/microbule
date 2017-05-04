package org.microbule.spring.config;

import java.util.concurrent.TimeUnit;

import com.google.common.collect.Lists;
import org.microbule.beanfinder.api.BeanFinder;
import org.microbule.config.api.ConfigService;
import org.microbule.config.core.DefaultConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrobuleConfiguration {

    @Bean
    public ConfigService configService(@Autowired BeanFinder finder) {
        return new DefaultConfigService(finder, Lists.newArrayList(), Lists.newArrayList(SpringEnvironmentConfigProvider.NAME), 10, TimeUnit.SECONDS);
    }
}
