package org.isf.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.imageio.ImageIO;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name="PATIENT")
@EntityListeners(AuditingEntityListener.class)
@AttributeOverrides({
        @AttributeOverride(name="createdBy", column=@Column(name="PAT_CREATED_BY")),
        @AttributeOverride(name="createdDate", column=@Column(name="PAT_CREATED_DATE")),
        @AttributeOverride(name="lastModifiedBy", column=@Column(name="PAT_LAST_MODIFIED_BY")),
        @AttributeOverride(name="active", column=@Column(name="PAT_ACTIVE")),
        @AttributeOverride(name="lastModifiedDate", column=@Column(name="PAT_LAST_MODIFIED_DATE"))
})
public class Patient  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="PAT_ID")
    private Integer code;

    @NotNull
    @Column(name="PAT_FNAME")
    private String firstName;

    @NotNull
    @Column(name="PAT_SNAME")
    private String secondName;

    @NotNull
    @Column(name="PAT_NAME")
    private String name;

    @Column(name="PAT_BDATE")
    private Date birthDate;

    @NotNull
    @Column(name="PAT_AGE")
    private int age;

    @NotNull
    @Column(name="PAT_AGETYPE")
    private String agetype;

    @NotNull
    @Column(name="PAT_SEX")
    private char sex;

    @Column(name="PAT_ADDR")
    private String address;

    @NotNull
    @Column(name="PAT_CITY")
    private String city;

    @Column(name="PAT_NEXT_KIN")
    private String nextKin;

    @NotNull
    @Column(name="PAT_EMAIL")
    private String email;

    @Column(name="PAT_TELE")
    private String telephone;

    @Column(name="PAT_NOTE")
    private String note;

    @NotNull
    @Column(name="PAT_MOTH_NAME")
    private String mother_name; // mother's name

    @Column(name="PAT_MOTH")
    private Character mother; // D=dead, A=alive

    @NotNull
    @Column(name="PAT_FATH_NAME")
    private String father_name; // father's name

    @Column(name="PAT_FATH")
    private Character father; // D=dead, A=alive

    @NotNull
    @Column(name="PAT_BTYPE")
    private String bloodType; // (0-/+, A-/+ , B-/+, AB-/+)

    @Column(name="PAT_ESTA")
    private Character hasInsurance; // Y=Yes, N=no

    @Column(name="PAT_PTOGE")
    private Character parentTogether;

    @Column(name="PAT_TAXCODE")
    private String taxCode;

    @Column(name="PAT_MAR_STAT")
    private String maritalStatus;

    @Column(name="PAT_PROFESSION")
    private String profession;

    @Column(name="PAT_AREA")
    private String area;

    @Column(name="PAT_SMOKER")
    private Character smoker;

    @Column(name="PAT_SMOKING_YEARS")
    private Integer smokingYears;

    @Column(name="PAT_SMOKING_DAY")
    private Integer smokingADay;

    @Column(name="PAT_ALCOHOL")
    private Character alcohol;

    @Column(name="PAT_ALCOHOL_YEARS")
    private Integer alcoholYears;

    @Column(name="PAT_ALCOHOL_WEEK")
    private Integer alcoholWeek;

    @Column(name="PAT_ALLERGIES")
    private String allergies;

    @Column(name="PAT_HEALTH_CARDS")
    private Character healthCards;

    @Column(name="PAT_PREVIOUS_MEDICAL_CONDITIONS")
    private String previousMedicalConditions;

    @Column(name="PAT_NATURE_OF_MEDICAL_CONDITION")
    private String natureOfMedicalCondition;

    @Column(name="PAT_EXISTING_MEDICATION")
    private Blob existingMedication;

    @Column(name="PAT_PREVIOUS_OPERATIONS")
    private Character previousOperations;

    @Column(name="PAT_PREVIOUS_OPER_YEAR_1")
    private String previousOperationYear1;

    @Column(name="PAT_PREVIOUS_OPER_NATURE_1")
    private String previousOperationNature1;

    @Column(name="PAT_PREVIOUS_OPER_YEAR_2")
    private String previousOperationYear2;

    @Column(name="PAT_PREVIOUS_OPER_NATURE_2")
    private String previousOperationNature2;

    @Column(name="PAT_PREVIOUS_OPER_YEAR_3")
    private String previousOperationYear3;

    @Column(name="PAT_PREVIOUS_OPER_NATURE_3")
    private String previousOperationNature3;

    @Column(name="PAT_HEREDITARY_DIS_FAMILY")
    private String hereditaryDiseasesInFamily;

    @Column(name="PAT_NEXT_KIN_RELATIONSHIP")
    private String nextKinRelationship;

    @Column(name="PAT_NEXT_TELEPHONE")
    private String nextKinTelephone;

    @Column(name="PAT_INSURANCE_COMPANY")
    private String insuranceCompany;

    @Column(name="PAT_INSURANCE_NUMBER")
    private String insuranceNumber;

    @Column(name="PAT_HEALTH_CARD_TYPE")
    private String healthCardType;

    @Column(name="PAT_HEALTH_CARD_NUMBER")
    private String healthCardNumber;

    @Column(name="PAT_BIOMETRIC")
    private Blob biometric;

    @Transient
    private String pddScore;

    @Transient
    private String dateOfLastVisit;

    @NotNull
    @JsonIgnore
    @Column(name="PAT_DELETED")
    private String deleted = "N";

    @Transient
    @JsonIgnore
    private float height;

    @Transient
    @JsonIgnore
    private float weight;

    @Column(name="PAT_PHOTO")
    @Lob
    @JsonIgnore
    private Blob photo;

    @Transient
    @JsonIgnore
    private Image photoImage;

    @Transient
    @JsonIgnore
    private volatile int hashCode = 0;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
    List<Examinations> examinations;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
    List<PatientMeasurements> patientMeasurements;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
    List<Visit> visits;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
    List<Pathology> pathologies;


    public Patient() {

        this.firstName = "";
        this.secondName = "";
        this.name = this.firstName + " " + this.secondName;
        this.birthDate = null;
        this.age = 0;
        this.agetype = "";
        this.sex = ' ';
        this.address = "";
        this.city = "";
        this.nextKin = "";
        this.telephone = "";
        this.mother_name = "";
        this.father_name = "";
        this.bloodType = "";
        this.taxCode = "";
        this.height = 0;
        this.weight = 0;
        this.maritalStatus = "";
        this.profession = "";
    }


    public Patient(String firstName, String secondName, Date birthDate, int age, String agetype, char sex,
                   String address, String city, String nextKin, String telephone,
                   String mother_name, char mother, String father_name, char father,
                   String bloodType, char economicStatut, char parentTogether, String personalCode,
                   String maritalStatus, String profession) { //Changed EduLev with bloodType
        this.firstName = firstName;
        this.secondName = secondName;
        this.name = this.firstName + " " + this.secondName;
        this.birthDate = birthDate;
        this.age = age;
        this.agetype = agetype;
        this.sex = sex;
        this.address = address;
        this.city = city;
        this.nextKin = nextKin;
        this.telephone = telephone;
        this.mother_name = mother_name;
        this.mother = mother;
        this.father_name = father_name;
        this.father = father;
        this.hasInsurance = economicStatut;
        this.bloodType = bloodType;
        this.parentTogether = parentTogether;
        this.taxCode = personalCode;
        this.height = 0;
        this.weight = 0;
        this.maritalStatus = maritalStatus;
        this.profession = profession;
    }

    public Patient(int code, String firstName, String secondName, String name, Date birthDate, int age, String agetype, char sex,
                   String address, String city, String nextKin, String telephone, String note,
                   String mother_name, char mother, String father_name, char father,
                   String bloodType, char economicStatut, char parentTogether, String taxCode,
                   float height, float weight, Blob photo, Image photoImage, String maritalStatus, String profession) { //Changed EduLev with bloodType
        this.code = code;
        this.firstName = firstName;
        this.secondName = secondName;
        this.name = name;
        this.birthDate = birthDate;
        this.age = age;
        this.agetype = agetype;
        this.sex = sex;
        this.address = address;
        this.city = city;
        this.nextKin = nextKin;
        this.telephone = telephone;
        this.note = note;
        this.mother_name = mother_name;
        this.mother = mother;
        this.father_name = father_name;
        this.father = father;
        this.hasInsurance = economicStatut;
        this.bloodType = bloodType;
        this.parentTogether = parentTogether;
        this.taxCode = taxCode;
        this.height = height;
        this.weight = weight;
        this.photo = photo;
        this.photoImage = photoImage;
        this.maritalStatus = maritalStatus;
        this.profession = profession;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getAge() {
        if (this.birthDate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM");
            Date date = null;
            try {
                date = formatter.parse(this.birthDate.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Instant instant = date.toInstant();
            ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
            LocalDate givenDate = zone.toLocalDate();
            Period period = Period.between(givenDate, LocalDate.now());
            int age = period.getYears();
        }
        return age;
    }

    //TODO need fix
    public void setAge(Date birthday) {
            Instant instant = birthday.toInstant();
            ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
            LocalDate givenDate = zone.toLocalDate();
            Period period = Period.between(givenDate, LocalDate.now());
            int age = period.getYears();
            this.age = age;
    }

    public void setAge() {
        Instant instant = this.birthDate.toInstant();
        ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
        LocalDate givenDate = zone.toLocalDate();
        Period period = Period.between(givenDate, LocalDate.now());
        int age = period.getYears();
        this.age = age;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.name = this.firstName + " " + this.secondName;
    }


    public void setSecondName(String secondName) {
        this.secondName = secondName;
        this.name = this.firstName + " " + this.secondName;
    }

    @JsonIgnore
    public Blob getBlobPhoto() {
        return photo;
    }

    @JsonIgnore
    public void setBlobPhoto(Blob photo) {
        this.photo = photo;
    }

    @JsonIgnore
    public Image getPhoto() {
        try {
            if (photo != null && photo.length() > 0) {
                BufferedInputStream is = new BufferedInputStream(photo.getBinaryStream());
                Image image = ImageIO.read(is);
                setPhoto(image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photoImage;
    }

    @JsonIgnore
    public void setPhoto(Image image) {
        this.photoImage = image;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Patient)) {
            return false;
        }

        Patient patient = (Patient)obj;
        return (this.getCode().equals(patient.getCode()));
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            final int m = 23;
            int c = 133;

            c = m * c + ((code == null) ? 0 : code);

            this.hashCode = c;
        }

        return this.hashCode;
    }

    @JsonIgnore
    public String getSearchString() {
        StringBuffer sbName = new StringBuffer();
        sbName.append(getCode());
        sbName.append(" ");
        sbName.append(getFirstName().toLowerCase());
        sbName.append(" ");
        sbName.append(getSecondName().toLowerCase());
        sbName.append(" ");
        sbName.append(getCity().toLowerCase());
        sbName.append(" ");
        if (getAddress() != null) sbName.append(getAddress().toLowerCase()).append(" ");
        if (getTelephone() != null) sbName.append(getTelephone()).append(" ");
        if (getNote() != null) sbName.append(getNote().toLowerCase()).append(" ");
        if (getTaxCode() != null) sbName.append(getTaxCode().toLowerCase()).append(" ");
        return sbName.toString();
    }

    @JsonIgnore
    public String getInformations() {
        int i = 0;
        StringBuffer infoBfr = new StringBuffer();
        if (city != null && !city.equals("")) {
            infoBfr.append(i > 0 ? " - " : "");
            infoBfr.append(city);
            i++;
        }
        if (address != null && !address.equals("")) {
            infoBfr.append(i > 0 ? " - " : "");
            infoBfr.append(address);
            i++;
        }
        if (telephone != null && !telephone.equals("")) {
            infoBfr.append(i > 0 ? " - " : "");
            infoBfr.append(telephone);
            i++;
        }
        if (note != null && !note.equals("")) {
            infoBfr.append(i > 0 ? " - " : "");
            infoBfr.append(note);
            i++;
        }
        if (taxCode != null && !taxCode.equals("")) {
            infoBfr.append(i > 0 ? " - " : "");
            infoBfr.append(taxCode);
            i++;
        }
        return infoBfr.toString();
    }
}
