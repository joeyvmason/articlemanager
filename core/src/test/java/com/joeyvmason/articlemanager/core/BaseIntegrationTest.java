package com.joeyvmason.articlemanager.core;

import com.joeyvmason.articlemanager.core.domain.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;

import java.util.List;

@ContextConfiguration(classes = {IntegrationTestConfig.class})
public class BaseIntegrationTest extends AbstractTestNGSpringContextTests  {

    @Value("${mongodb.databaseName}")
    private String databaseName;

//    @Autowired
//    private List<StatefulStub> statefulStubs;

    @Autowired
    private List<BaseRepository> baseRepositories;

    @AfterMethod
    public void tearDown() throws Exception {
        baseRepositories.forEach(BaseRepository::deleteAll);

//        logger.info("Resetting stubs");
//        statefulStubs.forEach(StatefulStub::reset);
    }
}
