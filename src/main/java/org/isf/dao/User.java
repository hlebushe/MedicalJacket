package org.isf.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.isf.dao.UserGroup;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Blob;
import java.time.*;
import java.util.Date;

@Entity
@Data
@Table(name="user")
@AttributeOverrides({
        @AttributeOverride(name="createdBy", column=@Column(name="US_CREATED_BY")),
        @AttributeOverride(name="createdDate", column=@Column(name="US_CREATED_DATE")),
        @AttributeOverride(name="lastModifiedBy", column=@Column(name="US_LAST_MODIFIED_BY")),
        @AttributeOverride(name="active", column=@Column(name="US_ACTIVE")),
        @AttributeOverride(name="lastModifiedDate", column=@Column(name="US_LAST_MODIFIED_DATE"))
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="US_ID_A")
    private String userName;

    @NotNull
    @ManyToOne
    @JoinColumn(name="US_UG_ID_A")
    private UserGroup userGroupName;

    @Column(name="US_FNAME")
    private String firstName;

    @Column(name="US_SNAME")
    private String secondName;

    @Column(name="US_NAME")
    private String name;

    @NotNull
    @Column(name="US_PASSWD")
    private String passwd;

    @Column(name="US_DESC")
    private String desc;

    @Column(name="US_EMAIL")
    private String email;

    @Column(name="US_AGE")
    private Integer age;

    @Column(name="US_BDATE")
    private Date dateOfBirth;

    @Column(name="US_SEX")
    private Character sex;

    @Column(name="US_ADDR")
    private String address;

    @Column(name="US_CITY")
    private String city;

    @Column(name="US_TELE")
    private String telephone;

    @Column(name="US_AADHAAR", unique = true)
    private String aadhaarNumber;

    @Column(name="US_EDUCATION")
    private String education;

    @Column(name="US_PROVIDER_NUMBER")
    private String providerNumber;

    @Column(name="US_ROLE")
    private String role;

    @Column(name="US_PHOTO")
    @Lob
    @JsonIgnore
    private Blob photo;

    @Column(name="US_BIOMETRIC")
    private Blob biometric;

    @Transient
    private volatile int hashCode = 0;

    @ManyToOne
    @JoinColumn(name = "center_id")
    private DifferentCentre differentCentre;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "US_MACHINE_ID", referencedColumnName = "MachineID")
    DeviceDetails deviceDetails;

    @Column(name="US_PREFERRED_PATHOLOGY")
    private String preferredPathology;

    public User(){
    }

    public User(String aName, UserGroup aGroup, String aPasswd, String aDesc){
        this.userName = aName;
        this.userGroupName = aGroup;
        this.passwd = aPasswd;
        this.desc = aDesc;
    }

    public String toString(){
        return getUserName();
    }

    @Override
    public boolean equals(Object anObject) {
        return (anObject == null) || !(anObject instanceof User) ? false
                : (getUserName().equalsIgnoreCase(
                ((User) anObject).getUserName()) && getDesc()
                .equalsIgnoreCase(
                        ((User) anObject).getDesc()));
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            final int m = 23;
            int c = 133;

            c = m * c + userName.hashCode();

            this.hashCode = c;
        }

        return this.hashCode;
    }

    public void setAge() {
        Instant instant = this.dateOfBirth.toInstant();
        ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
        LocalDate givenDate = zone.toLocalDate();
        Period period = Period.between(givenDate, LocalDate.now());
        int age = period.getYears();
        this.age = age;
    }

    public void setName() {
        this.name = this.firstName + " " + this.secondName;
    }

}
