package org.isf.service.model;

import lombok.Data;
import org.isf.service.JSONService;
import org.isf.service.response.PathologyReport;
import org.isf.service.response.RadiologyReport;

import java.util.List;

@Data
public class PathologyReportResponse {
    private String studyName;
    private String studyDate;
    private List<ReportLinkResponse> reportLinks;

    public PathologyReport toPathologyReport() {
        return PathologyReport.builder()
                .studyName(studyName)
                .studyDate(studyDate)
                .studyReport(reportLinks.get(0).getReport()).build();
    }
}
