package org.isf.dao;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name="nursing_station_data")
public class NursingStationData {

    @Id
    @Column(name = "PM_ID", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

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

    @Column(name = "PM_PAST_TASKS")
    private String pastTasks;

    @Column(name = "PM_FUTURE_TASKS")
    private String futureTasks;

    @Column(name = "PM_PATHOLOGY")
    private String pathology;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "PM_PAT_ID")
    Patient patient;

    @NotNull
    @Column(name = "PM_TIME")
    private Date date;
}
