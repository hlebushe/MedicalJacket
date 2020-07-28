package org.isf.service;

import org.isf.dao.Examinations;
import org.isf.models.ExaminationsModel;
import org.isf.dao.Patient;
import org.isf.repository.ExaminationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class ExaminationService {

    @Autowired
    private ExaminationsRepository examinationRepository;

    private String level5 = "#9000ff";

    private String level4 = "#ff2e2e";

    private String level3 = "#f7844a";

    private String level2 = "#ebad63";

    private String level1 = "#c4c700";

    private String level0 = "#000000";

//    public Examinations getExaminationByPatient(Patient patient) {
//        return examinationRepository.getAllByPatient(patient).get(0);
//    }

    public Examinations getLastExaminationByPatient(Patient patient) {
        return examinationRepository.getByPatientAndOrderByDate(patient).get(0);
    }

    public List<Examinations> getExaminations(Patient patient) {
        return examinationRepository.getByPatientAndOrderByDate(patient);
    }

    public ExaminationsModel setExaminationColors(ExaminationsModel examination, int age) throws IOException, ParseException {

        Properties prop = null;

        int score = 0;

        if (age > 18) {
            prop = readPropertiesFile("examination_adult.properties");
        } else if (age > 11 && age < 19) {
            prop = readPropertiesFile("examination_12_18.properties");
        } else if (age > 4 && age < 12) {
            prop = readPropertiesFile("examination_5_11.properties");
        } else if (age >= 1 && age < 5) {
            prop = readPropertiesFile("examination_1_4.properties");
        } else {
            prop = readPropertiesFile("examination_new_born.properties");
        }

            if (examination.getRespiratoryRate() == null) {
                examination.setRespiratoryRateColor(level0);
            } else if (examination.getRespiratoryRate() <= Integer.parseInt(prop.getProperty("respiratoryRateLevel5Max")) &&
                    examination.getRespiratoryRate() >= Integer.parseInt(prop.getProperty("respiratoryRateLevel5Min"))) {
                examination.setRespiratoryRateColor(level5);
                score = score + 5;
            } else if (examination.getRespiratoryRate() <= Integer.parseInt(prop.getProperty("respiratoryRateLevel5Max2")) &&
                    examination.getRespiratoryRate() >= Integer.parseInt(prop.getProperty("respiratoryRateLevel5Min2"))) {
                examination.setRespiratoryRateColor(level5);
                score = score + 5;
            } else if (examination.getRespiratoryRate() <= Integer.parseInt(prop.getProperty("respiratoryRateLevel4Max")) &&
                    examination.getRespiratoryRate() >= Integer.parseInt(prop.getProperty("respiratoryRateLevel4Min"))) {
                examination.setRespiratoryRateColor(level4);
                score = score + 4;
            } else if (examination.getRespiratoryRate() <= Integer.parseInt(prop.getProperty("respiratoryRateLevel3Max")) &&
                    examination.getRespiratoryRate() >= Integer.parseInt(prop.getProperty("respiratoryRateLevel3Min"))) {
                examination.setRespiratoryRateColor(level3);
                score = score + 3;
            } else if (examination.getRespiratoryRate() <= Integer.parseInt(prop.getProperty("respiratoryRateLevel3Max2")) &&
                    examination.getRespiratoryRate() >= Integer.parseInt(prop.getProperty("respiratoryRateLevel3Min2"))) {
                examination.setRespiratoryRateColor(level3);
                score = score + 3;
            } else if (examination.getRespiratoryRate() <= Integer.parseInt(prop.getProperty("respiratoryRateLevel2Max2")) &&
                    examination.getRespiratoryRate() >= Integer.parseInt(prop.getProperty("respiratoryRateLevel2Min2"))) {
                examination.setRespiratoryRateColor(level2);
                score = score + 2;
            } else if (examination.getRespiratoryRate() <= Integer.parseInt(prop.getProperty("respiratoryRateLevel2Max")) &&
                    examination.getRespiratoryRate() >= Integer.parseInt(prop.getProperty("respiratoryRateLevel2Min"))) {
                examination.setRespiratoryRateColor(level2);
                score = score + 2;
            } else if (examination.getRespiratoryRate() <= Integer.parseInt(prop.getProperty("respiratoryRateLevel1Max")) &&
                    examination.getRespiratoryRate() >= Integer.parseInt(prop.getProperty("respiratoryRateLevel1Min"))) {
                examination.setRespiratoryRateColor(level1);
                score = score + 1;
            } else if (examination.getRespiratoryRate() <= Integer.parseInt(prop.getProperty("respiratoryRateLevel1Max2")) &&
                    examination.getRespiratoryRate() >= Integer.parseInt(prop.getProperty("respiratoryRateLevel1Min2"))) {
                examination.setRespiratoryRateColor(level1);
                score = score + 1;
            } else {
                examination.setRespiratoryRateColor(level0);
            }

            if (examination.getO2Saturation() == null) {
                examination.setO2SaturationColor(level0);
            } else if (examination.getO2Saturation() <= Integer.parseInt(prop.getProperty("o2SaturationLevel5Max")) &&
                    examination.getO2Saturation() >= Integer.parseInt(prop.getProperty("o2SaturationLevel5Min"))) {
                examination.setO2SaturationColor(level5);
                score = score + 5;
            } else if (examination.getO2Saturation() <= Integer.parseInt(prop.getProperty("o2SaturationLevel4Max")) &&
                    examination.getO2Saturation() >= Integer.parseInt(prop.getProperty("o2SaturationLevel4Min"))) {
                examination.setO2SaturationColor(level4);
                score = score + 4;
            } else if (examination.getO2Saturation() <= Integer.parseInt(prop.getProperty("o2SaturationLevel3Max")) &&
                    examination.getO2Saturation() >= Integer.parseInt(prop.getProperty("o2SaturationLevel3Min"))) {
                examination.setO2SaturationColor(level3);
                score = score + 3;
            } else if (examination.getO2Saturation() <= Integer.parseInt(prop.getProperty("o2SaturationLevel2Max")) &&
                    examination.getO2Saturation() >= Integer.parseInt(prop.getProperty("o2SaturationLevel2Min"))) {
                examination.setO2SaturationColor(level2);
                score = score + 2;
            } else if (examination.getO2Saturation() <= Integer.parseInt(prop.getProperty("o2SaturationLevel1Max")) &&
                    examination.getO2Saturation() >= Integer.parseInt(prop.getProperty("o2SaturationLevel1Min"))) {
                examination.setO2SaturationColor(level1);
                score = score + 1;
            } else {
                examination.setO2SaturationColor(level0);
            }


            if (examination.getO2FlowRate() == null) {
                examination.setO2FlowRateColor(level0);
            } else if (examination.getO2FlowRate() >= Integer.parseInt(prop.getProperty("o2FlowRateLevel3Min"))) {
                examination.setO2FlowRateColor(level3);
                score = score + 3;
            } else if (examination.getO2FlowRate() <= Integer.parseInt(prop.getProperty("o2FlowRateLevel2Max")) &&
                    examination.getO2FlowRate() >= Integer.parseInt(prop.getProperty("o2FlowRateLevel2Min"))) {
                examination.setO2FlowRateColor(level2);
                score = score + 2;
            } else if (examination.getO2FlowRate() <= Integer.parseInt(prop.getProperty("o2FlowRateLevel1Max")) &&
                    examination.getO2FlowRate() >= Integer.parseInt(prop.getProperty("o2FlowRateLevel1Min"))) {
                examination.setO2FlowRateColor(level1);
                score = score + 1;
            } else {
                examination.setO2FlowRateColor(level0);
            }

            if (examination.getBloodPressureMin() == null) {
                examination.setBloodPressureMinColor(level0);
            } else if (examination.getBloodPressureMin() <= Integer.parseInt(prop.getProperty("bloodPressureMinLevel5Max")) &&
                    examination.getBloodPressureMin() >= Integer.parseInt(prop.getProperty("bloodPressureMinLevel5Min"))) {
                examination.setBloodPressureMinColor(level5);
                score = score + 5;
            } else if (examination.getBloodPressureMin() <= Integer.parseInt(prop.getProperty("bloodPressureMinLevel3Max")) &&
                    examination.getBloodPressureMin() >= Integer.parseInt(prop.getProperty("bloodPressureMinLevel3Min"))) {
                examination.setBloodPressureMinColor(level3);
                score = score + 3;
            } else if (examination.getBloodPressureMin() <= Integer.parseInt(prop.getProperty("bloodPressureMinLevel2Max")) &&
                    examination.getBloodPressureMin() >= Integer.parseInt(prop.getProperty("bloodPressureMinLevel2Min"))) {
                examination.setBloodPressureMinColor(level2);
                score = score + 2;
            } else if (examination.getBloodPressureMin() <= Integer.parseInt(prop.getProperty("bloodPressureMinLevel1Max")) &&
                    examination.getBloodPressureMin() >= Integer.parseInt(prop.getProperty("bloodPressureMinLevel1Min"))) {
                examination.setBloodPressureMinColor(level1);
                score = score + 1;
            } else {
                examination.setBloodPressureMinColor(level0);
            }

            if (examination.getBloodPressureMax() == null) {
                examination.setBloodPressureMaxColor(level0);
            } else if (examination.getBloodPressureMax() <= Integer.parseInt(prop.getProperty("bloodPressureMaxLevel5Max")) &&
                    examination.getBloodPressureMax() >= Integer.parseInt(prop.getProperty("bloodPressureMaxLevel5Min"))) {
                examination.setBloodPressureMaxColor(level5);
                score = score + 5;
            } else if (examination.getBloodPressureMax() <= Integer.parseInt(prop.getProperty("bloodPressureMaxLevel3Max")) &&
                    examination.getBloodPressureMax() >= Integer.parseInt(prop.getProperty("bloodPressureMaxLevel3Min"))) {
                examination.setBloodPressureMaxColor(level3);
                score = score + 3;
            } else if (examination.getBloodPressureMax() <= Integer.parseInt(prop.getProperty("bloodPressureMaxLevel2Max")) &&
                    examination.getBloodPressureMax() >= Integer.parseInt(prop.getProperty("bloodPressureMaxLevel2Min"))) {
                examination.setBloodPressureMaxColor(level2);
                score = score + 2;
            } else if (examination.getBloodPressureMax() <= Integer.parseInt(prop.getProperty("bloodPressureMaxLevel1Max")) &&
                    examination.getBloodPressureMax() >= Integer.parseInt(prop.getProperty("bloodPressureMaxLevel1Min"))) {
                examination.setBloodPressureMaxColor(level1);
                score = score + 1;
            } else {
                examination.setBloodPressureMaxColor(level0);
            }

            if (examination.getHeartRate() == null) {
                examination.setHeartRateColor(level0);
            } else if (examination.getHeartRate() <= Integer.parseInt(prop.getProperty("heartRateLevel5Max")) &&
                    examination.getHeartRate() >= Integer.parseInt(prop.getProperty("heartRateLevel5Min"))) {
                examination.setHeartRateColor(level5);
                score = score + 5;
            } else if (examination.getHeartRate() <= Integer.parseInt(prop.getProperty("heartRateLevel5Max2")) &&
                    examination.getHeartRate() >= Integer.parseInt(prop.getProperty("heartRateLevel5Min2"))) {
                examination.setHeartRateColor(level5);
                score = score + 5;
            } else if (examination.getHeartRate() <= Integer.parseInt(prop.getProperty("heartRateLevel4Max")) &&
                    examination.getHeartRate() >= Integer.parseInt(prop.getProperty("heartRateLevel4Min"))) {
                examination.setHeartRateColor(level4);
                score = score + 4;
            } else if (examination.getHeartRate() <= Integer.parseInt(prop.getProperty("heartRateLevel3Max")) &&
                    examination.getHeartRate() >= Integer.parseInt(prop.getProperty("heartRateLevel3Min"))) {
                examination.setHeartRateColor(level3);
                score = score + 3;
            } else if (examination.getHeartRate() <= Integer.parseInt(prop.getProperty("heartRateLevel3Max2")) &&
                    examination.getHeartRate() >= Integer.parseInt(prop.getProperty("heartRateLevel3Min2"))) {
                examination.setHeartRateColor(level3);
                score = score + 3;
            } else if (examination.getHeartRate() <= Integer.parseInt(prop.getProperty("heartRateLevel2Max")) &&
                    examination.getHeartRate() >= Integer.parseInt(prop.getProperty("heartRateLevel2Min"))) {
                examination.setHeartRateColor(level2);
                score = score + 2;
            } else if (examination.getHeartRate() <= Integer.parseInt(prop.getProperty("heartRateLevel2Max2")) &&
                    examination.getHeartRate() >= Integer.parseInt(prop.getProperty("heartRateLevel2Min2"))) {
                examination.setHeartRateColor(level2);
                score = score + 2;
            } else if (examination.getHeartRate() <= Integer.parseInt(prop.getProperty("heartRateLevel1Max")) &&
                    examination.getHeartRate() >= Integer.parseInt(prop.getProperty("heartRateLevel1Min"))) {
                examination.setHeartRateColor(level1);
                score = score + 1;
            } else if (examination.getHeartRate() <= Integer.parseInt(prop.getProperty("heartRateLevel1Max2")) &&
                    examination.getHeartRate() >= Integer.parseInt(prop.getProperty("heartRateLevel1Min2"))) {
                examination.setHeartRateColor(level1);
                score = score + 1;
            } else {
                examination.setHeartRateColor(level0);
            }

            if (examination.getTemperature() == null) {
                examination.setTemperatureColor(level0);
            } else if (examination.getTemperature() <= Double.parseDouble(prop.getProperty("temperatureLevel3Max")) &&
                    examination.getTemperature() >= Double.parseDouble(prop.getProperty("temperatureLevel3Min"))) {
                examination.setTemperatureColor(level3);
                score = score + 3;
            } else if (examination.getTemperature() <= Double.parseDouble(prop.getProperty("temperatureLevel3Max2")) &&
                    examination.getTemperature() >= Double.parseDouble(prop.getProperty("temperatureLevel3Min2"))) {
                examination.setTemperatureColor(level3);
                score = score + 3;
            } else if (examination.getTemperature() <= Double.parseDouble(prop.getProperty("temperatureLevel2Max")) &&
                    examination.getTemperature() >= Double.parseDouble(prop.getProperty("temperatureLevel2Min"))) {
                examination.setTemperatureColor(level2);
                score = score + 2;
            } else if (examination.getTemperature() <= Double.parseDouble(prop.getProperty("temperatureLevel2Max2")) &&
                    examination.getTemperature() >= Double.parseDouble(prop.getProperty("temperatureLevel2Min2"))) {
                examination.setTemperatureColor(level2);
                score = score + 2;
            } else if (examination.getTemperature() <= Double.parseDouble(prop.getProperty("temperatureLevel1Max")) &&
                    examination.getTemperature() >= Double.parseDouble(prop.getProperty("temperatureLevel1Min"))) {
                examination.setTemperatureColor(level1);
                score = score + 1;
            } else {
                examination.setTemperatureColor(level0);
            }

            if (examination.getBloodGlucoseLevel() == null) {
                examination.setBloodGlucoseLevelColor(level0);
            } else if (examination.getBloodGlucoseLevel() <= Double.parseDouble(prop.getProperty("bloodGlucoseLevel5Max2")) ||
                    examination.getBloodGlucoseLevel() >= Double.parseDouble(prop.getProperty("bloodGlucoseLevel5Min"))) {
                examination.setBloodGlucoseLevelColor(level5);
                score = score + 5;
            } else if (examination.getBloodGlucoseLevel() <= Double.parseDouble(prop.getProperty("bloodGlucoseLevel3Max2")) &&
                    examination.getBloodGlucoseLevel() >= Double.parseDouble(prop.getProperty("bloodGlucoseLevel3Min2"))) {
                examination.setBloodGlucoseLevelColor(level3);
                score = score + 3;
            } else if (examination.getBloodGlucoseLevel() == Double.parseDouble(prop.getProperty("bloodGlucoseLevel3"))) {
                examination.setBloodGlucoseLevelColor(level3);
                score = score + 3;
            } else if (examination.getBloodGlucoseLevel() == Double.parseDouble(prop.getProperty("bloodGlucoseLevel2"))) {
                examination.setBloodGlucoseLevelColor(level2);
                score = score + 2;
            } else if (examination.getBloodGlucoseLevel() <= Double.parseDouble(prop.getProperty("bloodGlucoseLevel1Max")) &&
                    examination.getBloodGlucoseLevel() >= Double.parseDouble(prop.getProperty("bloodGlucoseLevel1Min"))) {
                examination.setBloodGlucoseLevelColor(level1);
                score = score + 1;
            } else {
                examination.setBloodGlucoseLevelColor(level0);
            }

            if (examination.getSmell() == null) {
                examination.setSmellColor(level0);
            } else if (examination.getSmell() == "normal") {
                examination.setSmellColor(level0);
            } else if (examination.getSmell() == "week") {
                examination.setSmellColor(level1);
                score = score + 1;
            } else if (examination.getSmell() == "no sense") {
                examination.setSmellColor(level2);
                score = score + 2;
            } else {
                examination.setSmellColor(level0);
            }

            if (examination.getTaste() == null) {
                examination.setTasteColor(level0);
            } else if (examination.getTaste() == "normal") {
                examination.setTasteColor(level0);
            } else if (examination.getTaste() == "week") {
                examination.setTasteColor(level1);
                score = score + 1;
            } else if (examination.getTaste() == "no sense") {
                examination.setTasteColor(level2);
                score = score + 2;
            } else {
                examination.setTasteColor(level0);
            }

            if (examination.getConsciousness() == null) {
                examination.setConsciousnessColor(level0);
            } else if (examination.getConsciousness() == "alert") {
                examination.setConsciousnessColor(level0);
            } else if (examination.getConsciousness() == "voice") {
                examination.setConsciousnessColor(level1);
                score = score + 1;
            } else if (examination.getConsciousness() == "pain") {
                examination.setConsciousnessColor(level2);
                score = score + 2;
            } else if (examination.getConsciousness() == "unresp") {
                examination.setConsciousnessColor(level3);
                score = score + 2;
            } else {
                examination.setConsciousnessColor(level0);
            }

            examination.setBodyMassIndexColor(level0);

            examination.setScore(score);

        return examination;
    }

    public Properties readPropertiesFile(String fileName) throws IOException {
        Properties prop = null;
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        try {
            prop = new Properties();
            prop.load(new FileReader(file));
        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

        return prop;
    }

    public Examinations saveExaminaions(Examinations examinations) {
        return examinationRepository.save(examinations);
    }

}
