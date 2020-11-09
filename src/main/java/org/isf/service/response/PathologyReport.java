package org.isf.service.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PathologyReport {
    public String studyName;
    public String studyDate;
    public String studyReport;
}