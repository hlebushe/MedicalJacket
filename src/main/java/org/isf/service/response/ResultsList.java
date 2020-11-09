package org.isf.service.response;

import lombok.Data;

import java.util.List;

@Data
public class ResultsList {
    public String diagnosisName;
    public double possibility;
    public List<String> confirmedSymptoms;
    public List<String> nonConfirmedSymptoms;
}
