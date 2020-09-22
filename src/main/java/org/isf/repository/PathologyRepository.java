package org.isf.repository;

import org.isf.dao.Pathology;
import org.isf.dao.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PathologyRepository extends JpaRepository<Pathology, UUID> {

    @Query(value = "SELECT * FROM pathology WHERE PATHOLOGY_PAT_ID = :patient ORDER BY PATHOLOGY_DATE DESC", nativeQuery= true)
    List<Pathology> getAllByPatient(Patient patient);

    Optional<Pathology> findById(UUID id);
}
