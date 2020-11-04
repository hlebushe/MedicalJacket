package org.isf.controller.api;

import io.swagger.annotations.Api;
import org.isf.dao.User;
import org.isf.dto.UserDtoForMedjacket;
import org.isf.repository.UserGroupRepository;
import org.isf.repository.UserRepository;
import org.isf.service.FilesService;
import org.isf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "Medjacket API", description = "Patient")
@RequestMapping(value = "/v1/api/user/")
public class MedjacketRegistratioApiController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FilesService filesService;

    @Autowired
    UserGroupRepository userGroupRepository;
///http://198.154.99.188:8080/mj/v1/api/user/verify/123456789102
    @GetMapping("/verify/{aadhaarNumber}")
    public ResponseEntity<UserDtoForMedjacket> getViewUser(@PathVariable("aadhaarNumber") String aadhaarNumber, Model model) {

        UserDtoForMedjacket dto = new UserDtoForMedjacket();
        if (aadhaarNumber.length() == 12 && this.userRepository.existsByAadhaarNumber(aadhaarNumber)) {
           User user = userRepository.findByAadhaarNumber(aadhaarNumber);
            dto.setAadhaarNumber(user.getAadhaarNumber());
            dto.setEmail(user.getEmail());
            dto.setUserGroupName(user.getUserGroupName());
            dto.setFirstName(user.getFirstName());
            dto.setFirstName(user.getFirstName());
            dto.setSecondName(user.getSecondName());
            dto.setTelephone(user.getTelephone());
            return new ResponseEntity<UserDtoForMedjacket>(dto, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
