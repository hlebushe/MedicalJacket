package org.isf.models;

import org.isf.dao.Examinations;
import org.isf.dao.NursingStationData;
import org.isf.dao.Visit;
import org.isf.util.DateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ExaminationsModel {

    private String patientName;

    private String patientAge;

    private Character patientSex;

    private String patientAddress;

    private String patientAadhaarId;

    private UUID id;

    private Integer respiratoryRate;

    private Integer o2Saturation;

    private Integer o2FlowRate;

    private Integer bloodPressureMin;

    private Integer bloodPressureMax;

    private Integer heartRate;

    private Double temperature;

    private Double bloodGlucoseLevel;

    private Double bodyMassIndex;

    private String respiratoryRateColor;

    private String o2SaturationColor;

    private String o2FlowRateColor;

    private String bloodPressureMinColor;

    private String bloodPressureMaxColor;

    private String heartRateColor;

    private String temperatureColor;

    private String bloodGlucoseLevelColor;

    private String bodyMassIndexColor;

    private String smell;

    private String smellColor;

    private String taste;

    private String tasteColor;

    private String consciousness;

    private String consciousnessColor;

    private String date;

    private Integer score;

    private String scoreColor;

    private Boolean dripFlow;

    private List<String> external;

    private List<String> tasksFuture;

    private List<String> tasksPast;

    final double METER = 0.01;

    public ExaminationsModel(Examinations examination) {
        this.id = examination.getId();
        this.respiratoryRate = examination.getRespiratoryRate();
        this.o2Saturation = examination.getO2Saturation();
        this.o2FlowRate = examination.getO2FlowRate();
        this.bloodPressureMin = examination.getBloodPressureMin();
        this.bloodPressureMax = examination.getBloodPressureMax();
        this.heartRate = examination.getHeartRate();
        this.temperature = examination.getTemperature();
        this.bloodGlucoseLevel = examination.getBloodGlucoseLevel();
        this.smell = examination.getSmell();
        this.taste = examination.getTaste();
        this.consciousness = examination.getConsciousness();
        String formatted = DateUtil.formatDate(examination.getDate());
        this.date = formatted.substring(0, examination.getDate().toString().length() - 10);
        if (examination.getHeight() == null) {
            this.bodyMassIndex = null;
        } else {
            Double heightInMeters = examination.getHeight()*METER;
            if (examination.getHeight() == 0) {
                this.bodyMassIndex = 0.0;
            } else {
                this.bodyMassIndex = round(examination.getWeight() / (heightInMeters * heightInMeters), 2);
            }
        }

    }

    public ExaminationsModel(NursingStationData nursingStationData) {
        this.id = nursingStationData.getId();
        this.o2Saturation = nursingStationData.getOxygenSaturation();
        this.bloodPressureMin = nursingStationData.getBloodPressureDia();
        this.bloodPressureMax = nursingStationData.getBloodPressureSys();
        this.heartRate = nursingStationData.getHeartRate();
        this.temperature = nursingStationData.getTemperature();
        this.bloodGlucoseLevel = nursingStationData.getBloodGlucose();
        this.o2FlowRate = nursingStationData.getOxygenFlowRate();
        this.heartRate = nursingStationData.getHeartRate();
        this.patientName = nursingStationData.getPatient().getName();
        this.patientAge = String.valueOf(nursingStationData.getPatient().getAge());
        this.patientAddress = nursingStationData.getPatient().getAddress();
        this.patientSex = nursingStationData.getPatient().getSex();
        this.patientAadhaarId = "Aadhaar: " + nursingStationData.getPatient().getAadhaarId();
    }

    public void setVisitData(Visit visit) {
            String externalString = visit.getExaminationsPrescribed();
            String radiologyString = visit.getRadiologyPrescribed();
            List<String> externalsList = new ArrayList<>();
            if (!externalString.isEmpty()) {
                try {
                    String[] splitArrayExternals = externalString.split(",");

                    for (int i = 0; i < splitArrayExternals.length; i++) {
                        externalsList.add(splitArrayExternals[i]);
                    }

                    if (!radiologyString.isEmpty()) {
                        String[] splitArrayRadiology = radiologyString.split(",");

                        for (int i = 0; i < splitArrayRadiology.length; i++) {
                            externalsList.add(splitArrayRadiology[i]);
                        }
                    }
                    this.external = externalsList;

                } catch (Exception e) {
                    externalsList.add("No externals prescribed");
                    this.external = externalsList;
                }
            } else {
                externalsList.add("No externals prescribed");
                this.external = externalsList;
            }

    }

    public ExaminationsModel() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(Integer respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public Integer getO2Saturation() {
        return o2Saturation;
    }

    public void setO2Saturation(Integer o2Saturation) {
        this.o2Saturation = o2Saturation;
    }

    public Integer getO2FlowRate() {
        return o2FlowRate;
    }

    public void setO2FlowRate(Integer o2FlowRate) {
        this.o2FlowRate = o2FlowRate;
    }

    public Integer getBloodPressureMin() {
        return bloodPressureMin;
    }

    public void setBloodPressureMin(Integer bloodPressureMin) {
        this.bloodPressureMin = bloodPressureMin;
    }

    public Integer getBloodPressureMax() {
        return bloodPressureMax;
    }

    public void setBloodPressureMax(Integer bloodPressureMax) {
        this.bloodPressureMax = bloodPressureMax;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getBloodGlucoseLevel() {
        return bloodGlucoseLevel;
    }

    public void setBloodGlucoseLevel(Double bloodGlucoseLevel) {
        this.bloodGlucoseLevel = bloodGlucoseLevel;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getRespiratoryRateColor() {
        return respiratoryRateColor;
    }

    public void setRespiratoryRateColor(String respiratoryRateColor) {
        this.respiratoryRateColor = respiratoryRateColor;
    }

    public String getO2SaturationColor() {
        return o2SaturationColor;
    }

    public void setO2SaturationColor(String o2SaturationColor) {
        this.o2SaturationColor = o2SaturationColor;
    }

    public String getO2FlowRateColor() {
        return o2FlowRateColor;
    }

    public void setO2FlowRateColor(String o2FlowRateColor) {
        this.o2FlowRateColor = o2FlowRateColor;
    }

    public String getBloodPressureMinColor() {
        return bloodPressureMinColor;
    }

    public void setBloodPressureMinColor(String bloodPressureMinColor) {
        this.bloodPressureMinColor = bloodPressureMinColor;
    }

    public String getBloodPressureMaxColor() {
        return bloodPressureMaxColor;
    }

    public void setBloodPressureMaxColor(String bloodPressureMaxColor) {
        this.bloodPressureMaxColor = bloodPressureMaxColor;
    }

    public Double getBodyMassIndex() {
        return bodyMassIndex;
    }

    public void setBodyMassIndex(Double bodyMassIndex) {
        this.bodyMassIndex = bodyMassIndex;
    }

    public String getHeartRateColor() {
        return heartRateColor;
    }

    public void setHeartRateColor(String heartRateColor) {
        this.heartRateColor = heartRateColor;
    }

    public String getTemperatureColor() {
        return temperatureColor;
    }

    public void setTemperatureColor(String temperatureColor) {
        this.temperatureColor = temperatureColor;
    }

    public String getBloodGlucoseLevelColor() {
        return bloodGlucoseLevelColor;
    }

    public void setBloodGlucoseLevelColor(String bloodGlucoseLevelColor) {
        this.bloodGlucoseLevelColor = bloodGlucoseLevelColor;
    }

    public String getSmell() {
        return smell;
    }

    public void setSmell(String smell) {
        this.smell = smell;
    }

    public String getSmellColor() {
        return smellColor;
    }

    public void setSmellColor(String smellColor) {
        this.smellColor = smellColor;
    }

    public String getBodyMassIndexColor() {
        return bodyMassIndexColor;
    }

    public void setBodyMassIndexColor(String bodyMassIndexColor) {
        this.bodyMassIndexColor = bodyMassIndexColor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static Double round(Double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public Character getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(Character patientSex) {
        this.patientSex = patientSex;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public String getTasteColor() {
        return tasteColor;
    }

    public void setTasteColor(String tasteColor) {
        this.tasteColor = tasteColor;
    }

    public String getConsciousness() {
        return consciousness;
    }

    public void setConsciousness(String consciousness) {
        this.consciousness = consciousness;
    }

    public String getConsciousnessColor() {
        return consciousnessColor;
    }

    public void setConsciousnessColor(String consciousnessColor) {
        this.consciousnessColor = consciousnessColor;
    }

    public String getScoreColor() {
        return scoreColor;
    }

    public void setScoreColor(String scoreColor) {
        this.scoreColor = scoreColor;
    }

    public List<String> getExternal() {
        return external;
    }

    public void setExternal(List<String> external) {
        this.external = external;
    }

    public List<String> getTasksFuture() {
        return tasksFuture;
    }

    public void setTasksFuture(List<String> tasksFuture) {
        this.tasksFuture = tasksFuture;
    }

    public List<String> getTasksPast() {
        return tasksPast;
    }

    public void setTasksPast(List<String> tasksPast) {
        this.tasksPast = tasksPast;
    }

    public String getPatientAadhaarId() {
        return patientAadhaarId;
    }

    public void setPatientAadhaarId(String patientAadhaarId) {
        this.patientAadhaarId = patientAadhaarId;
    }

    public Boolean getDripFlow() {
        return dripFlow;
    }

    public void setDripFlow(Boolean dripFlow) {
        this.dripFlow = dripFlow;
    }

}
