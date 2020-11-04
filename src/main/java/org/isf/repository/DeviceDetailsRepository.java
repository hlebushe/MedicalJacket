package org.isf.repository;

import org.isf.dao.DeviceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceDetailsRepository extends JpaRepository<DeviceDetails, UUID> {

    DeviceDetails findByMachineID(String machineId);
}
