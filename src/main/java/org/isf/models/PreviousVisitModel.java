package org.isf.models;

import lombok.Data;
import org.isf.dao.Patient;
import org.isf.dao.Visit;

import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class PreviousVisitModel {

    private int visitID;
    private Patient patient;
    private String visitDate;
    private String type;
    private String mainComplaintSymptoms;
    private String mainComplaintDiagnosis;
    private String secondComplaintSymptoms;
    private String secondComplaintDiagnosis;
    private String treatment;
    private String management;


    public PreviousVisitModel(Visit visit) {
        this.visitID = visit.getVisitID();
        this.patient = visit.getPatient();

        Date date = visit.getDate();
        String dateStr = date.toString();
        this.visitDate = dateStr.substring(0, 10);

        if (visit.getType() == 'H') {
            this.type = "Home";
        } else {
            this.type = "Clinic";
        }

        String mainCompSympt =
                Stream.of(visit.getMainComplaintSymptom1(), visit.getMainComplaintSymptom2(), visit.getMainComplaintSymptom3(), visit.getMainComplaintSymptom4(), visit.getMainComplaintSymptom5())
                        .filter(s -> s != null && !s.isEmpty())
                        .collect(Collectors.joining(", "));

        this.mainComplaintSymptoms = mainCompSympt;

        String mainCompDia =
                Stream.of(visit.getMainComplaintDiagnosis1(), visit.getMainComplaintDiagnosis2(), visit.getMainComplaintDiagnosis3())
                        .filter(s -> s != null && !s.isEmpty())
                        .collect(Collectors.joining(", "));

        this.mainComplaintDiagnosis = mainCompDia;

        String secCompSympt =
                Stream.of(visit.getSecondaryComplaintSymptom1(), visit.getSecondaryComplaintSymptom2(), visit.getSecondaryComplaintSymptom3(), visit.getSecondaryComplaintSymptom4(), visit.getSecondaryComplaintSymptom5())
                        .filter(s -> s != null && !s.isEmpty())
                        .collect(Collectors.joining(", "));

        this.secondComplaintSymptoms = secCompSympt;

        this.secondComplaintDiagnosis = visit.getSecondaryComplaintDiagnosis();

        this.treatment = visit.getMedicinePrescribed();
        this.management = visit.getGeneralInstructions();
    }
}
