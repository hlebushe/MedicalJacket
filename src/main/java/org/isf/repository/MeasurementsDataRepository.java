package org.isf.repository;

import org.isf.dao.MeasurementsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MeasurementsDataRepository extends JpaRepository<MeasurementsData, UUID> {
}
