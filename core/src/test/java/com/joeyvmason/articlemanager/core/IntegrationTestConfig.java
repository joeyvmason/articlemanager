package com.joeyvmason.articlemanager.core;

import com.github.fakemongo.Fongo;
import com.joeyvmason.articlemanager.core.application.CoreConfig;
import com.mongodb.Mongo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@Import({CoreConfig.class})
public class IntegrationTestConfig {

    @Value("${mongodb.databaseName}")
    private String databaseName;

    @Bean
    @Primary
    public Mongo mongo() {
        return new Fongo(databaseName).getMongo();
    }


}
