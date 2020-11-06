package org.isf.service;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class JSONService {

    final String DICOM_RADIOLOGY_URL = "http://167.172.133.7:8777/patients/";

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

            for (int i = 0; i < arr.length(); i++) {
                diagnosis.addSymptom(arr.get(i).toString());
            }

            diagnosisList.add(diagnosis);

        }

        return diagnosisList;
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public List<RadiologyReport> getRadiologyByAadhaarId(String aadhaarId) {
        List<RadiologyReport> result = new ArrayList<>();

        try {
            JSONObject jsonObject = readJsonFromUrl(DICOM_RADIOLOGY_URL + aadhaarId);
            JSONArray arr = jsonObject.getJSONArray("radiology_studies");
            for (int i = 0; i < arr.length(); i++) {
                RadiologyReport report = new RadiologyReport();
                JSONObject study = arr.getJSONObject(i);
                report.setStudyName(study.getString("studydesc"));
                report.setStudyDate(study.getString("studydate"));
                report.setStudyLink(study.getString("studylink"));
                report.setStudyReport(study.getJSONArray("reportlinks").getJSONObject(0).getString("report"));
                result.add(report);
            }
        } catch (IOException e) {
            return result;
        }

        return result;
    }

    public List<PathologyReport> getPathologyByAadhaarId(String aadhaarId) {
        List<PathologyReport> result = new ArrayList<>();

        try {
            JSONObject jsonObject = readJsonFromUrl(DICOM_RADIOLOGY_URL + aadhaarId);
            JSONArray arr = jsonObject.getJSONArray("pathology_studies");
            for (int i = 0; i < arr.length(); i++) {
                PathologyReport report = new PathologyReport();
                JSONObject study = arr.getJSONObject(i);
                report.setStudyName(study.getString("studydesc"));
                report.setStudyDate(study.getString("studydate"));
                report.setStudyReport(study.getJSONArray("reportlinks").getJSONObject(0).getString("report"));
                result.add(report);
            }
        } catch (IOException e) {
            return result;
        }

        return result;
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

    @Data
    public class RadiologyReport {
        public String studyName;
        public String studyDate;
        public String studyLink;
        public String studyReport;
    }

    @Data
    public class PathologyReport {
        public String studyName;
        public String studyDate;
        public String studyReport;
    }
}
