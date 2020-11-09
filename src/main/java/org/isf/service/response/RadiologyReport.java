package org.isf.service.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RadiologyReport {
    public String studyName;
    public String studyDate;
    public String studyLink;
    public String studyReport;
}
