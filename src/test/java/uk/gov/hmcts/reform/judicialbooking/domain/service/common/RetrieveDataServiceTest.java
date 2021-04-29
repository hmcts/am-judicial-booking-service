package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import uk.gov.hmcts.reform.judicialbooking.domain.model.JudicialUserProfile;
import uk.gov.hmcts.reform.judicialbooking.feignclients.JRDFeignClient;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

class RetrieveDataServiceTest {

    private final JRDFeignClient jrdFeignClient = mock(JRDFeignClient.class);

    private final RetrieveDataService sut = new RetrieveDataService(jrdFeignClient);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getJudicialUserProfile() {
        List<String> userIds = Collections.singletonList("5629957f-4dcd-40b8-a0b2-e64ff5898b28");
        when(jrdFeignClient.getJudicialUserProfiles(userIds))
                .thenReturn(Collections.singletonList(TestDataBuilder.buildJudicialProfile()));
        List<JudicialUserProfile> judicialUserProfiles = sut.getJudicialUserProfile(userIds);
        assertNotNull(judicialUserProfiles);
        assertNotNull(judicialUserProfiles.get(0).getAuthorisations());
        assertNotNull(judicialUserProfiles.get(0).getAppointments());
    }
}

