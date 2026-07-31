package org.example.springcorelab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = SpringCoreLabConfig.class)
public class SpringCoreLabConfig {

    @Bean
    MessageGateway messageGateway() {
        return orderCode -> "Sent confirmation for " + orderCode;
    }
}
