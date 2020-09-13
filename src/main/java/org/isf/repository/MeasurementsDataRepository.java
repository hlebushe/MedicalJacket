package org.isf.repository;

import org.isf.dao.MeasurementsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementsDataRepository extends JpaRepository<MeasurementsData, Integer> {
}
