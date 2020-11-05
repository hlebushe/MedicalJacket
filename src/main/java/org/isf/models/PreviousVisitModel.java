package org.isf.models;

import lombok.Data;
import org.isf.dao.Patient;
import org.isf.dao.Visit;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class PreviousVisitModel {

    private UUID visitID;
    private Patient patient;
    private String visitDate;
    private String type;
    private String mainComplaintSymptoms;
    private String mainComplaintDiagnosis;
    private String secondComplaintDiagnosis;
    private String treatment;


    public PreviousVisitModel(Visit visit) {
        this.visitID = visit.getVisitID();
        this.patient = visit.getPatient();

        Date date = visit.getDate();
        String dateStr = date.toString();
        this.visitDate = dateStr.substring(0, 10);

        if (visit.getType() == 'H') {
            this.type = "Home";
        } else if (visit.getType() == 'T') {
            this.type = "Tele Consult";
        } else {
            this.type = "Clinic";
        }

        String mainCompSympt = visit.getMainComplaintSymptom();

        this.mainComplaintSymptoms = mainCompSympt;

        String mainCompDia =
                Stream.of(visit.getMainComplaintDiagnosis1(), visit.getMainComplaintDiagnosis2(), visit.getMainComplaintDiagnosis3())
                        .filter(s -> s != null && !s.isEmpty())
                        .collect(Collectors.joining(", "));

        this.mainComplaintDiagnosis = mainCompDia;

        this.secondComplaintDiagnosis = visit.getSecondaryComplaintDiagnosis();

        if (visit.getMedication1()==null || visit.getMedication2()==null || visit.getMedication3()==null || visit.getMedication4()==null || visit.getMedication5()==null || visit.getMedication6()==null) {
            this.treatment = "No Treatment";
        } else {
            String med1 = visit.getMedication1().split(";")[0];
            String med2 = visit.getMedication2().split(";")[0];
            String med3 = visit.getMedication3().split(";")[0];
            String med4 = visit.getMedication4().split(";")[0];
            String med5 = visit.getMedication5().split(";")[0];
            String med6 = visit.getMedication6().split(";")[0];

            String treatment =
                    Stream.of(med1, med2, med3, med4, med5, med6)
                            .filter(s -> s != null && !s.isEmpty())
                            .collect(Collectors.joining(", "));

            this.treatment = treatment;
        }
    }
}
