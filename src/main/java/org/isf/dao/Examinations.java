package org.isf.dao;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.isf.dao.Patient;
import org.isf.dao.Visit;
import org.isf.util.DateUtil;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name="examinations")
@EntityListeners(AuditingEntityListener.class)
public class Examinations {

    @Id
    @Column(name = "EXAMINATIONS_ID", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

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
    @Column(name = "EXAMINATIONS_DATE")
    private Date date;

    @Column(name = "EXAMINATIONS_SMELL")
    private String smell;

    @Column(name = "EXAMINATIONS_TASTE")
    private String taste;

    @Column(name = "EXAMINATIONS_CONSCIOUSNESS")
    private String consciousness;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "EXAMINATION_PAT_ID")
    Patient patient;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "examination")
    List<Visit> visits;

    public String getFormattedDate() {
        return DateUtil.formatDate(date);
    }


}
