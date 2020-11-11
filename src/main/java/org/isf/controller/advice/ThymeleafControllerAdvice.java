package org.isf.controller.advice;

import lombok.RequiredArgsConstructor;
import org.isf.dao.DeviceDetails;
import org.isf.dao.User;
import org.isf.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@RequiredArgsConstructor
@ControllerAdvice
public class ThymeleafControllerAdvice {

    private final UserRepository userRepository;

    @ModelAttribute("hospitalName")
    public String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return Optional.ofNullable(userRepository.findByUserName(auth.getName()))
                .map(User::getDeviceDetails)
                .map(DeviceDetails::getHospitalName)
                .orElse("");
    }

}
