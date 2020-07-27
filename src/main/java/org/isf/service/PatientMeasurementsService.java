package org.isf.service;

import org.isf.dao.Patient;
import org.isf.dao.PatientMeasurements;
import org.isf.models.ExaminationsModel;
import org.isf.repository.PatientMeasurementsRepository;
import org.isf.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientMeasurementsService {

    @Autowired
    PatientMeasurementsRepository patientMeasurementsRepository;

    @Autowired
    PatientRepository patientRepository;

    public List<Patient> getPatientsWithLastMeasurements() {

        List<Integer> patIDs = patientMeasurementsRepository.getPatientsWithLastMeasurements();

        List<Patient> patients = new ArrayList<>();

        for (Integer id : patIDs) {
            Patient p = patientRepository.findByCode(id);
            patients.add(p);
        }

        return patients;
    }

    public PatientMeasurements getLastByPatient(Patient patient) {
        PatientMeasurements measurements = patientMeasurementsRepository.getLastByPatient(patient);
        return measurements;
    }
}
