package org.isf.service;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class JSONService {

    public List<List<String>>getDiagnosis(String symptoms) throws FileNotFoundException {
        List<List<String>> resultsList = new ArrayList<>();

        String[] array = symptoms.split(",");
        List<String> symptomsList = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            symptomsList.add(array[i]);
        }

        List<Diagnosis> diagnosisList = getDiagnosisList();

        List<Diagnosis> containsDiagnosis = new ArrayList<>();

        for (Diagnosis diagnosis : diagnosisList) {
            for (String symptom : symptomsList) {
                if (diagnosis.getSymptoms().contains(symptom)) {
                    containsDiagnosis.add(diagnosis);
                    break;
                }
            }
        }

        List<ResultsList> resultList = new ArrayList<>();

        for (Diagnosis cont : containsDiagnosis) {
            ResultsList res = new ResultsList();
            Integer numberOfSymptoms = cont.getSymptoms().size();

            List<String> confirmedSymptoms = new ArrayList<>();
            List<String> nonConfirmedSymptoms = new ArrayList<>();

            int contains = 0;

            for (String symptom : cont.getSymptoms()) {
                if (symptomsList.contains(symptom)) {
                    contains++;
                    confirmedSymptoms.add(symptom);
                } else {
                    nonConfirmedSymptoms.add(symptom);
                }
            }

            res.setDiagnosisName(cont.getName());
            res.setPossibility((double)contains/(double)numberOfSymptoms);
            res.setConfirmedSymptoms(confirmedSymptoms);
            res.setNonConfirmedSymptoms(nonConfirmedSymptoms);

            resultList.add(res);

        }

        Collections.sort(resultList, Comparator.comparingDouble(ResultsList::getPossibility).reversed());

        for (ResultsList result : resultList) {
            DecimalFormat df = new DecimalFormat("#.#%");
            String perc = df.format(result.getPossibility());

            List<String> dList = new ArrayList<>();

            dList.add(result.getDiagnosisName());
            dList.add("Possibility: " + perc);
            dList.add(String.join(",", result.getConfirmedSymptoms()));
            dList.add(String.join(",", result.getNonConfirmedSymptoms()));

            resultsList.add(dList);

        }


        return resultsList;
    }

    public List<Diagnosis> getDiagnosisList() throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("diagnosisBySymptoms.json").getFile());

        FileInputStream fis = new FileInputStream(file);
        String jsonTxt = IOUtils.toString(fis);

        JSONObject obj = new JSONObject(jsonTxt);

        List<Diagnosis> diagnosisList = new ArrayList<>();

        Iterator<String> keys = obj.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Diagnosis diagnosis = new Diagnosis();
            diagnosis.setName(key);
            diagnosis.setSymptoms(new ArrayList<>());
            JSONArray arr = obj.getJSONArray(key);

            for (int i = 0; i < arr.length()-1; i++) {
                diagnosis.addSymptom(arr.get(i).toString());
            }

            diagnosisList.add(diagnosis);

        }

        return diagnosisList;
    }

    @Data
    public class Diagnosis {
        public String name;
        public List<String> symptoms;

        public void addSymptom(String symptom) {
            symptoms.add(symptom);
        }
    }

    @Data
    public class ResultsList {
        public String diagnosisName;
        public double possibility;
        public List<String> confirmedSymptoms;
        public List<String> nonConfirmedSymptoms;
    }
}
