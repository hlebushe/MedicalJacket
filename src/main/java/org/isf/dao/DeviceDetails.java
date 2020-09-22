package org.isf.dao;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@Table(name="device_details")
public class DeviceDetails {

    @Id
    @Column(name = "DeviceID", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "MachineID")
    private String machineID;

    @Column(name = "HospitalName")
    private String hospitalName;

    @Column(name = "HospitalBranch")
    private String hospitalBranch;

    @Column(name = "HospitalWard")
    private String hospitalWard;

    @Column(name = "HospitalAddress")
    private String hospitalAddress;

    @Column(name = "Email")
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "WebsiteAddress")
    private String websiteAddress;

    @Column(name = "HaveBranches")
    private String haveBranches;

    @Column(name = "MainContactPersonEmail")
    private String mainContactPersonEmail;

    @Column(name = "MainContactPersonName")
    private String mainContactPersonName;

    @Column(name = "NumberOfUsers")
    private String numberOfUsers;

}
