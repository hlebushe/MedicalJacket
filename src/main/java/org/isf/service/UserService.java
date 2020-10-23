package org.isf.service;

import org.isf.dao.DeviceDetails;
import org.isf.dao.User;
import org.isf.dao.UserGroup;
import org.isf.repository.UserGroupRepository;
import org.isf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserGroupRepository userGroupRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User user) {
        user.setPasswd(bCryptPasswordEncoder.encode(user.getPasswd()));
        return userRepository.save(user);
    }

    public User saveFromDevice(DeviceDetails deviceDetails) {
        UserGroup userGroup = userGroupRepository.findByCode("admin");
        User user = new User();

        user.setEmail(deviceDetails.getEmail());
        user.setUserGroupName(userGroup);
        user.setUserName(deviceDetails.getEmail());
        user.setPasswd(deviceDetails.getPassword());
        user.setDeviceDetails(deviceDetails);

        return userRepository.save(user);
    }

    public boolean checkIfUserNameBusy(String email) {
        User user = userRepository.findByUserName(email);

        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateUser(User user) {
        User userFromDB = userRepository.findByUserName(user.getUserName());
        UserGroup userGroup = userGroupRepository.findByCode("admin");

        if (userFromDB == null) {
            return false;
        }

        userFromDB.setFirstName(user.getFirstName());
        userFromDB.setSecondName(user.getSecondName());
        userFromDB.setDateOfBirth(user.getDateOfBirth());
        userFromDB.setPhoto(user.getPhoto());
        userFromDB.setUserGroupName(userGroup);
        userFromDB.setDesc(user.getDesc());
        userFromDB.setEmail(user.getEmail());
        userFromDB.setSex(user.getSex());
        userFromDB.setUserName(user.getEmail());
        userFromDB.setName();
        userFromDB.setAge();
        userFromDB.setAddress(user.getAddress());
        userFromDB.setCity(user.getCity());
        userFromDB.setEducation(user.getEducation());
        userFromDB.setTelephone(user.getTelephone());
        userFromDB.setAadhaarNumber(user.getAadhaarNumber());
        userFromDB.setProviderNumber(user.getProviderNumber());
        userFromDB.setBiometric(user.getBiometric());
        userFromDB.setRole(user.getRole());
        userFromDB.setDeviceDetails(userFromDB.getDeviceDetails());

        userRepository.save(userFromDB);
        return true;
    }
}
