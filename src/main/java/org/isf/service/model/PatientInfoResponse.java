package org.isf.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.isf.service.JSONService;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonDeserialize
public class PatientInfoResponse {
    @JsonProperty("radiology_studies")
    private List<RadiologyReportResponse> radiologyStudies = new ArrayList<>();
    @JsonProperty("pathology_studies")
    private List<PathologyReportResponse> pathologyStudies = new ArrayList<>();
}
