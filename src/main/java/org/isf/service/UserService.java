package org.isf.service;

import org.isf.dao.User;
import org.isf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User user) {
        user.setPasswd(bCryptPasswordEncoder.encode(user.getPasswd()));
        return userRepository.save(user);
    }
}
