package uk.gov.hmcts.reform.judicialbooking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudicialUserProfile {
    private String elinksId;
    private String personalCode;
    private String title;
    private String knowAs;
    private String surname;
    private String fullName;
    private String postNominals;
    private String contractTypeId;
    private String workPattern;
    private String emailId;
    private String joiningDate;
    private String lastWorkingDate;
    private String extractedDate;
    private String activeFlag;
    private String idamId;
    private String objectId;
    private List<Appointment> appointments;
    private List<Authorisation> authorisations;
}


