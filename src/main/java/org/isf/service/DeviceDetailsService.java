package org.isf.service;

import org.isf.dao.DeviceDetails;
import org.isf.repository.DeviceDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DeviceDetailsService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    DeviceDetailsRepository deviceDetailsRepository;

    public DeviceDetails saveDevice(DeviceDetails deviceDetails) {
        deviceDetails.setPassword(bCryptPasswordEncoder.encode(deviceDetails.getPassword()));
        return deviceDetailsRepository.save(deviceDetails);
    }
}
