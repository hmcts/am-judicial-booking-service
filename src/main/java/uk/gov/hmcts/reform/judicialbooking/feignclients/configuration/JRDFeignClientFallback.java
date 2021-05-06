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
                .emailId("Keith.Gill@robinhood.com")
                .contractTypeId("contractTypeId")
                .idamId("idamId")
                .knowAs("Geohot")
                .personalCode("PS32010")
                .authorisations(List.of(
                        Authorisation.builder().authorisationId("authId_1").build(),
                        Authorisation.builder().authorisationId("authId_2").build(),
                        Authorisation.builder().authorisationId("authId_3").build()
                )).appointments(List.of(
                        Appointment.builder()
                                .appointmentId("appointmentId_1")
                                .baseLocationId("baseLocationId_1")
                                .roleId("roleId_1")
                                .contractTypeId("contractTypeId_1")
                                .build(),
                        Appointment.builder()
                                .appointmentId("appointmentId_2")
                                .baseLocationId("baseLocationId_2")
                                .roleId("roleId_2")
                                .contractTypeId("contractTypeId_2")
                                .build()
                ))
                .build());
    }
}
