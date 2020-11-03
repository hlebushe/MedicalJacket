package org.isf.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.isf.enums.CentreType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Blob;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "different_centre")
@EntityListeners(AuditingEntityListener.class)
public class DifferentCentre {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String contactPerson;
    @NotEmpty
    private String address;
    @NotEmpty
    @Column(unique = true)
    private String email;
    @NotEmpty
    @Column(unique = true)
    private String mobile;
    @Column(name = "logo")
    @Lob
    @JsonIgnore
    private Blob logo;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "regi_date")
    private Date regiDate;

    private CentreType centreType;
}
