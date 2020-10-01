package org.isf.repository;

import org.isf.dao.Examinations;
import org.isf.dao.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExaminationsRepository extends JpaRepository<Examinations, UUID> {

//    List<Examinations> getAllByPatient(Patient patient);

    @Query(value = "SELECT * FROM examinations WHERE EXAMINATION_PAT_ID = :patient ORDER BY EXAMINATION_PAT_ID, EXAMINATIONS_DATE DESC", nativeQuery= true)
    List<Examinations> getByPatientAndOrderByDate(Patient patient);

    @Query(value = "SELECT * FROM examinations WHERE EXAMINATION_PAT_ID = :patient AND EXAMINATIONS_DATE >= :start AND EXAMINATIONS_DATE < :finish ORDER BY EXAMINATION_PAT_ID, EXAMINATIONS_DATE DESC", nativeQuery= true)
    List<Examinations> getByPatientAndDay(Patient patient, String start, String finish);

}
