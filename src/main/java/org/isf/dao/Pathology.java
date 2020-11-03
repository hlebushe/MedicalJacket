package org.isf.dao;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Blob;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name="pathology")
@EntityListeners(AuditingEntityListener.class)
public class Pathology {

    @Id
    @Column(name = "PATHOLOGY_ID", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(name="PATHOLOGY_DATA")
    private Blob pathologyData;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "PATHOLOGY_PAT_ID")
    Patient patient;

    @Column(name = "PATHOLOGY_DATE")
    private Date date;
}
