package uk.gov.hmcts.reform.judicialbooking.feignclients.configuration;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Appointment;
import uk.gov.hmcts.reform.judicialbooking.domain.model.Authorisation;
import uk.gov.hmcts.reform.judicialbooking.domain.model.JudicialUserProfile;
import uk.gov.hmcts.reform.judicialbooking.feignclients.JRDFeignClient;

import java.util.Collections;
import java.util.List;

@Component
public class JRDFeignClientFallback implements JRDFeignClient {

    @Override
    public List<JudicialUserProfile> getJudicialUserProfiles(List<String> userIds) {
        return Collections.singletonList(JudicialUserProfile.builder()
                .fullName("Keith Gill")
                .contractTypeId("contractTypeId")
                .authorisations(Collections.singletonList(
                        Authorisation.builder()
                                .authorisationId("authId")
                                .build()))
                .appointments(Collections.singletonList(
                        Appointment.builder()
                                .appointmentId("appointmentId")
                                .baseLocationId("baseLocationId")
                                .roleId("roleId")
                                .contractTypeId("contractTypeId")
                                .build()))
                .build());
    }
}
