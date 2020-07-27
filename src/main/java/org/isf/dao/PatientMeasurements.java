package org.isf.dao;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Table(name="PATIENT_MEASUREMENTS")
public class PatientMeasurements {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PM_ID")
    private Integer id;

    @Column(name = "PM_DEV_ID")
    private Integer devId;

    @Column(name = "PM_HEART_RATE")
    private Integer heartRate;

    @Column(name = "PM_OXYGEN")
    private Integer oxygen;

    @Column(name = "PM_PRESSURE_SYS")
    private Integer bloodPressureSys;

    @Column(name = "PM_PRESSURE_DIA")
    private Integer bloodPressureDia;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "PM_PAT_ID")
    Patient patient;

    @NotNull
    @Column(name = "PM_TIME")
    private Date date;
}
