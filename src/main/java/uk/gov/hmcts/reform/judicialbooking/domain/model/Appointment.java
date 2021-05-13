package uk.gov.hmcts.reform.judicialbooking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    private String appointmentId;
    private String roleId;
    private String roleDescEn;
    private String contractTypeId;
    private String contractTypeDescEn;
    private String baseLocationId;
    private String regionId;
    private String regionDescEn;
    private String isPrincipalAppointment;
    private String startDate;
    private String endDate;
}
