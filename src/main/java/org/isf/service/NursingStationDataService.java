package org.isf.service;

import org.isf.dao.Patient;
import org.isf.dao.NursingStationData;
import org.isf.repository.NursingStationDataRepository;
import org.isf.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NursingStationDataService {

    @Autowired
    NursingStationDataRepository nursingStationDataRepository;

    @Autowired
    PatientRepository patientRepository;

    public NursingStationData save(NursingStationData nursingStationData) {
        return nursingStationDataRepository.save(nursingStationData);
    }

    public List<Patient> getPatientsWithLastMeasurements() {

        List<UUID> patIDs = getGuidFromByteArray(nursingStationDataRepository.getPatientsWithLastMeasurements());

        List<Patient> patients = new ArrayList<>();

        for (UUID id : patIDs) {
            Patient p = patientRepository.findByCode(id);
            patients.add(p);
        }

        return patients;
    }

    public NursingStationData getLastByPatient(Patient patient) {
        NursingStationData measurements = nursingStationDataRepository.getLastByPatient(patient);
        return measurements;
    }

    public List<UUID> getGuidFromByteArray(List<byte[]> bytesArr) {
        List<UUID> result = new ArrayList<>();

        for (byte[] bytes: bytesArr) {
            ByteBuffer bb = ByteBuffer.wrap(bytes);
            long high = bb.getLong();
            long low = bb.getLong();
            UUID uuid = new UUID(high, low);
            result.add(uuid);
        }

        return result;
    }

    public NursingStationData findById(UUID code) {
        return nursingStationDataRepository.findById(code).get();
    }

    public boolean updateNursingStationDataTasks(UUID uid, String futureTasks, String pastTasks) {
        NursingStationData uD = nursingStationDataRepository.findById(uid).get();

        if (uD == null) {
            return false;
        }

        uD.setFutureTasks(futureTasks);
        uD.setPastTasks(pastTasks);

        nursingStationDataRepository.save(uD);
        return true;
    }

    public NursingStationData update(NursingStationData nursingStationData) {
        NursingStationData uD = nursingStationDataRepository.getLastByPatient(nursingStationData.getPatient());

        if (uD == null) {
            return null;
        }

        uD.setDate(nursingStationData.getDate());
        uD.setDevId(1);
        uD.setBloodPressureDia(nursingStationData.getBloodPressureDia());
        uD.setBloodPressureSys(nursingStationData.getBloodPressureSys());
        uD.setHeartRate(nursingStationData.getHeartRate());
        uD.setOxygenSaturation(nursingStationData.getOxygenSaturation());
        uD.setOxygenFlowRate(nursingStationData.getOxygenFlowRate());
        uD.setTemperature(nursingStationData.getTemperature());
        uD.setBloodGlucose(nursingStationData.getBloodGlucose());
        uD.setFutureTasks(nursingStationData.getFutureTasks());
        uD.setPastTasks("");

        return nursingStationDataRepository.save(uD);
    }

}
