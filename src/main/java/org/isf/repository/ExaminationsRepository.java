package org.isf.repository;

import org.isf.dao.Examinations;
import org.isf.dao.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationsRepository extends JpaRepository<Examinations, Integer> {

    Examinations getByPatient(Patient patient);
}
