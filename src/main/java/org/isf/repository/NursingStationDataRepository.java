package org.isf.repository;

import org.isf.dao.Patient;
import org.isf.dao.NursingStationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NursingStationDataRepository extends JpaRepository<NursingStationData, UUID> {

    @Query(value = "SELECT DISTINCT PM_PAT_ID FROM nursing_station_data", nativeQuery = true)
    List<byte[]> getPatientsWithLastMeasurements();

    @Query(value = "SELECT * FROM nursing_station_data WHERE PM_PAT_ID = :patient ORDER BY PM_TIME DESC LIMIT 1", nativeQuery= true)
    NursingStationData getLastByPatient(Patient patient);

    Optional<NursingStationData> findById(UUID id);

    @Query(value = "SELECT DISTINCT PM_PAT_ID FROM medjacketdb.nursing_station_data INNER JOIN medjacketdb.patient where PAT_MACHINE_ID = :machineId", nativeQuery = true)
    List<byte[]> getPatientsWithLastMeasurementsByMachineId(String machineId);
}
