package uk.gov.hmcts.reform.judicialbooking.feignclients.configuration;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.judicialbooking.domain.model.JudicialUserProfile;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;

class JRDFeignClientFallbackTest {


    private JRDFeignClientFallback jrdFeignClientFallback = new JRDFeignClientFallback();

    @Test
    void getJudicialUserProfiles() {

        List<JudicialUserProfile> response = jrdFeignClientFallback.getJudicialUserProfiles(Collections.singletonList(
                "5629957f-4dcd-40b8-a0b2-e64ff5898b28"));
        assertNotNull(response);
        assertEquals("Jiminy Cricket", response.get(0).getFullName());
        assertNotNull(response.get(0).getAppointments());
        assertNotNull(response.get(0).getAuthorisations());

    }
}