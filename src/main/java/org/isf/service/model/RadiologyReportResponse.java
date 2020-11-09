package org.isf.service.model;

import lombok.Data;
import org.isf.service.response.RadiologyReport;

import java.util.List;

@Data
public class RadiologyReportResponse {
    private String studyName;
    private String studyDate;
    private String studyLink;
    private List<ReportLinkResponse> reportLinks;

    public RadiologyReport toRadiologyReport() {
        return RadiologyReport.builder()
                .studyName(studyName)
                .studyDate(studyDate)
                .studyLink(studyLink)
                .studyReport(reportLinks.get(0).getReport()).build();
    }
}
