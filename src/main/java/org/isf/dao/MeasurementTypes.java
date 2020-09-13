package org.isf.dao;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name="measurement_types")
@EntityListeners(AuditingEntityListener.class)
public class MeasurementTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MT_ID")
    private Integer id;

    @Column(name = "MT_NAME")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measurementType")
    List<MeasurementsData> measurementsData;


}
