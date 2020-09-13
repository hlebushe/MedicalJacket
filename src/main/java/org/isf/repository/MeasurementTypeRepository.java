package org.isf.repository;

import org.isf.dao.MeasurementTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeasurementTypeRepository extends JpaRepository<MeasurementTypes, Integer> {

    Optional<MeasurementTypes> findById(Integer id);
}
