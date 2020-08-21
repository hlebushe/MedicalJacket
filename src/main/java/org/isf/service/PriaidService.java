package org.isf.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.isf.priaid.Diagnosis.Client.DiagnosisClient;
import org.isf.priaid.Diagnosis.Model.Gender;
import org.isf.priaid.Diagnosis.Model.HealthDiagnosis;
import org.isf.priaid.Diagnosis.Model.HealthItem;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Properties;

public class PriaidService {

    private static DiagnosisClient diagnosisClient;

    public void setClient() {
        String authUrl = "";
        String userName = "";
        String password = "";
        String language = "";
        String healthUrl = "";

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("config.properties").getFile());

        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            prop.load(input);

            userName = prop.getProperty("username");
            password = prop.getProperty("password");
            authUrl = prop.getProperty("priaid_authservice_url");
            healthUrl = prop.getProperty("priaid_healthservice_url");
            language = prop.getProperty("language");

        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        CheckRequiredArgs(userName, password, authUrl, healthUrl, language);

        try {
            diagnosisClient = new DiagnosisClient(userName, password, authUrl, language, healthUrl);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void CheckRequiredArgs(String userName, String password, String authUrl, String healthUrl, String language) {
        if ( userName == null || userName.isEmpty())
            throw new IllegalArgumentException("username in config.properties is required");

        if ( password == null || password.isEmpty())
            throw new IllegalArgumentException("password in config.properties is required");

        if (authUrl == null || authUrl.isEmpty())
            throw new IllegalArgumentException("priaid_authservice_url in config.properties is required");

        if (healthUrl == null || healthUrl.isEmpty())
            throw new IllegalArgumentException("priaid_healthservice_url in config.properties is required");

        if (language == null || language.isEmpty())
            throw new IllegalArgumentException("language in config.properties is required");
    }

    public List<HealthItem> getAllSymptoms() throws Exception {
        return diagnosisClient.loadSymptoms();
    }

    public List<HealthDiagnosis> getDiagnosisBySymptoms(List<Integer> selectedSymptoms, Gender gender, int yearOfBirth) throws Exception
    {
        return diagnosisClient.loadDiagnosis(selectedSymptoms, gender, yearOfBirth);
    }

}
