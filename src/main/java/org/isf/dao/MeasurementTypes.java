package org.isf.dao;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name="measurement_types")
@EntityListeners(AuditingEntityListener.class)
public class MeasurementTypes {

    @Id
    @Column(name = "MT_ID", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name = "MT_NAME")
    private String name;

    @Column(name = "MT_TYPE_INDEX")
    private Integer index;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measurementType")
    List<MeasurementsData> measurementsData;


}
