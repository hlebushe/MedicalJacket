package org.isf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
        List<String> records = new ArrayList<>();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("diseases.csv").getFile());
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                records.add(scanner.nextLine());
            }
        }
        return records;
    }

}
