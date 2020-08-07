package org.isf.dao;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Blob;
import java.util.Date;

@Entity
@Data
@Table(name="PATHOLOGY")
@EntityListeners(AuditingEntityListener.class)
public class Pathology {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PATHOLOGY_ID")
    private Integer id;

    @Column(name="PATHOLOGY_DATA")
    private Blob pathologyData;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "PATHOLOGY_PAT_ID")
    Patient patient;

    @Column(name = "PATHOLOGY_DATE")
    private Date date;
}
