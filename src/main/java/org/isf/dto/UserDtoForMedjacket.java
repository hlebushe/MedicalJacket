package org.isf.dto;

import lombok.Data;
import org.isf.dao.UserGroup;
import org.springframework.stereotype.Component;

import java.sql.Blob;
import java.util.Date;

@Data
@Component
public class UserDtoForMedjacket {
    private UserGroup userGroupName;
    private String firstName;
    private String secondName;
    private String email;
    private Date dateOfBirth;
    private Character sex;
    private String city;
    private String telephone;
    private String aadhaarNumber;


}
