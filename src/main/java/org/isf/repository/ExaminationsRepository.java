package org.isf.repository;

import org.isf.dao.Examinations;
import org.isf.dao.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminationsRepository extends JpaRepository<Examinations, Integer> {

//    List<Examinations> getAllByPatient(Patient patient);

    @Query(value = "SELECT * FROM examinations WHERE EXAMINATION_PAT_ID = :patient ORDER BY EXAMINATION_PAT_ID, EXAMINATIONS_DATE DESC", nativeQuery= true)
    List<Examinations> getByPatientAndOrderByDate(Patient patient);
}
