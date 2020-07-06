package org.isf.dao;

import lombok.Data;
import org.isf.dao.Patient;
import org.isf.dao.Visit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Table(name="EXAMINATIONS")
public class Examinations {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EXAMINATIONS_ID")
    private Integer id;

    @Column(name = "EXAMINATIONS_RESPIRATORY_RATE")
    private Integer respiratoryRate;

    @Column(name = "EXAMINATION_O2_SATURATION")
    private Integer o2Saturation;

    @Column(name = "EXAMINATION_O2_FLOW_RATE")
    private Integer o2FlowRate;

    @Column(name = "EXAMINATION_BLOOD_PRESSURE_MIN")
    private Integer bloodPressureMin;

    @Column(name = "EXAMINATION_BLOOD_PRESSURE_MAX")
    private Integer bloodPressureMax;

    @Column(name = "EXAMINATION_HEART_RATE")
    private Integer heartRate;

    @Column(name = "EXAMINATION_TEMPERATURE")
    private Double temperature;

    @Column(name = "EXAMINATION_BLOOD_GLUCOSE_LEVEL")
    private Double bloodGlucoseLevel;

    @Column(name = "EXAMINATION_WEIGHT")
    private Double weight;

    @Column(name = "EXAMINATION_HEIGHT")
    private Double height;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "EXAMINATION_PAT_ID")
    Patient patient;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "examination")
    List<Visit> visits;


}
