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
import org.isf.dao.Examinations;
import org.isf.dao.Patient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Table(name="VISITS")
@EntityListeners(AuditingEntityListener.class)
@AttributeOverrides({
        @AttributeOverride(name="createdBy", column=@Column(name="VST_CREATED_BY")),
        @AttributeOverride(name="createdDate", column=@Column(name="VST_CREATED_DATE")),
        @AttributeOverride(name="lastModifiedBy", column=@Column(name="VST_LAST_MODIFIED_BY")),
        @AttributeOverride(name="active", column=@Column(name="VST_ACTIVE")),
        @AttributeOverride(name="lastModifiedDate", column=@Column(name="VST_LAST_MODIFIED_DATE"))
})
public class Visit
{
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
    private GregorianCalendar date;

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

    @Column(name="VST_MAIN_COMPLAINT_DIAGNOSIS")
    private String mainComplaintDiagnosis;

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

    @NotNull
    @ManyToOne
    @JoinColumn(name="VST_PAT_EXAM")
    Examinations examination;


    @Transient
    private volatile int hashCode = 0;


    public Visit() {
        super();
    }

    public Visit(int visitID, GregorianCalendar date, Patient patient, String note, boolean sms) {
        super();
        this.visitID = visitID;
        this.date = date;
        this.patient = patient;
        this.note = note;
        this.sms = sms;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public void setDate(Date date) {
        GregorianCalendar gregorian = new GregorianCalendar();
        gregorian.setTime(date);
        setDate(gregorian);
    }

    public String toString() {

        return formatDateTime(this.date);
    }

    public String toStringSMS() {

        return formatDateTimeSMS(this.date);
    }

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
}
