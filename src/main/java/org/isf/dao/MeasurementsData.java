package org.isf.dao;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name="measurements_data")
@EntityListeners(AuditingEntityListener.class)
public class MeasurementsData {

    @Id
    @Column(name = "MD_ID", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

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
