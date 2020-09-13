package org.isf.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Table(name="visits")
@EntityListeners(AuditingEntityListener.class)
@AttributeOverrides({
        @AttributeOverride(name="createdBy", column=@Column(name="VST_CREATED_BY")),
        @AttributeOverride(name="createdDate", column=@Column(name="VST_CREATED_DATE")),
        @AttributeOverride(name="lastModifiedBy", column=@Column(name="VST_LAST_MODIFIED_BY")),
        @AttributeOverride(name="active", column=@Column(name="VST_ACTIVE")),
        @AttributeOverride(name="lastModifiedDate", column=@Column(name="VST_LAST_MODIFIED_DATE"))
})
public class Visit {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="VST_ID")
    private int visitID;

    @NotNull
    @ManyToOne
    @JoinColumn(name="VST_PAT_ID")
    Patient patient;

    @NotNull
    @Column(name="VST_DATE")
    private Date date;

    @Column(name="VST_NOTE")
    private String note;

    @Column(name="VST_SMS")
    private boolean sms;

    @Column(name="VST_TYPE")
    private Character type;

    @Column(name="VST_MAIN_COMPLAINT_SYMPTOM1")
    private String mainComplaintSymptom1;

    @Column(name="VST_MAIN_COMPLAINT_SYMPTOM2")
    private String mainComplaintSymptom2;

    @Column(name="VST_MAIN_COMPLAINT_SYMPTOM3")
    private String mainComplaintSymptom3;

    @Column(name="VST_MAIN_COMPLAINT_SYMPTOM4")
    private String mainComplaintSymptom4;

    @Column(name="VST_MAIN_COMPLAINT_SYMPTOM5")
    private String mainComplaintSymptom5;

    @Column(name="VST_MAIN_COMPLAINT_DIAGNOSIS1")
    private String mainComplaintDiagnosis1;

    @Column(name="VST_MAIN_COMPLAINT_DIAGNOSIS2")
    private String mainComplaintDiagnosis2;

    @Column(name="VST_MAIN_COMPLAINT_DIAGNOSIS3")
    private String mainComplaintDiagnosis3;

    @Column(name="VST_SECONDARY_COMPLAINT_SYMPTOM1")
    private String secondaryComplaintSymptom1;

    @Column(name="VST_SECONDARY_COMPLAINT_SYMPTOM2")
    private String secondaryComplaintSymptom2;

    @Column(name="VST_SECONDARY_COMPLAINT_SYMPTOM3")
    private String secondaryComplaintSymptom3;

    @Column(name="VST_SECONDARY_COMPLAINT_SYMPTOM4")
    private String secondaryComplaintSymptom4;

    @Column(name="VST_SECONDARY_COMPLAINT_SYMPTOM5")
    private String secondaryComplaintSymptom5;

    @Column(name="VST_SECONDARY_COMPLAINT_DIAGNOSIS")
    private String secondaryComplaintDiagnosis;

    @Column(name="VST_EXAMINATIONS_PRESCRIBED")
    private String examinationsPrescribed;

    @Column(name="VST_ADVISORY")
    private String advisory;

    @Column(name="VST_COMMENTS")
    private String comments;

    @Column(name="VST_NEXT_VISIT_DATE")
    private Date nextVisitDate;

    @Column(name="VST_MEDICATION_1")
    private String medication1;

    @Column(name="VST_MEDICATION_2")
    private String medication2;

    @Column(name="VST_MEDICATION_3")
    private String medication3;

    @Column(name="VST_MEDICATION_4")
    private String medication4;

    @Column(name="VST_MEDICATION_5")
    private String medication5;

    @Column(name="VST_MEDICATION_6")
    private String medication6;

    @Transient
    private String med1;

    @Transient
    private String med1_dosage;

    @Transient
    private String med1_duration;

    @Transient
    private String med1_caution;

    @Transient
    private String med2;

    @Transient
    private String med2_dosage;

    @Transient
    private String med2_duration;

    @Transient
    private String med2_caution;

    @Transient
    private String med3;

    @Transient
    private String med3_dosage;

    @Transient
    private String med3_duration;

    @Transient
    private String med3_caution;

    @Transient
    private String med4;

    @Transient
    private String med4_dosage;

    @Transient
    private String med4_duration;

    @Transient
    private String med4_caution;

    @Transient
    private String med5;

    @Transient
    private String med5_dosage;

    @Transient
    private String med5_duration;

    @Transient
    private String med5_caution;

    @Transient
    private String med6;

    @Transient
    private String med6_dosage;

    @Transient
    private String med6_duration;

    @Transient
    private String med6_caution;


    @NotNull
    @ManyToOne
    @JoinColumn(name="VST_PAT_EXAM")
    Examinations examination;

    @Transient
    private volatile int hashCode = 0;


    public Visit() {
        super();
    }

    public Visit(int visitID, Date date, Patient patient, String note, boolean sms) {
        super();
        this.visitID = visitID;
        this.date = date;
        this.patient = patient;
        this.note = note;
        this.sms = sms;
    }

//    public GregorianCalendar getDate() {
//        return date;
//    }
//
//    public void setDate(GregorianCalendar date) {
//        this.date = date;
//    }
//
//    public void setDate(Date date) {
//        GregorianCalendar gregorian = new GregorianCalendar();
//        gregorian.setTime(date);
//        setDate(gregorian);
//    }

//    public String toString() {
//
//        return formatDateTime(this.date);
//    }
//
//    public String toStringSMS() {
//
//        return formatDateTimeSMS(this.date);
//    }

    public String formatDateTime(GregorianCalendar time) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy - HH:mm:ss"); //$NON-NLS-1$
        return format.format(time.getTime());
    }

    public String formatDateTimeSMS(GregorianCalendar time) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm"); //$NON-NLS-1$
        return format.format(time.getTime());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Visit)) {
            return false;
        }

        Visit visit = (Visit)obj;
        return (visitID == visit.getVisitID());
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            final int m = 23;
            int c = 133;

            c = m * c + visitID;

            this.hashCode = c;
        }

        return this.hashCode;
    }

    public void defineMed1() {
        if (this.med1.isEmpty()) {
            this.medication1 = "";
        } else {
            this.medication1 = this.med1 + ";" + this.med1_dosage + ";" + this.med1_duration + ";" + this.med1_caution;
        }
    }

    public void defineMed2() {
        if (this.med2.isEmpty()) {
            this.medication2 = "";
        } else {
            this.medication2 = this.med2 + ";" + this.med2_dosage + ";" + this.med2_duration + ";" + this.med2_caution;
        }
    }

    public void defineMed3() {
        if (this.med3.isEmpty()) {
            this.medication3 = "";
        } else {
            this.medication3 = this.med3 + ";" + this.med3_dosage + ";" + this.med3_duration + ";" + this.med3_caution;
        }
    }

    public void defineMed4() {
        if (this.med4.isEmpty()) {
            this.medication4 = "";
        } else {
            this.medication4 = this.med4 + ";" + this.med4_dosage + ";" + this.med4_duration + ";" + this.med4_caution;
        }
    }

    public void defineMed5() {
        if (this.med5.isEmpty()) {
            this.medication5 = "";
        } else {
            this.medication5 = this.med5 + ";" + this.med5_dosage + ";" + this.med5_duration + ";" + this.med5_caution;
        }
    }

    public void defineMed6() {
        if (this.med6.isEmpty()) {
            this.medication6 = "";
        } else {
            this.medication6 = this.med6 + ";" + this.med6_dosage + ";" + this.med6_duration + ";" + this.med6_caution;
        }
    }

    public void defineMed() {
        defineMed1();
        defineMed2();
        defineMed3();
        defineMed4();
        defineMed5();
        defineMed6();
    }
}
