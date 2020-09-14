package org.isf.repository;


import org.isf.dao.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    @Query(value = "SELECT * FROM patient WHERE (PAT_DELETED='N' OR PAT_DELETED IS NULL) ORDER BY PAT_ID", nativeQuery= true)
    List<Patient> findAllWhereDeleted();

    @Transactional
    void deleteByCode(int code);

    @Query(value = "SELECT * FROM patient WHERE PAT_ID = :id", nativeQuery= true)
    List<Patient> findAllWhereId(@Param("id") Integer id);

    Patient findByCode(Integer code);


}
