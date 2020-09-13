package org.isf.dao;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Table(name="measurements_data")
@EntityListeners(AuditingEntityListener.class)
public class MeasurementsData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MD_ID")
    private Integer id;

    @Column(name = "MD_VALUE")
    private Double value;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "MD_TYPE")
    MeasurementTypes measurementType;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "MD_PAT_ID")
    Patient patient;

    @Column(name = "MD_TIME")
    private Date date;
}
