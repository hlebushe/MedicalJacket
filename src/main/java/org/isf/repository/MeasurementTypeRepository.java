package org.isf.repository;

import org.isf.dao.MeasurementTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MeasurementTypeRepository extends JpaRepository<MeasurementTypes, UUID> {

    Optional<MeasurementTypes> findById(UUID id);

    MeasurementTypes findByIndex(Integer index);
}
