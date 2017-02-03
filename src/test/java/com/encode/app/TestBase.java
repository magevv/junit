package com.encode.app;

import org.junit.*;

/**
 * Global fixture for test classes.
 */
public class TestBase {

    @BeforeClass
    public static void systemStart() {
        System.out.println("----- System started -----");
    }

    @AfterClass
    public static void systemStop() {
        System.out.println("----- System stopped -----");
    }

}
