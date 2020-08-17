package org.isf.service;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Service
public class CSVService {

    public List<String> getSymptomsList() throws IOException {
        List<String> records = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("symptoms.csv").getFile());
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                records.add(scanner.nextLine());
            }
        }
        return records;
    }

    public List<String> getDiseasesList() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("diseases.csv").getFile());
        List<String> records = new ArrayList<>();

        CSVReader reader = new CSVReader(new FileReader(file), ',', '"', 1);

        List<String[]> allRows = reader.readAll();

        for(String[] row : allRows){
            System.out.println(Arrays.toString(row));
            records.add(row[0]);
        }

        return records;
    }

}
