package org.isf.repository;

import org.isf.dao.Examinations;
import org.isf.dao.Patient;
import org.isf.dao.PatientMeasurements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientMeasurementsRepository extends JpaRepository<PatientMeasurements, Integer> {

    @Query(value = "SELECT DISTINCT PM_PAT_ID FROM PATIENT_MEASUREMENTS", nativeQuery = true)
    List<Integer> getPatientsWithLastMeasurements();

    @Query(value = "SELECT * FROM oh.PATIENT_MEASUREMENTS WHERE PM_PAT_ID = :patient ORDER BY PM_TIME DESC LIMIT 1", nativeQuery= true)
    PatientMeasurements getLastByPatient(Patient patient);
}
