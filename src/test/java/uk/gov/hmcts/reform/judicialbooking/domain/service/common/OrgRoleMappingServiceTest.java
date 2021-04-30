package uk.gov.hmcts.reform.judicialbooking.domain.service.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.judicialbooking.feignclients.ORMFeignClient;
import uk.gov.hmcts.reform.judicialbooking.helper.TestDataBuilder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrgRoleMappingServiceTest {

    private final ORMFeignClient ormFeignClient = mock(ORMFeignClient.class);

    private final OrgRoleMappingService sut = new OrgRoleMappingService(ormFeignClient);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getJudicialUserProfile() {
        when(ormFeignClient.createBookingAssignments(any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<Object> ormCreateResponse =
                sut.createBookingAssignments(TestDataBuilder.buildOrmBookingAssignmentsRequest());
        assertNotNull(ormCreateResponse);
        assertTrue(ormCreateResponse.getStatusCode().is2xxSuccessful());
    }
}