package com.encode.app;


import com.tngtech.java.junit.dataprovider.DataProvider;
import org.junit.runners.model.FrameworkMethod;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyDataPovider {


    @DataProvider
    public static List<String> fileNamesDataLoader(FrameworkMethod testMethod) throws IOException {

        ExternalFile testDataFile = testMethod.getAnnotation(ExternalFile.class);

        return testDataFile == null ? generateRandomFileName() : readFileNamesFromFile(testDataFile.value());

    }



    public static List<String> generateRandomFileName() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("tempfile" + new Random().nextInt() + ".txt");
        }
        return list;
    }

    public static List<String> readFileNamesFromFile(String testDataFile) throws IOException {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(testDataFile))
        ) {
            String s;
            while ((s = reader.readLine()) != null) {
                list.add(s + ".txt");
            }
            reader.close();
        }
        return list;
    }

}
