package com.joeyvmason.articlemanager.core.application;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@PropertySource("classpath:/conf/general.properties")
@EnableMongoRepositories(basePackages = "com.joeyvmason.articlemanager.core")
@EnableMongoAuditing
@Configuration
public class MongoDBConfig extends AbstractMongoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MongoDBConfig.class);

    @Value("${mongodb.uri}")
    private String uri;

    @Value("${mongodb.databaseName}")
    private String databaseName;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        return new MongoClient(mongoClientURI);
    }

    @Bean
    @DependsOn("mongo")
    public InitializeIndexes initializeIndexes() {
        logger.info("Initializing indexes");
        return new InitializeIndexes();
    }
}
