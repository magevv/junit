package com.encode.app;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;

@SuiteClasses({
        CreateFileTest.class
})
@RunWith(Categories.class)
@IncludeCategory({
        TestCategories.Positive.class,
        TestCategories.Negative.class
})
public class TestSuite {

}
