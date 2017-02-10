package com.encode.app;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;

public class TestBase {

    @ClassRule
    public static ExternalResource classLevelFixture = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            System.out.println("----- System started -----");
        }

        @Override
        protected void after() {
            System.out.println("----- System stopped -----");
        }
    };

    @Rule
    public RestartFailedRule restartFailed = new RestartFailedRule();

}
