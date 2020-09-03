package org.isf.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.isf.dao.UserGroup;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import java.sql.Blob;
import java.util.Date;

@Entity
@Data
@Table(name="USER")
@AttributeOverrides({
        @AttributeOverride(name="createdBy", column=@Column(name="US_CREATED_BY")),
        @AttributeOverride(name="createdDate", column=@Column(name="US_CREATED_DATE")),
        @AttributeOverride(name="lastModifiedBy", column=@Column(name="US_LAST_MODIFIED_BY")),
        @AttributeOverride(name="active", column=@Column(name="US_ACTIVE")),
        @AttributeOverride(name="lastModifiedDate", column=@Column(name="US_LAST_MODIFIED_DATE"))
})
public class User
{
    @Id
    @Column(name="US_ID_A")
    private String userName;

    @NotNull
    @ManyToOne
    @JoinColumn(name="US_UG_ID_A")
    private UserGroup userGroupName;

    @NotNull
    @Column(name="US_FNAME")
    private String firstName;

    @NotNull
    @Column(name="US_SNAME")
    private String secondName;

    @NotNull
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

    @Column(name="US_AADHAAR")
    private String aadhaarNumber;

    @Column(name="US_EDUCATION")
    private String education;

    @Column(name="US_MAR_STAT")
    private String martialStatus;

    @Column(name="US_ALLERGIES")
    private String allergies;

    @Column(name="US_SURGERIES")
    private String surgeries;

    @Column(name="US_MED_NOTES")
    private String medicalNotes;

    @Column(name="US_BTYPE")
    private String bloodType;

    @Column(name="US_PHOTO")
    @Lob
    @JsonIgnore
    private Blob photo;

    @Transient
    private volatile int hashCode = 0;


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

}
