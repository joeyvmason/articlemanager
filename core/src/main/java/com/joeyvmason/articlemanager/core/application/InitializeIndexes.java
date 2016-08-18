package com.joeyvmason.articlemanager.core.application;

import com.joeyvmason.articlemanager.core.domain.articles.Article;
import com.joeyvmason.articlemanager.core.domain.auth.AuthToken;
import com.joeyvmason.articlemanager.core.domain.users.User;
import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

public class InitializeIndexes {
    private static final Logger logger = LoggerFactory.getLogger(InitializeIndexes.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public InitializeIndexes(){}

    @PostConstruct
    public void initializeIndexes() {
        logger.info("Initializing MongoDB indexes");

        //User
        ensureIndex(User.class, new Index().on("emailAddress", Sort.DEFAULT_DIRECTION).unique().background());

        //Articles
        ensureIndex(Article.class, new Index().on("owner", Sort.DEFAULT_DIRECTION).background());

        //AuthToken
        ensureIndex(AuthToken.class, new Index().on("accessToken", Sort.DEFAULT_DIRECTION).unique().background());
        ensureIndex(AuthToken.class, new Index().on("created", Sort.DEFAULT_DIRECTION).expire(7, TimeUnit.DAYS).background());
    }

    private static class KeyBuilder {
        private BasicDBObject keys = new BasicDBObject();

        public static KeyBuilder keys() {
            return new KeyBuilder();
        }

        public KeyBuilder withKey(String key, int value) {
            keys.put(key, value);
            return this;
        }

        public BasicDBObject build() {
            return keys;
        }
    }

    private void ensureIndex(Class clazz, Index index) {
        try {
            mongoTemplate.indexOps(clazz).ensureIndex(index);
        } catch (Exception e) {
            logger.error("Unable to ensure index on Class({})", clazz, e);
        }
    }

    private void dropIndex(Class<?> entityClass, String indexName) {
        try {
            mongoTemplate.indexOps(entityClass).dropIndex(indexName);
        } catch (Exception e) {
            logger.warn("Unable to drop Index({}) on Class({})", indexName, entityClass);
        }
    }
}
