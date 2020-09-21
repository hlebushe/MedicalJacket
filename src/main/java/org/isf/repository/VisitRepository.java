package org.isf.repository;

import org.isf.dao.Patient;
import org.isf.dao.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {

    @Query(value = "SELECT * FROM visits WHERE VST_PAT_ID = :patient ORDER BY VST_PAT_ID, VST_DATE", nativeQuery=true)
    List<Visit> findAllWherePatientByOrderPatientAndDateAsc(@Param("patient") Integer patient);

    Visit findByVisitID(int id);

}
