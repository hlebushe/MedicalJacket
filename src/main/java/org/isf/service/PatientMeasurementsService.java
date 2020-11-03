package org.isf.service;

import org.isf.dao.Patient;
import org.isf.dao.PatientMeasurements;
import org.isf.models.ExaminationsModel;
import org.isf.repository.PatientMeasurementsRepository;
import org.isf.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientMeasurementsService {

    @Autowired
    PatientMeasurementsRepository patientMeasurementsRepository;

    @Autowired
    PatientRepository patientRepository;

    public List<Patient> getPatientsWithLastMeasurements() {

        List<UUID> patIDs = getGuidFromByteArray(patientMeasurementsRepository.getPatientsWithLastMeasurements());

        List<Patient> patients = new ArrayList<>();

        for (UUID id : patIDs) {
            Patient p = patientRepository.findByCode(id);
            patients.add(p);
        }

        return patients;
    }

    public PatientMeasurements getLastByPatient(Patient patient) {
        PatientMeasurements measurements = patientMeasurementsRepository.getLastByPatient(patient);
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
