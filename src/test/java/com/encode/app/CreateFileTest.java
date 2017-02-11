package com.encode.app;

import com.tngtech.java.junit.dataprovider.*;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.*;

/*
Testing Java function File.createNewFile()

Positive tests
  1. New empty file is created in the right directory
  2. Function returns TRUE if file was successfully created
  3. Function returns FALSE if file with such name already exists

Negative tests
  4. Exception if directory is read-only (works on Linux only)
 */

@RunWith(DataProviderRunner.class)
public class CreateFileTest extends TestBase {

    protected File dir;

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Rule
    public ExternalResource methodLevelFixture = new ExternalResource() {
        @Override
        protected void before() throws Throwable {
            dir = tmp.getRoot();
        }

        @Override
        protected void after() {
            if (dir != null) {
                dir.canWrite();
                dir.delete();
            }
        }
    };


    private static int attempt = 1;

    @Test
    @Unstable(5)
    public void unstableTest() {
        if (attempt == 5) {
            attempt = 1;
        } else {
            Assert.fail("Failed after " + (attempt++) + " attempts");
        }
    }

    @Category(TestCategories.Positive.class)
    @Test
    @UseDataProvider(value = "fileNamesDataLoader", location = MyDataPovider.class)
    @ExternalFile("src/test/resources/filenames.csv")
    public void test1(String fileName) throws IOException {
        File f = new File(dir + "/" + fileName);
        f.createNewFile();
        collector.checkThat(dir.list().length, CoreMatchers.not(0));
        collector.checkThat(f.getName(), CoreMatchers.equalTo(dir.list()[0]));
    }

    @Category(TestCategories.Positive.class)
    @Test
    @UseDataProvider(value = "fileNamesDataLoader", location = MyDataPovider.class)
    @ExternalFile("src/test/resources/filenames.csv")
    public void test2(String fileName) throws IOException {
        File f = new File(dir + "/" + fileName);
        Assert.assertTrue("Function returns 'false', expected 'true'", f.createNewFile());
    }

    @Category(TestCategories.Positive.class)
    @Test
    @UseDataProvider(value = "fileNamesDataLoader", location = MyDataPovider.class)
    public void test3(String fileName) throws IOException {
        File f = new File(dir + "/" + fileName);
        f.createNewFile(); // file exists
        Assert.assertFalse("Function returns 'true', expected 'false'", f.createNewFile());
    }

    @Category(TestCategories.Negative.class)
    @Test
    @UseDataProvider(value = "fileNamesDataLoader", location = MyDataPovider.class)
    public void test4(String fileName) throws IOException {
        File f = new File(dir + "/" + fileName);
        dir.setReadOnly(); // Linux only
        f.createNewFile();

        // try/catch here is only for testing purposes
        try {
            //throw new IOException("Permission denied");
        } finally  {
            thrown.expect(IOException.class);
            thrown.expectMessage("Permission denied");
        }
    }
}