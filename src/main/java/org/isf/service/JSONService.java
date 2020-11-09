package org.isf.service;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.isf.service.model.PathologyReportResponse;
import org.isf.service.model.PatientInfoResponse;
import org.isf.service.model.RadiologyReportResponse;
import org.isf.service.response.Diagnosis;
import org.isf.service.response.PathologyReport;
import org.isf.service.response.RadiologyReport;
import org.isf.service.response.ResultsList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JSONService {

    private final DicomClient dicomClient;

    public List<List<String>> getDiagnosis(String symptoms) throws FileNotFoundException {
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
            res.setPossibility((double) contains / (double) numberOfSymptoms);
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
        return getPatientInfo(aadhaarId).getRadiologyStudies()
                .stream()
                .map(RadiologyReportResponse::toRadiologyReport)
                .collect(Collectors.toList());
    }

    public List<PathologyReport> getPathologyByAadhaarId(String aadhaarId) {
        return getPatientInfo(aadhaarId).getPathologyStudies()
                .stream()
                .map(PathologyReportResponse::toPathologyReport)
                .collect(Collectors.toList());
    }

    public PatientInfoResponse getPatientInfo(String aadhaarId) {
        final String requestId = Optional.ofNullable(aadhaarId).orElse("");
        try {
            return Optional.ofNullable(dicomClient.getPatient(requestId).execute().body())
                    .orElse(new PatientInfoResponse());
        } catch (IOException e) {
            log.error("Dicom request failed with excepton", e);
            return new PatientInfoResponse();
        }
    }
}
