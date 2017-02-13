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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @DataProvider
    public static List<String> generateRandomFileName() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("tempfile" + new Random().nextInt() + ".txt");
        }
        return list;
    }

    @DataProvider
    public static List<String> readFileNamesFromFile() throws IOException {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader("src/test/resources/filenames.csv"))
        ) {
            String s;
            while ((s = reader.readLine()) != null) {
                list.add(s + ".txt");
            }
            reader.close();
        }
        return list;
    }

    private static int attempt = 1;

    @Test
    @Unstable(1)
    public void unstableTest() {
        if (attempt == 2) {
            attempt = 1;
        } else {
            Assert.fail("Failed after " + (attempt++) + " attempts");
        }
    }

    @Category(TestCategories.Positive.class)
    @Test
    @UseDataProvider("generateRandomFileName")
    public void test1(String fileName) throws IOException {
        File f = new File(dir + "/" + fileName);
        f.createNewFile();
        collector.checkThat(dir.list().length, CoreMatchers.not(0));
        collector.checkThat(f.getName(), CoreMatchers.equalTo(dir.list()[0]));
    }

    @Category(TestCategories.Positive.class)
    @Test
    @UseDataProvider("generateRandomFileName")
    public void test2(String fileName) throws IOException {
        File f = new File(dir + "/" + fileName);
        Assert.assertTrue("Function returns 'false', expected 'true'", f.createNewFile());
    }

    @Category(TestCategories.Positive.class)
    @Test
    @UseDataProvider("readFileNamesFromFile")
    public void test3(String fileName) throws IOException {
        File f = new File(dir + "/" + fileName);
        f.createNewFile(); // file exists
        Assert.assertFalse("Function returns 'true', expected 'false'", f.createNewFile());
    }

    @Category(TestCategories.Negative.class)
    @Test
    @UseDataProvider("generateRandomFileName")
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