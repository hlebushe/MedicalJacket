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
}
