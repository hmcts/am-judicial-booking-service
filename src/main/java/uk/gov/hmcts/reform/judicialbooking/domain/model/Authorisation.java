package uk.gov.hmcts.reform.judicialbooking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authorisation {
    private String authorisationId;
    private String jurisdiction;
    private String lowerLevel;
    private String startDate;
    private String endDate;
    private String createdDate;
    private String lastUpdatedDate;
}
